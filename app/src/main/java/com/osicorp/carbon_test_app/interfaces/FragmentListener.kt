package com.osicorp.carbon_test_app.interfaces

import com.osicorp.carbon_test_app.model.Post
import com.osicorp.carbon_test_app.view.fragments.BaseFragment

interface FragmentListener {

    fun expandPost(post: Post?)
    fun goToListFrag()
}