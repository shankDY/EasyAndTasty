package com.shank.eat.data.firebase

import android.arch.lifecycle.LiveData
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.shank.eat.common.TaskSourceOnCompleteListener
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
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.loadImage

class FirebaseRecipesRepository : RecipesRepository {


    //создаем рецепт
    override fun createRecipe(uid: String, recipe: Recipe): Task<Unit> {
        val reference = database.child("users-recipes").child(uid).push()
        return reference.setValue(recipe).toUnit()
    }


    //получаем рецепты юзеров
    override fun getFeedPosts(uid: String): LiveData<List<Recipe>> =
        FirebaseLiveData(database.child("users-recipes").child(uid)).map{
            it.children.map { it.asRecipe()!! }
        }

    //получаем  1 Рецепт юзера
    override fun getFeedPost(uid: String, postId: String): LiveData<Recipe> =
        FirebaseLiveData(database.child("users-recipes").child(uid).child(postId)).map{
            it.asRecipe()!!
        }

    //копируем посты юзеров на которые подписался
    override fun getMyRecipes(uid: String): LiveData<List<Recipe>> =
    //вычитаем посты юзера, для дальнейшей работы с ними(показать в профиле)
        FirebaseLiveData(database.child("users-recipes").child(uid)
            //фильтруем посты таким образом, чтобы скопировать только те посты,
            // который соответствует нашему юзеру
            .orderByChild("uid")
            .equalTo(uid)).map {
            it.children.map { it.asRecipe()!! }
        }

    //добавляем рецепт в избранное
    override fun addFavorites(uid: String?, recipe: Recipe?) =
        database.child("favorite-recipes").child(uid!!).push().setValue(recipe).toUnit()

    //получаем список любимых рецептов юзера
    override fun getFavoriteRecipes(uid:String): LiveData<List<Recipe>> =
        FirebaseLiveData(database.child("favorite-recipes").child(uid)).map {
            it.children.map {
                it.asRecipe()!!
            }
        }

    //получаем конкретный рецепт в ноде любимых рецептов пользователя
    override fun getFavoriteRecipe(uid:String, postId: String): LiveData<Recipe> =
        FirebaseLiveData(database.child("favorite-recipes").child(uid).child(postId)).map {
            it.asRecipe()!!
        }

    //копируем посты юзеров на которые подписался
    override fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task { taskSource ->

            //вычитаем посты юзеров, для дальнейшей работы с ними(например показ в ленте)
            database.child("users-recipes").child(postsAuthorUid)
                //фильтруем посты таким образом, чтобы скопировать только те посты,
                // который написал автор поста
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    //карта, которая содержит посты юзеров
                    val postsMap = it.children.map { it.key to it.value }.toMap()

                    //вычитываем посты юзера на которого подписался пользователь и
                    // кладем на ленту юзеру, который подписался
                    database.child("users-recipes").child(uid)
                        .updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }

    override fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task { taskSource ->

            //вычитаем все feed-посты нашего uid, у которых childUid равен postAuthorUid
            // и удаляем их
            database.child("users-recipes").child(postsAuthorUid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    //карта, которая содержит посты юзеров
                    val postsMap = it.children.map { it.key to null }.toMap()

                    //удаляем посты юзверя, от которого отписался пользователь
                    database.child("users-recipes").child(uid)
                        .updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
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
            it.children.map {
                it.asComment()!!
            }
        }

    //функция расширения, с помощью которой получаем замапенный список постов, где id поста - ключ
    private fun DataSnapshot.asRecipe(): Recipe? =
        getValue(Recipe::class.java)?.copy(id = key!!)

    // функция расширения, с помощью которой получаем замапенный список комментов,
    // где id комента - ключ
    fun DataSnapshot.asComment(): Comment? =
        getValue(Comment::class.java)?.copy(id = key!!)



    //создаем список покупок
    override fun createShopingList(uid: String, shopingList:ShopingList): Task<Unit> =
            database.child("shopingLists").child(uid).push().setValue(shopingList).toUnit()


    //получаем список всех покупок юзера
    override fun getShopingLists(uid: String): LiveData<List<ShopingList?>> =
            FirebaseLiveData(database.child("shopingLists").child(uid)).map {
                it.children.map { it.asShopingList() }
            }

    //получаем конкретный список юзера
    override fun getShopingList(uid:String, idShopingList:String) =
            FirebaseLiveData(database.child("shopingLists").child(uid).child(idShopingList)).map {
                it.asShopingList()
            }


    // функция расширения, с помощью которой получаем замапенный список комментов,
    // где id списка - ключ полученный из бд.
    fun DataSnapshot.asShopingList(): ShopingList? =
            getValue(ShopingList::class.java)?.copy(id = key!!)

}
