package com.example.floatingwindowapp.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.floatingwindowapp.R

private const val TAG = "FLMainFragment"

class FLInboxFragment : Fragment(R.layout.fl_inbox_fragment) {
    private val adapter = FLInboxAdapter {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_container, FLThreadFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance() = FLInboxFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "created")
        view.findViewById<RecyclerView>(R.id.inbox_recycler_view).apply {
            adapter = this@FLInboxFragment.adapter
        }
    }

    class FLInboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, clickListener: (Int) -> Unit) {
            itemView.findViewById<TextView>(R.id.row_text).apply {
                text = "$position"
            }

            itemView.setOnClickListener {
                clickListener(position)
            }
        }
    }

    class FLInboxAdapter(private val clickListener: (Int) -> Unit) :
        RecyclerView.Adapter<FLInboxViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FLInboxViewHolder =
            FLInboxViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.inbox_thread_view, parent, false) as View
            )

        override fun onBindViewHolder(holder: FLInboxViewHolder, position: Int) {
            holder.bind(position, clickListener)
        }

        override fun getItemCount(): Int = 30
    }
}
