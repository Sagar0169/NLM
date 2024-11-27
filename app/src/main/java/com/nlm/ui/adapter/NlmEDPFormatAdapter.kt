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
import com.nlm.callBack.CallBackDeleteFormatAtId
import com.nlm.callBack.CallBackNlmEdpFormat
import com.nlm.callBack.CallBackNlmEdpMonitor
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmEdpFormatBinding
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.NlmEdpFormatForNlm
import com.nlm.model.NlmEdpMonitoring
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmEDPFormatAdapter(
    private val context: Context,
    private val programmeList: MutableList<NlmEdpFormatForNlm>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackNlmEdpFormat,
    private val callBackDeleteFSPAtId: CallBackDeleteFormatAtId,
) : RecyclerView.Adapter<NlmEDPFormatAdapter.NlmEDPFormatAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmEDPFormatAdapterViewHolder {

        val binding = ItemNlmEdpFormatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NlmEDPFormatAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NlmEDPFormatAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" || getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            holder.binding.etCategory.isEnabled = false
            holder.binding.etNoProject.isEnabled = false
            holder.binding.etCostOfProject.isEnabled = false
            holder.binding.etTotalAnimal.isEnabled = false
            holder.binding.etTotalFarmers.isEnabled = false
            holder.binding.etTotalNoEmp.isEnabled = false
            holder.binding.etBirth.isEnabled = false
            holder.binding.etAvg.isEnabled = false

            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }

        holder.binding.etCategory.isEnabled = false
        holder.binding.etNoProject.isEnabled = false
        holder.binding.etCostOfProject.isEnabled = false
        holder.binding.etTotalAnimal.isEnabled = false
        holder.binding.etTotalFarmers.isEnabled = false
        holder.binding.etTotalNoEmp.isEnabled = false
        holder.binding.etBirth.isEnabled = false
        holder.binding.etAvg.isEnabled = false

        if (currentItem.id == null && currentItem.category_of_project == "dummy") {
            holder.binding.etCategory.setText("")
            holder.binding.etNoProject.setText("")
            holder.binding.etCostOfProject.setText("")
            holder.binding.etTotalAnimal.setText("")
            holder.binding.etTotalFarmers.setText("")
            holder.binding.etTotalNoEmp.setText("")
            holder.binding.etBirth.setText("")
            holder.binding.etAvg.setText("")

        } else {
            holder.binding.etCategory.setText(currentItem.category_of_project)
            holder.binding.etNoProject.setText(currentItem.no_of_project?.toString() ?: "")
            holder.binding.etCostOfProject.setText(currentItem.cost_of_project?.toString() ?: "")
            holder.binding.etTotalAnimal.setText(
                currentItem.total_animal_inducted?.toString() ?: ""
            )
            holder.binding.etTotalFarmers.setText(
                currentItem.total_farmers_impacted?.toString() ?: ""
            )
            holder.binding.etTotalNoEmp.setText(
                currentItem.total_employment_generated?.toString() ?: ""
            )
            holder.binding.etBirth.setText(currentItem.birth_percentage?.toString() ?: "")
            holder.binding.etAvg.setText(currentItem.average_revenue_earned?.toString() ?: "")
        }




        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                NlmEdpFormatForNlm(
                    currentItem.id,
                    currentItem.category_of_project,
                    currentItem.no_of_project,
                    currentItem.cost_of_project,
                    currentItem.total_animal_inducted,
                    currentItem.total_farmers_impacted,
                    currentItem.total_employment_generated,
                    currentItem.birth_percentage,
                    currentItem.average_revenue_earned,
                    currentItem.nlm_edp_id,
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
                            callBackDeleteFSPAtId.onClickItemFormatDelete(currentItem.id, position)
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

    inner class NlmEDPFormatAdapterViewHolder(val binding: ItemNlmEdpFormatBinding) :
        RecyclerView.ViewHolder(binding.root)
}


