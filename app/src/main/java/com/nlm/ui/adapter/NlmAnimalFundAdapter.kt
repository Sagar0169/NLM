package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackAnimalFund
import com.nlm.callBack.CallBackDeleteFormatAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmIaAnimalFundBinding
import com.nlm.model.AhidfFormatForNlm
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmAnimalFundAdapter(
    private val context: Context,
    private val programmeList: MutableList<AhidfFormatForNlm>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackAnimalFund,
    private val callBackDeleteFSPAtId: CallBackDeleteFormatAtId,
) : RecyclerView.Adapter<NlmAnimalFundAdapter.NlmAnimalFundAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmAnimalFundAdapterViewHolder {

        val binding = ItemNlmIaAnimalFundBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NlmAnimalFundAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NlmAnimalFundAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.etCategory.isEnabled = false
            holder.binding.etNoProject.isEnabled = false
            holder.binding.etCostOfProject.isEnabled = false
            holder.binding.etTermLoan.isEnabled = false
            holder.binding.etTotalNoEmp.isEnabled = false
            holder.binding.etProcessing.isEnabled = false
            holder.binding.etBirth.isEnabled = false
            holder.binding.etAvg.isEnabled = false

            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit"&& getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24) {
            holder.binding.btnEdit.showView()
            holder.binding.btnDelete.showView()
        }
        holder.binding.etCategory.isEnabled = false
        holder.binding.etNoProject.isEnabled = false
        holder.binding.etCostOfProject.isEnabled = false
        holder.binding.etTermLoan.isEnabled = false
        holder.binding.etTotalNoEmp.isEnabled = false
        holder.binding.etProcessing.isEnabled = false
        holder.binding.etBirth.isEnabled = false
        holder.binding.etAvg.isEnabled = false

        holder.binding.etCategory.setText(currentItem.category_of_project)
        holder.binding.etNoProject.setText(currentItem.no_of_project?.toString() ?: "")
        holder.binding.etCostOfProject.setText(currentItem.cost_of_project?.toString() ?: "")
        holder.binding.etTermLoan.setText(currentItem.term_loan?.toString() ?: "")
        holder.binding.etTotalNoEmp.setText(
            currentItem.total_employment_generated?.toString() ?: ""
        )
        holder.binding.etProcessing.setText(currentItem.processing_capacity?.toString() ?: "")
        holder.binding.etBirth.setText(currentItem.birth_percentage?.toString() ?: "")
        holder.binding.etAvg.setText(currentItem.average_revenue_earned?.toString() ?: "")


        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                AhidfFormatForNlm(
                    currentItem.id,
                    currentItem.category_of_project,
                    currentItem.no_of_project,
                    currentItem.cost_of_project,
                    currentItem.term_loan,
                    currentItem.total_employment_generated,
                    currentItem.processing_capacity,
                    currentItem.birth_percentage,
                    currentItem.average_revenue_earned,
                    currentItem.ahidf_id,
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

    inner class NlmAnimalFundAdapterViewHolder(val binding: ItemNlmIaAnimalFundBinding) :
        RecyclerView.ViewHolder(binding.root)
}


