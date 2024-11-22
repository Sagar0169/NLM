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
import com.nlm.callBack.CallBackItemImportExoticAchivementEdit
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.databinding.ItemImportOfExoticGoatAchievementBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.IdAndDetails
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class ImportExoticAchivementAdapter(
    private val context: Context?,
    private val programmeList: MutableList<ImportOfExoticGoatAchievement>,
    private var viewEdit: String?,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private val callBackEdit: CallBackItemImportExoticAchivementEdit
) : RecyclerView.Adapter<ImportExoticAchivementAdapter.ImportExoticAchivementViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportExoticAchivementViewHolder {

                val binding = ItemImportOfExoticGoatAchievementBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ImportExoticAchivementViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ImportExoticAchivementViewHolder, position: Int) {

         val items=programmeList[position]
        holder.binding.etNoOfAnimals.isEnabled=false
        holder.binding.etF1Generation.isEnabled=false
        holder.binding.etF2Generation.isEnabled=false
        holder.binding.etNoOfAnimalsDistributedF1.isEnabled=false
        holder.binding.etNoOfAnimalsDistributedF2.isEnabled=false
        holder.binding.etPerformanceOfTheAnimals.isEnabled=false
        holder.binding.etBalance.isEnabled=false
        holder.binding.etNoOfAnimals.isFocusable=false
        holder.binding.etF1Generation.isFocusable=false
        holder.binding.etF2Generation.isFocusable=false
        holder.binding.etNoOfAnimalsDistributedF1.isFocusable=false
        holder.binding.etNoOfAnimalsDistributedF2.isFocusable=false
        holder.binding.etPerformanceOfTheAnimals.isFocusable=false
        holder.binding.etBalance.isFocusable=false
        holder.binding.btnEdit.showView()
        if (viewEdit=="view")
        {  holder.binding.btnEdit.hideView()
            holder.binding.etNoOfAnimals.isEnabled=false
            holder.binding.etF1Generation.isEnabled=false
            holder.binding.etF2Generation.isEnabled=false
            holder.binding.etNoOfAnimalsDistributedF1.isEnabled=false
            holder.binding.etNoOfAnimalsDistributedF2.isEnabled=false
            holder.binding.etPerformanceOfTheAnimals.isEnabled=false
            holder.binding.etBalance.isEnabled=false
            holder.binding.etNoOfAnimals.isFocusable=false
            holder.binding.etF1Generation.isFocusable=false
            holder.binding.etF2Generation.isFocusable=false
            holder.binding.etNoOfAnimalsDistributedF1.isFocusable=false
            holder.binding.etNoOfAnimalsDistributedF2.isFocusable=false
            holder.binding.etPerformanceOfTheAnimals.isFocusable=false
            holder.binding.etBalance.isFocusable=false
            holder.binding.btnDelete.hideView()
        }


        holder.binding.etNoOfAnimals.setText(items.number_of_animals?.toString() ?: "")
        holder.binding.etF1Generation.setText(items.f1_generation_produced)
        holder.binding.etF2Generation.setText(items.f2_generation_produced)
        holder.binding.etNoOfAnimalsDistributedF1.setText(items.no_of_animals_f1?.toString() ?: "")
        holder.binding.etNoOfAnimalsDistributedF2.setText(items.no_of_animals_f2?.toString() ?: "")
        holder.binding.etPerformanceOfTheAnimals.setText(items.performance_animals_doorstep)
        holder.binding.etBalance.setText(items.balance)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,3)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                ImportOfExoticGoatAchievement(
                    number_of_animals = items.number_of_animals,
                    f1_generation_produced = items.f1_generation_produced,
                    f2_generation_produced = items.f2_generation_produced,
                    no_of_animals_f1 = items.no_of_animals_f1,
                    no_of_animals_f2 = items.no_of_animals_f2,
                    performance_animals_doorstep = items.performance_animals_doorstep,
                    balance = items.balance,
                    id =items.id ,
                    import_of_exotic_goat_id = items.import_of_exotic_goat_id
                ),position,2)
        }
    }

    override fun getItemCount(): Int = programmeList.size

    inner class ImportExoticAchivementViewHolder(val binding: ItemImportOfExoticGoatAchievementBinding) :
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
