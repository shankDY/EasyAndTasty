package com.shank.eat.screens.btm_navigation_screens

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import com.shank.eat.R
import com.shank.eat.screens.common.BaseActivity
import com.shank.eat.screens.common.BottomNavController
import com.shank.eat.screens.common.setUpNavigation
import com.shank.eat.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), BottomNavController.NavGraphProvider {



    private val navController by lazy(LazyThreadSafetyMode.NONE){
        Navigation.findNavController(this, R.id.btm_nav_host)
    }

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE){
        BottomNavController(this,R.id.btm_nav_host,R.id.nav_item_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAuthGuard {
            Log.d(TAG, "onCreate")

            bottomNavController.setNavGraphProvider(this)
            bottom_navigation.setUpNavigation(bottomNavController)



            //Если savedInstanceState == null, значит активити только созданно. и тогда мы создаем фрагмент
            // (в нашем случаи можем переходить по фрагментам с помощью нижней навигации)
            if (savedInstanceState == null)
                bottomNavController.onNavigationItemSelected()



//            findNavController(R.id.btm_nav_host).addOnDestinationChangedListener {
//                    _, destination, _ ->
//                when (destination.id) {
//                    R.id.recipeFragment,
//                    R.id.nav_item_add_recipe,
//                    R.id.commentsFragment,
//                    R.id.shopingListOpenFragment-> hideBottomNavigation()
//                else -> showBottomNavigation()
//            }  }
            }
        }

    override fun getNavGraphId(itemId: Int): Int = when(itemId){
        R.id.nav_item_home ->R.navigation.nav_item_home
        R.id.nav_item_search ->R.navigation.nav_item_search
        R.id.nav_item_add_recipe ->R.navigation.nav_item_add_recipe
        R.id.nav_item_shoping_list ->R.navigation.nav_item_shoping_list
        R.id.nav_item_profile ->R.navigation.nav_item_profile
        else -> R.navigation.nav_item_home
    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()

    override fun onBackPressed() = bottomNavController.onBackPressed()






    companion object {
        const val TAG = "MainActivity"
    }
}
