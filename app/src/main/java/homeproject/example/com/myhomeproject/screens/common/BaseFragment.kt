package homeproject.example.com.myhomeproject.screens.common

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import homeproject.example.com.myhomeproject.screens.App


abstract class BaseFragment : Fragment() {

    lateinit var commonViewModel: CommonViewModel
    private var parentActivity: BaseActivity? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)

        //обработчик ошибок
        commonViewModel.errorMessage.observe(this, Observer{it?.let{
            context?.showToast(it)
        }})
    }

    //reified позволяет передать тип, не указывая его явно, есть только в inline функции
    //inline функция, инлайнится в то место, где вызывана
    inline fun <reified T: BaseViewModel> initViewModel() =
        ViewModelProviders.of(this, ViewModelFactory(
            activity?.application as App,
            commonViewModel,
            commonViewModel)).get(T::class.java)



    abstract fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?,
                                         savedInstanceState: Bundle?): View

    fun getBaseActivity(): BaseActivity = parentActivity!!

    //храним все статические переменные класса в данном объекте
    companion object {
        const val TAG = "BaseFragment"
    }
}