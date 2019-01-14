package homeproject.example.com.myhomeproject.common

import android.content.Context
import com.google.android.gms.tasks.Task

interface AuthManager{
    fun signOut()
    fun signIn(email: String, password: String): Task<Unit>
    fun googlesignOut(context:Context): Task<Unit>
}