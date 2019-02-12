package com.shank.eat.screens.btm_navigation_screens.profile

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.shank.eat.screens.btm_navigation_screens.profile.favorite_recipes.Favorite_RecipesFragment
import com.shank.eat.screens.btm_navigation_screens.profile.my_recipes.MyRecipesFragment

class ProfilePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    // по позиции элемента показываем определенный фрагмент
    override fun getItem(position: Int): Fragment =
        when (position){
            0 -> MyRecipesFragment()
            else -> Favorite_RecipesFragment()
        }

    //задаем название табам

    override fun getPageTitle(position: Int): CharSequence =
        when(position){
            0 -> "Мои рецепты"
            else -> "Любимые рецепты"
        }




    //количество страниц(табов)
    override fun getCount(): Int = 2


}