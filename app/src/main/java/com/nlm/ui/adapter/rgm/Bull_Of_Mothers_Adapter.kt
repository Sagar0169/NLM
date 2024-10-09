package com.nlm.ui.adapter.rgm

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemBullOfMothersBinding
import com.nlm.model.Bull_Mothers
import com.nlm.ui.activity.rashtriya_gokul_mission.BullMotherFarms
import com.nlm.ui.activity.rashtriya_gokul_mission.SemenStation
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView

class Bull_Of_Mothers_Adapter(private val implementingAgencyList: List<Bull_Mothers>, private val isFrom:Int,val Role_name:String) :
    RecyclerView.Adapter<Bull_Of_Mothers_Adapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemBullOfMothersBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemBullOfMothersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_bull_of_mothers,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]
        if (Role_name==AppConstants.SUPER_ADMIN||Role_name==AppConstants.Nodal_Officer||Role_name==AppConstants.NLM||Role_name==AppConstants.ADMIN)
        {

            holder.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }


    holder.mBinding.etFarmName.text = item.Name_of_The_Farm
    holder.mBinding.etStateName.text = item.State_Name
    holder.mBinding.etDistrictName.text = item.District_Name
    holder.mBinding.etStatus.text = item.IA_Status
    holder.mBinding.etStatusNlm.text = item.NLM_Status
    holder.mBinding.tvCreated.text = item.Created_On






    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, BullMotherFarms::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, BullMotherFarms::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 3)
        holder.itemView.context.startActivity(intent)
    }



    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, SemenStation::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, SemenStation::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 3)
        holder.itemView.context.startActivity(intent)
    }






//
//        holder.tvStateName.setOnClickListener {
//            callBackItemDistrict.onClickItemDistrict(stateName)
//        }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }
}
