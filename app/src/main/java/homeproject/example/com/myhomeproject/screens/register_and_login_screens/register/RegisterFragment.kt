package homeproject.example.com.myhomeproject.screens.register_and_login_screens.register

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.btm_navigation_screens.MainActivity
import homeproject.example.com.myhomeproject.screens.common.BaseFragment
import kotlinx.android.synthetic.main.progressbar.*
import kotlinx.android.synthetic.main.register_fragment.*


class RegisterFragment : BaseFragment(){

    private lateinit var mViewModel: RegisterViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.register_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = initViewModel()

        regbutton.setOnClickListener{
            login_progrssBar.visibility = View.VISIBLE
            mViewModel.onDataEntered(
                fullname = full_name_input.text.toString(),
                username = username_input.text.toString(),
                email = email_input_reg.text.toString(),
                password = password_input.text.toString()
            )
        }

        //переход на HomeActivity
        mViewModel.goToHomeScreen.observe(this, Observer {
            login_progrssBar.visibility = View.GONE
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        })
    }

    companion object {
        const val TAG = "RegisterFragment"
    }

}