package homeproject.example.com.myhomeproject.screens.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log

import homeproject.example.com.myhomeproject.R

//кастомный imageButtom
class CustomImageButton : android.support.v7.widget.AppCompatImageButton {

    //данный метод вызывается, когда мы кликаем на кнопку.
    // меняем цвет области клика кнопки
    override fun drawableStateChanged() {
        Log.d("Button", "isPressed: $isPressed")
        if (isPressed) {
            setBackgroundResource(R.drawable.btn_click_bg)
        } else {
            setBackgroundResource(android.R.color.transparent)
        }
        super.drawableStateChanged()

    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}


}