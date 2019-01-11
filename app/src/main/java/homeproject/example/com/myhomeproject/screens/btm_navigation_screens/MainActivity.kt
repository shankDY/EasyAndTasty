package homeproject.example.com.myhomeproject.screens.btm_navigation_screens

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.data.firebase.common.auth
import homeproject.example.com.myhomeproject.screens.common.BaseActivity
import homeproject.example.com.myhomeproject.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.btm_view.*
import kotlinx.android.synthetic.main.home_fragment.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAuthGuard {
            Log.d(TAG, "onCreate")

            setup_bnv(bottom_navigation_view)

            //подключение navigation controller
            bottom_navigation_view.setupWithNavController(Navigation.findNavController(this,
                R.id.btm_nav_host_fragment
            ))

        }
    }

    private fun setup_bnv(bottom_navigation_view: BottomNavigationViewEx?) {

        bottom_navigation_view?.setTextVisibility(false)// отключаем показ текста под иконкой
        bottom_navigation_view?.setIconSize(29f, 29f) //размер иконки navigationBottom
    }

    companion object {
        const val TAG = "ShopingListFragment"
    }
}
