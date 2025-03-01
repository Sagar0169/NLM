package com.nlm.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemRgmImplementingAgencyBinding
import com.nlm.model.RGM_IA
import com.nlm.ui.activity.rashtriya_gokul_mission.RGMStateImplementingAgency
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView

class RGM_IA_Adapter(private val implementingAgencyList: List<RGM_IA>, private val isFrom:Int,val Role_name:String) :
    RecyclerView.Adapter<RGM_IA_Adapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemRgmImplementingAgencyBinding) : RecyclerView.ViewHolder(mBinding.root) {
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }
    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemRgmImplementingAgencyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rgm_implementing_agency,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]
        if (Role_name==AppConstants.SUPER_ADMIN||Role_name==AppConstants.Nodal_Officer||Role_name==AppConstants.ADMIN||Role_name==AppConstants.NLM)
        {

            holder.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }
    holder.mBinding.etAgencyName.text = item.Agency_Name
    holder.mBinding.etState.text = item.State
    holder.mBinding.etDistrict.text = item.District
    holder.mBinding.etStatus.text=item.Status_IA
    holder.mBinding.etStatusNlm.text=item.Status_NLM
    holder.mBinding.etCreated.text=item.Created
    holder.mBinding.etRemarksNLM.text=item.Remarks_of_nlm
    holder.mBinding.etVist.text=item.Visit
    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, RGMStateImplementingAgency::class.java)
        intent.putExtra("view", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, RGMStateImplementingAgency::class.java)
        intent.putExtra("View/Edit", "edit")
        intent.putExtra("itemId", item.id)
        holder.itemView.context.startActivity(intent)
    }
}
//
//        holder.tvStateName.setOnClickListener {
//            callBackItemDistrict.onClickItemDistrict(stateName)
//        }


    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }
}
