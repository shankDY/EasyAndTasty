package com.shank.eat.screens.common

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.shank.eat.screens.register_and_login_screens.HostLoginActivity

//главный класс для всех активити
abstract class BaseActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //отключаем смену ориентации экрана на книжную, для того,
        // чтобы активити так часто не убивались
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


    }

    //переход в логинАктивити
    fun goToLogin() {
        startActivity(Intent(this, HostLoginActivity::class.java))
        finish()
    }

    fun onFragmentAttached() {}


    //храним все статические переменные класса в данном объекте
    companion object {
        const val TAG = "BaseActivity"
    }
}