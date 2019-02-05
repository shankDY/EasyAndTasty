package com.shank.eat.screens.btm_navigation_screens.shoping_lists.shoping_list_open

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shank.eat.R
import com.shank.eat.screens.btm_navigation_screens.add_recipe.AddRecipeFragment
import com.shank.eat.screens.common.BaseFragment
import kotlinx.android.synthetic.main.shoping_list_open_fragment.*

class ShopingListOpenFragment : BaseFragment() {

    private lateinit var mViewModel: ShopingListOpenViewModel



    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View {
        Log.d(AddRecipeFragment.TAG, "onCreate")
        return inflater.inflate(R.layout.shoping_list_open_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //id нашего списка покупок(получаем при клике на определенную карту)
        val shopingListID = arguments!!.getString("shoping_list_ID")?: findNavController().popBackStack()

        //инициализация mViewModel
        mViewModel = initViewModel()

        mViewModel.init(shopingListID.toString())


        mViewModel.shopingList.observe(this, Observer {

            it.let {shopingList ->

                //устанавливаем имя рецепта в текствью в тулбаре
                recipe_name.text = shopingList?.recipeName
                //передаем список с ингредиентами в метод с RecyclerView
                initRecyclerIngredients(shopingList?.ingredients)
            }
        })

        back_img.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    //инициализация recyclerView
    private fun initRecyclerIngredients(ingredients: List<String>?) {

        //инициализируем адаптер
        val adapter = ListIngredientsAdapter(ingredients)

        //отвечает за функциональность нашего recyclerView
        ingredients_recycler.layoutManager = LinearLayoutManager(context)

        //подключаем адаптер в нашем recyclerView
        ingredients_recycler.adapter = adapter
    }


    companion object {

        const val TAG = "ShopingListOpenFragment"
    }
}
