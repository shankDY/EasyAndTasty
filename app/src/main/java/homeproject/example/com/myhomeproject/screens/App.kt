package homeproject.example.com.myhomeproject.screens

import android.app.Application
import homeproject.example.com.myhomeproject.common.firebase.FirebaseAuthManager
import homeproject.example.com.myhomeproject.data.firebase.FirebaseRecipesRepository
import homeproject.example.com.myhomeproject.data.firebase.FirebaseUsersRepository

class App : Application() {

    val usersRepo by lazy { FirebaseUsersRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val resipesRepo by lazy { FirebaseRecipesRepository() }
}