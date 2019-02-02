package com.shank.eat.screens.btm_navigation_screens

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.home.HomeFragment
import com.shank.eat.screens.common.BaseActivity
import com.shank.eat.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAuthGuard {
            Log.d(TAG, "onCreate")

            //подключение navigation bottom
            bottom_navigation_view.setupWithNavController(Navigation.findNavController(this,
                R.id.btm_nav_host_fragment
            ))
        }
    }

    companion object {
        const val TAG = "ShopingListFragment"
    }
}
