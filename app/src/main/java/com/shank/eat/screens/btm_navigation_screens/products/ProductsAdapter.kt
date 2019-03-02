package com.shank.eat.screens.btm_navigation_screens.products

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.shank.eat.R
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.SimpleCallback
import kotlinx.android.synthetic.main.items_products.view.*


class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    var shopingList = listOf<ShopingList?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.view){
            name_recipe.text = shopingList[position]?.recipeName
            calories.text = "Калории: ${shopingList[position]?.calories}"

            val bundle = Bundle()
            bundle.putString("shoping_list_ID", shopingList[position]!!.id)

            //переходим в ShopingListOpenFragment
            card_list_shoping.setOnClickListener {

                findNavController().navigate(R.id.action_nav_item_shoping_list_to_shopingListOpenFragment,bundle)
            }
        }
    }



    fun updatePosts(newShopingList: List<ShopingList?>) {
        //считает разницу между старым и новым значением аргумента, находит те, что изменились
        // и перерисовываем только те, что изменились, а не весь адаптер
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.shopingList, newShopingList) { it!!.id })

        //обновление элементов
        this.shopingList = newShopingList
        //diffResult внутри себя считает, какие позиции изменились и
        // затем обновляет наш recyclerView
        diffResult.dispatchUpdatesTo(this)
    }



    //размер списка с ингредиентами
    override fun getItemCount(): Int = shopingList.size
}