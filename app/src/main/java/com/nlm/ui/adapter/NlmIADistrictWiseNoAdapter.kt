package com.nlm.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.District
import com.nlm.model.ImplementingAgencyInvolvedDistrictWise

class NlmIADistrictWiseNoAdapter(
    private val programmeList: MutableList<ImplementingAgencyInvolvedDistrictWise>,
    private val viewEdit: String?
) : RecyclerView.Adapter<NlmIADistrictWiseNoAdapter.NlmIADistrictWiseNo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIADistrictWiseNo {

                val binding = ItemDistrictWiseNoNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIADistrictWiseNo(binding)

        }

    override fun onBindViewHolder(holder: NlmIADistrictWiseNo, position: Int) {

        val currentItem = programmeList[position]
        if (viewEdit=="view")
        {
            holder.binding.etState.isEnabled=false
            holder.binding.etLocationOfAi.isEnabled=false
            holder.binding.etAiPerformed.isEnabled=false
            holder.binding.btnDelete.visibility= View.GONE
            holder.binding.tvSubmit.visibility= View.GONE
        }
        holder.binding.etState.text=currentItem.name_of_district.toString()
        holder.binding.etLocationOfAi.setText(currentItem.location_of_ai_centre)
        holder.binding.etAiPerformed.setText(currentItem.ai_performed)

//        holder.binding.etState.setOnClickListener { showBottomSheetDialog("district",holder.binding.etState) }
        holder.binding.btnDelete.setOnClickListener {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
    inner class NlmIADistrictWiseNo(val binding: ItemDistrictWiseNoNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
