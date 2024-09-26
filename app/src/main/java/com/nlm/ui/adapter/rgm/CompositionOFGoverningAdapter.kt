package com.nlm.ui.adapter.rgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemCompositionOfGoverningBinding

class CompositionOFGoverningAdapter(
    private val programmeList: MutableList<Array<String>>,
) : RecyclerView.Adapter<CompositionOFGoverningAdapter.AvailabilityOfEquipmentViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

                val binding = ItemCompositionOfGoverningBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    AvailabilityOfEquipmentViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {


        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

        // Add new row
        holder.binding.btnAdd.setOnClickListener {
            programmeList.add(arrayOf("", "",""))
            notifyItemInserted(programmeList.size - 1)
            notifyItemChanged(position)
        }
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (programmeList.size > 1) {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size)
            }
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
    private fun handleButtonVisibility(btnAdd: ImageButton, btnDelete: ImageButton, position: Int) {
        if (position == programmeList.size - 1) {
            // Last item, show Add button, hide Delete button
            btnAdd.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
        } else {
            // Hide Add button, show Delete button for other items
            btnAdd.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }
    }
    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemCompositionOfGoverningBinding) :
        RecyclerView.ViewHolder(binding.root)
}
