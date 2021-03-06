package com.shank.eat.model

import com.google.firebase.database.Exclude

//добавили пусттые строки в значении. чтобы создался дефолтный пустой конструктор
//@Exclude - чтобы Firebase игнорировала данный параметр, не охраняла в бд
data class User (val name:String="",  val email:String="",
                 val follows: Map<String, Boolean> = emptyMap(),// подписки
                 val followers: Map<String, Boolean> = emptyMap(),//подписчики
                 val website:String?= null, val bio:String?= null, val phone:String? = null,
                 val photo: String? = null, @get:Exclude val uid: String = "")