package com.nlm.ui.adapter.ndd

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemDcsCenterVisitBinding
import com.nlm.model.DcsCenterVisit
import com.nlm.ui.activity.national_dairy_development.AddDCSCenterVisit
import com.nlm.ui.activity.national_dairy_development.AddDairyPlantVisit

class DCSCenterVisitAdapter(
    private val implementingAgencyList: List<DcsCenterVisit>) :

    RecyclerView.Adapter<DCSCenterVisitAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemDcsCenterVisitBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemDcsCenterVisitBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_dcs_center_visit,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvNameOfDCS.text = item.nameOfDCS
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.tvfssai.text = item.fssai
        holder.mBinding.tvDOV.text = item.dateOfValidity

holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddDCSCenterVisit::class.java)
            intent.putExtra("milkUnion", item)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddDCSCenterVisit::class.java)
            intent.putExtra("milkUnion", item)
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
