package com.osicorp.carbon_test_app.model.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osicorp.carbon_test_app.model.Post

class FakePostDoa {

    // A fake database table
    private val postList = mutableListOf<Post>()
    private val posts = MutableLiveData<List<Post>>()

    init {
        posts.value = postList
    }

    fun addPost(quote: Post) {
        postList.add(quote)
        posts.value = postList
    }

    fun addListOfPosts(list: List<Post>){
        postList.addAll(list)
        posts.value = postList
    }

    fun getPosts() = posts as LiveData<List<Post>>

    fun getAPostWithId(id:String):Post?{
        for (p in postList){
            if (id.equals(p.postId))
                return p
        }
        return null
    }
}