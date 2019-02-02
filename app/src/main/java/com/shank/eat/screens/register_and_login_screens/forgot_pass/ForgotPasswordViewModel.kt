package com.shank.eat.screens.register_and_login_screens.forgot_pass

import android.app.Application
import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.R
import com.shank.eat.common.SingleLiveEvent
import com.shank.eat.data.UsersRepository
import com.shank.eat.data.firebase.common.auth
import com.shank.eat.screens.common.BaseViewModel
import com.shank.eat.screens.common.CommonViewModel

class ForgotPasswordViewModel(private val commonViewModel: CommonViewModel,
                              private val app: Application,
                              onFailureListener: OnFailureListener,
                              private val usersRepo: UsersRepository
) : BaseViewModel(onFailureListener) {

    private val _goToLoginScreen = SingleLiveEvent<Unit>()
    //ссылка на переход на LoginFragment
    val goToLoginScreen: LiveData<Unit> = _goToLoginScreen

    fun resetPass(email: String) {
        //проверка, что юзер ввел email
        if (email.isNotEmpty()){
            //проверка, что email существует
            usersRepo.isUserExistsForEmail(email).addOnSuccessListener {exist ->
                //если email существует, то отправляем сообщение на почту с инструкцией для сброса пароля
                // и даем добро на переход в Login Fragment
                if (exist){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener{
                        if (it.isSuccessful){
                            //даем добро на переход в Login Fragment
                            _goToLoginScreen.call()
                        }else{
                            commonViewModel.setErrorMessage(app.getString(R.string.email_not_exist))
                        }
                    }.addOnFailureListener(onFailureListener)
                }
            }.addOnFailureListener(onFailureListener)
        }else{
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
        }
    }
}
