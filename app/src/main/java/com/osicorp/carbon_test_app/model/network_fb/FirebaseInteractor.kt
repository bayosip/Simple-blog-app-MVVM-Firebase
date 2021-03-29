package com.osicorp.carbon_test_app.model.network_fb

import android.util.Log
import androidx.lifecycle.LiveData
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.osicorp.carbon_test_app.model.Constants
import com.osicorp.carbon_test_app.model.Post
import com.osicorp.carbon_test_app.model.database.DbPost
import com.osicorp.carbon_test_app.model.database.PostDatabase
import com.osicorp.carbon_test_app.model.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import kotlin.coroutines.suspendCoroutine

class FirebaseInteractor private constructor(repo:Repository): FirebaseInteractorInterface{

    
    private val firestore: FirebaseFirestore
    private val storage: StorageReference
    private val mAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var isFirstPageFirstLoad = true
    private var lastVisible: DocumentSnapshot? = null
    private var listenerRegistration: ListenerRegistration? = null

    private var repo: Repository?=null

    companion object{
        private const val TAG = "FirebaseInteractor"
        private var instance : FirebaseInteractor?=null

        fun getInstance(repo: Repository):FirebaseInteractorInterface{

            val temp = instance
            if (temp !=null)return temp

            synchronized(this){
                val fbInterface = FirebaseInteractor(repo)
                return fbInterface
            }
        }
    }

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
       mAuth = FirebaseAuth.getInstance()
       firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = settings
       storage = FirebaseStorage.getInstance().reference
        user = mAuth.currentUser
    }


    @ExperimentalCoroutinesApi
    override fun firstPageFirstLoad(): Flow<List<Post>> {
        Log.w(TAG, "firstPageFirstLoad: called" )
        val firstQuery: Query = firestore.collection(Constants.POSTS)
            .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
            .limit(5)

        return callbackFlow{  listenerRegistration =
            firstQuery.addSnapshotListener { documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException? ->
                if (documentSnapshots != null && !documentSnapshots.isEmpty) {
                    if (isFirstPageFirstLoad) {
                        lastVisible = documentSnapshots.documents[documentSnapshots.size() - 1]
                    }
                    val list = LinkedList<Post>()
                    for (doc in documentSnapshots.documentChanges) {
                        if (doc.type == DocumentChange.Type.ADDED) {
                            val blogPostId = doc.document.id
                            val blogPost: Post = doc.document.toObject(Post::class.java)
                                .withId(blogPostId)
                            Log.d(TAG, "firstPageFirstLoad: ${blogPost.TimeStamp}")
                            list.add(blogPost)
//                            repo?.addPostToRepo(blogPost)
                        }
                    }
                    isFirstPageFirstLoad = false
                    offer(list)

                }
            }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration?.remove()
            }
        }

    }

    @ExperimentalCoroutinesApi
    override fun loadMorePost():Flow<List<Post>> {
        Log.w(TAG, "loadMorePost: called")
            val nextQuery = firestore.collection(Constants.POSTS)
                .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3)
            return callbackFlow{

                listenerRegistration =
                    nextQuery.addSnapshotListener { documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException? ->
                        if (documentSnapshots != null && !documentSnapshots.isEmpty) {
                            lastVisible = documentSnapshots.documents[documentSnapshots.size() - 1]
                            val list = LinkedList<Post>()
                            for (doc in documentSnapshots.documentChanges) {
                                if (doc.type == DocumentChange.Type.ADDED) {
                                    val blogPostId = doc.document.id
                                    val blogPost: Post = doc.document.toObject(Post::class.java)
                                        .withId(blogPostId)
                                    //repo?.addPostToRepo(blogPost)
                                    list.add(blogPost)
                                    offer(list)
                                }
                            }
                        }
                    }
                awaitClose {
                    Log.d(TAG, "Cancelling posts listener")
                    listenerRegistration?.remove()
                }
            }
    }
}