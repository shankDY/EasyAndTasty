package com.shank.eat.screens.btm_navigation_screens.home

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.shank.eat.R
import com.shank.eat.model.Recipe
import com.shank.eat.screens.common.SimpleCallback
import com.shank.eat.screens.common.loadImage
import com.shank.eat.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.feed_item.view.*

//адаптер для постов
class FeedAdapter(private val listener: Listener) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    interface Listener{

        //подгружаем лакосики
        fun loadLikes(id: String, position: Int)

        //функция , которая позволит переключать лайки, вкл или выкл
        fun toogleLike(postId: String)
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    //список постов
    private var posts = listOf<Recipe>()

    // карта содержит позицию(индекс в списке) юзера и лайк
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()

    //дефолтные значения в карте
    private val defaultPostLikes = FeedPostLikes(0, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        //рендерим наши лайки(если они есть в карте, если нет берем дефолтные значения)
        val likes = postLikes[position]?: defaultPostLikes

        //обращаемся к viewHolder напрямую. Вызывает указанный функциональный блок с данным
        // приемником в качестве приемника и возвращает его результат.
        with(holder.view){
            user_photo_image.loadUserPhoto(posts[position].userPhoto)
            username_text.text = posts[position].username
            post_image.loadImage(posts[position].recipeImg)

            val bundle = Bundle()
            bundle.putString("postId", post.id)

            //переходим в recipe fragment
            post_image.setOnClickListener {
                findNavController().navigate(R.id.action_nav_item_home_to_recipeFragment,bundle)
            }

            //если лайков не, то прячим текст

            if (likes.likesCount == 0) {
                likes_text.visibility = View.GONE
            }else{
                likes_text.visibility = View.VISIBLE
                //для соглосовывания окончаний при разном количестве(1 like, 2 likes)
                val likesCountText = holder.view.context.resources.getQuantityString(
                    R.plurals.likes_count,likes.likesCount, likes.likesCount)

                likes_text.text = likesCountText

            }

            //лайкаем посты
            like_image.setOnClickListener{listener.toogleLike(post.id)}
            //если лайкнули , то ставим черное сердечко, если нет прозрачное
            like_image.setImageResource(
                if(likes.likedByUser) R.drawable.ic_favorite
                else R.drawable.ic_like)

            listener.loadLikes(post.id, position)

            //переходим в comments fragment
            comment_image.setOnClickListener {
                findNavController().navigate(R.id.action_nav_item_home_to_commentsFragment, bundle)
            }

        }


    }

    //количество постов
    override fun getItemCount() = posts.size



    //обновляем наш recicler с рецептами
    fun updatePosts(newPosts: List<Recipe>) {
        //считает разницу между старым и новым значением аргумента, находит те, что изменились
        // и перерисовываем только те, что изменились, а не весь адаптер
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.posts, newPosts) { it.id })

        //обновление постов
        this.posts = newPosts
        //diffResult внутри себя считает, какие позиции изменились и
        // затем обновляет наш recyclerView
        diffResult.dispatchUpdatesTo(this)
    }

    //обновляем наши лайки.(т.е если значение измениться, то отбразим это на нашей картев)
    fun updatePostLikes(position: Int, likes: FeedPostLikes) {

        //передаем в карту position и лайк
        postLikes += (position to likes)
        //говорим нашему viewHolder перерисовать вьюшку по позиции
        notifyItemChanged(position)
    }



}