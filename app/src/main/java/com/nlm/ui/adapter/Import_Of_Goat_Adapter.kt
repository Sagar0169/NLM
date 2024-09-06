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
import com.nlm.databinding.ItemImportOfExoticGoatBinding
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.ImportOfGoat
import com.nlm.model.OnlyCreated
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.activity.Artificial_Insemination
import com.nlm.ui.activity.ImportOfExoticGoat
import com.nlm.ui.activity.NLSIAForm
import com.nlm.ui.activity.NodalOfficerDetailActivity
import com.nlm.ui.activity.RspLabSemen
import com.nlm.ui.activity.StateSemenBank
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class Import_Of_Goat_Adapter(private val implementingAgencyList: List<ImportOfGoat>,) :
    RecyclerView.Adapter<Import_Of_Goat_Adapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemImportOfExoticGoatBinding) : RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemImportOfExoticGoatBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_import_of_exotic_goat,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]



    holder.mBinding.tvNoofFramer.text = item.Number_Of_Farmers
    holder.mBinding.tvDate.text = item.created






    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, ImportOfExoticGoat::class.java)
        intent.putExtra("nodalOfficer", item)
        intent.putExtra("isFrom", 2)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, ImportOfExoticGoat::class.java)
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
