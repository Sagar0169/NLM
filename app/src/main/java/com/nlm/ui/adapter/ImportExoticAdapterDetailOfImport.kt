package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.ImportOfExoticGoatDetailImport

class ImportExoticAdapterDetailOfImport(
    private val programmeList: MutableList<ImportOfExoticGoatDetailImport>,
    private var viewEdit: String?
) : RecyclerView.Adapter<ImportExoticAdapterDetailOfImport.ImportExoticAdapterDetailOfImportViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportExoticAdapterDetailOfImportViewHolder {

                val binding = ItemImportExoticGermplasmBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    ImportExoticAdapterDetailOfImportViewHolder(binding)

        }

    override fun onBindViewHolder(holder: ImportExoticAdapterDetailOfImportViewHolder, position: Int) {

         val items=programmeList[position]
        // Handle visibility of add/delete buttons
         if (viewEdit=="view")
         {
             holder.binding.etSpeciesBreed.isEnabled=false
             holder.binding.etYear.isEnabled=false
             holder.binding.etPlaceOfProcurement.isEnabled=false
             holder.binding.etPlaceOfInduction.isEnabled=false
             holder.binding.etProcurementCost.isEnabled=false
             holder.binding.etUnit.isEnabled=false
             holder.binding.btnDelete.visibility= View.GONE
         }

        holder.binding.etSpeciesBreed.setText(items.species_breed)
        holder.binding.etYear.setText(items.year)
        holder.binding.etPlaceOfProcurement.setText(items.place_of_procurement)
        holder.binding.etPlaceOfInduction.setText(items.place_of_induction)
        holder.binding.etProcurementCost.setText(items.procurement_cost.toString())
        holder.binding.etUnit.setText(items.unit)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {

                programmeList.removeAt(position)
                notifyItemRemoved(position)


        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ImportExoticAdapterDetailOfImportViewHolder(val binding: ItemImportExoticGermplasmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
