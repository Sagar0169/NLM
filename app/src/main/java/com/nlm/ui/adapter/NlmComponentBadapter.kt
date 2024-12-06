package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlmComponentBBinding
import com.nlm.model.NDDComponentBListData
import com.nlm.model.NLM_CompB
import com.nlm.ui.activity.national_dairy_development.NlmComponentBDairyDevelopment
import com.nlm.ui.activity.national_livestock_mission.RspLabSemenForms
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmComponentBadapter(
    private val context: Context,
    private val implementingAgencyList: ArrayList<NDDComponentBListData>,
    private val callBackDeleteAtId: CallBackDeleteAtId
) :
    RecyclerView.Adapter<NlmComponentBadapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding: ItemNlmComponentBBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemNlmComponentBBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_nlm_component_b,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, @SuppressLint("RecyclerView") position: Int) {
        val item = implementingAgencyList[position]

        holder.mBinding.tvName.text = item.name_of_dcs_mpp
        holder.mBinding.tvState.text = item.state_name
        holder.mBinding.tvDistrict.text = item.district_name
        holder.mBinding.tvTehsil.text = item.name_of_tehsil
        holder.mBinding.tvVillage.text = item.name_of_revenue_village
        holder.mBinding.tvNlmStatus.text = item.is_draft_text
        holder.mBinding.tvCreatedAt.text = convertDate(item.created_on)

//        if(item.is_view){
//            holder.mBinding.ivView.showView()
//        }
//        else{
//            holder.mBinding.ivView.hideView()
//        }
//        if(item.is_delete){
//            holder.mBinding.ivDelete.showView()
//        }
//        else{
//            holder.mBinding.ivDelete.hideView()
//        }
//        if(item.is_edit){
//            holder.mBinding.ivEdit.showView()
//        }
//        else{
//            holder.mBinding.ivEdit.hideView()
//        }

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
            val intent = Intent(holder.itemView.context, NlmComponentBDairyDevelopment::class.java)
            intent.putExtra("View/Edit", "view")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, NlmComponentBDairyDevelopment::class.java)
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
