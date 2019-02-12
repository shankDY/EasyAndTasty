package com.shank.eat.screens.btm_navigation_screens.profile

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Recipe
import com.shank.eat.screens.common.BaseViewModel

class ProfileViewModel(onFailureListener: OnFailureListener,
                       private val recipesRepo: RecipesRepository,
                       private val usersRepo: UsersRepository
) : BaseViewModel(onFailureListener) {

    val user = usersRepo.getUser()
    var myRecipes: LiveData<List<Recipe>> = recipesRepo.getMyRecipes(usersRepo.currentUid()!!)

}
