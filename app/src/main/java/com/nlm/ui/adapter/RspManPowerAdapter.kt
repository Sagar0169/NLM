package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackItemManPower
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.databinding.ItemRspManpowerBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.model.StateSemenBankOtherManpower
import com.nlm.model.StateSemenManPower
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class RspManPowerAdapter(
    private val programmeList: MutableList<StateSemenBankOtherAddManpower>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackItemManPower
) : RecyclerView.Adapter<RspManPowerAdapter.RspManPowerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RspManPowerViewHolder {

        val binding = ItemRspManpowerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RspManPowerViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RspManPowerViewHolder, position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view") {
            holder.binding.etDesignation.isEnabled = false
            holder.binding.etQualification.isEnabled = false
            holder.binding.etExperience.isEnabled = false
            holder.binding.etTrainingStatus.isEnabled = false
            holder.binding.btnDelete.hideView()
        }
        else if (viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.etDesignation.setText(currentItem.designation)
        holder.binding.etQualification.setText(currentItem.qualification)
        holder.binding.etExperience.setText(currentItem.experience)
        holder.binding.etTrainingStatus.setText(currentItem.training_status)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                StateSemenBankOtherAddManpower(
                    currentItem.designation,
                    currentItem.qualification,
                    currentItem.experience,
                    currentItem.training_status,
                    null,
                    currentItem.id,
                    currentItem.state_semen_bank_id

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

    inner class RspManPowerViewHolder(val binding: ItemRspManpowerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
