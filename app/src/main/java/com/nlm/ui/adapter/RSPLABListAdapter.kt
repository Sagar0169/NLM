package com.nlm.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.databinding.ItemRspLabListBinding
import com.nlm.databinding.ItemStateSemenBinding
import com.nlm.model.NLMIA_data
import com.nlm.model.Rsp_lab_data
import com.nlm.model.State_Semen_Bank

import com.nlm.ui.activity.national_livestock_mission.RspLabSemenForms

import com.nlm.utilities.hideView


class RSPLABListAdapter(private val implementingAgencyList: List<Rsp_lab_data>, val isFrom:Int, val Role_name:String) :
    RecyclerView.Adapter<RSPLABListAdapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemRspLabListBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemRspLabListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rsp_lab_list,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]
        if (Role_name=="Super Admin")
        {
            holder.mBinding.ivView.hideView()
            holder.mBinding.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }



    holder.mBinding.etState.text = item.state
    holder.mBinding.etDistricts.text = item.District
    holder.mBinding.etPhone.text = item.Phone
    holder.mBinding.etEstablishment.text = item.Establishment_Year
    holder.mBinding.etCreated.text = item.created
    holder.mBinding.etStatus.text = item.NLM_Status








    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, RspLabSemenForms::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, RspLabSemenForms::class.java)
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
