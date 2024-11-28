package com.nlm.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemFormat6Edit
import com.nlm.callBack.CallBackItemImportExoticAchivementEdit
import com.nlm.callBack.DialogCallback

import com.nlm.databinding.ItemYearWiseFinancialProgressBinding
import com.nlm.model.AssistanceForQfspFinancialProgres
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class Format6YearWiseFinancialProgressAdapter(
    private val context:Context,
    private val programmeList: MutableList<AssistanceForQfspFinancialProgres>,
    private var viewEdit: String?,
    private val callBackEdit: CallBackItemFormat6Edit,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<Format6YearWiseFinancialProgressAdapter.Format6YearWiseFinancialProgressViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Format6YearWiseFinancialProgressViewHolder {

                val binding = ItemYearWiseFinancialProgressBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        return    Format6YearWiseFinancialProgressViewHolder(binding)

        }

    override fun onBindViewHolder(holder: Format6YearWiseFinancialProgressViewHolder, position: Int) {

         val items=programmeList[position]
        // Handle visibility of add/delete buttons
        holder.binding.tvDistrictName.isEnabled=false
        holder.binding.etAssistancedByDAHD.isEnabled=false
        holder.binding.etAmountUtilizedByState.isEnabled=false
        holder.binding.etAreaCovered.isEnabled=false
        holder.binding.etFrarmersImpacted.isEnabled=false
         if (viewEdit=="view")
         {
             holder.binding.btnDelete.hideView()

         }
        else if(viewEdit=="edit"){
            holder.binding.btnEdit.showView()
         }
        if (items.district==null)
        {
            holder.binding.tvDistrictName.text = items.name_of_district
        }
        else{
            holder.binding.tvDistrictName.text = items.district.name
        }
        holder.binding.etAssistancedByDAHD.setText(items.assistance_provided_first)
        items.amount_utilized_state_first?.let {
            holder.binding.etAmountUtilizedByState.setText(it.toString())
        } ?: run {
            holder.binding.etAmountUtilizedByState.setText("")
        }
        items.area_covered_first?.let {
            holder.binding.etAreaCovered.setText(it.toString())
        } ?: run {
            holder.binding.etAreaCovered.setText("")
        }
        holder.binding.etFrarmersImpacted.setText(items.farmers_impacted_first)


        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,2)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                AssistanceForQfspFinancialProgres(
                    assistance_for_qfsp_id=items.assistance_for_qfsp_id,
                    financial_year_first=items.financial_year_first,
                    name_of_district = items.name_of_district,
                    assistance_provided_first=items.assistance_provided_first,
                    amount_utilized_state_first=items.amount_utilized_state_first,
                    area_covered_first=items.area_covered_first,
                    farmers_impacted_first=items.farmers_impacted_first,
                    id=items.id,
                ),position,2)
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility

    inner class Format6YearWiseFinancialProgressViewHolder(val binding: ItemYearWiseFinancialProgressBinding) :
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
