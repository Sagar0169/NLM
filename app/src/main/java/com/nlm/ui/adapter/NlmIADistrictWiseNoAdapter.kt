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
) : RecyclerView.Adapter<NlmIADistrictWiseNoAdapter.NlmIADistrictWiseNo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIADistrictWiseNo {

                val binding = ItemDistrictWiseNoNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIADistrictWiseNo(binding)

        }

    override fun onBindViewHolder(holder: NlmIADistrictWiseNo, position: Int) {

        val currentItem = programmeList[position]
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



    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
    }
    inner class NlmIADistrictWiseNo(val binding: ItemDistrictWiseNoNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
