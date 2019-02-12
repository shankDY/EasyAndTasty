package com.shank.eat.screens.btm_navigation_screens.profile.follow

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
import com.shank.eat.screens.common.BaseFragment
import kotlinx.android.synthetic.main.follow_users_fragment.*

class FollowUsersFragment : BaseFragment(), FriendsAdapter.Listener {

    private lateinit var mUser: User
    private lateinit var mAdapter: FriendsAdapter


    private lateinit var mViewModel: FollowUsersViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.follow_users_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //инициализация viewModel
        mViewModel = initViewModel()

        //инициализация recyclerAdapter
        mAdapter = FriendsAdapter(this)
        follow_recycler.adapter = mAdapter
        follow_recycler.layoutManager = LinearLayoutManager(context)


        //вычитываем юзеров и их друзей с бд
        mViewModel.userAndFriends.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            it?.let { (user, otherUsers) ->
                mUser = user//наш пользователь

                //передаем в адаптер всех пользователей и тех пользователей ,
                // которых мы фоловим(на которых подписались)
                mAdapter.update(otherUsers, mUser.follows)
            }
        })

        back_image.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    //подписываемся
    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    //отписываемся
    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    //если нажали следовать, то информация добавляется в бд
    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {

        mViewModel.setFollow(mUser.uid, uid, follow)
            .addOnSuccessListener { onSuccess() }

    }

    companion object {
        const val TAG = "FollowUsersFragment"
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
