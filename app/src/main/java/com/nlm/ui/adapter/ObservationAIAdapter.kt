package com.nlm.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemFormat4Edit
import com.nlm.callBack.CallBackItemImportExoticAchivementEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAiObservationBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class ObservationAIAdapter(
    private val context: Context?,
    private val programmeList: MutableList<ArtificialInseminationObservationByNlm>,
    private val viewEdit:String?,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private val callBackEdit: CallBackItemFormat4Edit
) : RecyclerView.Adapter<ObservationAIAdapter.ObservationAIViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationAIViewHolder {

                val binding = ItemAiObservationBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ObservationAIViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ObservationAIViewHolder, position: Int) {

         val items=programmeList[position]
        holder.binding.etNameOfTheCenter.isEnabled=false
        holder.binding.etNumberOFAiPerformed.isEnabled=false
        holder.binding.etWhetherManPowerTrainedForGoatAI.isEnabled=false
        holder.binding.etEquipmentAvailable.isEnabled=false
        if (viewEdit=="view")
        {

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
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,2)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                ArtificialInseminationObservationByNlm(
                   name_of_center = items.name_of_center,
                    number_of_ai_performed = items.number_of_ai_performed,
                    power_trained_ai = items.power_trained_ai,
                    quipment_available = items.quipment_available,
                    id =items.id ,
                ),position,2)
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ObservationAIViewHolder(val binding: ItemAiObservationBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < programmeList.size) {
            programmeList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, programmeList.size)
        } else {
            Log.e("Error", "Invalid index: $position for programmeList of size ${programmeList.size}")
        }
    }
}
