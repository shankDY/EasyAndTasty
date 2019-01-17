package homeproject.example.com.myhomeproject.data.firebase

import com.google.android.gms.tasks.Task
import homeproject.example.com.myhomeproject.common.toUnit
import homeproject.example.com.myhomeproject.data.RecipesRepository
import homeproject.example.com.myhomeproject.data.firebase.common.database
import homeproject.example.com.myhomeproject.model.Recipe

class FirebaseRecipesRepository : RecipesRepository {

    //создаем рецепт
    override fun createRecipe(uid: String, recipe: Recipe): Task<Unit> {
        val reference = database.child("users-recipes").child(uid).push()
        return reference.setValue(recipe).toUnit()
    }
}