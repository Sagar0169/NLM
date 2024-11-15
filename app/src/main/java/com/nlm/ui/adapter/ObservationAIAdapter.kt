package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAiObservationBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.utilities.showView

class ObservationAIAdapter(
    private val programmeList: MutableList<ArtificialInseminationObservationByNlm>,
    private val viewEdit:String?
) : RecyclerView.Adapter<ObservationAIAdapter.ObservationAIViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationAIViewHolder {

                val binding = ItemAiObservationBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ObservationAIViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ObservationAIViewHolder, position: Int) {

         val items=programmeList[position]

        if (viewEdit=="view")
        {
            holder.binding.etNameOfTheCenter.isEnabled=false
            holder.binding.etNumberOFAiPerformed.isEnabled=false
            holder.binding.etWhetherManPowerTrainedForGoatAI.isEnabled=false
            holder.binding.etEquipmentAvailable.isEnabled=false
            holder.binding.btnDelete.visibility= View.GONE
        }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameOfTheCenter.setText(items.name_of_center)
        holder.binding.etNumberOFAiPerformed.setText(items.number_of_ai_performed.toString())
        holder.binding.etWhetherManPowerTrainedForGoatAI.setText(items.power_trained_ai)
        holder.binding.etEquipmentAvailable.setText(items.quipment_available)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {

                programmeList.removeAt(position)
                notifyItemRemoved(position)


        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ObservationAIViewHolder(val binding: ItemAiObservationBinding) :
        RecyclerView.ViewHolder(binding.root)
}
