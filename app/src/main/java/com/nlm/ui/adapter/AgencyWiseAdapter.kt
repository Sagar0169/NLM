package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.ItemAnyOfAssetsRgmBinding

class AgencyWiseAdapter(
    private val programmeList: MutableList<Array<String>>,
    private val isFrom: Int,
    private val isNOOfCamps: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View type constants
    private val TYPE_AGENCY_WISE = 0
    private val TYPE_ASSETS_RGM = 1

    // Get the view type based on the value of isFrom
    override fun getItemViewType(position: Int): Int {
        return if (isFrom == 1) TYPE_ASSETS_RGM else TYPE_AGENCY_WISE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ASSETS_RGM -> {
                val binding = ItemAnyOfAssetsRgmBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AssetsRGMViewHolder(binding)
            }
            else -> {
                val binding = ItemAgencyWiseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AgencyWiseViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AssetsRGMViewHolder -> {
                // Bind data for the AssetsRGMViewHolder
                if (isNOOfCamps) {
                    holder.binding.tv11.text = "No. of Fertility camps"
                    holder.binding.tv12.text = "No. of Animals Treated"
                } else {
                    holder.binding.tv11.text = "Assets /Components"
                    holder.binding.tv12.text = "Reasons"
                }

                // Handle visibility of add/delete buttons
                handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

                // Add new row for this layout
                holder.binding.btnAdd.setOnClickListener {
                    programmeList.add(arrayOf("", ""))
                    notifyItemInserted(programmeList.size - 1)
                    notifyItemChanged(position)
                }

                // Delete row for this layout
                holder.binding.btnDelete.setOnClickListener {
                    if (programmeList.size > 1) {
                        programmeList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, programmeList.size)
                    }
                }
            }

            is AgencyWiseViewHolder -> {

                // Handle visibility of add/delete buttons
                handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)

                // Add new row for this layout
                holder.binding.btnAdd.setOnClickListener {
                    programmeList.add(arrayOf("", "", "", "", ""))
                    notifyItemInserted(programmeList.size - 1)
                    notifyItemChanged(position)
                }

                // Delete row for this layout
                holder.binding.btnDelete.setOnClickListener {
                    if (programmeList.size > 1) {
                        programmeList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, programmeList.size)
                    }
                }
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

    // ViewHolder for the AgencyWise layout using View Binding
    inner class AgencyWiseViewHolder(val binding: ItemAgencyWiseBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ViewHolder for the AssetsRGM layout using View Binding
    inner class AssetsRGMViewHolder(val binding: ItemAnyOfAssetsRgmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
