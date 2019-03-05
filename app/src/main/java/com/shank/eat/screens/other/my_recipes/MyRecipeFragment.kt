package com.shank.eat.screens.other.my_recipes

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.recyclerAnimatorOff
import kotlinx.android.synthetic.main.fragment_my_recipes.*

class MyRecipeFragment : BaseFragment(), MyRecypeAdapter.Listener{

    private lateinit var mAdapter: MyRecypeAdapter
    private lateinit var mViewModel: MyRecipesViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_my_recipes, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()


        mAdapter = MyRecypeAdapter(this)

        myRecipesRecyclerView.adapter = mAdapter

        //отключаем анимацию отрисовки картинки
        recyclerAnimatorOff(myRecipesRecyclerView)

        myRecipesRecyclerView.layoutManager = LinearLayoutManager(context)

        mViewModel.myRecipes.observe(viewLifecycleOwnerLiveData.value!!, Observer { it?.let {
            mAdapter.updatePosts(it)
        }})

    }



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
