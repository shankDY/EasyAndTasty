package com.shank.eat.screens.other.recipes

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Recipe
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.BaseViewModel

class RecipeViewModel(onFailureListener: OnFailureListener,
                      private val recipesRepo: RecipesRepository,
                      private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    //список комментов
    lateinit var recipe: LiveData<Recipe>
    // id поста
    private lateinit var postId: String
    private val uid = usersRepo.currentUid()

    fun init(postId: String) {
        if (!this::postId.isInitialized) {
            this.postId = postId
            //иннициализация списка комеентариев
            this.recipe = recipesRepo.getFeedPost(uid!!,postId)
        }
    }


    //загружаем список ингредиентов в бд
    fun createShopingList(
        nameRecipe: String?,
        calories: String?,
        ingredients: ArrayList<String>
    ) {
        if (nameRecipe!!.isNotEmpty() && calories!!.isNotEmpty() && ingredients.isNotEmpty()){

            recipesRepo.createShopingList(uid!!,mkShopingList(nameRecipe,calories, ingredients))
                .addOnFailureListener(onFailureListener)

        }
    }

    private fun mkShopingList(nameRecipe: String, calories: String, ingredients: ArrayList<String>): ShopingList =
            ShopingList(
                recipeName = nameRecipe,
                calories = calories,
                ingredients = ingredients
            )

    fun addFavorites(postId: String, postsAuthorUid: String) {
        recipesRepo.addFavorites(uid = uid!!, postId = postId, postsAuthorUid = postsAuthorUid)
    }
}
