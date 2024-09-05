package com.nlm.ui.adapter.rgm

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemBreedMultiplicationBinding
import com.nlm.databinding.ItemMilkUnionVisitBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.ui.activity.AddBreedMultiplication
import com.nlm.ui.activity.AddMilkUnionVisit
import com.nlm.ui.activity.NodalOfficerDetailActivity

class BreedMultiplicationAdapter(
    private val implementingAgencyList: List<MilkUnionVisit>) :

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
        holder.mBinding.tvCreated.text = item.createdBy
        holder.mBinding.tvName.text = item.nameOfMilkUnion




        holder.itemView.setOnClickListener {
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
