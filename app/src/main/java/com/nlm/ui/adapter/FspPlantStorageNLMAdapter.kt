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
import com.nlm.callBack.CallBackSemenDose
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemFspPlantStorageBinding
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.RspAddAverage
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class FspPlantStorageNLMAdapter(
    private val context: Context,
    private val programmeList: MutableList<FspPlantStorageCommentsOfNlm>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackFspCommentNlm,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<FspPlantStorageNLMAdapter.FspPlantStorageNLMViewHolder
        >() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FspPlantStorageNLMViewHolder
    {

        val binding = ItemFspPlantStorageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FspPlantStorageNLMViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FspPlantStorageNLMViewHolder
                                  , @SuppressLint("RecyclerView") position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view"
        ) {
            holder.binding.etNameOfAgency.isEnabled = false
            holder.binding.etAddress.isEnabled = false
            holder.binding.etQuantity.isEnabled = false
            holder.binding.etInfra.isEnabled = false
            holder.binding.btnDelete.visibility= View.GONE
            holder.binding.btnEdit.visibility= View.GONE
        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameOfAgency.setText(currentItem.name_of_agency)
        holder.binding.etAddress.setText(currentItem.address)
        holder.binding.etQuantity.setText(currentItem.quantity_of_seed_graded)
        holder.binding.etInfra.setText(currentItem.infrastructure_available)
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
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                FspPlantStorageCommentsOfNlm(
                    currentItem.id,
                    currentItem.name_of_agency,
                    currentItem.address,
                    currentItem.quantity_of_seed_graded,
                    currentItem.infrastructure_available,
                    currentItem.fsp_plant_storage_id
                ),position,2)
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

    inner class FspPlantStorageNLMViewHolder
        (val binding: ItemFspPlantStorageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
