package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R

class ProgrammeAdapter(
    private val programmeList: MutableList<Array<String>>
) : RecyclerView.Adapter<ProgrammeAdapter.ProgrammeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_programme, parent, false)
        return ProgrammeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgrammeViewHolder, position: Int) {
        val programme = programmeList[position]

        // Restore the saved text for the item
        holder.etProgrammeName.setText(programme[0])
        holder.etPlace.setText(programme[1])
        holder.etDurationYear.setText(programme[2])

        holder.etProgrammeName.addTextChangedListener {
            programmeList[holder.adapterPosition][0] = it.toString()
        }

        holder.etPlace.addTextChangedListener {
            programmeList[holder.adapterPosition][1] = it.toString()
        }

        holder.etDurationYear.addTextChangedListener {
            programmeList[holder.adapterPosition][2] = it.toString()
        }

        // Manage visibility of buttons
        holder.btnAdd.visibility = if (position == programmeList.size - 1) View.VISIBLE else View.GONE
        holder.btnDelete.visibility = View.VISIBLE

        holder.btnDelete.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION && position < programmeList.size) {
                if (programmeList.size > 1) {
                    // Save data before removing the item
                    programmeList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, programmeList.size)
                }
            }
        }


        holder.btnAdd.setOnClickListener {
            // Save data before adding a new item
            programmeList.add(arrayOf("", "", ""))
            notifyItemInserted(programmeList.size - 1)
            notifyItemChanged(holder.adapterPosition)  // Update the previous last item to hide its add button
        }
    }


    override fun getItemCount(): Int = programmeList.size

    inner class ProgrammeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etProgrammeName: EditText = itemView.findViewById(R.id.etProgrammeName)
        val etPlace: EditText = itemView.findViewById(R.id.etPlace)
        val etDurationYear: EditText = itemView.findViewById(R.id.etDurationYear)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val btnAdd: ImageButton = itemView.findViewById(R.id.btnAdd)
    }
}
