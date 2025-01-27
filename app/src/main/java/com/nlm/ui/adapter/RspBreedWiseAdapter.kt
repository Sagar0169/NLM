package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackBreedAvg
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackSemenDoseAvg
import com.nlm.callBack.DialogCallback

import com.nlm.databinding.ItemRspBreedWiseBinding
import com.nlm.model.Result
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspAddEquipment
import com.nlm.model.RspBreedList
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class RspBreedWiseAdapter(
    private val context: Context,
    private val programmeList: MutableList<RspBreedList>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackBreedAvg,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<RspBreedWiseAdapter.RspBreedWiseAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RspBreedWiseAdapterViewHolder {

        val binding = ItemRspBreedWiseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RspBreedWiseAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RspBreedWiseAdapterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {
            holder.binding.tvYear.isEnabled = false
            holder.binding.tvBreed.isEnabled = false
            holder.binding.etInsemination.isEnabled = false
            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.tvYear.setText(currentItem.breed_maintained)
        holder.binding.tvBreed.setText(currentItem.no_of_animals?.toString() ?: "")
        holder.binding.etInsemination.setText(currentItem.average_age)
        // Delete row
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

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                RspBreedList(
                    currentItem.breed_maintained,
                    currentItem.no_of_animals,
                    currentItem.average_age,
                    currentItem.id,
                    currentItem.rsp_laboratory_semen_id
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
            Log.e("Error", "Invalid index: $position for programmeList of size ${programmeList.size}")
        }
    }
    inner class RspBreedWiseAdapterViewHolder(val binding: ItemRspBreedWiseBinding) :
        RecyclerView.ViewHolder(binding.root)
}

