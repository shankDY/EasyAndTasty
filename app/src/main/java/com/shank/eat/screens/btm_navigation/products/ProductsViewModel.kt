package com.shank.eat.screens.btm_navigation.products

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.data.common.map
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.BaseViewModel

class ProductsViewModel(onFailureListener: OnFailureListener,
                        private val recipesRepo: RecipesRepository,
                        usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {

    private var  uid = usersRepo.currentUid()
    var shopingLists: LiveData<List<ShopingList?>>


    init {

        //загружает список покупок и сортирует их по дате добавления
        shopingLists = recipesRepo.getShopingLists(uid!!).map {

            it.sortedByDescending {
                it!!.timestampDate()
            }
        }
    }
}
