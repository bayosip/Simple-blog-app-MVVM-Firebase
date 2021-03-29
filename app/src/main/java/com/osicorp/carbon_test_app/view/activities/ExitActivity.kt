package com.osicorp.carbon_test_app.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.osicorp.carbon_test_app.R

class ExitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit)
        finishAndRemoveTask()
    }
}