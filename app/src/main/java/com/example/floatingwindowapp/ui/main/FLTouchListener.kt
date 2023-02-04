package com.example.floatingwindowapp.ui.main

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

private const val HORIZONTAL_SWIPE_THRESHOLD = 20

class FLTouchListener : View.OnTouchListener {
    private var x: Float = 0F
    private var y: Float = 0F

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.rawX
                y = event.rawY
                true
            }
            MotionEvent.ACTION_MOVE -> {
               if (abs(event.rawX - x) > HORIZONTAL_SWIPE_THRESHOLD) {
                   Window.setTouch(false)
                   false
               } else {
                   true
               }
            }
            else -> true
        }
    }

}
