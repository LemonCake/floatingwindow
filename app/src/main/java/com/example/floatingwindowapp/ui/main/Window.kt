package com.example.floatingwindowapp.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity.LEFT
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.example.floatingwindowapp.AppContext
import com.example.floatingwindowapp.R
import kotlin.math.roundToInt

@SuppressLint("StaticFieldLeak")
object Window {
    private val context = AppContext.get()
    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val rootView: View = layoutInflater.inflate(R.layout.window, null)

    private val windowParams = WindowManager.LayoutParams(
        0,
        0,
        0,
        0,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
        PixelFormat.TRANSLUCENT
    )

    init {
        initWindowParams()
        initWindow()
    }

    private fun initWindow() {
        rootView.findViewById<Button>(R.id.bye_button)
            .apply { setOnClickListener { windowManager.removeView(rootView) } }
    }

    @SuppressLint("StaticFieldLeak")
    private fun initWindowParams() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        windowParams.apply {
            gravity = TOP or LEFT
            x = 0
            y = dm.heightPixels / 4
            width = dm.widthPixels
            height = (dm.heightPixels / 2)
        }
    }

    fun show(windowToken: IBinder?) {
        if (!rootView.isAttachedToWindow) {
            windowParams.token = windowToken
            windowManager.addView(rootView, windowParams)
        }
    }

    fun hide() {
        if (rootView.isAttachedToWindow) {
            windowManager.removeView(rootView)
        }
    }

    fun move(windowToken: IBinder?, offset: Float) {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        windowParams.x = (offset * dm.widthPixels).roundToInt()
        windowParams.token = windowToken
        windowManager.updateViewLayout(rootView, windowParams)
    }
}