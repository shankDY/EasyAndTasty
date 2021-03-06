package com.shank.eat.screens.other.comments

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.model.Comment
import com.shank.eat.screens.common.SimpleCallback
import com.shank.eat.screens.common.loadUserPhoto
import com.shank.eat.screens.common.setCaptionText

import kotlinx.android.synthetic.main.items_comments.view.*

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    //список комментов
    private var comments = listOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_comments, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        with(holder.view) {
            photo.loadUserPhoto(comment.user_photo)
            text.setCaptionText(comment.username, comment.text, comment.timestampDate())
        }
    }

    //обновление комментариев
    fun updateComments(newComments: List<Comment>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(comments, newComments) {it.id})
        this.comments = newComments
        diffResult.dispatchUpdatesTo(this)
    }

    //размер списка с коментариями
    override fun getItemCount(): Int = comments.size
}