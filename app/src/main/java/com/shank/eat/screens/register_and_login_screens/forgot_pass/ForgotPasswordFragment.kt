package com.shank.eat.screens.register_and_login_screens.forgot_pass

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.shank.eat.R
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.coordinateBtnAndInputs
import com.shank.eat.screens.register_and_login_screens.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {


    private lateinit var mViewModel: ForgotPasswordViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(LoginFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_forgot_password, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Инициализация NavController - а, для работы с navigation
       val navController = Navigation.findNavController(reset_btn)

        //инициализация mViewModel
        mViewModel = initViewModel()

        //если поля заполнены кнопка активна
        coordinateBtnAndInputs(reset_btn, email_input)
        //при клике на кнопку resetPass. вызываем соответсвующий метод
        reset_btn.setOnClickListener {
            mViewModel.resetPass(email_input.text.toString())
        }

        //при успешной авторизации переходим в MainActivity, с которой разворачиваем HomeFragment
        mViewModel.goToLoginScreen.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            navController.popBackStack()
        })


    }

    companion object {
        const val TAG = "ForgotPasswordFragment"
    }
}
