package com.shank.eat.screens.other.people.side_profile_view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.fragment_side_profile_view.*

class SideProfileViewFragment : BaseFragment() {
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_side_profile_view, parent, false)
    }


    private lateinit var mViewModel: SideProfileViewViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()
        val userId = arguments?.getString("uid")

        if(userId!!.isNotEmpty()) {
            //инициализируем наш userId
            mViewModel.init(userId)
        }

        mViewModel.user.observe(viewLifecycleOwnerLiveData.value!!, Observer {user ->
            username_text.text = user?.name
            profile_image.loadUserPhoto(user?.photo)
            followers_count_text.text = user?.followers?.size.toString()
            following_count_text.text = user?.follows?.size.toString()
            email_text.text = user?.email
            phone_text.text = user?.phone
            website_text.text = user?.website
        })

        mViewModel.recipes.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            it.let {myRecipes ->
                //количество рецептов, который создал пользователь
                posts_count_text.text = myRecipes?.size.toString()
            }
        })

        //переход на окно с рецептами пользователя
        recipe_count.setOnClickListener {
//            findNavController().navigate(R.id.action_sideProfileViewFragment_to_myRecipeFragment2)
        }

        //подписаться
        follow_btn.setOnClickListener {
            setFollow(userId,true){
                Log.d(TAG,"Подписан на $userId")
            }
        }

        //отписаться
        unfollow_btn.setOnClickListener {
           setFollow(userId,false){
               Log.d(TAG,"Отписан от $userId")
           }
        }
    }
    //если нажали следовать, то информация добавляется в бд
    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        if (follow){
            follow_btn.visibility = View.GONE
            unfollow_btn.visibility = View.VISIBLE
        }else{
            follow_btn.visibility = View.VISIBLE
            unfollow_btn.visibility = View.GONE
        }
        mViewModel.setFollow( uid, follow).addOnSuccessListener {
            onSuccess()
        }
    }

    companion object {
        const val TAG = "SideProfileViewFragment"
    }
}
