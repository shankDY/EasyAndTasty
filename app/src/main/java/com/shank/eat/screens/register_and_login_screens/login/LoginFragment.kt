package com.shank.eat.screens.register_and_login_screens.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.shank.eat.R
import com.shank.eat.data.firebase.common.googleSignInClient
import com.shank.eat.screens.btm_navigation.MainActivity
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.coordinateBtnAndInputs
import com.shank.eat.screens.common.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_progressbar.*


class LoginFragment : BaseFragment(){


    private lateinit var mViewModel: LoginViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_login, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //инициализируем viewModel для LoginFragment
        mViewModel = initViewModel()

        // кнопка Log in активна, если email и password заполнены,
        coordinateBtnAndInputs(login_btn, email_input, password_input)

        //подключаем обработчики нажатия
        login_btn.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            mViewModel.onLoginClick(
                email = email_input.text.toString(),
                password = password_input.text.toString()
            )
        }

        // по клику на кнопку гугл. вызываем соответсвующий метод
        buttonGoogleLogin.setOnClickListener {
            signIn()
        }


        //переход на registerEmailFragment
        buttonEmailLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment,null)
        }



        //переход на ForgotPasswordFragment

        forgot_password_text.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment,null)
        }


        //при успешной авторизации переходим в MainActivity, с которой разворачиваем HomeFragment
        mViewModel.goToHomeScreen.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        })
    }

    //данный метод вызывает веб форму регистрации гугл
    private fun signIn() {
        val signInIntent = googleSignInClient(context!!).signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }


    // получаем ответ на об успешности(или нет) выполнения авторизации в соцсетях
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Результат, возвращаемый после запуска Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Вход в Google прошел успешно, авторизуемся в firebase
                val account = task.getResult(ApiException::class.java)
                mViewModel.authWithGoogle(account)
            } catch (e: ApiException) {
                Log.d(TAG, "Google sign in failed", e)
                context?.showToast(getString(R.string.google_failed))
            }
        }
    }

    companion object {
        const val TAG = "LoginFragment"
        //число по которому мы можем в ответе узнать, что результат выполнился для этой операции или нет
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
