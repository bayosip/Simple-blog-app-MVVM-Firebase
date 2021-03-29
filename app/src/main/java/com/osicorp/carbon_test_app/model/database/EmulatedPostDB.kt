package com.osicorp.carbon_test_app.model.database

import android.content.Context
import androidx.room.Room
import com.osicorp.carbon_test_app.model.Constants

class EmulatedPostDB private constructor(){

    var postDao = FakePostDoa()
        private set

    companion object{

        @Volatile
        private var dbInstance: EmulatedPostDB? = null

        fun getDb() =
        // Already instantiated? - return the instance
            // Otherwise instantiate in a thread-safe manner
            dbInstance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                dbInstance ?: EmulatedPostDB().also { dbInstance = it }
            }
    }
}