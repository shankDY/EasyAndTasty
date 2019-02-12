package com.shank.eat.screens.common

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shank.eat.screens.App
import kotlinx.android.synthetic.main.activity_main.*


abstract class BaseFragment : Fragment() {

    lateinit var commonViewModel: CommonViewModel
    private var parentActivity: BaseActivity? = null
    private val ENTER_DURATION: Long = 1
    private val EXIT_DURATION: Long = 2

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.parentActivity = activity
            activity?.onFragmentAttached()
        }
    }


    override fun onDetach() {
        parentActivity = null
        super.onDetach()
    }



    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanseState: Bundle?): View? {
        return provideYourFragmentView(inflater, parent, savedInstanseState)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // инициализируем нашу общью ViewModel
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)


        //обработчик ошибок( если ошибка появляется, то показываем тост)
        commonViewModel.errorMessage.observe(viewLifecycleOwnerLiveData.value!!, Observer{it?.let{
            context?.showToast(it)
        }})
    }


    /**ViewModelProvider - будет отвественен за создание вьюмодели,
    что мы ему указали. для того фрагмента, что мы ему передали.
    т.е при первом создании активити, создается viewModel, а при следущих уничтожениях
    и возобновлениях работы будет возвращать туже модель
    Т.к фрагменты не надежны, их может убить система и создать заново,
    например при смене ориентации система убьет фрагмент и создаст новое, и все данные будут
    загружаться заново**/

    //reified позволяет передать тип, не указывая его явно, есть только в inline функции
    //inline функция, инлайнится в то место, где вызывана
    inline fun <reified T: BaseViewModel> initViewModel() =
        ViewModelProviders.of(this, ViewModelFactory(
            getBaseActivity().application as App,
            commonViewModel,
            commonViewModel)).get(T::class.java)



    abstract fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View

    fun getBaseActivity(): BaseActivity = parentActivity!!


    //скрываем bottomNavigation
     fun hideBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(activity!!.bottom_navigation) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = EXIT_DURATION
            }
        }
    }

    //показываем bottomNavigation
   fun showBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(activity!!.bottom_navigation) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = ENTER_DURATION
        }

    }


    //храним все статические переменные класса в данном объекте
    companion object {
        const val TAG = "BaseFragment"
    }
}