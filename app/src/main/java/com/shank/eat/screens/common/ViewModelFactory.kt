package com.shank.eat.screens.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.screens.App
import com.shank.eat.screens.btm_navigation_screens.add_recipe.AddRecipeViewModel
import com.shank.eat.screens.btm_navigation_screens.home.HomeViewModel
import com.shank.eat.screens.btm_navigation_screens.shoping_lists.ShopingListsViewModel
import com.shank.eat.screens.btm_navigation_screens.home.comments.CommentsViewModel
import com.shank.eat.screens.btm_navigation_screens.home.recipes.RecipeOpenViewModel
import com.shank.eat.screens.register_and_login_screens.forgot_pass.ForgotPasswordViewModel
import com.shank.eat.screens.register_and_login_screens.login.LoginViewModel
import com.shank.eat.screens.register_and_login_screens.register.RegisterViewModel
import com.shank.eat.screens.btm_navigation_screens.shoping_lists.shoping_list_open.ShopingListOpenViewModel


//в данном классе будем ссылаться на бд(в нашем случае firebase), остальные классы не будут знать,
// какую бд мы используем
class ViewModelFactory(private val app: App,
                       private val commonViewModel: CommonViewModel,
                       private val onFailureListener: OnFailureListener) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val usersRepo = app.usersRepo
        val authManager = app.authManager
        val recipesRepo = app.resipesRepo

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(authManager,app,commonViewModel,onFailureListener,usersRepo) as T
        }else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
        }else if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)){
            return ForgotPasswordViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
        }else if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java)){
            return AddRecipeViewModel(commonViewModel,app,onFailureListener,recipesRepo,usersRepo) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(onFailureListener,recipesRepo,usersRepo) as T
        }else if (modelClass.isAssignableFrom(RecipeOpenViewModel::class.java)){
            return RecipeOpenViewModel(onFailureListener,recipesRepo,usersRepo) as T
        }else if (modelClass.isAssignableFrom(CommentsViewModel::class.java)){
            return CommentsViewModel(recipesRepo,usersRepo,onFailureListener,app) as T
        }else if (modelClass.isAssignableFrom(ShopingListsViewModel::class.java)){
            return ShopingListsViewModel(onFailureListener,recipesRepo,usersRepo) as T
        }else if (modelClass.isAssignableFrom(ShopingListOpenViewModel::class.java)){
            return ShopingListOpenViewModel(onFailureListener,recipesRepo,usersRepo) as T
        }else{
            error("Unknow view model class $modelClass")
        }
    }

}
