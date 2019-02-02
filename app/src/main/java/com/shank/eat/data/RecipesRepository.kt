package com.shank.eat.data

import android.arch.lifecycle.LiveData
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.Task
import com.shank.eat.model.Comment
import com.shank.eat.model.Recipe

interface RecipesRepository {

    //создаем рецепт
    fun createRecipe(uid: String, recipe: Recipe): Task<Unit>

    fun clearRecipeData(ingredientsViewList: ArrayList<View>, ingredients_text: ArrayList<String>,
                        linear_ingredients: LinearLayout?,imageView: ImageView,vararg inputs: EditText)


    //отчищаем поля и соответствующие им списки
    fun getFeedPosts(uid: String): LiveData<List<Recipe>>

    //читаем лайкт
    fun getLikes(postId: String): LiveData<List<FeedPostLike>>

    //переключение лайков
    fun toogleLike(postId: String, uid: String): Task<Unit>

    //загружаем комментарии в бд
    fun createComment(postId: String, comment: Comment): Task<Unit>

    //вычитываем комментарии с бд
    fun getComments(postId: String): LiveData<List<Comment>>
}

data class FeedPostLike(val userId: String)