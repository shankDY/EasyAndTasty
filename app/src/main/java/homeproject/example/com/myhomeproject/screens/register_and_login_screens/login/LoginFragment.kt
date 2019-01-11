package homeproject.example.com.myhomeproject.screens.register_and_login_screens.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.btm_navigation_screens.MainActivity
import homeproject.example.com.myhomeproject.screens.common.BaseFragment
import homeproject.example.com.myhomeproject.screens.common.coordinateBtnAndInputs
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : BaseFragment(), View.OnClickListener {


    private lateinit var mViewModel: LoginViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.login_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()

        // кнопка Log in активна, если email и password заполнены,
        coordinateBtnAndInputs(login_btn, email_input, password_input)

        //подключаем обработчики нажатия
        login_btn.setOnClickListener(this)

        //переход на registerEmailFragment
        create_account_text.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.action_loginFragment_to_registerFragment, null))


        //при успешной авторизации переходим в MainActivity, с которой разворачиваем HomeFragment
        mViewModel.goToHomeScreen.observe(this, Observer {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        })
    }

    //обработчик клика по кнопке login_btn и create_account_text
    override fun onClick(view: View) {
        when(view.id){
            R.id.login_btn ->
                mViewModel.onLoginClick(
                    email = email_input.text.toString(),
                    password = password_input.text.toString()
                )
        }
    }

    companion object {
        const val TAG = "LoginFragment"
    }

}
