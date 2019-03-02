package com.shank.eat.screens.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.screens.App
import com.shank.eat.screens.btm_navigation_screens.add_recipe.AddRecipeViewModel
import com.shank.eat.screens.btm_navigation_screens.home.HomeViewModel
import com.shank.eat.screens.btm_navigation_screens.home.comments.CommentsViewModel
import com.shank.eat.screens.btm_navigation_screens.home.recipes.RecipeOpenViewModel
import com.shank.eat.screens.btm_navigation_screens.profile.ProfileViewModel
import com.shank.eat.screens.btm_navigation_screens.profile.favorite_recipes.FavoriteRecipesViewModel
import com.shank.eat.screens.btm_navigation_screens.profile.follow.FollowUsersViewModel
import com.shank.eat.screens.btm_navigation_screens.shoping_lists.ShopingListsViewModel
import com.shank.eat.screens.btm_navigation_screens.shoping_lists.shoping_list_open.ShopingListOpenViewModel
import com.shank.eat.screens.register_and_login_screens.forgot_pass.ForgotPasswordViewModel
import com.shank.eat.screens.register_and_login_screens.login.LoginViewModel
import com.shank.eat.screens.register_and_login_screens.register.RegisterViewModel


//в данном классе будем ссылаться на бд(в нашем случае firebase), остальные классы не будут знать,
// какую бд мы используем
class ViewModelFactory(private val app: App,
                       private val commonViewModel: CommonViewModel,
                       private val onFailureListener: OnFailureListener) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val usersRepo = app.usersRepo
        val authManager = app.authManager
        val recipesRepo = app.resipesRepo

        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(authManager,app,commonViewModel,onFailureListener,usersRepo) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) ->
                ForgotPasswordViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
            modelClass.isAssignableFrom(AddRecipeViewModel::class.java) ->
                AddRecipeViewModel(commonViewModel,app,onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(RecipeOpenViewModel::class.java) ->
                RecipeOpenViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(CommentsViewModel::class.java) ->
                CommentsViewModel(recipesRepo,usersRepo,onFailureListener,app) as T
            modelClass.isAssignableFrom(ShopingListsViewModel::class.java) ->
                ShopingListsViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(ShopingListOpenViewModel::class.java) ->
                ShopingListOpenViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(FavoriteRecipesViewModel::class.java) ->
                FavoriteRecipesViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(FollowUsersViewModel::class.java) ->
                FollowUsersViewModel(onFailureListener,recipesRepo,usersRepo) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(onFailureListener,recipesRepo,usersRepo) as T
            else -> error("Unknow view model class $modelClass")
        }
    }

}
