package com.osicorp.carbon_test_app.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.osicorp.carbon_test_app.model.Post
import com.osicorp.carbon_test_app.model.database.EmulatedPostDB
import com.osicorp.carbon_test_app.model.database.FakePostDoa
import com.osicorp.carbon_test_app.model.repository.Repository
import kotlinx.coroutines.launch

class ListViewModel(app:Application):AndroidViewModel(app) {


    private val _posts = MutableLiveData<List<Post>>()
    val postData: LiveData<List<Post>>
    private val repo: Repository

    init {
        val db:FakePostDoa = EmulatedPostDB.getDb().postDao
        repo = Repository.getInstance(db)
//        fbInterface = FirebaseInteractor.getInstance(repo)
        postData = repo.getAllPosts()
    }


    fun getPostWithId(id: String): Post {
        return repo.getPost(id)
    }

    fun loadFirst(){
        viewModelScope.launch {
            repo.firstPullFromFirebase()
        }
    }
    fun loadMorePosts(){
        viewModelScope.launch {
            repo.nextPullFromFirebase()
        }
    }
}
