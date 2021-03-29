package com.osicorp.carbon_test_app.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "posts")
data class DbPost (@PrimaryKey(autoGenerate = false) val postid: String,
                   val title: String,
                   val url: String?,
                   val mediatype: Long,
                   val timestamp: Long) {
}