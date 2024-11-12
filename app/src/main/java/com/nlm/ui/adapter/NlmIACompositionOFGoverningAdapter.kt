package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit

import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAGoverningBodyBoardOfDirectorsFragment
import com.nlm.utilities.showView

class NlmIACompositionOFGoverningAdapter(
    private val context: Context,
    private val programmeList: MutableList<ImplementingAgencyAdvisoryCommittee>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemTypeIACompositionListEdit
) : RecyclerView.Adapter<NlmIACompositionOFGoverningAdapter.NlmIACompositionOFGoverning>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIACompositionOFGoverning {

                val binding = ItemCompositionOfGoverningNlmIaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIACompositionOFGoverning(binding)

        }

    override fun onBindViewHolder(holder: NlmIACompositionOFGoverning, position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit=="view")
        {
            holder.binding.nameOfOfficial.isEnabled=false
            holder.binding.nameOfDesignation.isEnabled=false
            holder.binding.nameOfOrganization.isEnabled=false
            holder.binding.btnDelete.visibility=View.GONE
        }else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }

        holder.binding.nameOfOfficial.setText(currentItem.name_of_the_official)
        holder.binding.nameOfDesignation.setText(currentItem.designation)
        holder.binding.nameOfOrganization.setText(currentItem.organization)
        // Delete button logic
        holder.binding.btnDelete.setOnClickListener {
                  programmeList.removeAt(position)
                notifyItemRemoved(position)
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(IdAndDetails(
                name_of_the_official = currentItem.name_of_the_official,
                designation = currentItem.designation,
                organization = currentItem.organization,
                id = currentItem.id,
                implementing_agency_id = currentItem.implementing_agency_id
            ),position,1)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
    }

    override fun getItemCount(): Int = programmeList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class NlmIACompositionOFGoverning(val binding: ItemCompositionOfGoverningNlmIaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
