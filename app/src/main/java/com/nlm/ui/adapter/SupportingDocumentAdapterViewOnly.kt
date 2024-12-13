package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackItemUploadDocEdit
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
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme

class SupportingDocumentAdapterViewOnly(
    private val context: Context?,
    private val programmeList: MutableList<ImplementingAgencyDocument>,
    private val callBackEdit: CallBackItemUploadDocEdit
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
        holder.itemView.setOnClickListener{
                callBackEdit.onClickItemEditDoc(
                    ImplementingAgencyDocument(
                        description = items.description,
                        id = items.id,
                        implementing_agency_id = items.implementing_agency_id,
                        artificial_insemination_id = items.artificial_insemination_id,
                        rsp_laboratory_semen_id = items.rsp_laboratory_semen_id,
                        state_semen_bank_id = items.state_semen_bank_id,
                        import_of_exotic_goat_id = items.import_of_exotic_goat_id,
                        assistance_for_qfsp_id = items.assistance_for_qfsp_id,
                        fsp_plant_storage_id = items.fsp_plant_storage_id,
                        ia_document = items.ia_document,
                        is_edit = false,
                        is_ia=true
                    ),position)

        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class SupportingDocument(val binding: ItemAddDocumentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
