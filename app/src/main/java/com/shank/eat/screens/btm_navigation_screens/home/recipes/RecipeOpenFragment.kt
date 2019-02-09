package com.shank.eat.screens.btm_navigation_screens.home.recipes

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.add_recipe.AddRecipeFragment
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadImage
import com.shank.eat.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.recipe_fragment.*


class RecipeOpenFragment : BaseFragment() {



    private lateinit var mOpenViewModel: RecipeOpenViewModel
    private var ingredients = arrayListOf<String>()
    private var recipeName:String? = null
    private var calories: String? = null

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(AddRecipeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.recipe_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mOpenViewModel = initViewModel()


        //id нашего рецепта(получаем при клике на определенную карту)
        val postId = arguments!!.getString("postId")?: findNavController().popBackStack()



        //инициализируем наш postId - и получаем рецепт
        mOpenViewModel.init(postId.toString())


        //как только получаем рецепт с бд. то проставляем данные в поля
        mOpenViewModel.recipe.observe(viewLifecycleOwnerLiveData.value!!, Observer { it.let {recipe ->

                recipe_image.loadImage(recipe?.recipeImg)
                name_recipe_text.text = recipe?.nameRecipe
                categories_text.text = recipe?.calories
                recipe_difficulty_text.text = recipe?.difficulty
                coocking_time_text.text = recipe?.cookingTime
                calories_text.text = recipe?.calories
                protein_text.text = recipe?.protein
                fat_text.text = recipe?.fat
                carbohydrates_text.text = recipe?.carbohydrates
                instruction_text.text = recipe?.instruction

                //данные для списка покупок
                ingredients.addAll(recipe?.ingredients!!)
                recipeName = recipe.nameRecipe
                calories = recipe.calories

                initRecyclerIngredients(recipe.ingredients)
            }
        })


        listShop_btn.setOnClickListener{

            mOpenViewModel.createShopingList(recipeName, calories ,ingredients)
        }

        back_img_recipe.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    //инициализация recyclerView(список ингредиентов)
    private fun initRecyclerIngredients(ingredients: List<String>) {
        //инициализируем адаптер
        val adapter = IngredientsRecipeAdapter(ingredients)

        //отвечает за функциональность нашего recyclerView
        recycler_ingredients.layoutManager = LinearLayoutManager(context)

        //подключаем адаптер в нашем recyclerView
        recycler_ingredients.adapter = adapter
    }


    companion object {

        const val TAG = "RecipeOpenFragment"
    }
}
