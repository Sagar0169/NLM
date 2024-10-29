package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.nlm.databinding.ItemStateSemenInfrastructureBinding
import com.nlm.model.StateSemenInfraGoat
import com.nlm.ui.adapter.RspManPowerAdapter.RspManPowerViewHolder

class StateSemenInfrastructureAdapter(
    private val programmeList: MutableList<StateSemenInfraGoat>,
) : RecyclerView.Adapter<StateSemenInfrastructureAdapter.StateSemenInfrastructureViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateSemenInfrastructureViewHolder {

                val binding = ItemStateSemenInfrastructureBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    StateSemenInfrastructureViewHolder(binding)

        }

    override fun onBindViewHolder(holder: StateSemenInfrastructureViewHolder, position: Int) {
        val currentItem = programmeList[position]
        holder.binding.etListOfEquipment.setText(currentItem.listOfEquipment)
        holder.binding.etYearOfProcurement.setText(currentItem.yearOfProcurement)
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

    inner class StateSemenInfrastructureViewHolder(val binding: ItemStateSemenInfrastructureBinding) :
        RecyclerView.ViewHolder(binding.root)
}
