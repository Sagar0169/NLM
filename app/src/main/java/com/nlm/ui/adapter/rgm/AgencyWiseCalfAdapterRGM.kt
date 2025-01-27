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
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseAi
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseCalf
import com.nlm.callBack.CallBackItemTypeRGMStateIACompositionList
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.ItemAnyOfAssetsRgmBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyAgencyWiseCalfBorn
import com.nlm.model.RgmImplementingAgencyCompositionOfGoverningBody
import com.nlm.model.RgmImplementingAgencyProjectMonitoring
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class AgencyWiseCalfAdapterRGM(
    private val context: Context,
    private val RGMAgencyWiseCalfList: MutableList<RgmImplementingAgencyAgencyWiseCalfBorn>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemTypeRGMStateIAAgencyWiseAi,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<AgencyWiseCalfAdapterRGM.AvailabilityOfEquipmentViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

        val binding = ItemAgencyWiseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return    AvailabilityOfEquipmentViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {

        val currentItem = RGMAgencyWiseCalfList[position]
        holder.binding.etAgency.isEnabled=false
        holder.binding.et2022.isEnabled=false
        holder.binding.et2023.isEnabled=false
        holder.binding.et2024.isEnabled=false
        if (viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
        }else {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etAgency.setText(currentItem.agency_name)
        holder.binding.et2022.setText(currentItem.first_year)
        holder.binding.et2023.setText(currentItem.secound_year)
        holder.binding.et2024.setText(currentItem.third_year)


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
                RgmImplementingAgencyAgencyWiseAiDone(
                    agency_name = currentItem.agency_name,
                    first_year = currentItem.first_year,
                    secound_year = currentItem.secound_year,
                    third_year = currentItem.third_year,
                id = currentItem.id, //item id
                    rgm_implementing_agency_id= currentItem.rgm_implementing_agency_id// table id
            ),position,2)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
    }

    override fun getItemCount(): Int = RGMAgencyWiseCalfList.size
    // Helper method to manage button visibility
    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < RGMAgencyWiseCalfList.size) {
            RGMAgencyWiseCalfList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, RGMAgencyWiseCalfList.size)
        } else {
            Log.e("Error", "Invalid index: $position for programmeList of size ${RGMAgencyWiseCalfList.size}")
        }
    }
    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemAgencyWiseBinding) :
        RecyclerView.ViewHolder(binding.root)
}
