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

class ImportOfExoticGoatVerifiedNlmAdapter(
    private val programmeList: MutableList<ImportOfExoticGoatVerifiedNlm>,
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
        holder.binding.etSpeciesBreed.setText(items.species_breed)
        holder.binding.etYear.setText(items.year)
        holder.binding.etF2GenerationDistributed
        holder.binding.etF1GenerationProduced.setText(items.f1_generation_produced)
        items.f2_generation_produced?.let { holder.binding.etF2GenerationProduced.setText(it) }
        items.f2_generation_distributed?.let { holder.binding.etF2GenerationDistributed.setText(it) }

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
