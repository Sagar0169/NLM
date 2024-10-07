package com.nlm.ui.adapter.ndd

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemNlmComponentABinding
import com.nlm.model.NLMComponentA
import com.nlm.ui.activity.national_dairy_development.AddMilkUnionVisit
import com.nlm.ui.activity.national_dairy_development.AddNLMComponentA
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView

class NLMComponentAAdapter(
    private val implementingAgencyList: List<NLMComponentA>,val Role_name:String) :

    RecyclerView.Adapter<NLMComponentAAdapter.Viewholder>() {

    // ViewHolder class to hold the view elements
    class Viewholder(val mBinding:ItemNlmComponentABinding) : RecyclerView.ViewHolder(mBinding.root) {

    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Viewholder {
        val mBinding :ItemNlmComponentABinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_nlm_component_a,
            viewGroup,
            false
        )
        return Viewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.mBinding.tvNpdd.text = item.npdd
        holder.mBinding.tvState.text = item.state
        holder.mBinding.tvDistrict.text = item.district
        holder.mBinding.tvCreated.text = item.created
        holder.mBinding.tvSubmit.text = item.submit
        holder.mBinding.tvYear.text = item.year

        if (Role_name== AppConstants.SUPER_ADMIN||Role_name== AppConstants.ADMIN)
        {

            holder.mBinding.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }


        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddNLMComponentA::class.java)
            intent.putExtra("milkUnion", item)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddNLMComponentA::class.java)
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
