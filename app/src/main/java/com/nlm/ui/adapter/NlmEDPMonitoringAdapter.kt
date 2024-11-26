
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
import com.nlm.callBack.CallBackNlmEdpMonitor
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmEdpMonitotringBinding
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.NlmEdpMonitoring
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmEDPMonitoringAdapter(
    private val context: Context,
    private val programmeList: MutableList<NlmEdpMonitoring>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackNlmEdpMonitor,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<NlmEDPMonitoringAdapter.NlmEDPMonitoringAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmEDPMonitoringAdapterViewHolder {

        val binding = ItemNlmEdpMonitotringBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NlmEDPMonitoringAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NlmEDPMonitoringAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.etNameOfBeneficiary.isEnabled = false
            holder.binding.etCategory.isEnabled = false
            holder.binding.etProjcetFinancing.isEnabled = false
            holder.binding.etTypeofFarming.isEnabled = false
            holder.binding.etCapacity.isEnabled = false
            holder.binding.tvFull.isEnabled = false
            holder.binding.etFinacial.isEnabled = false
            holder.binding.etNoAnimals.isEnabled = false
            holder.binding.etBalance.isEnabled = false
            holder.binding.etNoFarmers.isEnabled = false
            holder.binding.etNoJob.isEnabled = false

            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameOfBeneficiary.isEnabled = false
        holder.binding.etCategory.isEnabled = false
        holder.binding.etProjcetFinancing.isEnabled = false
        holder.binding.etTypeofFarming.isEnabled = false
        holder.binding.etCapacity.isEnabled = false
        holder.binding.tvFull.isEnabled = false
        holder.binding.etFinacial.isEnabled = false
        holder.binding.etNoAnimals.isEnabled = false
        holder.binding.etBalance.isEnabled = false
        holder.binding.etNoFarmers.isEnabled = false
        holder.binding.etNoJob.isEnabled = false

        holder.binding.etNameOfBeneficiary.setText(currentItem.name_of_beneficiary)
        holder.binding.etCategory.setText(currentItem.category_of_project)
        holder.binding.etProjcetFinancing.setText(currentItem.project_financing)
        holder.binding.etTypeofFarming.setText(currentItem.type_of_farming)
        holder.binding.etCapacity.setText(currentItem.capacity)
        holder.binding.tvFull.text = currentItem.whether_full
        holder.binding.etFinacial.setText(currentItem.financial_status)
        holder.binding.etNoAnimals.setText(currentItem.number_of_animals_marketed.toString())
        holder.binding.etBalance.setText(currentItem.balance_of_animal.toString())
        holder.binding.etNoFarmers.setText(currentItem.number_of_farmers.toString())
        holder.binding.etNoJob.setText(currentItem.number_of_job.toString())

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                NlmEdpMonitoring(
                    currentItem.id,
                    currentItem.name_of_beneficiary,
                    currentItem.category_of_project,
                    currentItem.project_financing,
                    currentItem.type_of_farming,
                    currentItem.capacity,
                    currentItem.whether_full,
                    currentItem.financial_status,
                    currentItem.number_of_animals_marketed,
                    currentItem.balance_of_animal,
                    currentItem.number_of_farmers,
                    currentItem.number_of_job,
                    currentItem.nlm_edp_id
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

    inner class NlmEDPMonitoringAdapterViewHolder(val binding: ItemNlmEdpMonitotringBinding) :
        RecyclerView.ViewHolder(binding.root)
}

