package com.shank.eat.screens.btm_navigation.profile

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(){

    private lateinit var mViewModel: ProfileViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_profile, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()

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
                //количество рецептов, который создал пользователь
                posts_count_text.text = myRecipes?.size.toString()
            }
        })

        favorite.setOnClickListener { findNavController().navigate(R.id.action_nav_item_profile_to_favorite) }
        my_recipes.setOnClickListener { findNavController().navigate(R.id.action_nav_item_profile_to_my_recipes) }
        seach_people.setOnClickListener { findNavController().navigate(R.id.action_nav_item_profile_to_seach_people) }
        eddit_profile.setOnClickListener { findNavController().navigate(R.id.action_nav_item_profile_to_edit_profile) }

    }


    companion object {
        const val TAG = "ProfileFragment"
    }
}
