package homeproject.example.com.myhomeproject.common.firebase

import com.google.android.gms.tasks.Task
import homeproject.example.com.myhomeproject.common.AuthManager
import homeproject.example.com.myhomeproject.common.toUnit
import homeproject.example.com.myhomeproject.data.firebase.common.auth


//реализация authManager(выход из аккаунта)
class FirebaseAuthManager: AuthManager {

    //регистрация профиля
    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()

    //выход из профиля
    override fun signOut() { auth.signOut() }
}