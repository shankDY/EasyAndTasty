package homeproject.example.com.myhomeproject.screens.common

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import homeproject.example.com.myhomeproject.screens.App
import homeproject.example.com.myhomeproject.screens.register_and_login_screens.HostLoginActivity

//главный класс для всех активити
abstract class BaseActivity: AppCompatActivity() {

    lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //отключаем смену ориентации экрана на книжную, для того,
        // чтобы активити так часто не убивались
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)

        //обработчик ошибок
        commonViewModel.errorMessage.observe(this, Observer{ it ->
            it?.let{
            showToast(it)
        }})
    }



    /**ViewModelProvider - будет отвественен за создание вьюмодели,
    что мы ему указали. для той активити , что мы ему передали.
    т.е при первом создании активити, создается viewModel, а при следущих уничтожениях
    и возобновлениях работы будет возвращать туже модель
    Т.к активити не надежная, ее может убить система и создать заново,
    например при смене ориентации система убьет активити и создаст новое, и все данные будут
    загружаться заново**/

    //reified позволяет передать тип, не указывая его явно, есть только в inline функции
    //inline функция, инлайнится в то место, где вызывана
    inline fun <reified T: BaseViewModel> initViewModel() =
        ViewModelProviders.of(this, ViewModelFactory(
            application as App,
            commonViewModel,
            commonViewModel)).get(T::class.java)


    //переход в логинАктивити
    fun goToLogin() {
        startActivity(Intent(this, HostLoginActivity::class.java))
        finish()
    }

    fun onFragmentAttached() {

    }


    //храним все статические переменные класса в данном объекте
    companion object {
        const val TAG = "BaseActivity"
    }
}