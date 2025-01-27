package com.nlm.ui.adapter.rgm

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeRGMStateIACompositionList
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.model.RgmImplementingAgencyCompositionOfGoverningBody
import com.nlm.model.RgmImplementingAgencyProjectMonitoring
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class ProjectMonitoringCommitteeAdapterRGM(
    private val programmeList: MutableList<RgmImplementingAgencyProjectMonitoring>,
    private val context: Context,
//    private val programmeList: MutableList<ImplementingAgencyAdvisoryCommittee>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemTypeRGMStateIACompositionList,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<ProjectMonitoringCommitteeAdapterRGM.AvailabilityOfEquipmentViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

                val binding = ItemCompositionOfGoverningBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    AvailabilityOfEquipmentViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {

        val currentItem = programmeList[position]
        holder.binding.etDesignation.isEnabled=false
        holder.binding.etOrganization.isEnabled=false
        if (viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
        }else {
            holder.binding.btnEdit.showView()
        }

        holder.binding.etDesignation.setText(currentItem.designation)
        holder.binding.etOrganization.setText(currentItem.organization)

        holder.binding.btnDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        callBackDeleteAtId.onClickItem(currentItem.id,position,2)
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                RgmImplementingAgencyCompositionOfGoverningBody(
                designations = currentItem.designation,
                organization = currentItem.organization,
                id = currentItem.id, //item id
                    rgm_implementing_agency_id= currentItem.rgm_implementing_agency_id// table id
            ),position,2)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
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
    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemCompositionOfGoverningBinding) :
        RecyclerView.ViewHolder(binding.root)
}
