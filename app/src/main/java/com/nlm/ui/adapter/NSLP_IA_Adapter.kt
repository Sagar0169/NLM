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
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.model.OnlyCreated
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.activity.NLSIAForm
import com.nlm.ui.activity.NodalOfficerDetailActivity
import com.nlm.ui.activity.RspLabSemen
import com.nlm.ui.activity.StateSemenBank
import com.nlm.ui.adapter.AddDocumentAdapter.MyViewHolder
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NSLP_IA_Adapter(private val implementingAgencyList: List<OnlyCreatedNlm>,val isFrom:Int) :
    RecyclerView.Adapter<NSLP_IA_Adapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemNlspFormsBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemNlspFormsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_nlsp_forms,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]

if (isFrom==1)
{

    holder.mBinding.tvState.text = item.state
//        holder.mBinding.districtName.text = item.district
    holder.mBinding.name.text = item.name
//        holder.mBinding.phoneNumber.text = item.phone
    holder.mBinding.tvDate.text = item.created
    holder.mBinding.llDistrict.hideView()
    holder.mBinding.llFarmer.hideView()

    holder.mBinding.llYear.hideView()



    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, RspLabSemen::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, RspLabSemen::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 3)
        holder.itemView.context.startActivity(intent)
    }
}
        else
{
    if (isFrom==0)
    {
        holder.mBinding.tvState.text = item.state
        holder.mBinding.districtName.text = item.district
        holder.mBinding.tvBlock.text = "Location"
        holder.mBinding.name.text = item.name
        holder.mBinding.phoneNumber.text = item.phone
        holder.mBinding.tvDate.text = item.created
        holder.mBinding.llDistrict.showView()
        holder.mBinding.llFarmer.showView()

        holder.mBinding.llYear.showView()
        holder.mBinding.tvYearofe.text=item.year_of_est
        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RspLabSemen::class.java)
            intent.putExtra("nodalOfficer", item)
            intent.putExtra("isFrom", 2)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, RspLabSemen::class.java)
            intent.putExtra("nodalOfficer", item)
            intent.putExtra("isFrom", 3)
            holder.itemView.context.startActivity(intent)
        }
    }
    else if (isFrom==2){

            holder.mBinding.tvState.text = item.state
            holder.mBinding.districtName.text = item.district
            holder.mBinding.tvBlock.text = "Location"
            holder.mBinding.name.text = item.name
            holder.mBinding.phoneNumber.text = item.phone
            holder.mBinding.tvDate.text = item.created
            holder.mBinding.llDistrict.showView()
            holder.mBinding.llFarmer.showView()

            holder.mBinding.llYear.showView()
            holder.mBinding.tvYearofe.text=item.year_of_est
            holder.mBinding.ivView.setOnClickListener {
                val intent = Intent(holder.itemView.context, StateSemenBank::class.java)
                intent.putExtra("nodalOfficer", item)
                intent.putExtra("isFrom", 2)
                holder.itemView.context.startActivity(intent)
            }
            holder.mBinding.ivEdit.setOnClickListener {
                val intent = Intent(holder.itemView.context, StateSemenBank::class.java)
                intent.putExtra("nodalOfficer", item)
                intent.putExtra("isFrom", 3)
                holder.itemView.context.startActivity(intent)

        }
    }
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
