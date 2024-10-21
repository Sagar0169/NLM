package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.AddItemCallBackFundsRecieved
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.ImplementingAgencyFundsReceived

class NlmIAFundsRecievedAdapter(
    private val programmeList: MutableList<ImplementingAgencyFundsReceived>
    ) : RecyclerView.Adapter<NlmIAFundsRecievedAdapter.NlmIAFundsRecieved>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIAFundsRecieved {

                val binding = ItemFundsReceivedNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIAFundsRecieved(binding)

        }

    override fun onBindViewHolder(holder: NlmIAFundsRecieved, position: Int) {

        val currentItem = programmeList[position]

        holder.binding.etYear.setText(currentItem.year?.toString())
        holder.binding.etFormDahd.setText(currentItem.from_dahd?.toString())
        holder.binding.etStateGovt.setText(currentItem.state_govt?.toString())
        holder.binding.etAnyOther.setText(currentItem.any_other?.toString())
        holder.binding.etPhysicalProgress.setText(currentItem.physical_progress?.toString())

        // Delete row
        holder.binding.btnDelete.setOnClickListener {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int = programmeList.size

    inner class NlmIAFundsRecieved(val binding: ItemFundsReceivedNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
