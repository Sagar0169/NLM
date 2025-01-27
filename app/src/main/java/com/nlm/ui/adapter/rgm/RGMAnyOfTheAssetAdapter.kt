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
import com.nlm.callBack.CallBackItemTypeRGMStateIAAnyOfTheAssetEdit
import com.nlm.callBack.CallBackItemTypeRGMStateIACompositionList
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.AnyOfTheAssetsCreatedBinding
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.ItemAnyOfAssetsRgmBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyAgencyWiseCalfBorn
import com.nlm.model.RgmImplementingAgencyAnyOfTheAsset
import com.nlm.model.RgmImplementingAgencyCompositionOfGoverningBody
import com.nlm.model.RgmImplementingAgencyProjectMonitoring
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class RGMAnyOfTheAssetAdapter(
    private val context: Context,
    private val RgmImplementingAgencyAnyOfTheAssetList: MutableList<RgmImplementingAgencyAnyOfTheAsset>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemTypeRGMStateIAAnyOfTheAssetEdit,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<RGMAnyOfTheAssetAdapter.AvailabilityOfEquipmentViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

        val binding = AnyOfTheAssetsCreatedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return    AvailabilityOfEquipmentViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {

        val currentItem = RgmImplementingAgencyAnyOfTheAssetList[position]
        holder.binding.etAssets.isEnabled=false
        holder.binding.etReasons.isEnabled=false

        if (viewEdit=="view")
        {
            holder.binding.btnDelete.visibility=View.GONE
        }else {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etAssets.setText(currentItem.assets)
        holder.binding.etReasons.setText(currentItem.reasons)


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
                RgmImplementingAgencyAnyOfTheAsset(
                    assets= currentItem.assets,
                    reasons = currentItem.reasons,
                id = currentItem.id, //item id
                    rgm_implementing_agency_id= currentItem.rgm_implementing_agency_id// table id
            ),position,1)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
    }

    override fun getItemCount(): Int = RgmImplementingAgencyAnyOfTheAssetList.size
    // Helper method to manage button visibility
    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < RgmImplementingAgencyAnyOfTheAssetList.size) {
            RgmImplementingAgencyAnyOfTheAssetList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, RgmImplementingAgencyAnyOfTheAssetList.size)
        } else {
            Log.e("Error", "Invalid index: $position for programmeList of size ${RgmImplementingAgencyAnyOfTheAssetList.size}")
        }
    }
    inner class AvailabilityOfEquipmentViewHolder(val binding: AnyOfTheAssetsCreatedBinding) :
        RecyclerView.ViewHolder(binding.root)
}
