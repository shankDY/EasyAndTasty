package com.shank.eat.screens.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.add_recipe.AddRecipeFragment
import com.shank.eat.screens.common.BaseFragment
import kotlinx.android.synthetic.main.recipe_fragment.*


class RecipeFragment : BaseFragment() {


    private lateinit var mViewModel: RecipeViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(AddRecipeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.recipe_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()

        val postId = arguments!!.getString("postId")?: findNavController().popBackStack()

        textM.text = postId.toString()

    }


    companion object {

        const val TAG = "RecipeFragment"
    }
}
