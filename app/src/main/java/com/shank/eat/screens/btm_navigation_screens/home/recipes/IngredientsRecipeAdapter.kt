package com.shank.eat.screens.btm_navigation_screens.home.recipes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import kotlinx.android.synthetic.main.ingredients_recipe_item.view.*


class IngredientsRecipeAdapter(private val ingredients: List<String>?) : RecyclerView.Adapter<IngredientsRecipeAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.view){

            position_ingredients_text.text = (position+1).toString()
            ingredients_text.text = ingredients!![position]
        }
    }


    //размер списка с ингредиентами
    override fun getItemCount(): Int = ingredients!!.size
}