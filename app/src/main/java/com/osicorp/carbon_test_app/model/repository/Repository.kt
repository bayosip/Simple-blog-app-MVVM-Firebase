package com.osicorp.carbon_test_app.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.firebase.ui.auth.AuthUI
import com.osicorp.carbon_test_app.model.GeneralUtils
import com.osicorp.carbon_test_app.model.Post
import com.osicorp.carbon_test_app.model.database.DbPost
import com.osicorp.carbon_test_app.model.database.FakePostDoa
import com.osicorp.carbon_test_app.model.database.PostDao
import com.osicorp.carbon_test_app.model.database.ePost
import com.osicorp.carbon_test_app.model.network_fb.FirebaseInteractor
import com.osicorp.carbon_test_app.model.network_fb.FirebaseInteractorInterface
import kotlinx.coroutines.flow.first
import java.util.*

class Repository private constructor(private val dao: FakePostDoa) {

//    val getAllPosts: LiveData<List<Post>>
    private val fbInterface:FirebaseInteractorInterface

    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: Repository? = null
        private const val TAG = "Repository"

        fun getInstance(quoteDao: FakePostDoa):Repository {
            val temp = instance
            if (temp!=null)return temp
            synchronized(this) {
                instance = Repository(quoteDao)
                Log.d(TAG, "getInstance: $instance")
                return instance!!
            }
        }
    }

    init {
        fbInterface = FirebaseInteractor.getInstance(this)
    }

    suspend fun firstPullFromFirebase(){
        dao.addListOfPosts(fbInterface.firstPageFirstLoad().first())
    }

  suspend fun nextPullFromFirebase(){
        dao.addListOfPosts(fbInterface.loadMorePost().first())
    }

    fun getAllPosts() = dao.getPosts()


    fun addPostToRepo(post: Post){
        val p = post as ePost
        Log.d(TAG, "addPostToRepo: ${this}")
        dao.addPost(p)
    }

    fun getPost(id:String):Post{
        val post = dao.getAPostWithId(id)
        return post!!
    }

    class LoginRepo{

        private val loginProviders: List<AuthUI.IdpConfig>? = Arrays.asList(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        fun getLoginProviders(): List<AuthUI.IdpConfig>?{
            return loginProviders
        }
    }


}