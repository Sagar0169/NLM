package com.nlm.ui.adapter.rgm

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseAi
import com.nlm.callBack.CallBackItemTypeRGMStateIANumberOfFertilityCampsEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.NumberOfFertilityCampsItemsBinding
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyNumberOfFertility
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class RGMNumberOfFertilityCampsAdapter(
    private val context: Context,
    private val RGMAgencyWiseCalfList: MutableList<RgmImplementingAgencyNumberOfFertility>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemTypeRGMStateIANumberOfFertilityCampsEdit,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<RGMNumberOfFertilityCampsAdapter.AvailabilityOfEquipmentViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

        val binding = NumberOfFertilityCampsItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return    AvailabilityOfEquipmentViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {

        val currentItem = RGMAgencyWiseCalfList[position]
        holder.binding.etNumberOfFertility.isEnabled=false
        holder.binding.etNoOfAnimals.isEnabled=false

        if (viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
        }else {
            holder.binding.btnEdit.showView()
        }
        currentItem.no_of_fertility_camps.toString().let { holder.binding.etNumberOfFertility.setText(it) }
        currentItem.no_of_animals_treated.toString().let { holder.binding.etNoOfAnimals.setText(it) }


        holder.binding.btnDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        callBackDeleteAtId.onClickItem(currentItem.id,position,3)
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                RgmImplementingAgencyNumberOfFertility(
                    no_of_fertility_camps = currentItem.no_of_fertility_camps,
                    no_of_animals_treated = currentItem.no_of_animals_treated,
                id = currentItem.id, //item id
                    rgm_implementing_agency_id= currentItem.rgm_implementing_agency_id// table id
            ),position,1)
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
    inner class AvailabilityOfEquipmentViewHolder(val binding: NumberOfFertilityCampsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
