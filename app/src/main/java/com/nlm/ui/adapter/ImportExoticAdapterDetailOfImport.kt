package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackItemImportExoticAchivementEdit
import com.nlm.callBack.CallBackItemImportExoticDetailtEdit
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class ImportExoticAdapterDetailOfImport(
    private val programmeList: MutableList<ImportOfExoticGoatDetailImport>,
    private var viewEdit: String?,
    private val callBackEdit: CallBackItemImportExoticDetailtEdit
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
        holder.binding.etSpeciesBreed.isEnabled=false
        holder.binding.etYear.isEnabled=false
        holder.binding.etPlaceOfProcurement.isEnabled=false
        holder.binding.etPlaceOfInduction.isEnabled=false
        holder.binding.etProcurementCost.isEnabled=false
        holder.binding.etUnit.isEnabled=false
        holder.binding.btnEdit.showView()
         if (viewEdit=="view")
         {    holder.binding.btnEdit.hideView()
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
        holder.binding.etProcurementCost.setText(items.procurement_cost?.toString() ?: "")
        holder.binding.etUnit.setText(items.unit)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {

                programmeList.removeAt(position)
                notifyItemRemoved(position)


        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItemDetail(
                ImportOfExoticGoatDetailImport(
                    place_of_procurement = items.place_of_procurement,
                    place_of_induction = items.place_of_induction,
                    procurement_cost = items.procurement_cost,
                    species_breed = items.species_breed,
                    unit = items.unit,
                    year = items.year,
                    id =items.id ,
                    import_of_exotic_goat_id = items.import_of_exotic_goat_id
                ),position,2)
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ImportExoticAdapterDetailOfImportViewHolder(val binding: ItemImportExoticGermplasmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
