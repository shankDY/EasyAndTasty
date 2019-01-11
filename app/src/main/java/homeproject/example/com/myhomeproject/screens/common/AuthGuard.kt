package homeproject.example.com.myhomeproject.screens.common

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseAuth
import homeproject.example.com.myhomeproject.data.firebase.common.auth

//сущность, которая отвечает за авторизацию в firebase
class AuthGuard(private val activity: BaseActivity, f: (String) -> Unit) : LifecycleObserver {

    init {

        val user = auth.currentUser
        //если юзер не авторизован, то попадает на экран логина
        if (user == null){
            activity.goToLogin()
        }else{
            f(user.uid)
            //добавляем обзервера в случае,если мы уже зареганы, тогда сможем слушать evants
            activity.lifecycle.addObserver(this)
        }
    }

    private  val listener=  FirebaseAuth.AuthStateListener {
        //если юзер не зарегистрирован в системе, то переходи в окно регистрации
        if ( it.currentUser == null){
            //переходим в окно регистрации
            activity.goToLogin()
        }
    }

    //когда активити или фрагмент живы, то мы слушаем колбеки
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        auth.addAuthStateListener(listener)
    }

    //когда активити неактивна, то наши слушатели уничтожаются
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        auth.removeAuthStateListener(listener)
    }
}

//функция , которая позволит нам устанавливать authGuard в каждое активити
fun BaseActivity.setupAuthGuard(f: (String) -> Unit){
    AuthGuard(this, f)
}