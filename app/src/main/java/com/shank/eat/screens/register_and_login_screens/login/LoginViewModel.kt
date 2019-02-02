package com.shank.eat.screens.register_and_login_screens.login

import android.app.Application
import android.arch.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.GoogleAuthProvider
import com.shank.eat.R
import com.shank.eat.common.AuthManager
import com.shank.eat.common.SingleLiveEvent
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseViewModel
import com.shank.eat.screens.common.CommonViewModel

class LoginViewModel(private  val authManager: AuthManager, private val app: Application,
                     private val commonViewModel: CommonViewModel, onFailureListener: OnFailureListener,
                     private val usersRepo: UsersRepository): BaseViewModel(onFailureListener) {

    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    //ссылка на переход на homeActivity
    val goToHomeScreen: LiveData<Unit> = _goToHomeScreen



    //данная функция вызываеися, когда мы кликаем на кнопку login и авторизуемся в системе,
    fun onLoginClick(email: String, password: String) {
        //если юзер авторизован прозваниваем goToHomeScreen
        if (validate(email, password)) {
            authManager.signIn(email, password).addOnSuccessListener {
                _goToHomeScreen.call()
            }.addOnFailureListener(onFailureListener)
        } else {
            //application - context, делаем так, т.к ViewModel не должна иметь ссылку на активити
            commonViewModel.setErrorMessage(app.getString(R.string.enter_email_and_password))
        }
    }

    //проверка пустой пороль и емаил или нет
    private fun validate(email:String,pass:String):Boolean = email.isNotEmpty() and pass.isNotEmpty()

    fun authWithGoogle(account: GoogleSignInAccount?) {

        //После успешного входа в систему получаем токен идентификатора из объекта GoogleSignInAccount,
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        // обмениваем его на учетные данные Firebase,
        usersRepo.createGoogleUser(credential, mkGoogleUser(account?.displayName!!,account.email!!,
            account.photoUrl.toString())).addOnSuccessListener {
            _goToHomeScreen.call()
        }.addOnFailureListener(onFailureListener)


    }

    //заполняем данные нашего data class, данными аолученными из гугл аккаунта
    private fun mkGoogleUser(fullname: String, email: String, photo: String?): User =
        User(name = fullname, email = email, photo = photo)



}