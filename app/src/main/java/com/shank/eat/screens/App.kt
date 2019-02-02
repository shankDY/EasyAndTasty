package com.shank.eat.screens

import android.app.Application
import com.shank.eat.common.firebase.FirebaseAuthManager
import com.shank.eat.data.firebase.FirebaseRecipesRepository
import com.shank.eat.data.firebase.FirebaseUsersRepository

class App : Application() {

    val usersRepo by lazy { FirebaseUsersRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val resipesRepo by lazy { FirebaseRecipesRepository() }
}