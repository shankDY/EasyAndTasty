package com.shank.eat.screens.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.annotation.IdRes
import android.support.annotation.NavigationRes
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.shank.eat.R

class BottomNavController(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appStartDestinationId: Int
) {
    private val navigationBackStack = BackStack.of(appStartDestinationId)
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    private var listener: OnNavigationItemChanged? = null
    private var navGraphProvider: NavGraphProvider? = null

    interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    init {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                activity = ctx
                fragmentManager = (activity as FragmentActivity).supportFragmentManager
                break
            }
            ctx = ctx.baseContext
        }
    }

    fun setOnItemNavigationChanged(listener: (itemId: Int) -> Unit) {
        this.listener = object : OnNavigationItemChanged {
            override fun onItemChanged(itemId: Int) {
                listener.invoke(itemId)
            }
        }
    }

    fun setNavGraphProvider(provider: NavGraphProvider) {
        navGraphProvider = provider
    }

    fun onNavigationItemReselected(item: MenuItem) {
        //  Если пользователь нажимает второй раз кнопку навигации, мы вставляем задний стек в корень
        activity.findNavController(containerId).popBackStack(item.itemId, false)
    }

    fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()): Boolean {

        // управление фрагментами(переключение фрагментов нижней навигации)
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
                ?: NavHostFragment.create(navGraphProvider?.getNavGraphId(itemId)
                        ?: throw RuntimeException("You need to set up a NavGraphProvider with " +
                                "BottomNavController#setNavGraphProvider")
                )
        fragmentManager.beginTransaction()
                //анимация переходов
                .setCustomAnimations(
                        R.anim.nav_default_enter_anim,
                        R.anim.nav_default_exit_anim,
                        R.anim.nav_default_pop_enter_anim,
                        R.anim.nav_default_pop_exit_anim
                )
                .replace(containerId, fragment, itemId.toString())
                .addToBackStack(null)
                .commit()

        // Add to back stack
        navigationBackStack.moveLast(itemId)

        listener?.onItemChanged(itemId)

        return true
    }

    //клик по кнопке назад
    fun onBackPressed() {
        val childFragmentManager = fragmentManager.findFragmentById(containerId)!!
                .childFragmentManager
        when {
            // We should always try to go back on the child fragment manager stack before going to
            // the navigation stack. It important to use the child fragment manager instead of the
            // NavController because if the user change tabs super fast commit of the
            // supportFragmentManager may mess up with the NavController child fragment manager back
            // stack
            childFragmentManager.popBackStackImmediate() -> { }
            // Задний стек фрагментов пуст, поэтому попробуйте вернуться в стек навигации
            navigationBackStack.size > 1 -> {
                //  Удалить последний элемент из заднего стека
                navigationBackStack.removeLast()

                // Обновить контейнер новым фрагментом
                onNavigationItemSelected()
            }


            // Если в стеке есть только один, и это не домашняя страница навигации, мы должны
            // убедитесь, что приложение всегда выходит из startDestination
            navigationBackStack.last() != appStartDestinationId -> {
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationId)
                onNavigationItemSelected()
            }
            // Navigation stack is empty, so finish the activity
            else -> activity.finish()
        }
    }


    //задний стек фрагментов.
    private class BackStack : ArrayList<Int>() {
        companion object {
            fun of(vararg elements: Int): BackStack {
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size - 1)
        fun moveLast(item: Int) {
            remove(item)
            add(item)
        }
    }
}

// Convenience extension to set up the navigation
fun BottomNavigationView.setUpNavigation(bottomNavController: BottomNavController, onReselect: ((menuItem: MenuItem) -> Unit)? = null) {
    setOnNavigationItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }
    setOnNavigationItemReselectedListener {
        bottomNavController.onNavigationItemReselected(it)
        onReselect?.invoke(it)
    }
    bottomNavController.setOnItemNavigationChanged { itemId ->
        menu.findItem(itemId).isChecked = true
    }
}