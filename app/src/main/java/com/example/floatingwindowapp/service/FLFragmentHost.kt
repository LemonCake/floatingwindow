package com.example.floatingwindowapp.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentController
import androidx.fragment.app.FragmentHostCallback
import androidx.fragment.app.FragmentManager
import com.example.floatingwindowapp.ui.main.Window

class FLFragmentHost(private val rootView: View) {
    private val handler = Handler(Looper.getMainLooper())
    private val fragmentController = FragmentController.createController(HostCallbacks())

    init {
        fragmentController.attachHost(null)
        fragmentController.dispatchCreate()
        fragmentController.dispatchStart()
        fragmentController.dispatchResume()
    }

    inner class HostCallbacks : FragmentHostCallback<FLFragmentHost>(rootView.context, handler, 0) {
        override fun onGetHost(): FLFragmentHost = this@FLFragmentHost

        override fun onFindViewById(id: Int): View? =
            rootView.findViewById(id)

        override fun onGetLayoutInflater(): LayoutInflater =
            (rootView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).cloneInContext(
                rootView.context
            )
    }

    fun getFragmentManager(): FragmentManager = fragmentController.supportFragmentManager
}
