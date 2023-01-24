package com.example.floatingwindowapp.service

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.floatingwindowapp.R

class FLFragmentNext : Fragment(R.layout.fl_fragment_next) {
    companion object {
        fun newInstance() = FLFragmentNext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.pop_fragment_button).apply {
            setOnClickListener { parentFragmentManager.popBackStack() }
        }
    }
}
