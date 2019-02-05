package com.shank.eat.screens.common

import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.navigation.NavOptions
import com.shank.eat.R
import com.shank.eat.common.formatRelativeTimestamp
import java.util.*

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



//данная функция екстеншен(Расширение) класса Context. А классы Context наследуют все Активити
//данная функция позволяет нам показывать тосты
fun Context.showToast(text:String?, duration: Int = Toast.LENGTH_SHORT ){
    text?.let{ Toast.makeText(this,it,duration ).show()}
}


//загружаем в imagView нашу картинку. и кропаем ее по центру
fun ImageView.loadImage(image: String?) =
        GlideApp.with(this)
            .load(image)
            .thumbnail( 0.1f )
            .centerCrop()
            .fallback(R.drawable.image_placeholder).into(this)



//функция расширения для ImageView
//которая позволяет загрузить нам наше фото с помощью Glide
fun ImageView.loadUserPhoto(photoUrl: String?) =
//fallback - картинка, которая показывается, когда у юзера отсутсвует фото в профиле
//если активити уничтожена, то не будем вызывать нащ glideApp. и не будем вставлять картинку
        GlideApp.with(this)
            .load(photoUrl)
            .thumbnail(0.1f)
            .fallback(R.drawable.person)
            .into(this)



//aniation fragment Transactions
fun options(): NavOptions? = NavOptions.Builder()
    //данные анимации срабатываю при клике на кнопки(программные)
    .setEnterAnim(R.animator.slide_in_right)
    .setExitAnim(R.animator.slide_out_left)
    //данные анимации срабатывают при выталкивании фрагмента из стека(по клику на кнопку назад(системную))
    .setPopEnterAnim(R.animator.slide_in_right)
    .setPopExitAnim(R.animator.slide_out_left)
    .build()



//создаем утилитарную функцию Spannable text
fun TextView.setCaptionText(username: String, caption: String, date: Date? = null){

    //spannable: username(bold, clickable) caption
    val usernameSpannable = SpannableString(username)
    //мы выделяем часть текста(username). Делаем его жирным
    usernameSpannable.setSpan(
        StyleSpan(Typeface.BOLD), 0, usernameSpannable.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    usernameSpannable.setSpan(object: ClickableSpan(){

        override fun updateDrawState(ds: TextPaint?) {}

        //кликаем по имени пользователя
        override fun onClick(widget: View) {

        }
    },0, usernameSpannable.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    val dateSpannable = date?.let{
        val dateText = formatRelativeTimestamp(date, Date())
        val spannableString = SpannableString(dateText)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.grey)),
            0, dateText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString
    }
    /*spannable -> username  text -> caption -> SpannableStringBuilder*/
    //apply дает нам возможность возратить в данном случае сразу спенебл
    //Вызывает указанный функциональный блок с этим значением в качестве
    // приемника и возвращает это значение.
    text = SpannableStringBuilder().apply {
        append(usernameSpannable) //добавляем юзернейм
        append(" ")//пробел
        append(caption)
        dateSpannable?.let {
            append(" ")
            append(it)
        }
    }
    //делаем текст кликабельным
    movementMethod = LinkMovementMethod.getInstance()
}



//отключает мерцание В recyclerView картинок при загрузке через glide
fun recyclerAnimatorOff(feed_recycler: RecyclerView?) {

    val animator = feed_recycler?.itemAnimator
    if (animator is SimpleItemAnimator) {
        animator.supportsChangeAnimations = false
    }
}
