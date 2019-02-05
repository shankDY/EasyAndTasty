package com.shank.eat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class ShopingList(val recipeName:String = "", val ingredients: List<String> = listOf(),
                       val calories:String = "", val timestamp: Any = ServerValue.TIMESTAMP,
                       @get:Exclude val id: String = ""){

    //получаем наш timeStamp кастим его к лонгу(best практика, в бд проставлять таймстемп,
    // чтобы не зависить от клиента)
    fun timestampDate(): Date = Date(timestamp as Long)
}

