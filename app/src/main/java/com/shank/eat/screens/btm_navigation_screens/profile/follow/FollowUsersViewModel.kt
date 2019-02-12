package com.shank.eat.screens.btm_navigation_screens.profile.follow

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.data.common.map
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseViewModel

class FollowUsersViewModel(onFailureListener: OnFailureListener,
                           private val recipesRepo: RecipesRepository,
                           private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    //получаем список юзеров и их друзей, из viewModel
    val userAndFriends: LiveData<Pair<User, List<User>>> =
        usersRepo.getUsers().map{ allUsers ->

            /**
            отфильтруем наших юзеров(по uid). Чтобы узнать где наш, а где остальные

             * Разделяет исходную коллекцию на пару списков,
              * где * first * list содержит элементы, для которых [предикат] дал true,
              * while * second * list содержит элементы, для которых [предикат] дал `false`.

            если у юзера uid == нашему uid, то это наш пользователь
             **/
            val (userList, otherUsersList) = allUsers.partition {
                it.uid == usersRepo.currentUid()
            }
            userList.first() to otherUsersList
        }

    fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
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
