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
import com.nlm.callBack.CallBackItemImportExoticVerifiedByNlm
import com.nlm.callBack.DialogCallback
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
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class ImportOfExoticGoatVerifiedNlmAdapter(
    private val context: Context?,
    private val programmeList: MutableList<ImportOfExoticGoatVerifiedNlm>,
    private val callBackDeleteAtId: CallBackDeleteAtId,
    private var viewEdit: String?,
    private val callBackEdit: CallBackItemImportExoticVerifiedByNlm
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
        holder.binding.btnEdit.showView()
        holder.binding.etSpeciesBreed.isEnabled=false
        holder.binding.etYear.isEnabled=false
        holder.binding.etF2GenerationDistributed.isEnabled=false
        holder.binding.etF1GenerationProduced.isEnabled=false
        holder.binding.etF1GenerationDistributed.isEnabled=false
        holder.binding.etF2GenerationProduced.isEnabled=false
         if (viewEdit=="view")
         {
             holder.binding.btnDelete.hideView()
             holder.binding.btnEdit.hideView()

         }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
         }
        holder.binding.etSpeciesBreed.setText(items.species_breed)
        holder.binding.etYear.setText(items.year)
        holder.binding.etF1GenerationProduced.setText(items.f1_generation_produced)
        holder.binding.etF1GenerationDistributed.setText(items.f2_generation_distributed)
        items.f2_generation_produced?.let { holder.binding.etF2GenerationProduced.setText(it) }
        items.number_of_animals?.let { holder.binding.etF2GenerationDistributed.setText(it.toString()) }?:holder.binding.etF2GenerationDistributed.setText("")

        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,4)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }

        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                ImportOfExoticGoatVerifiedNlm(
                    number_of_animals = items.number_of_animals,
                    f1_generation_produced = items.f1_generation_produced,
                    f2_generation_produced = items.f2_generation_produced,
                    f2_generation_distributed = items.f2_generation_distributed,
                    id =items.id ,
                    species_breed=items.species_breed,
                    import_of_exotic_goat_id = items.import_of_exotic_goat_id,
                    year = items.year
                ),position,2)
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class ImportOfExoticGoatVerifiedNlmViewHolder(val binding: ItemImportExoticVerifiedNlmBinding) :
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
