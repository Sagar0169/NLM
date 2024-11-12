package com.nlm.ui.adapter.rgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.RspAddBucksList
import com.nlm.utilities.showView

class AverageSemenDoseAdapter(
    private val programmeList: MutableList<RspAddBucksList>,
) : RecyclerView.Adapter<AverageSemenDoseAdapter.AverageSemenDoseViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AverageSemenDoseViewHolder {

                val binding = ItemQualityBuckBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    AverageSemenDoseViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AverageSemenDoseViewHolder, position: Int) {
        val currentItem = programmeList[position]
//        if (viewEdit == "view") {
//            holder.binding.etListOfEquipment.isEnabled = false
//            holder.binding.etYearOfProcurement.isEnabled = false
//            holder.binding.btnDelete.hideView()
//        }
        holder.binding.etBreedMaintained.setText(currentItem.etBreedMaintained)
        holder.binding.etAnimal.setText(currentItem.etAnimal)
        holder.binding.etAvgAge.setText(currentItem.etAvgAge)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int = programmeList.size

    inner class AverageSemenDoseViewHolder(val binding: ItemQualityBuckBinding) :
        RecyclerView.ViewHolder(binding.root)
}
