package homeproject.example.com.myhomeproject.data.firebase

import homeproject.example.com.myhomeproject.data.UsersRepository
import homeproject.example.com.myhomeproject.data.firebase.common.auth
import homeproject.example.com.myhomeproject.data.firebase.common.database
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import homeproject.example.com.myhomeproject.common.toUnit
import homeproject.example.com.myhomeproject.model.User

class FirebaseUsersRepository : UsersRepository {

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
}