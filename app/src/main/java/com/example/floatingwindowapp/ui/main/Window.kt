package com.example.floatingwindowapp.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity.LEFT
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.example.floatingwindowapp.AppContext
import com.example.floatingwindowapp.R
import com.example.floatingwindowapp.service.FLFragmentHost
import com.example.floatingwindowapp.service.FLInboxFragment
import com.example.floatingwindowapp.service.FLThreadFragment
import kotlin.math.roundToInt

@SuppressLint("StaticFieldLeak")
object Window {
    private val context = AppContext.get()
    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val rootView: View = layoutInflater.inflate(R.layout.fl_root_view, null)
    private lateinit var fragmentHost: FLFragmentHost

    private val windowParams = WindowManager.LayoutParams(
        0,
        0,
        0,
        0,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        PixelFormat.TRANSLUCENT
    )

    init {
        initWindowParams()
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
            rootView.setOnTouchListener(FLTouchListener())
            fragmentHost = FLFragmentHost(rootView)
            fragmentHost.getFragmentManager().beginTransaction()
                .replace(R.id.fl_container, FLInboxFragment.newInstance())
                .commitNow()
        }
    }

    fun hide() {
        if (rootView.isAttachedToWindow) {
            windowManager.removeView(rootView)
            fragmentHost.getFragmentManager().apply {
                repeat(backStackEntryCount) { popBackStackImmediate() }
            }
        }
    }

    fun move(offset: Float) {
        if (rootView.isAttachedToWindow) {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            windowParams.x = (offset * dm.widthPixels).roundToInt()
            windowManager.updateViewLayout(rootView, windowParams)
        }
    }

    fun setTouch(touch: Boolean) {
        val flags = if (!touch) {
            windowParams.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        } else {
            windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
        }

        windowParams.flags = flags
        windowManager.updateViewLayout(rootView, windowParams)
    }
}
