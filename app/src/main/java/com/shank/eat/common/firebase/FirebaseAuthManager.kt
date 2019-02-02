package com.shank.eat.common.firebase

import android.content.Context
import com.google.android.gms.tasks.Task
import com.shank.eat.common.AuthManager
import com.shank.eat.common.toUnit
import com.shank.eat.data.firebase.common.auth
import com.shank.eat.data.firebase.common.googleSignInClient


//реализация authManager(выход из аккаунта)
class FirebaseAuthManager: AuthManager {


    //регистрация профиля
    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()

    //выход из профиля
    override fun signOut() { auth.signOut() }

    override fun googlesignOut(context: Context)  = googleSignInClient(context).signOut().toUnit()

}