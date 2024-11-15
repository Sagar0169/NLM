package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.databinding.ItemImportExoticVerifiedNlmBinding
import com.nlm.databinding.ItemImportOfExoticGoatAchievementBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.utilities.hideView

class ImportOfExoticGoatVerifiedNlmAdapter(
    private val programmeList: MutableList<ImportOfExoticGoatVerifiedNlm>,
    private var viewEdit: String?
) : RecyclerView.Adapter<ImportOfExoticGoatVerifiedNlmAdapter.ImportOfExoticGoatVerifiedNlmViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportOfExoticGoatVerifiedNlmViewHolder {

                val binding = ItemImportExoticVerifiedNlmBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ImportOfExoticGoatVerifiedNlmViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ImportOfExoticGoatVerifiedNlmViewHolder, position: Int) {

         val items=programmeList[position]
        // Handle visibility of add/delete buttons
         if (viewEdit=="view")
         {
             holder.binding.etSpeciesBreed.isEnabled=false
             holder.binding.etYear.isEnabled=false
             holder.binding.etF2GenerationDistributed.isEnabled=false
             holder.binding.etF1GenerationProduced.isEnabled=false
             holder.binding.etF1GenerationDistributed.isEnabled=false
             holder.binding.etF2GenerationProduced.isEnabled=false
             holder.binding.btnDelete.hideView()

         }
        holder.binding.etSpeciesBreed.setText(items.species_breed)
        holder.binding.etYear.setText(items.year)

        holder.binding.etF1GenerationProduced.setText(items.f1_generation_produced)
        holder.binding.etF1GenerationDistributed.setText(items.f2_generation_distributed)
        items.f2_generation_produced?.let { holder.binding.etF2GenerationProduced.setText(it) }
        items.number_of_animals?.let { holder.binding.etF2GenerationDistributed.setText(it.toString()) }?:holder.binding.etF2GenerationDistributed.setText("")

        // Delete row
        holder.binding.btnDelete.setOnClickListener {

                programmeList.removeAt(position)
                notifyItemRemoved(position)


        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ImportOfExoticGoatVerifiedNlmViewHolder(val binding: ItemImportExoticVerifiedNlmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
