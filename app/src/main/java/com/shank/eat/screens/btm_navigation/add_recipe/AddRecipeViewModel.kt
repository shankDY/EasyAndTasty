package com.shank.eat.screens.btm_navigation.add_recipe

import android.app.Application
import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.R
import com.shank.eat.common.SingleLiveEvent
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.Recipe
import com.shank.eat.model.User
import com.shank.eat.screens.common.BaseViewModel
import com.shank.eat.screens.common.CommonViewModel


class AddRecipeViewModel(private val commonViewModel: CommonViewModel, private val app: Application,
                         onFailureListener: OnFailureListener, private val recipesRepo: RecipesRepository,
                         private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    val user = usersRepo.getUser()
    private var img: Uri? = null
    private val _clearEdditText = SingleLiveEvent<Unit>()
    val clearEdditText = _clearEdditText

    //создаем пост
    fun publish(
        user: User,
        nameRecipe: String,
        category: String,
        recipeDificulty: String,
        ingredients: ArrayList<String>,
        coockingTime: String,
        instraction: String,
        calories: String,
        protein: String,
        fat: String,
        carbohydrates: String
    ) {
        if (nameRecipe.isNotEmpty() && category.isNotEmpty() && recipeDificulty.isNotEmpty() && ingredients.isNotEmpty()
            && coockingTime.isNotEmpty() && instraction.isNotEmpty() && calories.isNotEmpty()
            && protein.isNotEmpty() && fat.isNotEmpty() && carbohydrates.isNotEmpty()){

            if (img != null){

                usersRepo.uploadUserImage(user.uid, img!!).onSuccessTask { downloadUrl ->

                    recipesRepo.createRecipe(
                        user.uid, mkRecipe(user, nameRecipe, category, recipeDificulty, ingredients,
                            coockingTime,instraction, downloadUrl.toString(), calories, protein, fat, carbohydrates
                        )).addOnSuccessListener {
                        //если рецепт был добавлен в бд, то отчищаем все поля и удаляем фотографию
                        _clearEdditText.call()
                    }.addOnFailureListener(onFailureListener)

                }.addOnFailureListener(onFailureListener)

            }

        }else{
            commonViewModel.setErrorMessage(app.getString(R.string.enter_fields))
        }


    }


    //заполняем наш dataClass Recipe
    private fun mkRecipe(
        user: User,
        nameRecipe: String,
        category: String,
        recipeDificulty: String,
        ingredients: ArrayList<String>,
        coockingTime: String,
        instraction: String,
        photoRecipe: String?,
        calories: String,
        protein: String,
        fat: String,
        carbohydrates: String
    ): Recipe {

        return Recipe(uid = user.uid, username = user.name,userPhoto = user.photo,nameRecipe = nameRecipe,
            ingredients = ingredients, cookingTime = coockingTime, instruction = instraction, category = category,
            difficulty = recipeDificulty, recipeImg = photoRecipe, calories = calories, protein = protein,fat = fat,
            carbohydrates = carbohydrates)
    }

    //добавляем поля
    fun addFields(view: View, viewList: ArrayList<View>, linear_ingredients: LinearLayout) {
        //добавляем в список view
        viewList.add(view)
        //добавляем view в linearLayout
        linear_ingredients.addView(view)
    }


    //отчищаем наши editText и списки
    fun clearEdittext(ingredientsViewList: ArrayList<View>, ingredients_text: ArrayList<String>,
                      linear_ingredients: LinearLayout?,imageView: ImageView, vararg inputs: EditText) {

        recipesRepo.clearRecipeData( ingredientsViewList, ingredients_text, linear_ingredients,imageView,*inputs)

    }

    //добавляем элементы в список
    fun addArgs(ingredients: ArrayList<String>, vararg args: String) {
        for (i in args){
            if (i.isNotEmpty()){
                ingredients.add(i)
            }
        }
    }

    fun getUri(uri: Uri) {
       this.img = uri
    }


}
