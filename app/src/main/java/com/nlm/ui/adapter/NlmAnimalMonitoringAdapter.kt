package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackAssistanceEANlm
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackNlmAnimalMonitor
import com.nlm.callBack.CallBackNlmEdpMonitor
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmAnimalBinding
import com.nlm.model.AhidfMonitoring
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.NlmEdpMonitoring
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmAnimalMonitoringAdapter(
    private val context: Context,
    private val programmeList: MutableList<AhidfMonitoring>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackNlmAnimalMonitor,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<NlmAnimalMonitoringAdapter.NlmAnimalMonitoringAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmAnimalMonitoringAdapterViewHolder {

        val binding = ItemNlmAnimalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NlmAnimalMonitoringAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NlmAnimalMonitoringAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.etNameOfBeneficiary.isEnabled = false
            holder.binding.etCategory.isEnabled = false
            holder.binding.tvProjectFinancing.isEnabled = false
            holder.binding.etTypeofFarming.isEnabled = false
            holder.binding.etCapacity.isEnabled = false
            holder.binding.tvFull.isEnabled = false
            holder.binding.etFinacial.isEnabled = false
            holder.binding.etProcessingCapacity.isEnabled = false
            holder.binding.etNumberOfFarmer.isEnabled = false
            holder.binding.etNoJob.isEnabled = false

            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameOfBeneficiary.isEnabled = false
        holder.binding.etCategory.isEnabled = false
        holder.binding.tvProjectFinancing.isEnabled = false
        holder.binding.etTypeofFarming.isEnabled = false
        holder.binding.etCapacity.isEnabled = false
        holder.binding.tvFull.isEnabled = false
        holder.binding.etFinacial.isEnabled = false
        holder.binding.etProcessingCapacity.isEnabled = false
        holder.binding.etNumberOfFarmer.isEnabled = false
        holder.binding.etNoJob.isEnabled = false

        holder.binding.etNameOfBeneficiary.setText(currentItem.name_of_beneficiary)
        holder.binding.etCategory.setText(currentItem.category_of_project)
        holder.binding.tvProjectFinancing.text = currentItem.project_financing
        holder.binding.etTypeofFarming.setText(currentItem.type_of_farming)
        holder.binding.etCapacity.setText(currentItem.capacity)
        holder.binding.tvFull.text = currentItem.whether_full
        holder.binding.etFinacial.setText(currentItem.financial_status?.toString() ?: "")
        holder.binding.etProcessingCapacity.text =
            if (currentItem.processing_capacity_nlm.toString().toInt() == 0) {
                "No"
            } else {
                "Yes"
            }
        holder.binding.etNumberOfFarmer.setText(currentItem.number_of_farmers?.toString() ?: "")
        holder.binding.etNoJob.setText(currentItem.number_of_job?.toString() ?: "")

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                AhidfMonitoring(
                    currentItem.id,
                    currentItem.name_of_beneficiary,
                    currentItem.category_of_project,
                    currentItem.project_financing,
                    currentItem.type_of_farming,
                    currentItem.capacity,
                    currentItem.whether_full,
                    currentItem.financial_status,
                    currentItem.processing_capacity_nlm,
                    currentItem.number_of_farmers,
                    currentItem.number_of_job,
                    currentItem.ahidf_id
                ), position, 2
            )
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }

        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteFSPAtId.onClickItemDelete(currentItem.id, position)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }


    }

    override fun getItemCount(): Int = programmeList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < programmeList.size) {
            programmeList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, programmeList.size)
        } else {
            Log.e(
                "Error",
                "Invalid index: $position for programmeList of size ${programmeList.size}"
            )
        }
    }

    inner class NlmAnimalMonitoringAdapterViewHolder(val binding: ItemNlmAnimalBinding) :
        RecyclerView.ViewHolder(binding.root)
}

