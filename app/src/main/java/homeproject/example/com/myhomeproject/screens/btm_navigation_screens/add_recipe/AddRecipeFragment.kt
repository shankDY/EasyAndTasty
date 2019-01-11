package homeproject.example.com.myhomeproject.screens.btm_navigation_screens.add_recipe

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import homeproject.example.com.myhomeproject.R
import homeproject.example.com.myhomeproject.screens.common.BaseFragment
import kotlinx.android.synthetic.main.add_recipe_fragment.*


class AddRecipeFragment : BaseFragment() {

    private lateinit var viewModel: AddRecipeViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreate")
        return inflater.inflate(R.layout.add_recipe_fragment, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddRecipeViewModel::class.java)

        //Создаем список вьюх которые будут создаваться
        val allEds = arrayListOf<View>()

        // текст - добавить ингридиент
        add_ingredients_text.setOnClickListener {
            val view :View = layoutInflater.inflate(R.layout.custom_ingredients_layout,null)
            onAddField(view,allEds)

        }

    }

    private fun onAddField(view: View, allEds: ArrayList<View>) {
        allEds.add(view)
        linear_ingredients.addView(view)
    }

    companion object {
        const val TAG = "AddRecipeFragment"
    }
}
