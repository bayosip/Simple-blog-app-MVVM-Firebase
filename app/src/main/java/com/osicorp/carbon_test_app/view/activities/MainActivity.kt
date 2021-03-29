package com.osicorp.carbon_test_app.view.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.databinding.ActivityMainBinding
import com.osicorp.carbon_test_app.interfaces.FragmentListener
import com.osicorp.carbon_test_app.model.GeneralUtils
import com.osicorp.carbon_test_app.model.Post
import com.osicorp.carbon_test_app.view.fragments.BaseFragment
import com.osicorp.carbon_test_app.view.fragments.DetailsFragment
import com.osicorp.carbon_test_app.view.fragments.ListFragment
import com.osicorp.carbon_test_app.view.fragments.LoginFragment


class MainActivity : AppCompatActivity(), FragmentListener {
    private lateinit var binding: ActivityMainBinding
    private var mediaController: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.appbarHome.appToolbar)
        NavigationUI.setupWithNavController(binding.appbarHome.appToolbar,findNavController(R.id.fragContainer))

        this.mediaController = MediaController(this)
    }

    private fun setupHomeActionBar() {
        val ab = supportActionBar
        ab!!.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab!!.setDisplayHomeAsUpEnabled(false)
        ab!!.setDisplayShowCustomEnabled(false) // enable overriding the default toolbar layout
        ab!!.setDisplayShowTitleEnabled(false)
    }

    private fun setupOtherActionBar() {
        // Get the ActionBar here to configure the way it behaves.
//        ab.setDisplayOptions();
        val ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab!!.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
    }

    fun changeFragment(newFragment: BaseFragment, tag: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragContainer)
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragContainer, newFragment, tag)
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss()
        }
        if (newFragment is DetailsFragment) setupHomeActionBar()
    }

    override fun expandPost(post: Post?) {
        setupOtherActionBar()
        val fragment = post?.Title?.let {
            DetailsFragment.getInstance(
                post?.Url,
                it,
                post?.MediaType!!
            )
        }!!
        changeFragment(fragment, "Details")
    }

    override fun goToListFrag() {
        changeFragment(ListFragment.getInstance(), "List")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragContainer)
        onBackPressed()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragContainer)
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.remove(fragment).commitAllowingStateLoss()
            val tag = fragment.tag
            if (!TextUtils.isEmpty(tag) && tag == "Details") {
                setupHomeActionBar()
            }
            else{
                GeneralUtils.exitApp(this@MainActivity)
            }
        }
    }
}