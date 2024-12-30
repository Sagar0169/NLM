package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackItemGoatSemen
import com.nlm.callBack.CallBackItemManPower

import com.nlm.databinding.ItemStateSemenInfrastructureBinding
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.model.StateSemenInfraGoat
import com.nlm.ui.adapter.RspManPowerAdapter.RspManPowerViewHolder
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class StateSemenInfrastructureAdapter(
    private val programmeList: MutableList<StateSemenInfraGoat>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemGoatSemen
) : RecyclerView.Adapter<StateSemenInfrastructureAdapter.StateSemenInfrastructureViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StateSemenInfrastructureViewHolder {

        val binding = ItemStateSemenInfrastructureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StateSemenInfrastructureViewHolder(binding)

    }

    override fun onBindViewHolder(holder: StateSemenInfrastructureViewHolder, position: Int) {
        val currentItem = programmeList[position]
        holder.binding.etListOfEquipment.isEnabled = false
        holder.binding.etYearOfProcurement.isEnabled = false
        if (viewEdit == "view") {

            holder.binding.btnDelete.hideView()
        }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.etListOfEquipment.setText(currentItem.infrastructure_list_of_equipment)
        holder.binding.etYearOfProcurement.setText(currentItem.infrastructure_year_of_procurement)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                StateSemenInfraGoat(
                    currentItem.infrastructure_list_of_equipment,
                    currentItem.infrastructure_year_of_procurement,
                    currentItem.id,
                    currentItem.infra_goat_id
                ),position,2)
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

    inner class StateSemenInfrastructureViewHolder(val binding: ItemStateSemenInfrastructureBinding) :
        RecyclerView.ViewHolder(binding.root)
}
