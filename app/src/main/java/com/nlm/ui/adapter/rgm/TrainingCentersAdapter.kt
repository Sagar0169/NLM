package com.nlm.ui.adapter.rgm

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemTrainingCentersBinding
import com.nlm.model.TrainingCenters
import com.nlm.ui.activity.rashtriya_gokul_mission.AddTrainingCenters
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView

class TrainingCentersAdapter(
    private val implementingAgencyList: List<TrainingCenters>,val Role_name:String) :

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
        holder.mBinding.tvSettingYear.text = item.submit
        holder.mBinding.tvDistrict.text = item.district


        if (Role_name== AppConstants.SUPER_ADMIN||Role_name== AppConstants.Nodal_Officer||Role_name== AppConstants.NLM||Role_name== AppConstants.ADMIN)
        {

            holder.mBinding.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }

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
