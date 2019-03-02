package com.shank.eat.screens.btm_navigation_screens.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.common.firebase.FirebaseAuthManager
import com.shank.eat.data.firebase.common.auth
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.recyclerAnimatorOff
import kotlinx.android.synthetic.main.fragment_home.*




class HomeFragment : BaseFragment(), FeedAdapter.Listener{

    private lateinit var mViewModel: HomeViewModel
    private lateinit var mAdapter: FeedAdapter // recyclerView adapter для постов юзеров

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_home, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mViewModel = initViewModel()


        mAdapter = FeedAdapter(this)

        feed_recycler.adapter = mAdapter


        //отключаем анимацию отрисовки картинки
        recyclerAnimatorOff(feed_recycler)

        feed_recycler.layoutManager = LinearLayoutManager(context)


        /* если передадим fragment, как lifecyclerOwner, то мы будем передавать новый идентичный экземпляр Observer
         при каждом повторном присоединении фрагмента, но LiveData не удалит предыдущих наблюдателей,
         потому что LifecyclerOwner(Fragment) не достиг состояния DESTROYED.
         В конечном итоге это приводит к росту числа идентичных наблюдателей, работающих одновременно, и один и тот
         же код (updatePosts) выполняется несколько раз
         Рекомендуемое решение состоит в том, чтобы использовать жизненный цикл представления фрагмента с помощью
          getViewLifecycleOwner () или getViewLifecycleOwnerLiveData (), которые были добавлены в библиотеку
          поддержки 28.0.0 и AndroidX 1.0.0, так что LiveData будет удалять наблюдателей при
          каждом разрушении представления фрагмента:*/
        mViewModel.feedPosts.observe(viewLifecycleOwnerLiveData.value!!, Observer{ it?.let{ mAdapter.updatePosts(it) } })


        img.setOnClickListener {
            auth.signOut()

            FirebaseAuthManager().googlesignOut(getBaseActivity())
        }


    }


    //подгружаем лайкосики
    override fun loadLikes(id: String, position: Int){

        if(mViewModel.getLikes(id) == null){
            mViewModel.loadLikes(id).observe(viewLifecycleOwnerLiveData.value!!, Observer { it?.let{ postLikes ->
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
