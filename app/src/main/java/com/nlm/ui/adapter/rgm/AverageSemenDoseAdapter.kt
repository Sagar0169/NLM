package com.nlm.ui.adapter.rgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.nlm.databinding.ItemQualityBuckBinding

class AverageSemenDoseAdapter(
    private val programmeList: MutableList<Array<String>>,
) : RecyclerView.Adapter<AverageSemenDoseAdapter.AverageSemenDoseViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AverageSemenDoseViewHolder {

                val binding = ItemQualityBuckBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    AverageSemenDoseViewHolder(binding)

        }

    override fun onBindViewHolder(holder: AverageSemenDoseViewHolder, position: Int) {


        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

        // Add new row
        holder.binding.btnAdd.setOnClickListener {
            programmeList.add(arrayOf("", "",""))
            notifyItemInserted(programmeList.size - 1)
            notifyItemChanged(position)
        }
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (programmeList.size > 1) {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size)
            }
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
    private fun handleButtonVisibility(btnAdd: ImageButton, btnDelete: ImageButton, position: Int) {
        if (position == programmeList.size - 1) {
            // Last item, show Add button, hide Delete button
            btnAdd.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
        } else {
            // Hide Add button, show Delete button for other items
            btnAdd.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }
    }
    inner class AverageSemenDoseViewHolder(val binding: ItemQualityBuckBinding) :
        RecyclerView.ViewHolder(binding.root)
}
