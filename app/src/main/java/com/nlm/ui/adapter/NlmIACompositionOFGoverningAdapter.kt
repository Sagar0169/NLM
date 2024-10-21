package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.AddItemCallBack
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.ImplementingAgencyAdvisoryCommittee

class NlmIACompositionOFGoverningAdapter(
    private val programmeList: MutableList<ImplementingAgencyAdvisoryCommittee>,
//    private val callBackItem: AddItemCallBack,
) : RecyclerView.Adapter<NlmIACompositionOFGoverningAdapter.NlmIACompositionOFGoverning>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIACompositionOFGoverning {

                val binding = ItemCompositionOfGoverningNlmIaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIACompositionOFGoverning(binding)

        }

    override fun onBindViewHolder(holder: NlmIACompositionOFGoverning, position: Int) {
        val currentItem = programmeList[position]
        holder.binding.nameOfOfficial.setText(currentItem.name_of_the_official)
        holder.binding.nameOfDesignation.setText(currentItem.designation)
        holder.binding.nameOfOrganization.setText(currentItem.organization)
        // Delete button logic
        holder.binding.btnDelete.setOnClickListener {
                  programmeList.removeAt(position)
                notifyItemRemoved(position)
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
