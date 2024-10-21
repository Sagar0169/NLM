package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.runtime.currentRecomposeScope
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.AddItemCallBack
import com.nlm.callBack.AddItemCallBackProjectMonitoring
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring

class NlmIAProjectMonitoringCommitteeAdapter(
    private val programmeList: MutableList<ImplementingAgencyProjectMonitoring>,
    private val callBackItem: AddItemCallBackProjectMonitoring,
) : RecyclerView.Adapter<NlmIAProjectMonitoringCommitteeAdapter.NlmIACompositionOFGoverning>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIACompositionOFGoverning {

                val binding = ItemCompositionOfGoverningNlmIaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIACompositionOFGoverning(binding)

        }

    override fun onBindViewHolder(holder: NlmIACompositionOFGoverning, position: Int) {

        val currentItem = programmeList[position]

        // Set the existing data (if any) in the fields to prevent them from being cleared
        holder.binding.nameOfOfficial.setText(currentItem.name_of_official ?: "")
        holder.binding.nameOfDesignation.setText(currentItem.designation ?: "")
        holder.binding.nameOfOrganization.setText(currentItem.organization ?: "")

        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

        // Add button logic
        holder.binding.btnAdd.setOnClickListener {
            val currentText = holder.binding.nameOfOfficial.text.toString().trim()
            val currentText2 = holder.binding.nameOfDesignation.text.toString().trim()
            val currentText3 = holder.binding.nameOfOrganization.text.toString().trim()

            // Ensure that the current field is not empty
            if (currentText.isNotEmpty() && currentText2.isNotEmpty() && currentText3.isNotEmpty()) {
                // Update the current item with the new data
                programmeList[position] = ImplementingAgencyProjectMonitoring(currentText, currentText2, currentText3, currentItem.id)
                // Add a new empty item at the end of the list
                callBackItem.onClickItem2(programmeList)
                programmeList.add(ImplementingAgencyProjectMonitoring("", "", "", null))

                // Notify that a new item has been inserted
                notifyItemInserted(programmeList.size - 1)
                notifyItemChanged(position)  // Update the current row
            } else {
                // Show a message to the user to fill in all fields before adding a new one
                Toast.makeText(holder.itemView.context, "Please fill in all fields before adding a new one.", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete button logic
        holder.binding.btnDelete.setOnClickListener {
            if (programmeList.size > 1) {
                // Remove the item at the current position
                programmeList.removeAt(position)

                // Notify that an item has been removed and update the RecyclerView
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
    inner class NlmIACompositionOFGoverning(val binding: ItemCompositionOfGoverningNlmIaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
