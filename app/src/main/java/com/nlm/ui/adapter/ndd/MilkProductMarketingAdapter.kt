package com.nlm.ui.adapter.ndd

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemMilkProcessingBinding
import com.nlm.databinding.ItemMilkProductMarketingBinding
import com.nlm.model.MilkProcessing
import com.nlm.model.MilkProductMarketing
import com.nlm.ui.activity.AddMilkUnionVisit

class MilkProductMarketingAdapter(
    private val implementingAgencyList: List<MilkProductMarketing>) :

    RecyclerView.Adapter<MilkProductMarketingAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemMilkProductMarketingBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemMilkProductMarketingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_milk_product_marketing,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvNameofMilkUnion.text = item.nameOfMilkUnion
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.nameOfRetail.text = item.nameOfRetailShop
        holder.mBinding.tvDateOfInspection.text = item.dateOfInspection



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddMilkUnionVisit::class.java)
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
