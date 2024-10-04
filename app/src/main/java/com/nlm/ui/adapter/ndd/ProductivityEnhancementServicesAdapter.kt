package com.nlm.ui.adapter.ndd

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemProductivityEnhancementServicesBinding
import com.nlm.model.ProductivityEnhancementServices
import com.nlm.ui.activity.national_dairy_development.AddNLMComponentA
import com.nlm.ui.activity.national_dairy_development.AddProductivityEnhancementServices

class ProductivityEnhancementServicesAdapter(
    private val implementingAgencyList: List<ProductivityEnhancementServices>) :

    RecyclerView.Adapter<ProductivityEnhancementServicesAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemProductivityEnhancementServicesBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemProductivityEnhancementServicesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_productivity_enhancement_services,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvDcs.text = item.dcs
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.tvTehsil.text = item.tehsil
        holder.mBinding.tvRevenue.text = item.revenue




        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddProductivityEnhancementServices::class.java)
            intent.putExtra("milkUnion", item)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddProductivityEnhancementServices::class.java)
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
