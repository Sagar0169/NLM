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

class SupportingDocumentAdapterViewOnly(
    private val programmeList: MutableList<ImplementingAgencyDocument>,
    private val viewEdit: String?,
) : RecyclerView.Adapter<SupportingDocumentAdapterViewOnly.SupportingDocument>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportingDocument {

                val binding = ItemAddDocumentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    SupportingDocument(binding)

        }

    override fun onBindViewHolder(holder: SupportingDocument, position: Int) {

        val items=programmeList[position]
        // Handle visibility of add/delete buttons
        holder.binding.btnDelete.visibility=View.GONE
        holder.binding.btnEdit.visibility=View.GONE
        holder.binding.etDescription.text=items.description
        holder.binding.etFile.text=items.ia_document
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class SupportingDocument(val binding: ItemAddDocumentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
