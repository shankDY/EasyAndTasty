package com.shank.eat.screens.register_and_login_screens

import android.os.Bundle
import android.util.Log
import com.shank.eat.R
import com.shank.eat.screens.common.BaseActivity


class HostLoginActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        Log.d(TAG, "onCreate")
    }


    companion object {
        const val TAG = "HostLoginActivity"
    }
}
