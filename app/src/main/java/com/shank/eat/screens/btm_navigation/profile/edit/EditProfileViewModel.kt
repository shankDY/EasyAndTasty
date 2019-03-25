package com.shank.eat.screens.btm_navigation.profile.edit

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseViewModel

class EditProfileViewModel(onFailureListener: OnFailureListener,
                           val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    val user = usersRepo.getUser()

    //изменяем данные пользователя
    fun updateUserProfile(currentUser: User, newUser: User): Task<Unit> =
        usersRepo.updateUserProfile(currentUser = currentUser, newUser = newUser)
            .addOnFailureListener(onFailureListener)

    //изменяем email пользователя
    fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit> =
        usersRepo.updateEmail(
            currentEmail = currentEmail,
            newEmail = newEmail,
            password = password).addOnFailureListener(onFailureListener)


}
