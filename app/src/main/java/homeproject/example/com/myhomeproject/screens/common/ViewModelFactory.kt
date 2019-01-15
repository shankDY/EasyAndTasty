package homeproject.example.com.myhomeproject.screens.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import homeproject.example.com.myhomeproject.screens.App
import homeproject.example.com.myhomeproject.screens.register_and_login_screens.forgot_pass.ForgotPasswordViewModel
import homeproject.example.com.myhomeproject.screens.register_and_login_screens.login.LoginViewModel
import homeproject.example.com.myhomeproject.screens.register_and_login_screens.register.RegisterViewModel


//в данном классе будем ссылаться на бд(в нашем случае firebase), остальные классы не будут знать,
// какую бд мы используем
class ViewModelFactory(private val app: App,
                       private val commonViewModel: CommonViewModel,
                       private val onFailureListener: OnFailureListener) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val usersRepo = app.usersRepo
        val authManager = app.authManager

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(authManager,app,commonViewModel,onFailureListener) as T
        }else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
        }else if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)){
            return ForgotPasswordViewModel(commonViewModel,app,onFailureListener,usersRepo) as T
        }else{
            error("Unknow view model class $modelClass")
        }
    }

}
