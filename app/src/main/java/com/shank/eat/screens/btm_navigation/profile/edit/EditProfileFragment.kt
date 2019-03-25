package com.shank.eat.screens.btm_navigation.profile.edit

import android.arch.lifecycle.Observer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.model.User
import com.shank.eat.screens.btm_navigation.MainActivity
import com.shank.eat.screens.common.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BaseFragment() {
    private lateinit var mPendingUser: User // юзер ожидающий изменения
    private lateinit var mUser: User
    private lateinit var mPicture: PictureHelper
    private var imgUri: Uri? = null
    private var mPassword: String? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG,"Created")
        return inflater.inflate(R.layout.fragment_edit_profile, parent, false)
    }

    companion object {
        const val TAG = "EditProfileFragment"
    }

    private lateinit var mViewModel: EditProfileViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //инициализация нашей viewModel
        mViewModel = initViewModel()
        mPicture = PictureHelper(this)

        mViewModel.user.observe(viewLifecycleOwnerLiveData.value!!, Observer {

            it.let {user ->
                mUser = user!!
                //проставляем данные пользователя в соответствующие поля
                username_text.text = mUser.name
                setImage(mUser.photo)
                email_edit.setText(mUser.email)
                phone_edit.setText(mUser.phone)
                website_edit.setText(mUser.website)
            }
        })


        photo_btn.setOnClickListener {
            //показываем наш диалог выбора
            PhotoDialogFragment().show(childFragmentManager,"PhotoDialogFragment")
        }

        //изменить данные пользователя
        edit_btn.setOnClickListener {
            updateProfile()
        }
        back_img_recipe.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
        val context = context as MainActivity
        imgUri = (context).img
        mPassword =(context).password
        if (mPassword != null){
            passConfirm(mPassword!!)
        }
        if (imgUri != null) {
            setImage(imgUri.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation()
    }

    private fun setImage(imgUri: String?) {
        profile_image.loadUserPhoto(imgUri)
    }

    // метод записи измененных данных в бд
    private fun updateProfile() {
        //для этого нам надо:
        // get user from inputs
        mPendingUser = readInputs()
        //validate(проверка, что поля заполнены)
        val error = validate(mPendingUser)
        if (error == null){
            if (mPendingUser.email == mUser.email){
                //update user
                updateUser(mPendingUser)
            } else{

                //первый параметр fragment manager, второй - тег, который нужен андроиду,
                // чтобы сохранять состояние диалога, когда например приложение  свернуто
                PasswordDialog().show(childFragmentManager,"password_dialog")


            }
        }
        else{
            context?.showToast(error)
        }
    }


    //получем пороль пользователя с нашей диаложки
    fun passConfirm(password: String) {
        //добавим проверку пустой пароль или нет
        if (password.isNotEmpty()) {
            mViewModel.updateEmail(
                currentEmail = mUser.email,
                newEmail = mPendingUser.email,
                password = password)
                .addOnSuccessListener { updateUser(mPendingUser) }
        }else{
            context?.showToast(getString(R.string.you_should_enter_your_password))
        }
    }

    private fun updateUser(user: User) {

        mViewModel.updateUserProfile(currentUser = mUser, newUser = user)
            .addOnSuccessListener {
                context?.showToast(getString(R.string.profile_saved))
            }
    }

    //читаем с editText данные введенные пользователем и помещаем в наш DAO
    private fun readInputs(): User {
        return User(
            name = username_text.text.toString(),
            email = email_edit.text.toString(),
            website = website_edit.text.toString(),
            phone = phone_edit.text.toString()
        )
    }

    //если все поля заполнены возращаем null, иначе ошибку
    private fun validate(user: User): String? = when{
        user.name.isEmpty() -> getString(R.string.please_enter_name)
        user.email.isEmpty() -> getString(R.string.please_enter_email)
        else -> null
    }

}
