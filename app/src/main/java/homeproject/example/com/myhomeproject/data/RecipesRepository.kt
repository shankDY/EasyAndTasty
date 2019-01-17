package homeproject.example.com.myhomeproject.data

import com.google.android.gms.tasks.Task
import homeproject.example.com.myhomeproject.model.Recipe

interface RecipesRepository {

    fun createRecipe(uid: String, recipe: Recipe): Task<Unit>
}