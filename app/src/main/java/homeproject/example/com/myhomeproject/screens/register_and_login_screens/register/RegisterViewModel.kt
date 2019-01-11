package homeproject.example.com.myhomeproject.screens.register_and_login_screens.register

import android.app.Application
import com.google.android.gms.tasks.OnFailureListener
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.common.SingleLiveEvent
import homeproject.example.com.myhomeproject.data.UsersRepository
import homeproject.example.com.myhomeproject.model.User
import homeproject.example.com.myhomeproject.screens.common.BaseViewModel
import homeproject.example.com.myhomeproject.screens.common.CommonViewModel

class RegisterViewModel(private val commonViewModel: CommonViewModel,
                        private val app: Application,
                        onFailureListener: OnFailureListener,
                        private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener){

    private var email: String? = null
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    val goToHomeScreen = _goToHomeScreen

    //обрабатываем введенные данные юзера, если не пустые, то проверяем на уникальность email
    fun onDataEntered(fullname: String, username: String, email: String, password: String) {
        if (email.isNotEmpty()) {
            this.email = email
            //проверка уникальности email
            usersRepo.isUserExistsForEmail(email).addOnSuccessListener { exist ->
                if (!exist){
                    onRegister(fullname, username, password)
                }else{
                    commonViewModel.setErrorMessage(app.getString(R.string.email_exist))
                }
            }.addOnFailureListener(onFailureListener)
        }else{
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
        }

    }

    //Регистрируем пользователя
    fun onRegister(fullname: String, username: String, password: String) {
        //если мы получили все необходимые данные с нашего фрагмента 2
        if (fullname.isNotEmpty() and  password.isNotEmpty() and username.isNotEmpty()){
            // тут на всякий случай проверим получен ли email юзера
            val localEmail = email
            //если все ок, то
            if (localEmail != null){
                usersRepo.createUser(mkUser(fullname,localEmail, username),password).addOnSuccessListener {
                    //переход на HomeActivity
                    _goToHomeScreen.call()
                }.addOnFailureListener(onFailureListener)

            }else{
                commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
            }
        }else{
            commonViewModel.setErrorMessage(app.getString(R.string.enter_fields))
        }
    }

    //заполняем наш User
    private fun mkUser(fullname: String, email: String, username: String): User {
        return User(name = fullname,username = username, email = email)
    }
}
