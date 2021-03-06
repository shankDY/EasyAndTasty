package com.shank.eat.screens.btm_navigation.home

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.data.common.map
import com.shank.eat.model.Recipe
import com.shank.eat.screens.common.BaseViewModel

class HomeViewModel(onFailureListener: OnFailureListener,
                    private val recipesRepo: RecipesRepository,
                    usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    private var  uid = usersRepo.currentUid()
    var feedPosts: LiveData<List<Recipe>>

    //карта лайков, где ключ uid , value - лайки
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()

   init {

        //загружает feedposts и сортирует их по дате добавления
        feedPosts = recipesRepo.getFeedPosts(uid!!).map {
            //сортируем посты по дате добавления
            it.sortedByDescending { it.timestampDate() }
        }
   }




    //считываем лайки юзеров
    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    //подгружаем лайки
    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (loadedLikes[postId] == null){
            val liveData = recipesRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                    //id юзеров, пролайковших пост(количество id будет соответствовать
                    // количеству лайков)
                    likesCount = likes.size,

                    //если id юзера находится в множестве id , кто пролайкал этот пост,
                    // значит мы пролайкали этот пост
                    likedByUser = likes.find { it.userId == uid } != null)
            }
            //заполняем нашу карту
            loadedLikes = loadedLikes + (postId to liveData)
            return liveData
        }else{

            return existingLoadedLikes!!
        }

    }


    //переключатель лайков
    fun toogleLike(postId: String) {

        recipesRepo.toogleLike(postId, uid!!).addOnFailureListener(onFailureListener)
    }




}
