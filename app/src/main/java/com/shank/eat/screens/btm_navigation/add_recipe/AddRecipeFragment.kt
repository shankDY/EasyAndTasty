package com.shank.eat.screens.btm_navigation.add_recipe

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseFragment
import com.shank.eat.screens.common.PhotoDialogFragment
import com.shank.eat.screens.common.PictureHelper
import com.shank.eat.screens.common.loadImage
import kotlinx.android.synthetic.main.fragment_add_recipe.*
import com.shank.eat.screens.btm_navigation.MainActivity


class AddRecipeFragment : BaseFragment() {

    private lateinit var mViewModel: AddRecipeViewModel

    private lateinit var mUser: User

    private lateinit var mPicture: PictureHelper

    private var imgUri: Uri? = null

    private val MY_PERMISSIONS_REQUEST: Int = 123


    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_recipe, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d(TAG, "onCreate")

        //инициализация mViewModel
        mViewModel = initViewModel()

        //инициализируем наш PictureHelper
        mPicture = PictureHelper(this)

        //по клику на  floatImg открываем камеру
        floatImg.setOnClickListener {

            //запрашиваем права
            getPermissions()
        }

        //возвращение на предыдущий фрагмент
        back_img_recipe.setOnClickListener {
            requireActivity().onBackPressed()
        }


        //получаем авторизованного в данный момент пользователя
        mViewModel.user.observe(viewLifecycleOwnerLiveData.value!!, android.arch.lifecycle.Observer {
                it?.let{
                mUser = it
            }
        })


        //Создаем список вьюх(поле ингридиент)
        val ingredientsViewList = arrayListOf<View>()

        // список ингридиентов(данные, которые ввел пользователь)
        val ingredients = arrayListOf<String>()

        // добавляем поле ингридиент
        add_ingredients_text.setOnClickListener {
            val view :View = layoutInflater.inflate(R.layout.layout_more_ingredients,null)

            //вызываем соответствующий метод во viewModel, который позволит добавить нам поле
            mViewModel.addFields(view, ingredientsViewList, linear_ingredients)
        }


        //публикуем рецепт
        publish_btn.setOnClickListener {

            //добавляем данные с  первых трех ingredients view
            addargs(ingredients,ingredients1_input.text.toString(), ingredients2_input.text.toString(),
                ingredients3_input.text.toString())



            //проходим по списку и считываем текст с editext, которые соответсвуют полям для ввода ингридиентов
            if (ingredientsViewList.isNotEmpty()){
                for (i in 0 until ingredientsViewList.size) {

                    addargs(ingredients,(ingredientsViewList[i].
                        findViewById(R.id.ingredients_input) as EditText).text.toString()
                    )
                }
            }
            mViewModel.getUri(imgUri!!)
            // по клику на кнопку опубликовать.  передаем наши данные введенные пользователем во viewModel
            mViewModel.publish(user = mUser, nameRecipe = name_recipe_input.text.toString(),
                category = categories.selectedItem.toString(),  ingredients = ingredients,
                recipeDificulty = recipe_difficulty.selectedItem.toString(),
                coockingTime = coocking_time_text.text.toString(), instraction = instruction_input.text.toString(),
                calories = calories.text.toString(), protein = protein.text.toString(),
                fat = fat.text.toString(), carbohydrates = carbohydrates.text.toString())
        }



        //слушаем колбек. Если все успешно было добавленно в бд, то отчищаем наши EditText и соответствующие им списки
        mViewModel.clearEdditText.observe(viewLifecycleOwner, Observer {
            mPicture.fileDell()
            mViewModel.clearEdittext(ingredientsViewList, ingredients, linear_ingredients,recipe_image,name_recipe_input,
                ingredients1_input, ingredients2_input, ingredients3_input, coocking_time_text, instruction_input,
                calories,protein,fat,carbohydrates)
        })

    }


    //добавляем элементы в список
    private fun addargs(ingredients: ArrayList<String>, vararg args: String) {
        mViewModel.addArgs(ingredients, *args)

    }


    //загружаем картинку в imageView
    private fun setImage(uri: Uri?) {
        //загружаем картинку выбранную юзером в imageView
        recipe_image.loadImage(uri.toString())
    }

    //запрашиваем разрешение на чтение и запись в память
    private fun getPermissions() {
        // Here, thisActivity is the current activity
        if (context?.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // разрешения не предоставлены
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST)
            } else {
                // тут лучше показать объяснение, для чего необходима камера. посредством диалога(реализовать диалог объяснения)
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST)
            }
        } else {
            // Permission has already been granted
            PhotoDialogFragment().show(childFragmentManager,"PhotoDialogFragment")
        }

    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
        imgUri = (context as MainActivity).img
        setImage(imgUri)
    }
    override fun onStop() {
        super.onStop()
        showBottomNavigation()
    }
    //обработка ответа на  разрешении
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // разрешение предоставленно, можно выполнять действия
                    PhotoDialogFragment().show(childFragmentManager,"PhotoDialogFragment")
                } else {
                    Log.d(TAG,"разрешения отклонены")
                }
                return
            }
        }
    }

    companion object {
        const val TAG = "AddRecipeFragment"
    }



}
