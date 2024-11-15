package com.nlm.ui.adapter.rgm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackSemenDoseAvg

import com.nlm.databinding.ItemQualityBuckBinding
import com.nlm.model.Result
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspAddEquipment
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class AverageSemenDoseAdapter(
    private val context: Context,
    private val programmeList: MutableList<RspAddBucksList>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackSemenDoseAvg
) : RecyclerView.Adapter<AverageSemenDoseAdapter.AverageSemenDoseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AverageSemenDoseViewHolder {

        val binding = ItemQualityBuckBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AverageSemenDoseViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AverageSemenDoseViewHolder, position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {
            holder.binding.etBreedMaintained.isEnabled = false
            holder.binding.etAnimal.isEnabled = false
            holder.binding.etAvgAge.isEnabled = false
            holder.binding.btnDelete.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etBreedMaintained.setText(currentItem.breed_maintained)
        currentItem.no_of_animals.toString().let { holder.binding.etAnimal.setText(it) }
        holder.binding.etAvgAge.setText(currentItem.average_age)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                RspAddBucksList(
                    currentItem.breed_maintained,
                    currentItem.no_of_animals,
                    currentItem.average_age,
                    currentItem.id,
                    currentItem.rsp_laboratory_semen_id
                ), position, 2
            )
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }

    }

    override fun getItemCount(): Int = programmeList.size

    inner class AverageSemenDoseViewHolder(val binding: ItemQualityBuckBinding) :
        RecyclerView.ViewHolder(binding.root)
}
