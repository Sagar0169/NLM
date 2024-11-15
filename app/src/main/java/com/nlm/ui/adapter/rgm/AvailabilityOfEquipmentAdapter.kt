package com.nlm.ui.adapter.rgm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackItemManPower
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.model.Result
import com.nlm.model.RspAddEquipment
import com.nlm.model.RspBasicInfoEquipment
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class AvailabilityOfEquipmentAdapter(
    private val context: Context,
    private val programmeList: MutableList<RspAddEquipment>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackAvilabilityEquipment
) : RecyclerView.Adapter<AvailabilityOfEquipmentAdapter.AvailabilityOfEquipmentViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailabilityOfEquipmentViewHolder {

        val binding = ItemAvilabilityOfEquipmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AvailabilityOfEquipmentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            holder.binding.etListOfEquipment.isEnabled = false
            holder.binding.etYearOfProcurement.isEnabled = false
            holder.binding.etMake.isEnabled = false
            holder.binding.btnDelete.hideView()
        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etListOfEquipment.setText(currentItem.list_of_equipment)
        holder.binding.etMake.setText(currentItem.make)
        holder.binding.etMake.showView()
        holder.binding.tvMake.showView()
        holder.binding.etYearOfProcurement.setText(currentItem.year_of_procurement)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                RspAddEquipment(
                    currentItem.id,
                    currentItem.list_of_equipment,
                    currentItem.make,
                    currentItem.year_of_procurement,
                    currentItem.rsp_laboratory_semen_id
                ),position,2)
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

    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemAvilabilityOfEquipmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
