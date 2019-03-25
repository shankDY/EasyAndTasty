package com.shank.eat.screens.other.people.side_profile_view

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Recipe
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseViewModel

class SideProfileViewViewModel(onFailureListener: OnFailureListener,
                               private val recipesRepo: RecipesRepository,
                               private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    //получаем пользователя
    lateinit var user: LiveData<User>
    lateinit var recipes: LiveData<List<Recipe>>
    //наш авторизированный пользователь
    val currentUid: String = usersRepo.currentUid()!!
    //uid пользователя, данные которого мы хотим получить
    lateinit var userId: String

    fun init(uid: String){
        if (!this::userId.isInitialized){
            this.userId = uid
            this.user = usersRepo.getUser(userId)
            this.recipes = recipesRepo.getMyRecipes(userId)
        }
    }

    fun setFollow(uid: String, follow: Boolean): Task<Void> {
        // currentUid наш авторизованный пользователь
        // whenAll, данная функция выполнит все таски паралельно
        return (if (follow){
            Tasks.whenAll(
                usersRepo.addFollow(currentUid, uid),
                usersRepo.addFollower(currentUid, uid),
                recipesRepo.copyFeedPosts(postsAuthorUid = uid, uid = currentUid))
        }else{
            Tasks.whenAll(
                usersRepo.deleteFollow(currentUid, uid),
                usersRepo.deleteFollower(currentUid, uid),
                recipesRepo.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid))
        }).addOnFailureListener(onFailureListener)
    }



}
