package com.nlm.ui.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackFspCommentNlm
import com.nlm.callBack.CallBackFspNonNlm
import com.nlm.callBack.CallBackSemenDose
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemFspNonPlantNlmBinding
import com.nlm.model.FpFromNonForestFilledByNlmTeam
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.RspAddAverage
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class FspNonPlantStorageNLMAdapter(
    private val context: Context,
    private val programmeList: MutableList<FpFromNonForestFilledByNlmTeam>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackFspNonNlm,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<FspNonPlantStorageNLMAdapter.FspNonPlantStorageNLMAdapterViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FspNonPlantStorageNLMAdapterViewHolder
    {

        val binding = ItemFspNonPlantNlmBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FspNonPlantStorageNLMAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FspNonPlantStorageNLMAdapterViewHolder
                                  , @SuppressLint("RecyclerView") position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.tvDistrict.isEnabled = false
            holder.binding.etBlock.isEnabled = false
            holder.binding.etVillage.isEnabled = false
            holder.binding.etArea.isEnabled = false
            holder.binding.etEstimated.isEnabled = false
            holder.binding.etConsumer.isEnabled = false
            holder.binding.tvAgency.isEnabled = false
            holder.binding.btnDelete.visibility= View.GONE
            holder.binding.btnEdit.visibility= View.GONE
        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.tvDistrict.isEnabled = false
        holder.binding.etBlock.isEnabled = false
        holder.binding.etVillage.isEnabled = false
        holder.binding.etArea.isEnabled = false
        holder.binding.etEstimated.isEnabled = false
        holder.binding.etConsumer.isEnabled = false
        holder.binding.tvAgency.isEnabled = false

        holder.binding.tvDistrict.text = currentItem.district_code.toString()
        holder.binding.etBlock.setText(currentItem.block_name)
        holder.binding.etVillage.setText(currentItem.village_name)
        holder.binding.etArea.setText(currentItem.area_covered)
        holder.binding.etEstimated.setText(currentItem.estimated_quantity)
        holder.binding.etConsumer.setText(currentItem.consumer_fodder)
        holder.binding.tvAgency.text = currentItem.agency_involved

        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                FpFromNonForestFilledByNlmTeam(
                    currentItem.id,
                    currentItem.district_code,
                    currentItem.block_name,
                    currentItem.village_name,
                    currentItem.area_covered,
                    currentItem.estimated_quantity,
                    currentItem.consumer_fodder,
                    currentItem.agency_involved,
                    currentItem.fp_from_non_forest_id
                ),position,2)
        }
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteFSPAtId.onClickItemDelete(currentItem.id,position)
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
            Log.e("Error", "Invalid index: $position for programmeList of size ${programmeList.size}")
        }
    }

    inner class FspNonPlantStorageNLMAdapterViewHolder
        (val binding: ItemFspNonPlantNlmBinding) :
        RecyclerView.ViewHolder(binding.root)
}

