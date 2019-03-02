package com.shank.eat.screens.register_and_login_screens.register

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.MainActivity
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.coordinateBtnAndInputs
import kotlinx.android.synthetic.main.layout_progressbar.*
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseFragment(){

    private lateinit var mViewModel: RegisterViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.fragment_register, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //инициализация viewModel
        mViewModel = initViewModel()

        //если поля заполнены кнопка активна
        coordinateBtnAndInputs(regbutton, username_input, email_input_reg, password_input)

        regbutton.setOnClickListener{
            //вкл видимость прогрес бара
            progressBar.visibility = View.VISIBLE

            //отправляем введенные данные во viewModel для обработки
            mViewModel.onDataEntered(
                username = username_input.text.toString(),
                email = email_input_reg.text.toString(),
                password = password_input.text.toString()
            )
        }

        //переход на HomeActivity
        mViewModel.goToHomeScreen.observe(viewLifecycleOwnerLiveData.value!!, Observer {
            progressBar.visibility = View.GONE
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        })
    }

    companion object {
        const val TAG = "RegisterFragment"
    }

}