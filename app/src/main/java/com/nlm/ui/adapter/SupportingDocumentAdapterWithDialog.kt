package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAddDocumentBinding
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemSupportingDocumentsBinding
import com.nlm.model.DocumentData
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility

class SupportingDocumentAdapterWithDialog(
    private val context: Context?,
    private val programmeList: ArrayList<ImplementingAgencyDocument>,
    private val viewEdit: String?,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private val callBackEdit: CallBackItemUploadDocEdit
) : RecyclerView.Adapter<SupportingDocumentAdapterWithDialog.SupportingDocument>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportingDocument {

                val binding = ItemAddDocumentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    SupportingDocument(binding)

        }

    override fun onBindViewHolder(holder: SupportingDocument, @SuppressLint("RecyclerView") position: Int) {

        val items=programmeList[position]
        // Handle visibility of add/delete buttons
        holder.binding.etDescription.text=items.description
        if (items.ia_document!=null &&  getPreferenceOfScheme(context, AppConstants.SCHEME, Result::class.java)?.role_id==24)
        {
        holder.binding.etFile.text=items.ia_document}
        else{
            holder.binding.etFile.text=items.nlm_document
        }
        // Delete row
        if(viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
            holder.binding.btnEdit.visibility=View.GONE
        }
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,1)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }
        holder.binding.btnEdit.setOnClickListener{
            if (items.ia_document!=null &&  getPreferenceOfScheme(context, AppConstants.SCHEME, Result::class.java)?.role_id==24)
            {

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

                    ),position)}
            else{
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
                        nlm_document = items.nlm_document,

                        ),position)
            }

//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
//        holder.binding.btnDelete.setOnClickListener {
//            // add alert here
//
//            if (position >= 0 && position < programmeList.size) {
//                programmeList.removeAt(position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, programmeList.size) // Notify changes in the range
//            }
//        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class SupportingDocument(val binding: ItemAddDocumentBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < programmeList.size) {
            programmeList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, programmeList.size)
        } else {
            Log.e("Error", "Invalid index: $position for programmeList of size ${programmeList.size}")
        }
    }

}

