package homeproject.example.com.myhomeproject.screens.register_and_login_screens.login

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.GoogleAuthProvider
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.common.AuthManager
import homeproject.example.com.myhomeproject.common.SingleLiveEvent
import homeproject.example.com.myhomeproject.data.firebase.common.auth
import homeproject.example.com.myhomeproject.screens.common.BaseViewModel
import homeproject.example.com.myhomeproject.screens.common.CommonViewModel
import homeproject.example.com.myhomeproject.screens.common.showToast

class LoginViewModel(private  val authManager: AuthManager,
                     private val app: Application,
                     private val commonViewModel: CommonViewModel,
                     onFailureListener: OnFailureListener): BaseViewModel(onFailureListener) {

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
        Log.d(LoginFragment.TAG, "firebaseAuthWithGoogle:" + account?.id!!)

        //После успешного входа в систему получите токен идентификатора из объекта GoogleSignInAccount,
        // обмениваем его на учетные данные Firebase,
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                //если юзер авторизовался, то переходим на главный экран
                if (user == null) {
                    _goToHomeScreen.call()
                } else {
                    app.showToast(app.getString(R.string.google_failed))
                }
            }
        }.addOnFailureListener(onFailureListener)

    }
}