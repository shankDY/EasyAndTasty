package com.shank.eat.screens.other.comments

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.R
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Comment
import com.shank.eat.model.User
import com.shank.eat.screens.App
import com.shank.eat.screens.common.BaseViewModel
import com.shank.eat.screens.common.showToast

class CommentsViewModel(private val recipesRepo: RecipesRepository,
                        usersRepo: UsersRepository,
                        onFailureListener: OnFailureListener, val app:App): BaseViewModel(onFailureListener) {
    //список комментов
    lateinit var comments: LiveData<List<Comment>>
    //наш юзер
    val user: LiveData<User> = usersRepo.getUser()
    // id поста
    private lateinit var postId: String

    fun init(postId: String) {
        if (!this::postId.isInitialized) {
            this.postId = postId
            //иннициализация списка комеентариев
            comments = recipesRepo.getComments(postId)
        }
    }

    fun createComment(text: String, user: User){
        if (text.isNotEmpty()){

            val comment = Comment(
                uid = user.uid,
                username = user.name,
                user_photo = user.photo,
                text = text)
            recipesRepo.createComment(postId,comment).addOnFailureListener(onFailureListener)
        }else{
            app.showToast(app.getString(R.string.add_comments))
        }
    }

}