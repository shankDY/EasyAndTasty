package homeproject.example.com.myhomeproject.data

import android.arch.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import homeproject.example.com.myhomeproject.model.User

interface UsersRepository {
    fun isUserExistsForEmail(email: String): Task<Boolean>
    fun createUser(user: User, password: String): Task<Unit>
    fun getUser(): LiveData<User>
    fun getUser(uid: String): LiveData<User>
    fun currentUid(): String?
    fun createGoogleUser(credential: AuthCredential?): Task<Unit>
}