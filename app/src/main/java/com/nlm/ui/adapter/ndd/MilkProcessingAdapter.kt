package com.nlm.ui.adapter.ndd

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemMilkProcessingBinding
import com.nlm.model.MilkProcessing
import com.nlm.model.NDDDairyPlantListData
import com.nlm.model.NDDMilkProcessingListData
import com.nlm.ui.activity.national_dairy_development.AddDCSCenterVisit
import com.nlm.ui.activity.national_dairy_development.AddMilkProcessing
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class MilkProcessingAdapter(
    private val context: Context,
    private val implementingAgencyList: ArrayList<NDDMilkProcessingListData>,
    private val callBackDeleteAtId: CallBackDeleteAtId
) :

    RecyclerView.Adapter<MilkProcessingAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding: ItemMilkProcessingBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding: ItemMilkProcessingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_milk_processing,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, @SuppressLint("RecyclerView") position: Int) {
        val item = implementingAgencyList[position]

        holder.mBinding.tvNameOfProcessingPlant.text = item.name_processing_plant
        holder.mBinding.tvNameofMilkUnion.text = item.name_milk_union
        holder.mBinding.tvState.text = item.state_name
        holder.mBinding.tvDistrict.text = item.district_name
        holder.mBinding.tvCreatedBy.text = item.created_by_text
        holder.mBinding.tvNlmStatus.text = item.is_draft.toString()
        holder.mBinding.tvCreated.text = convertDate(item.created_at)

        if(item.is_view){
            holder.mBinding.ivView.showView()
        }
        else{
            holder.mBinding.ivView.hideView()
        }
        if(item.is_delete){
            holder.mBinding.ivDelete.showView()
        }
        else{
            holder.mBinding.ivDelete.hideView()
        }
        if(item.is_edit){
            holder.mBinding.ivEdit.showView()
        }
        else{
            holder.mBinding.ivEdit.hideView()
        }

        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        if (item != null) {
                            callBackDeleteAtId.onClickItem(item.id,position,0)
                        }
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }

        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddMilkProcessing::class.java)
            intent.putExtra("View/Edit", "view")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddMilkProcessing::class.java)
            intent.putExtra("View/Edit", "edit")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
    }


    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun onDeleteButtonClick(position: Int) {
        implementingAgencyList.removeAt(position)
        notifyItemRemoved(position)
    }
}
