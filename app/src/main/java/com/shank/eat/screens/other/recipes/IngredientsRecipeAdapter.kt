package com.shank.eat.screens.other.recipes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.R
import kotlinx.android.synthetic.main.items_ingredients.view.*


class IngredientsRecipeAdapter(private val ingredients: List<String>?) : RecyclerView.Adapter<IngredientsRecipeAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_ingredients, parent, false)
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