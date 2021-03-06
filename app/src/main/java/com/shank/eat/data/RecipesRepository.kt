package com.shank.eat.data

import android.arch.lifecycle.LiveData
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.Task
import com.shank.eat.model.Comment
import com.shank.eat.model.Recipe
import com.shank.eat.model.ShopingList

interface RecipesRepository {
    //создаем рецепт
    fun createRecipe(uid: String, recipe: Recipe): Task<Unit>
    //отчищаем поля заполненные  и списки
    fun clearRecipeData(ingredientsViewList: ArrayList<View>, ingredients_text: ArrayList<String>,
                        linear_ingredients: LinearLayout?,imageView: ImageView,vararg inputs: EditText)
    //получаем рецепты пользователей
    fun getFeedPosts(uid: String): LiveData<List<Recipe>>
    //получаем конкретный рецепт пользователя
    fun getFeedPost(uid: String, postId: String): LiveData<Recipe>
    //получаем все рецепты, конкретного юзера
    fun getMyRecipes(uid: String): LiveData<List<Recipe>>
    //читаем лайкт
    fun getLikes(postId: String): LiveData<List<FeedPostLike>>
    //переключение лайков
    fun toogleLike(postId: String, uid: String): Task<Unit>
    //загружаем комментарии в бд
    fun createComment(postId: String, comment: Comment): Task<Unit>
    //вычитываем комментарии с бд
    fun getComments(postId: String): LiveData<List<Comment>>
    //загружаем список в бд
    fun createShopingList(uid: String, shopingList: ShopingList): Task<Unit>
    //получем список всех покупок юзера
    fun getShopingLists(uid: String): LiveData<List<ShopingList?>>
    //получаем конкретный список , конкретного юзера
    fun getShopingList(uid: String, idShopingList: String): LiveData<ShopingList?>
    //копируем посты юзера накоторого подписались себе в ленту
    fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    //удаляем посты юзера, от которого отписались
    fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    //добавляем рецепт в любимые рецепты
    fun addFavorites(uid: String, postId: String, postsAuthorUid: String):Task<Unit>
    //получаем список любимых рецептов юзера
    fun getFavoriteRecipes(uid: String): LiveData<List<Recipe>>
    //получаем конкретный рецепт в ноде любимых рецептов пользователя
    fun getFavoriteRecipe(uid: String, postId: String): LiveData<Recipe>
}

data class FeedPostLike(val userId: String)