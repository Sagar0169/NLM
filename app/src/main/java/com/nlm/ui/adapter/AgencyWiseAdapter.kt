package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R

class AgencyWiseAdapter(
    private val programmeList: MutableList<Array<String>>
) : RecyclerView.Adapter<AgencyWiseAdapter.ProgrammeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agency_wise, parent, false)
        return ProgrammeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgrammeViewHolder, position: Int) {


        // Manage visibility of buttons based on position
        if (position == programmeList.size - 1) {
            // If this is the last item, hide the add button (since it can only be removed)
            holder.btnAdd.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.GONE
        } else {
            // For newly added items or existing ones, hide Add button and show Delete button
            holder.btnAdd.visibility = View.GONE
            holder.btnDelete.visibility = View.VISIBLE
        }

        // Add button click listener
        holder.btnAdd.setOnClickListener {
            // Add a new row (array with default empty values)
            programmeList.add(arrayOf("", "", "", "", ""))
            notifyItemInserted(programmeList.size - 1)
            notifyItemChanged(position)
        }

        // Delete button click listener
        holder.btnDelete.setOnClickListener {
            // Remove the row from the list
            if (programmeList.size > 1) {  // Ensure there's at least one row remaining
                programmeList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size)  // Update other items
            }
        }
    }

    override fun getItemCount(): Int = programmeList.size

    inner class ProgrammeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val btnAdd: ImageButton = itemView.findViewById(R.id.btnAdd)
    }
}
