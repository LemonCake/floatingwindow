package com.example.floatingwindowapp.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.floatingwindowapp.R
import com.example.floatingwindowapp.ui.main.Window

private const val TAG = "FLMainFragment"

class FLMainFragment : Fragment(R.layout.fl_fragment_main) {
    companion object {
        fun newInstance() = FLMainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "created")
        view.findViewById<Button>(R.id.next_fragment_button).apply {
            setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(R.id.fl_container, FLFragmentNext.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }

    }
}
