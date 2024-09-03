package com.nlm.ui.adapter.ndd

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemMilkUnionVisitBinding
import com.nlm.databinding.ItemStateCenterVisitBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.StateCenterVisit
import com.nlm.ui.activity.AddMilkUnionVisit
import com.nlm.ui.activity.NodalOfficerDetailActivity

class StateCenterLabVisitAdapter(
    private val implementingAgencyList: List<StateCenterVisit>) :

    RecyclerView.Adapter<StateCenterLabVisitAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemStateCenterVisitBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemStateCenterVisitBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_state_center_visit,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.tvLocation.text = item.location



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
