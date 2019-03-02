package com.shank.eat.screens.other_screens.favorite_recipes

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.home.HomeFragment
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.recyclerAnimatorOff
import kotlinx.android.synthetic.main.fragment_favorite_recipes.*

class Favorite_RecipesFragment : BaseFragment(), FavoritesAdapter.Listener {

    private lateinit var mViewModel: FavoriteRecipesViewModel
    private lateinit var mAdapter: FavoritesAdapter // recyclerView adapter для постов юзеров

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(HomeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_favorite_recipes, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //инициализация viewModel
        mViewModel = initViewModel()
        //инициализация recyclerAdapter
        mAdapter = FavoritesAdapter(this)

        back_img.setOnClickListener {
            findNavController().popBackStack()
        }

        //подключение адаптера к recyclerView
        favoriteRecyclerView.adapter = mAdapter
        //отключаем анимацию отрисовки картинки
        recyclerAnimatorOff(favoriteRecyclerView)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(context)

        mViewModel.favorites.observe(viewLifecycleOwnerLiveData.value!!, Observer{ it?.let{ mAdapter.updatePosts(it) } })

    }

    //подгружаем лайкосики
    override fun loadLikes(id: String, position: Int){

        if(mViewModel.getLikes(id) == null){
            mViewModel.loadLikes(id).observe(viewLifecycleOwnerLiveData.value!!, Observer { it?.let{ postLikes ->
                mAdapter.updatePostLikes(position, postLikes)
            } })
        }
    }

    //переключатель лайк
    override fun toogleLike(postId: String) {
        Log.d(TAG, "toogleLike: $postId")
        mViewModel.toogleLike(postId)
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

    companion object {
        const val TAG = "Favorite_RecipesFragment"
    }

}
