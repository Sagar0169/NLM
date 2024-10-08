package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemSupportingDocumentsBinding

class SupportingDocumentAdapter(
    private val programmeList: MutableList<Array<String>>,
) : RecyclerView.Adapter<SupportingDocumentAdapter.SupportingDocument>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportingDocument {

                val binding = ItemSupportingDocumentsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    SupportingDocument(binding)

        }

    override fun onBindViewHolder(holder: SupportingDocument, position: Int) {


        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

        // Add new row
        holder.binding.btnAdd.setOnClickListener {
            programmeList.add(arrayOf(""))
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
    inner class SupportingDocument(val binding: ItemSupportingDocumentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
