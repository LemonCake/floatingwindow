package com.example.floatingwindowapp.service

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.floatingwindowapp.R
import com.example.floatingwindowapp.ui.main.FLTouchListener

class FLThreadFragment : Fragment(R.layout.fl_thread_fragment) {
    companion object {
        fun newInstance() = FLThreadFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.pop_fragment_button).apply {
            setOnClickListener { parentFragmentManager.popBackStack() }
        }
    }
}
