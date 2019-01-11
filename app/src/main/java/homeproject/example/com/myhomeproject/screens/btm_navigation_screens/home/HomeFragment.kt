package homeproject.example.com.myhomeproject.screens.btm_navigation_screens.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.common.BaseFragment


class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.home_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
