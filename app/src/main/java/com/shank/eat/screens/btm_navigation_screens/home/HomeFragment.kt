package com.shank.eat.screens.btm_navigation_screens.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.shank.eat.R
import com.shank.eat.data.firebase.common.auth
import com.shank.eat.screens.common.BaseFragment
import kotlinx.android.synthetic.main.home_fragment.*




class HomeFragment : BaseFragment(), FeedAdapter.Listener{

    private lateinit var mViewModel: HomeViewModel
    private lateinit var mAdapter: FeedAdapter // recyclerView adapter для постов юзеров

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.home_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = initViewModel()

        mAdapter = FeedAdapter(this)
        feed_recycler.adapter = mAdapter
        feed_recycler.layoutManager = LinearLayoutManager(context)

        mViewModel.feedPosts.observe(this, Observer{ it ->
            it?.let{
            mAdapter.updatePosts(it) }
        })





        img.setOnClickListener {
            auth.signOut()
        }


    }

    //подгружаем лайкосики
    override fun loadLikes(postId: String, position: Int){

        if(mViewModel.getLikes(postId) == null){
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let{ postLikes ->
                    mAdapter.updatePostLikes(position, postLikes)
                }
            })
        }
    }

    //переключатель лайк
    override fun toogleLike(postId: String) {

        Log.d(TAG, "toogleLike: $postId")

        mViewModel.toogleLike(postId)
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
