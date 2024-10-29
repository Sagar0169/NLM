package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.StateSemenBankOtherManpower
import com.nlm.model.StateSemenManPower

class RspManPowerAdapter(
    private val programmeList: MutableList<StateSemenBankOtherManpower>,
) : RecyclerView.Adapter<RspManPowerAdapter.RspManPowerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RspManPowerViewHolder {

        val binding = ItemRspManpowerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RspManPowerViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RspManPowerViewHolder, position: Int) {
        val currentItem = programmeList[position]
        holder.binding.etDesignation.setText(currentItem.designation)
        holder.binding.etQualification.setText(currentItem.qualification)
        holder.binding.etExperience.setText(currentItem.experience)
        holder.binding.etTrainingStatus.setText(currentItem.training_status)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int = programmeList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class RspManPowerViewHolder(val binding: ItemRspManpowerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
