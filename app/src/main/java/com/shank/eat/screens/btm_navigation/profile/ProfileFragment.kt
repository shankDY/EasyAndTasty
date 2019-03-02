package com.shank.eat.screens.btm_navigation.profile

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadUserPhoto
import com.shank.eat.screens.common.recyclerAnimatorOff
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), MyRecypeAdapter.Listener{

    private lateinit var mAdapter: MyRecypeAdapter
    private lateinit var mViewModel: ProfileViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_profile, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()
        //инициализация табов
        initTabs()
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


        mAdapter = MyRecypeAdapter(this)

        myRecipesRecyclerView.adapter = mAdapter

        //отключаем анимацию отрисовки картинки
        recyclerAnimatorOff(myRecipesRecyclerView)

        myRecipesRecyclerView.layoutManager = LinearLayoutManager(context)

        mViewModel.myRecipes.observe(viewLifecycleOwnerLiveData.value!!, Observer { it?.let {
            mAdapter.updatePosts(it)
        }})

    }

    private fun initTabs() {
        tabs_profile.addTab(tabs_profile.newTab().setText("Мои рецепты").setIcon(R.drawable.ic_my_recipes))
        tabs_profile.addTab(tabs_profile.newTab().setText("Избранное").setIcon(R.drawable.ic_favorite))
        tabs_profile.addTab(tabs_profile.newTab().setText("Поиск людей").setIcon(R.drawable.ic_add_friends))

        //handling tab click event
        tabs_profile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG,tab.position.toString())
                when(tab.position){
                    1 -> findNavController().navigate(R.id.action_nav_item_profile_to_favorite_RecipesFragment)
                    2 -> findNavController().navigate(R.id.action_nav_item_profile_to_followUsersFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

//  private  fun setOnSelectedView(position: Int) {
//        val tab = tabs_profile.getTabAt(position)
//        if (tab != null){
//            if (tab.isSelected){
//                findNavController().navigate(R.id.action_nav_item_profile_to_favorite_RecipesFragment)
//            }
//        }
//    }

    //подгружаем лайкосики
    override fun loadLikes(id: String, position: Int){

        if(mViewModel.getLikes(id) == null){
            mViewModel.loadLikes(id).observe(viewLifecycleOwner, Observer { it?.let{ postLikes ->
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
        const val TAG = "ProfileFragment"
    }
}
