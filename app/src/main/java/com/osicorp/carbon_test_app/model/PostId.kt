package com.osicorp.carbon_test_app.model

import com.google.firebase.firestore.Exclude

open class PostId {

    @Exclude
    var postId: String? = null

    open fun <T : PostId?> withId(id: String): T {
        postId = id
        return this as T
    }
}