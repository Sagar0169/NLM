package com.nlm.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.AddDocsItemBinding
import com.nlm.databinding.ItemArtificialInsemenationFormsBinding
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.OnlyCreated
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.activity.Artificial_Insemination
import com.nlm.ui.activity.NLSIAForm
import com.nlm.ui.activity.NodalOfficerDetailActivity
import com.nlm.ui.activity.RspLabSemen
import com.nlm.ui.activity.StateSemenBank
import com.nlm.ui.adapter.AddDocumentAdapter.MyViewHolder
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class Artificial_Insemination_adapter(private val implementingAgencyList: List<ArtificialInsemenation>,) :
    RecyclerView.Adapter<Artificial_Insemination_adapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemArtificialInsemenationFormsBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemArtificialInsemenationFormsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_artificial_insemenation_forms,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]



    holder.mBinding.tvState.text = item.state
//        holder.mBinding.districtName.text = item.district
    holder.mBinding.name.text = item.liquid_nitrogen
//        holder.mBinding.phoneNumber.text = item.phone
    holder.mBinding.tvDate.text = item.created
    holder.mBinding.districtName.text=item.district
    holder.mBinding.phoneNumber.text=item.frozen_semen_straws

    holder.mBinding.tvYearofe.text=item.cryocans



    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, Artificial_Insemination::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, Artificial_Insemination::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 3)
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
