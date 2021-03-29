package com.osicorp.carbon_test_app.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.osicorp.carbon_test_app.model.Constants

@Database(entities = [DbPost::class], version = 1)
abstract class PostDatabase: RoomDatabase() {

    abstract fun getDao():PostDao

    companion object{

         @Volatile
         private var dbInstance: PostDatabase? = null

        fun getDb(context: Context): PostDatabase{
            val instance = dbInstance
            if (instance!=null)return instance

            synchronized(this){
                val singleInsta = Room.databaseBuilder(context, PostDatabase::class.java,
                    Constants.DB_TABLE_POST).build()
                dbInstance = singleInsta
                return dbInstance!!
            }
        }
    }
}