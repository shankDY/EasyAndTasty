package homeproject.example.com.myhomeproject.screens.register_and_login_screens.forgot_pass

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.common.BaseFragment
import homeproject.example.com.myhomeproject.screens.register_and_login_screens.login.LoginFragment
import kotlinx.android.synthetic.main.forgot_password_fragment.*

class ForgotPasswordFragment : BaseFragment() {


    private lateinit var mViewModel: ForgotPasswordViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(LoginFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.forgot_password_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       val navController = Navigation.findNavController(reset_btn)

        //инициализация mViewModel
        mViewModel = initViewModel()

        //при клике на кнопку resetPass. вызываем соответсвующий метод
        reset_btn.setOnClickListener {
            mViewModel.resetPass(email_input.text.toString())
        }

        //при успешной авторизации переходим в MainActivity, с которой разворачиваем HomeFragment
        mViewModel.goToLoginScreen.observe(this, Observer {
            navController.popBackStack()
        })


    }

    companion object {
        const val TAG = "ForgotPasswordFragment"
    }
}
