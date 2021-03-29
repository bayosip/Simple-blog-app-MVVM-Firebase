package com.osicorp.carbon_test_app.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.firebase.ui.auth.AuthUI
import com.osicorp.carbon_test_app.model.repository.Repository

class LoginViewModel(app:Application): AndroidViewModel(app) {

    private var repo: Repository.LoginRepo?=null

    init {
        repo = Repository.LoginRepo()
    }
    fun getLoginProviders(): List<AuthUI.IdpConfig>?{
        return repo?.getLoginProviders()
    }
}