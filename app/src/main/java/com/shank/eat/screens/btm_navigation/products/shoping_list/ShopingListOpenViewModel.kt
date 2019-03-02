package com.shank.eat.screens.btm_navigation.products.shoping_list

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.shank.eat.data.RecipesRepository
import com.shank.eat.data.UsersRepository
import com.shank.eat.model.ShopingList
import com.shank.eat.screens.common.BaseViewModel

class ShopingListOpenViewModel(onFailureListener: OnFailureListener,
                               private val recipesRepo: RecipesRepository,
                               private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {


    private var  uid = usersRepo.currentUid()

    lateinit var shopingList: LiveData<ShopingList?>

    // id поста
    private lateinit var shopingListID: String

    fun init(shopingListID: String) {
        if (!this::shopingListID.isInitialized) {
            this.shopingListID = shopingListID
            //иннициализация списка комеентариев
            this.shopingList = recipesRepo.getShopingList(uid!!,shopingListID)
        }
    }
}
