package com.shank.eat.screens.btm_navigation_screens.home.recipes

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Recipe
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.BaseViewModel

class RecipeOpenViewModel(onFailureListener: OnFailureListener,
                          private val recipesRepo: RecipesRepository,
                          private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    //список комментов
    lateinit var recipe: LiveData<Recipe>
    // id поста
    private lateinit var postId: String

    fun init(postId: String) {
        if (!this::postId.isInitialized) {
            this.postId = postId
            //иннициализация списка комеентариев
            this.recipe = recipesRepo.getFeedPost(usersRepo.currentUid()!!,postId)
        }
    }


    //загружаем список ингредиентов в бд
    fun createShopingList(
        nameRecipe: String?,
        calories: String?,
        ingredients: ArrayList<String>
    ) {
        if (nameRecipe!!.isNotEmpty() && calories!!.isNotEmpty() && ingredients.isNotEmpty()){

            recipesRepo.createShopingList(usersRepo.currentUid()!!,mkShopingList(nameRecipe,calories, ingredients))
                .addOnFailureListener(onFailureListener)

        }
    }

    private fun mkShopingList(nameRecipe: String, calories: String, ingredients: ArrayList<String>): ShopingList =
            ShopingList(
                recipeName = nameRecipe,
                calories = calories,
                ingredients = ingredients
            )

    fun addFavorites(mRecipe: Recipe?) {
        recipesRepo.addFavorites(usersRepo.currentUid(),mRecipe)
    }
}
