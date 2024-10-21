package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.AddItemCallBackFundsRecieved
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.ImplementingAgencyFundsReceived

class NlmIAFundsRecievedAdapter(
    private val programmeList: MutableList<ImplementingAgencyFundsReceived>,
    private val callBack: AddItemCallBackFundsRecieved,

) : RecyclerView.Adapter<NlmIAFundsRecievedAdapter.NlmIAFundsRecieved>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIAFundsRecieved {

                val binding = ItemFundsReceivedNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIAFundsRecieved(binding)

        }

    override fun onBindViewHolder(holder: NlmIAFundsRecieved, position: Int) {

        val currentItem = programmeList[position]

        // Set the existing data (if any) in the fields to prevent them from being cleared
        holder.binding.etYear.setText(currentItem.year?.toString() ?: "")
        holder.binding.etFormDahd.setText(currentItem.from_dahd?.toString() ?: "")
        holder.binding.etStateGovt.setText(currentItem.state_govt?.toString() ?: "")
        holder.binding.etAnyOther.setText(currentItem.any_other?.toString() ?: "")
        holder.binding.etPhysicalProgress.setText(currentItem.physical_progress?.toString() ?: "")

        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

        // Add new row
        holder.binding.btnAdd.setOnClickListener {
            if (isInputValid(holder)) {
                val year = holder.binding.etYear.text.toString().toIntOrNull() ?: 0
                val fromDahd = holder.binding.etFormDahd.text.toString().toDoubleOrNull() ?: 0.0
                val stateGovt = holder.binding.etStateGovt.text.toString().toDoubleOrNull() ?: 0.0
                val anyOther = holder.binding.etAnyOther.text.toString().toDoubleOrNull() ?: 0.0
                val physicalProgress = holder.binding.etPhysicalProgress.text.toString().toDoubleOrNull() ?: 0.0

                // Update the current item
                programmeList[position] = ImplementingAgencyFundsReceived(
                    year,
                    fromDahd,
                    stateGovt,
                    anyOther,
                    physicalProgress,
                    currentItem.id
                )
                callBack.onClickItem(programmeList)
                // Add a new empty row for further input
                programmeList.add(
                    ImplementingAgencyFundsReceived(
                        null, // Default or empty value
                        null, // Default or empty value
                        null, // Default or empty value
                        null, // Default or empty value
                        null, // Default or empty value
                        null, // Default or empty value
                    )
                )
                notifyItemInserted(programmeList.size - 1)
                notifyItemChanged(position)
            } else {
                Toast.makeText(holder.itemView.context, "Please fill in all fields before adding a new one.", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (programmeList.size > 1) {
                // Remove the item at the current position
                programmeList.removeAt(position)
                // Notify that an item has been removed and update the RecyclerView
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size)
            } else {
                Toast.makeText(holder.itemView.context, "At least one item must remain.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputValid(holder: NlmIAFundsRecieved): Boolean {
        return holder.binding.etYear.text.isNotEmpty() && holder.binding.etFormDahd.text.isNotEmpty() && holder.binding.etStateGovt.text.isNotEmpty()
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
    inner class NlmIAFundsRecieved(val binding: ItemFundsReceivedNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
