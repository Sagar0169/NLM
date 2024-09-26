package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ItemNlmComponentBBinding
import com.nlm.model.NLM_CompB

class NlmComponentBadapter(private val Context: Context, private val implementingAgencyList: List<NLM_CompB>) :
    RecyclerView.Adapter<NlmComponentBadapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemNlmComponentBBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemNlmComponentBBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_nlm_component_b,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]


    holder.mBinding.tvName.text = item.Name_Of_The_DCS
    holder.mBinding.tvStateName.text = item.State_Name
    holder.mBinding.tvDistrict.text = item.District
    holder.mBinding.tvTehsil2.text = item.Tehsil
    holder.mBinding.tvCreated.text=item.Created_At












    holder.mBinding.ivView.setOnClickListener {
        Toast.makeText(Context,"form to be added ", Toast.LENGTH_SHORT).show()
    }
    holder.mBinding.ivEdit.setOnClickListener {
        Toast.makeText(Context,"form to be added ",Toast.LENGTH_SHORT).show()
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
