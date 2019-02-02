package com.shank.eat.data.firebase

import android.arch.lifecycle.LiveData
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.shank.eat.common.ValueEventListenerAdapter
import com.shank.eat.common.task
import com.shank.eat.common.toUnit
import com.shank.eat.data.FeedPostLike
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.common.map
import com.shank.eat.data.firebase.common.FirebaseLiveData
import com.shank.eat.data.firebase.common.database
import com.shank.eat.model.Comment
import com.shank.eat.model.Recipe
import com.shank.eat.screens.common.loadImage

class FirebaseRecipesRepository : RecipesRepository {


    //создаем рецепт
    override fun createRecipe(uid: String, recipe: Recipe): Task<Unit> {
        val reference = database.child("users-recipes").child(uid).push()
        return reference.setValue(recipe).toUnit()
    }


    //получаем Feedposts юзера
    override fun getFeedPosts(uid: String): LiveData<List<Recipe>> =
        FirebaseLiveData(database.child("users-recipes").child(uid)).map{
            it.children.map { it.asRecipe()!! }
        }


    //отчищаем поля(EditText) и списки
    override fun clearRecipeData(ingredientsViewList: ArrayList<View>, ingredients_text: ArrayList<String>,
                                 linear_ingredients: LinearLayout?,imageView: ImageView, vararg inputs: EditText) {


        //если список вьюх не пустой, то отчищаем его и удаляем из контейнера
        if (ingredientsViewList.isNotEmpty()){

            //отчищаем список view
            ingredientsViewList.clear()

            //удаляем динамически добавленные элементы
            linear_ingredients?.removeAllViews()

            //загружаем дефолтную картинку
            imageView.loadImage(null)
        }

        if (ingredients_text.isNotEmpty()){

            //отчищаем EditText поля
            for (i in inputs){
                i.text.clear()
            }

            //отчищаем список введенных пользователем данных
            ingredients_text.clear()
        }
    }



    //читаем лайки
    override fun getLikes(postId: String): LiveData<List<FeedPostLike>> =
        FirebaseLiveData(database.child("likes").child(postId)).map {
            //когда мы вычитываем посты, то нам приходя ключи от uid
            //берем всех юзеров и мапим на FeedPostLike(делаем из них класс,
            // чтобы не возвращть просто стринг)
            it.children.map { FeedPostLike(it.key!!) }
        }

    //переключатель лайков
    override fun toogleLike(postId: String, uid: String): Task<Unit> {
        //получаем ссылку на место хранения лайков
        val reference = database.child("likes").child(postId).child(uid)
        return task { taskSource ->
            reference.addListenerForSingleValueEvent(ValueEventListenerAdapter { like ->
                //если нода существует(поставили уже лайк), то удаляем лайк, иначе
                // записываем его в бд
                if (!like.exists()){
                    reference.setValue(true)
                }else{
                    reference.removeValue()
                }
                taskSource.setResult(Unit)
            })
        }
    }



    //создаем комментарий
    override fun createComment(postId: String, comment: Comment): Task<Unit> =
        database.child("comments").child(postId).push().setValue(comment).toUnit()

    //вычитываем наши комменты
    override fun getComments(postId: String): LiveData<List<Comment>> =
        FirebaseLiveData(database.child("comments").child(postId)).map {
            //получили список коментиков и замапили его
            it.children.map {it.asComment()!!}
        }



    //функция расширения, с помощью которой получаем замапенный список постов, где id поста - ключ
    private fun DataSnapshot.asRecipe(): Recipe? =
        getValue(Recipe::class.java)?.copy(id = key!!)

    // // функция расширения, с помощью которой получаем замапенный список комментов,
    // где id комента - ключ
    fun DataSnapshot.asComment(): Comment? =
        getValue(Comment::class.java)?.copy(id = key!!)


}