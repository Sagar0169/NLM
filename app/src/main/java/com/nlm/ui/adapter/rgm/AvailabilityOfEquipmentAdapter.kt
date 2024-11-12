package com.nlm.ui.adapter.rgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.model.RspBasicInfoEquipment
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class AvailabilityOfEquipmentAdapter(
    private val programmeList: MutableList<RspBasicInfoEquipment>,
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
//        if (viewEdit == "view") {
//            holder.binding.etListOfEquipment.isEnabled = false
//            holder.binding.etYearOfProcurement.isEnabled = false
//            holder.binding.btnDelete.hideView()
//        }
        holder.binding.etListOfEquipment.setText(currentItem.rsp_list_of_equipment)
        holder.binding.etMake.setText(currentItem.rsp_make)
        holder.binding.etMake.showView()
        holder.binding.tvMake.showView()
        holder.binding.etYearOfProcurement.setText(currentItem.rsp_year_of_procurement)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = programmeList.size


    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemAvilabilityOfEquipmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
