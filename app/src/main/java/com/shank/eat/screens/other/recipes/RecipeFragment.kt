package com.shank.eat.screens.other.recipes

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.model.Recipe
import com.shank.eat.screens.btm_navigation.add_recipe.AddRecipeFragment
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.loadImage
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : BaseFragment() {



    private lateinit var mViewModel: RecipeViewModel
    private var ingredients = arrayListOf<String>()
    private var recipeName:String? = null
    private var calories: String? = null
    private var mRecipe: Recipe? = null

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(AddRecipeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_recipe, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()


        //id нашего рецепта(получаем при клике на определенную карту)
        val postId = arguments?.getString("postId")



        //инициализируем наш postId - и получаем рецепт
        mViewModel.init(postId.toString())


        //как только получаем рецепт с бд. то проставляем данные в поля
        mViewModel.recipe.observe(viewLifecycleOwnerLiveData.value!!, Observer { it.let { recipe ->

            mRecipe = recipe

            recipe_image.loadImage(recipe?.recipeImg)
            name_recipe_text.text = recipe?.nameRecipe
            categories_text.text = recipe?.category
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
        } })


        listShop_btn.setOnClickListener{

            mViewModel.createShopingList(recipeName, calories ,ingredients)
        }



        back_img_recipe.setOnClickListener {
            findNavController().popBackStack()
        }

        floatImg_favorite.setOnClickListener {
            mViewModel.addFavorites(mRecipe)
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

        const val TAG = "RecipeFragment"
    }

}
