package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAddDocumentBinding
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemSupportingDocumentsBinding
import com.nlm.model.DocumentData
import com.nlm.model.ImplementingAgencyDocument

class SupportingDocumentAdapterWithDialog(
    private val programmeList: MutableList<ImplementingAgencyDocument>,
    private val viewEdit: String?,
) : RecyclerView.Adapter<SupportingDocumentAdapterWithDialog.SupportingDocument>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportingDocument {

                val binding = ItemAddDocumentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    SupportingDocument(binding)

        }

    override fun onBindViewHolder(holder: SupportingDocument, position: Int) {

        val items=programmeList[position]
        // Handle visibility of add/delete buttons
        holder.binding.etDescription.text=items.description
        if (items.ia_document!=null)
        {
        holder.binding.etFile.text=items.ia_document}
        else{
            holder.binding.etFile.text=items.nlm_document
        }
        // Delete row
        if(viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
        }
        holder.binding.btnDelete.setOnClickListener {
            if (position >= 0 && position < programmeList.size) {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size) // Notify changes in the range
            }
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class SupportingDocument(val binding: ItemAddDocumentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
