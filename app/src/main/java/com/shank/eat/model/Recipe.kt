package com.shank.eat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class Recipe(
    val uid: String = "", val nameRecipe: String = "", val username: String = "",
    val ingredients: List<String> = emptyList(), val instruction: String = "",
    val category: String = "", val cookingTime: String = "", val difficulty: String = "",
    val timestamp: Any = ServerValue.TIMESTAMP, val recipeImg: String? = null,
    val calories: String = "", val protein: String = "", val fat: String = "",
    val carbohydrates: String = "",val userPhoto: String? = null,
    @get:Exclude val id: String = "", @get:Exclude val commentsCount: Int = 0) {

    //получаем наш timeStamp кастим его к лонгу(best практика, в бд проставлять таймстемп,
    // чтобы не зависить от клиента)
    fun timestampDate(): Date = Date(timestamp as Long)
}