package com.nlm.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackSemenDose
import com.nlm.databinding.ItemRspSemendoseBinding
import com.nlm.model.Result
import com.nlm.model.RspAddAverage
import com.nlm.model.RspAddEquipment
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class SemenDoseAdapter(
    private val context: Context,
    private val programmeList: MutableList<RspAddAverage>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackSemenDose
) : RecyclerView.Adapter<SemenDoseAdapter.SemenDoseViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SemenDoseViewHolder {

        val binding = ItemRspSemendoseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SemenDoseViewHolder(binding)

    }

    override fun onBindViewHolder(holder: SemenDoseViewHolder, position: Int) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            holder.binding.etNameOfBreed.isEnabled = false
            holder.binding.etTwentyTwo.isEnabled = false
            holder.binding.etTwentyThree.isEnabled = false
            holder.binding.etTwentyFour.isEnabled = false
            holder.binding.btnDelete.hideView()
        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNameOfBreed.setText(currentItem.name_of_breed)
        holder.binding.etTwentyTwo.setText(currentItem.twentyOne_twentyTwo)
        holder.binding.etTwentyThree.setText(currentItem.twentyTwo_twentyThree)
        holder.binding.etTwentyFour.setText(currentItem.twentyThree_twentyFour)
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            programmeList.removeAt(position)
            notifyItemRemoved(position)
        }
        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                RspAddAverage(
                    currentItem.id,
                    currentItem.name_of_breed,
                    currentItem.twentyOne_twentyTwo,
                    currentItem.twentyTwo_twentyThree,
                    currentItem.twentyThree_twentyFour,
                    currentItem.rsp_laboratory_semen_id
                ), position, 2
            )
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

    inner class SemenDoseViewHolder(val binding: ItemRspSemendoseBinding) :
        RecyclerView.ViewHolder(binding.root)
}
