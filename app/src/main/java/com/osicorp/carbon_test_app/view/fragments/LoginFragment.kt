package com.osicorp.carbon_test_app.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.model.Constants
import com.osicorp.carbon_test_app.view_model.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [BaseFragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment() {

    private  lateinit var viewModel:LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

            companion object {
                private const val TAG = "LoginFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseLogin()
    }

    override fun initialiseWidgets(view: View) {}

    fun initialiseLogin() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(viewModel.getLoginProviders()!!)
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.FirebaseUI_AuthMethodPicker) // Set theme
                .build(),
                Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: passed")
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            when (requestCode) {
                Constants.RC_SIGN_IN -> {
                        val response = IdpResponse.fromResultIntent(data)
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                        }
                    }
                }
        }
    }
}