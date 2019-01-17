package homeproject.example.com.myhomeproject.screens.btm_navigation_screens.add_recipe

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.model.User
import homeproject.example.com.myhomeproject.screens.common.BaseFragment
import kotlinx.android.synthetic.main.add_recipe_fragment.*


class AddRecipeFragment : BaseFragment() {

    private lateinit var mViewModel: AddRecipeViewModel

    private lateinit var mUser: User

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.add_recipe_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onCreate")

        //инициализация mViewModel
        mViewModel = initViewModel()


        //получаем авторизованного в данный момент пользователя
        mViewModel.user.observe(this, android.arch.lifecycle.Observer {
                it?.let{
                mUser = it
            }
        })


        //Создаем список вьюх(поле ингридиент)
        val ingredientsViewList = arrayListOf<View>()


        // список ингридиентов(данные, которые ввел пользователь)
        val ingredients = arrayListOf<String>()

        //публикуем рецепт
        publish_btn.setOnClickListener {



            //добавляем данные с  первых трех ingredients view

            val element1 = ingredients1_input.text.toString()
            val element2 = ingredients2_input.text.toString()
            val element3 = ingredients3_input.text.toString()

            if (element1.isNotEmpty()){
                ingredients.add(element1)
            }else if (element2.isNotEmpty()){
                ingredients.add(element2)
            } else if (element3.isNotEmpty()){
                ingredients.add(element3)
            }else{
                ingredients.add(element1)
                ingredients.add(element2)
                ingredients.add(element3)
            }

            // добавляем поле ингридиент
            add_ingredients_text.setOnClickListener {
                val view :View = layoutInflater.inflate(R.layout.custom_ingredients_layout,null)
                onAddField(view,ingredientsViewList,linear_ingredients)
            }


            //проходим по списку и считываем текст с editext, которые соответсвуют полям
            // для ввода ингридиентов
            if (ingredientsViewList.isNotEmpty()){
                for (i in 0..ingredientsViewList.size) {
                    ingredients.add((ingredientsViewList[i].findViewById(R.id.ingredients_input) as EditText).text.toString())
                }
            }



            mViewModel.publish(user = mUser,name = name_recipe_input.text.toString(), category = categories.selectedItem.toString(),
                recipeDificulty = recipe_difficulty.selectedItem.toString(), ingredients = ingredients,
                coockingTime = coocking_time_text.text.toString(), instraction = instruction_input.text.toString())
        }

        mViewModel.clearEdditText.observe(this, Observer {
            clearEdittext(name_recipe_input, ingredients1_input, ingredients2_input, ingredients3_input,
                coocking_time_text, instruction_input, ingredientsViewList, ingredients)
        })

    }

    private fun clearEdittext(
        name_recipe_input: EditText?,
        ingredients1_input: EditText?,
        ingredients2_input: EditText?,
        ingredients3_input: EditText?,
        coocking_time_text: EditText?,
        instruction_input: EditText?,
        ingredientsViewList: ArrayList<View>,
        ingredients: ArrayList<String>
    ) {
        //отчищаем статичные поля
        name_recipe_input?.text?.clear()
        ingredients1_input?.text?.clear()
        ingredients2_input?.text?.clear()
        ingredients3_input?.text?.clear()
        coocking_time_text?.text?.clear()
        instruction_input?.text?.clear()

        if (ingredientsViewList.isNotEmpty()){

            //отчищаем список view
            ingredientsViewList.clear()

            //удаляем динамически добавленные элементы
            linear_ingredients.removeAllViews()
        }

        if (ingredients.isNotEmpty())
        //отчищаем список введенных пользователем данных
            ingredients.clear()
    }


    //добавить view
    private fun onAddField(
        view: View, viewList: ArrayList<View>, linear_ingredients: LinearLayout) {

        mViewModel.addFields(view, viewList, linear_ingredients)
    }



    companion object {
        const val TAG = "AddRecipeFragment"
    }
}
