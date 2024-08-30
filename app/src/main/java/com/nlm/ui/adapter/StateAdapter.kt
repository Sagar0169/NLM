package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nlm.R

class StateAdapter(
    private val stateList: List<String>,
    private val context: Context,
    private val onItemClicked: (String) -> Unit // Lambda for item clicks
) : RecyclerView.Adapter<StateAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tvStateName)

        fun bind(state: String) {
            textView.text = state

            // Set click listener for the item
            itemView.setOnClickListener {
                onItemClicked(state) // Call the lambda function with the selected item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stateList[position])
    }

    override fun getItemCount(): Int {
        return stateList.size
    }
}
