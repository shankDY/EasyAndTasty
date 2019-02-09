package com.shank.eat.screens.btm_navigation_screens.shoping_lists

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import kotlinx.android.synthetic.main.shoping_list_fragment.*


class ShopingListsFragment : BaseFragment() {

    private lateinit var mViewModel: ShopingListsViewModel
    private lateinit var mAdapter: ShopingListsAdapter // recyclerView adapter для постов юзеров

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.shoping_list_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //инициализация mViewModel
        mViewModel = initViewModel()


        mAdapter = ShopingListsAdapter()
       shoping_list_recycler.adapter = mAdapter

        shoping_list_recycler.layoutManager = LinearLayoutManager(context)

        mViewModel.shopingLists.observe(viewLifecycleOwnerLiveData.value!!, Observer{ it?.let{
                mAdapter.updatePosts(it) }
        })


    }

    companion object {
        const val TAG = "ShopingListsFragment"
    }
}
