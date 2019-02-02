package com.shank.eat.screens.btm_navigation_screens.shoping_list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment


class ShopingListFragment : BaseFragment() {

    private lateinit var viewModel: ShopingListViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.shoping_list_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ShopingListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        const val TAG = "ShopingListFragment"
    }
}
