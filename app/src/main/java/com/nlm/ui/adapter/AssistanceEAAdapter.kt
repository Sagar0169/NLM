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
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmTrainingInstituteBinding
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class AssistanceEAAdapter(
    private val context: Context,
    private val programmeList: MutableList<AssistanceForEaTrainingInstitute>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackAssistanceEANlm,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<AssistanceEAAdapter.AssistanceEAAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssistanceEAAdapterViewHolder {

        val binding = ItemNlmTrainingInstituteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AssistanceEAAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: AssistanceEAAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.etNameInstitute.isEnabled = false
            holder.binding.etAddress.isEnabled = false
            holder.binding.etTraining.isEnabled = false
            holder.binding.etNoParticipants.isEnabled = false
            holder.binding.etNoProvide.isEnabled = false
            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameInstitute.isEnabled = false
        holder.binding.etAddress.isEnabled = false
        holder.binding.etTraining.isEnabled = false
        holder.binding.etNoParticipants.isEnabled = false
        holder.binding.etNoProvide.isEnabled = false
        holder.binding.etNameInstitute.setText(currentItem.name_of_institute)
        currentItem.address_for_training.toString().let { holder.binding.etAddress.setText(it) }
        holder.binding.etTraining.setText(currentItem.training_courses_run)
        holder.binding.etNoParticipants.setText(currentItem.no_of_participants_trained?.toString()?:"")
        holder.binding.etNoProvide.setText(currentItem.no_of_provide_information?.toString()?:"")
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

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                AssistanceForEaTrainingInstitute(
                    currentItem.id,
                    currentItem.name_of_institute,
                    currentItem.address_for_training,
                    currentItem.training_courses_run,
                    currentItem.no_of_participants_trained,
                    currentItem.no_of_provide_information,
                    currentItem.assistance_for_ea_id
                ), position, 2
            )
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
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

    inner class AssistanceEAAdapterViewHolder(val binding: ItemNlmTrainingInstituteBinding) :
        RecyclerView.ViewHolder(binding.root)
}

