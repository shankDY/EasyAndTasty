package com.shank.eat.screens.btm_navigation_screens.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.profile_fragment.*


class ProfileFragment : BaseFragment() {


    private lateinit var mViewModel: ProfileViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.profile_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()

        val pageAdapter = ProfilePagerAdapter(childFragmentManager)
        profile_viewpager.adapter = pageAdapter

        tabs_profile.setupWithViewPager(profile_viewpager)

        discover_people_image.setOnClickListener {
            findNavController().navigate(R.id.action_nav_item_profile_to_followUsersFragment)
        }

        mViewModel.user.observe(viewLifecycleOwnerLiveData.value!!, Observer { it ->
            it?.let {
                profile_image.loadUserPhoto(it.photo)
                username_text.text = it.name
                //количество фоловеров
                followers_count_text.text = it.followers.size.toString()
                //количество подписок
                following_count_text.text = it.follows.size.toString()
            }
        })


        mViewModel.myRecipes.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            it.let {myRecipes ->
                posts_count_text.text = myRecipes?.size.toString()
            }
        })


    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
