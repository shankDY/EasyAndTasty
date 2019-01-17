package homeproject.example.com.myhomeproject.data.firebase

import android.arch.lifecycle.LiveData
import homeproject.example.com.myhomeproject.data.UsersRepository
import homeproject.example.com.myhomeproject.data.firebase.common.auth
import homeproject.example.com.myhomeproject.data.firebase.common.database
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.DataSnapshot
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.common.toUnit
import homeproject.example.com.myhomeproject.data.common.map
import homeproject.example.com.myhomeproject.data.firebase.common.liveData
import homeproject.example.com.myhomeproject.model.User

class FirebaseUsersRepository : UsersRepository {


    override fun createGoogleUser(credential: AuthCredential?): Task<Unit> =

        // обмениваем токен с гугл аккаунта пользователя на данные , которые помещаем в фаербейс
        auth.signInWithCredential(credential!!).onSuccessTask {
            val gUser = auth.currentUser
            database.child("users").child(gUser?.uid!!).setValue(
                mkGoogleUser(gUser.displayName!!, gUser.email!!))
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


    override fun getUser(): LiveData<User> = getUser(currentUid()!!)

    //получаем юзера по uid
    override fun getUser(uid: String): LiveData<User> =
        database.child("users").child(uid).liveData().map { it.asUser()!! }

    //ссылка на авторизованного юзера
    override fun currentUid() = auth.currentUser?.uid


    // функция расширения, с помощью которой получаем замапенный список юзеров, где uid -ключ
    private fun DataSnapshot.asUser(): User? =
        getValue(User::class.java)?.copy(uid = key!!)


    //заполняем данные нашего data class
    private fun mkGoogleUser(fullname: String, email: String): User {
        return User(name = fullname, email = email)
    }
}