package com.osicorp.carbon_test_app.interfaces

import android.widget.MediaController

interface ListListener {

    fun showSelectedPostWithId(id:String)
    fun getMediaController():MediaController
}