package com.osicorp.carbon_test_app.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.osicorp.carbon_test_app.interfaces.FragmentListener

abstract class BaseFragment: Fragment() {

    private  val TAG = "BaseFragment"
    protected var listener: FragmentListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FragmentListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseWidgets(view)
        Log.e(TAG, "onViewCreated: "+javaClass.simpleName)
    }

    protected abstract fun initialiseWidgets(view: View)
}