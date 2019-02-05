package com.shank.eat.data.firebase

import android.arch.lifecycle.LiveData
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.DataSnapshot
import com.shank.eat.common.toUnit
import com.shank.eat.data.UsersRepository
import com.shank.eat.data.common.map
import com.shank.eat.data.firebase.common.*
import com.shank.eat.model.User

class FirebaseUsersRepository : UsersRepository {


    //создаем запись в бд для авторизованного юзера через гугл аккаунт
    override fun createGoogleUser(credential: AuthCredential?, user: User): Task<Unit> =

        // авторизуемся в системе с помощью токена с гугл аккаунта
        auth.signInWithCredential(credential!!).onSuccessTask {
            val gUser = auth.currentUser
            database.child("users").child(gUser?.uid!!).setValue(user)
        }.toUnit()


    //создаем юзверя(регистрируем пользователя в нашей системе)
    override fun createUser(user: User, password: String): Task<Unit> =
            auth.createUserWithEmailAndPassword(user.email,password).onSuccessTask {
                database.child("users").child(it!!.user.uid).setValue(user)
            }.toUnit()


    //проверка уникальности email
    override fun isUserExistsForEmail(email: String): Task<Boolean> =
            auth.fetchSignInMethodsForEmail(email).onSuccessTask {
                val signInMethods = it?.signInMethods?: emptyList<String>()
                Tasks.forResult(signInMethods.isNotEmpty())
            }


    //таким образом мы получаем аторизованного  юзера
    override fun getUser(): LiveData<User> = getUser(currentUid()!!)

    //и мапим их к юзеру.(т.е получаем авторизованного в данный момент юзера)
    override fun getUser(uid: String): LiveData<User> =
        database.child("users").child(uid).liveData().map { it.asUser()!! }

    //ссылка на авторизованного юзера
    override fun currentUid() = auth.currentUser?.uid



    // функция расширения, с помощью которой получаем замапенный список юзеров, где uid -ключ(uid юзера из User,
    //будет соответствовать uid полученного с бд юзера )
    private fun DataSnapshot.asUser(): User? =
        getValue(User::class.java)?.copy(uid = key!!)



    //загружаем фотки юзеров в сторедж(посты)
    override fun uploadUserImage(uid: String, imageUri: Uri): Task<Uri> {

        //upload image to user folder <- storage
        //lastPathSegment - имя файла
        val usersStorageReference = storage.child("users")
            .child(uid).child("images").child(imageUri.lastPathSegment)

        //загржуаем фотку по нужному адрессу и берем публичный url для нашей фотки,
        // чтобы запостить ее
        return usersStorageReference.putFile(imageUri).onSuccessTask { it ->
            usersStorageReference.downloadUrl.addOnSuccessListener {
                Tasks.forResult(it)
            }
        }
    }
}
