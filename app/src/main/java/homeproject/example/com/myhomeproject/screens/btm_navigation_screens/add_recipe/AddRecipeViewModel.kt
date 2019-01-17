package homeproject.example.com.myhomeproject.screens.btm_navigation_screens.add_recipe

import android.app.Application
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.tasks.OnFailureListener
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.common.SingleLiveEvent
import homeproject.example.com.myhomeproject.data.RecipesRepository
import homeproject.example.com.myhomeproject.data.UsersRepository
import homeproject.example.com.myhomeproject.data.firebase.common.auth
import homeproject.example.com.myhomeproject.data.firebase.common.currentUid
import homeproject.example.com.myhomeproject.model.Recipe
import homeproject.example.com.myhomeproject.model.User
import homeproject.example.com.myhomeproject.screens.common.BaseViewModel
import homeproject.example.com.myhomeproject.screens.common.CommonViewModel

class AddRecipeViewModel(private val commonViewModel: CommonViewModel, private val app: Application,
                         onFailureListener: OnFailureListener, private val recipesRepo: RecipesRepository,
                         private val usersRepo: UsersRepository
) : BaseViewModel(onFailureListener) {

    val user = usersRepo.getUser()
    private val _clearEdditText = SingleLiveEvent<Unit>()
    val clearEdditText = _clearEdditText

    //создаем пост
    fun publish(user: User, name: String, category: String, recipeDificulty: String, ingredients: ArrayList<String>,
                coockingTime: String, instraction: String) {

        if (name.isNotEmpty() and category.isNotEmpty() and recipeDificulty.isNotEmpty() and ingredients.isNotEmpty()
            and coockingTime.isNotEmpty() and instraction.isNotEmpty()){

            recipesRepo.createRecipe(
                user.uid, mkRecipe(user, name, category, recipeDificulty, ingredients,
                coockingTime,instraction)).addOnSuccessListener {
                //если рецепт был добавлен в бд, то отчищаем все поля
                _clearEdditText.call()
            }.addOnFailureListener(onFailureListener)

        }else{
            commonViewModel.setErrorMessage(app.getString(R.string.enter_fields))
        }
    }


    //заполняем наш dataClass Recipe
    private fun mkRecipe(user: User, name: String, category: String, recipeDificulty: String,
        ingredients: ArrayList<String>, coockingTime: String, instraction: String): Recipe {
        return Recipe(
            uid = user.uid,
            name = name,
            ingredients = ingredients,
            cookingTime = coockingTime,
            instruction = instraction,
            category = category,
            difficulty = recipeDificulty
        )
    }

    fun addFields(view: View, viewList: ArrayList<View>, linear_ingredients: LinearLayout) {
        //добавляем в список view
        viewList.add(view)
        //добавляем view в linearLayout
        linear_ingredients.addView(view)
    }


}
