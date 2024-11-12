package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.databinding.ItemImportOfExoticGoatAchievementBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.utilities.hideView

class ImportExoticAchivementAdapter(
    private val programmeList: MutableList<ImportOfExoticGoatAchievement>,
    private var viewEdit: String?
) : RecyclerView.Adapter<ImportExoticAchivementAdapter.ImportExoticAchivementViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportExoticAchivementViewHolder {

                val binding = ItemImportOfExoticGoatAchievementBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ImportExoticAchivementViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ImportExoticAchivementViewHolder, position: Int) {

         val items=programmeList[position]
        if (viewEdit=="view")
        {
            holder.binding.etNoOfAnimals.isEnabled=false
            holder.binding.etF1Generation.isEnabled=false
            holder.binding.etF2Generation.isEnabled=false
            holder.binding.etNoOfAnimalsDistributedF1.isEnabled=false
            holder.binding.etNoOfAnimalsDistributedF2.isEnabled=false
            holder.binding.etPerformanceOfTheAnimals.isEnabled=false
            holder.binding.etBalance.isEnabled=false
            holder.binding.btnDelete.hideView()
        }
        // Handle visibility of add/delete buttons
        holder.binding.etNoOfAnimals.setText(items.number_of_animals.toString())
        holder.binding.etF1Generation.setText(items.f1_generation_produced)
        holder.binding.etF2Generation.setText(items.f2_generation_produced)
       holder.binding.etNoOfAnimalsDistributedF1.setText(items.no_of_animals_f1.toString())
       holder.binding.etNoOfAnimalsDistributedF2.setText(items.no_of_animals_f2.toString())
        holder.binding.etPerformanceOfTheAnimals.setText(items.performance_animals_doorstep)
        holder.binding.etBalance.setText(items.balance)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {

                programmeList.removeAt(position)
                notifyItemRemoved(position)


        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ImportExoticAchivementViewHolder(val binding: ItemImportOfExoticGoatAchievementBinding) :
        RecyclerView.ViewHolder(binding.root)
}
