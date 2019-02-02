package com.shank.eat.data

import android.arch.lifecycle.LiveData
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.shank.eat.model.User

interface UsersRepository {

    //проверка уникальности email
    fun isUserExistsForEmail(email: String): Task<Boolean>
    //создаем юзера в бд
    fun createUser(user: User, password: String): Task<Unit>
    //получаем юзера
    fun getUser(): LiveData<User>
    //получаем авторизованного в данный момент юзера
    fun getUser(uid: String): LiveData<User>
    //создаем запись в бд для авторизованного юзера через гугл аккаунт
    fun createGoogleUser(credential: AuthCredential?, user: User): Task<Unit>

    //загружаем фотку в хранилище и получаем публичный uri
    fun uploadUserImage(uid: String, imageUri: Uri): Task<Uri>
}