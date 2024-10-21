package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.model.ImplementingAgencyProjectMonitoring

class NlmIAProjectMonitoringCommitteeAdapter(
    private val programmeList: MutableList<ImplementingAgencyProjectMonitoring>,
) : RecyclerView.Adapter<NlmIAProjectMonitoringCommitteeAdapter.NlmIACompositionOFGoverning>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIACompositionOFGoverning {

                val binding = ItemCompositionOfGoverningNlmIaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIACompositionOFGoverning(binding)

        }

    override fun onBindViewHolder(holder: NlmIACompositionOFGoverning, position: Int) {

        val currentItem = programmeList[position]

        holder.binding.nameOfOfficial.setText(currentItem.name_of_official)
        holder.binding.nameOfDesignation.setText(currentItem.designation)
        holder.binding.nameOfOrganization.setText(currentItem.organization)

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

    inner class NlmIACompositionOFGoverning(val binding: ItemCompositionOfGoverningNlmIaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
