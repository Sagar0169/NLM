package com.nlm.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.AddItemCallBackFundsRecieved
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemFundsReceivedListEdit
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class NlmIAFundsRecievedAdapter(
    private val context: Context,
    private val programmeList: MutableList<ImplementingAgencyFundsReceived>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemFundsReceivedListEdit,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    ) : RecyclerView.Adapter<NlmIAFundsRecievedAdapter.NlmIAFundsRecieved>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIAFundsRecieved {

                val binding = ItemFundsReceivedNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIAFundsRecieved(binding)

        }

    override fun onBindViewHolder(holder: NlmIAFundsRecieved, position: Int) {

        val currentItem = programmeList[position]
        holder.binding.tvSubmit.visibility= View.GONE
        if (viewEdit=="view")
        {
            holder.binding.etYear.isEnabled=false
            holder.binding.etFormDahd.isEnabled=false
            holder.binding.etStateGovt.isEnabled=false
            holder.binding.etAnyOther.isEnabled=false
            holder.binding.etPhysicalProgress.isEnabled=false

            holder.binding.btnDelete.visibility= View.GONE
            holder.binding.tvSubmit.visibility= View.GONE
        }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.etYear.setText(currentItem.year?.toString())
        holder.binding.etFormDahd.setText(currentItem.from_dahd?.toString())
        holder.binding.etStateGovt.setText(currentItem.state_govt?.toString())
        holder.binding.etAnyOther.setText(currentItem.any_other?.toString())
        holder.binding.etPhysicalProgress.setText(currentItem.physical_progress?.toString())

        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        callBackDeleteAtId.onClickItem(currentItem.id,position,1)
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                ImplementingAgencyFundsReceived(
                year = currentItem.year,
                from_dahd = currentItem.from_dahd,
                state_govt = currentItem.state_govt,
                 any_other = currentItem.any_other,
                physical_progress = currentItem.physical_progress,
                id = currentItem.id,
                implementing_agency_id = currentItem.implementing_agency_id
            ),position)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
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
    override fun getItemCount(): Int = programmeList.size

    inner class NlmIAFundsRecieved(val binding: ItemFundsReceivedNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
