package homeproject.example.com.myhomeproject.screens.register_and_login_screens

import android.os.Bundle
import android.util.Log
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.common.BaseActivity


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
