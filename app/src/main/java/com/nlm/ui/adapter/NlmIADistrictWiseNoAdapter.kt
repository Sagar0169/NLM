package com.nlm.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
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
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemNLMDistrictWiseListEdit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemAvilabilityOfEquipmentBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.District
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyInvolvedDistrictWise
import com.nlm.utilities.Utility
import com.nlm.utilities.showView

class NlmIADistrictWiseNoAdapter(
    private val context: Context,
    private val programmeList: MutableList<ImplementingAgencyInvolvedDistrictWise>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemNLMDistrictWiseListEdit,
    private val callBackDeleteAtId: CallBackDeleteAtId,

) : RecyclerView.Adapter<NlmIADistrictWiseNoAdapter.NlmIADistrictWiseNo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NlmIADistrictWiseNo {

                val binding = ItemDistrictWiseNoNlsiaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    NlmIADistrictWiseNo(binding)

        }

    override fun onBindViewHolder(holder: NlmIADistrictWiseNo, position: Int) {

        val currentItem = programmeList[position]
        holder.binding.etState.isEnabled=false
        holder.binding.etLocationOfAi.isEnabled=false
        holder.binding.etAiPerformed.isEnabled=false
        if (viewEdit=="view")
        {
            holder.binding.btnDelete.visibility= View.GONE
            holder.binding.tvSubmit.visibility= View.GONE
        }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.etState.text=currentItem.name_of_district.toString()
        holder.binding.etLocationOfAi.setText(currentItem.location_of_ai_centre)
        holder.binding.etAiPerformed.setText(currentItem.ai_performed)

        holder.binding.btnDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        callBackDeleteAtId.onClickItem(currentItem.id,position,1)
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                ImplementingAgencyInvolvedDistrictWise(
                    name_of_district = currentItem.name_of_district,
                    location_of_ai_centre = currentItem.location_of_ai_centre,
                    ai_performed = currentItem.ai_performed,
                    year = currentItem.year,
                    id = currentItem.id,
                    implementing_agency_id = currentItem.implementing_agency_id
                ),position)
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }
    }
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
    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
    inner class NlmIADistrictWiseNo(val binding: ItemDistrictWiseNoNlsiaBinding) :
        RecyclerView.ViewHolder(binding.root)
}
