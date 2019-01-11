package homeproject.example.com.myhomeproject.screens.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

//vararg т.к у нас не один фиксированный input , а список
//данная функция позволяет нам делать кнопку не доступной, если поля не заполнены
fun coordinateBtnAndInputs(btn: Button, vararg inputs: EditText) {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        //переопределение методов работы с editText. позволит нам сделать кнопку неактивной,
        // когда текс не написан и наоборот активной, когда написан
        override fun afterTextChanged(s: Editable?) {
            //если все инпуты не пустые , то кнопка активна
            btn.isEnabled = inputs.all { it.text.isNotEmpty() }
        }
    }
    inputs.forEach { it.addTextChangedListener(watcher) }

    //первоначально все кнопки неактивны
    btn.isEnabled = inputs.all { it.text.isNotEmpty() }
}

//фукция расширения для EditText, которая вернет null(в бд будет пустая строка),
// если необязательные  поля пустые
fun Editable.toStringOrNull(): String?{
    val str = toString()
    return if (str .isEmpty()) null else str
}

//данная функция екстеншен(Расширение) класса Context. А классы Context наследуют все Активити
//данная функция позволяет нам показывать тосты
fun Context.showToast(text:String?, duration: Int = Toast.LENGTH_SHORT ){
    text?.let{ Toast.makeText(this,it,duration ).show()}
}


