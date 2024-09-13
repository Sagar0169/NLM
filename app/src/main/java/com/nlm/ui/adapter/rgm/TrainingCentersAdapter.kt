package com.nlm.ui.adapter.rgm

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemBreedMultiplicationBinding
import com.nlm.databinding.ItemMilkUnionVisitBinding
import com.nlm.databinding.ItemTrainingCentersBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.TrainingCenters
import com.nlm.ui.activity.AddBreedMultiplication
import com.nlm.ui.activity.AddMilkUnionVisit
import com.nlm.ui.activity.AddTrainingCenters
import com.nlm.ui.activity.NodalOfficerDetailActivity

class TrainingCentersAdapter(
    private val implementingAgencyList: List<TrainingCenters>) :

    RecyclerView.Adapter<TrainingCentersAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemTrainingCentersBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemTrainingCentersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_training_centers,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.tvVillage.text = item.village
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvSubmit.text = item.submit
        holder.mBinding.tvDistrict.text = item.district




        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddTrainingCenters::class.java)
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