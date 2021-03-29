package com.osicorp.carbon_test_app.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<DbPost>>

    @Query("SELECT * FROM posts WHERE postid = :id")
    fun getAPostWithId(id:String):DbPost

    @Insert
    suspend fun insertPost(post: DbPost)

    @Delete
    fun delete(post: DbPost)
}