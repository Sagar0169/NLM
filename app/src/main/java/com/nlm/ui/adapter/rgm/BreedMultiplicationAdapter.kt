package com.nlm.ui.adapter.rgm

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemBreedMultiplicationBinding
import com.nlm.model.BreedMultiplication
import com.nlm.model.MilkUnionVisit
import com.nlm.ui.activity.national_dairy_development.AddStateCenterLabtVisit
import com.nlm.ui.activity.rashtriya_gokul_mission.AddBreedMultiplication
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView

class BreedMultiplicationAdapter(
    private val implementingAgencyList: List<BreedMultiplication>, val Role_name:String) :

    RecyclerView.Adapter<BreedMultiplicationAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemBreedMultiplicationBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemBreedMultiplicationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_breed_multiplication,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        if (Role_name== AppConstants.SUPER_ADMIN||Role_name== AppConstants.Nodal_Officer||Role_name== AppConstants.ADMIN)
        {

            holder.mBinding.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }
        holder.mBinding.etCreated.text = item.created
        holder.mBinding.tvName.text = item.nameOfBeneficiary
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.etStatus.text = item.iaStatus
        holder.mBinding.etStatusNlm.text = item.nlmStatus






        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddBreedMultiplication::class.java)
            intent.putExtra("milkUnion", item)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddBreedMultiplication::class.java)
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
