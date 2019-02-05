package com.shank.eat.screens.btm_navigation_screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.shank.eat.R
import com.shank.eat.screens.common.BaseActivity
import com.shank.eat.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private val ENTER_DURATION: Long = 1
    private val EXIT_DURATION: Long = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAuthGuard {
            Log.d(TAG, "onCreate")

            //подключение navigation bottom
            bottom_navigation_view.setupWithNavController(
                Navigation.findNavController(
                    this,
                    R.id.btm_nav_host_fragment
                )
            )

            findNavController(R.id.btm_nav_host_fragment).addOnDestinationChangedListener {
                    _, destination, _ ->
                when (destination.id) {
                    R.id.recipeFragment,
                    R.id.nav_item_add_recipe,
                    R.id.commentsFragment,
                    R.id.shopingListOpenFragment-> hideBottomNavigation()
                else -> showBottomNavigation()
            }  }
            }
        }

    //скрываем bottomNavigation
    private fun hideBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(bottom_navigation_view) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = EXIT_DURATION
            }
        }
    }



    //показываем bottomNavigation
    private fun showBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(bottom_navigation_view) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = ENTER_DURATION
        }

    }

    companion object {
        const val TAG = "ShopingListsFragment"
    }
}
