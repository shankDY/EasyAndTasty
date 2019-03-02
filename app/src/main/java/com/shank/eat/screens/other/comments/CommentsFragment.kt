package com.shank.eat.screens.other.comments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.model.User
import com.shank.eat.screens.btm_navigation.home.HomeFragment
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadUserPhoto
import com.shank.eat.screens.common.recyclerAnimatorOff
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : BaseFragment() {


    private lateinit var viewModel: CommentsViewModel

    private lateinit var mAdapter: CommentsAdapter

    private lateinit var mUser: User

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(HomeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_comments, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = initViewModel()

        val postId = arguments!!.getString("postId")?: findNavController().popBackStack()


            //инициализация postId
            viewModel.init(postId.toString())


        //получаем зареганного в данный момент юзер и загружаем ее фотку
        viewModel.user.observe(viewLifecycleOwnerLiveData.value!!, Observer{
            it?.let {
                mUser = it
                //подгружаем фотку юзера в imagView, перед вводом коммента
                user_photo.loadUserPhoto(mUser.photo)
            }
        })

        //инициализация адаптера
        comments_recycler.layoutManager = LinearLayoutManager(getBaseActivity())
        mAdapter = CommentsAdapter()
        recyclerAnimatorOff(comments_recycler)
        comments_recycler.adapter = mAdapter


        //слушаем комментарии, если есть новые , то добавляем их recicler view
        viewModel.comments.observe( viewLifecycleOwnerLiveData.value!!, Observer { it.let {
                mAdapter.updateComments(it!!)
            }
        })

        //жмем кнопку отправить и загуржаем коммент в бж
        post_comment_text.setOnClickListener {
            viewModel.createComment(comment_text.text.toString(), mUser)
            //обнуляем наш текствью
            comment_text.setText("")
        }

        back_img.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val TAG = "CommentsFragment"
    }

    override fun onStart() {
        super.onStart()
        //скрываем NavigationBottom при входе во фрагмент
        hideBottomNavigation()
    }

    override fun onStop() {
        super.onStop()
        //показываем NavigationBottom при выходе из фрагмента
        showBottomNavigation()
    }
}
