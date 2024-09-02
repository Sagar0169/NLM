package com.nlm.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.model.NodalOfficer
import com.nlm.ui.activity.NodalOfficerDetailActivity
import com.nlm.ui.activity.RegistrationActivity

class ImplementingAgencyAdapter(private val implementingAgencyList: List<NodalOfficer>) :
    RecyclerView.Adapter<ImplementingAgencyAdapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvState)
        val tvNodalOfficerName: TextView = itemView.findViewById(R.id.tvNodalOfficeName)
        val tvNodalOfficerEmail: TextView = itemView.findViewById(R.id.tvNodalOfficerEmail)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        val tvAgencyName: TextView = itemView.findViewById(R.id.tvAgencyName)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_implementing_agency, parent, false)
        return ImplementingAgencyViewholder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {
        val item = implementingAgencyList[position]
        holder.tvStateName.text = item.state
        holder.tvNodalOfficerName.text = item.nodalOfficerName
        holder.tvNodalOfficerEmail.text = item.nodalOfficerEmail
        holder.tvCreated.text = item.created
        holder.tvAgencyName.text = item.agencyName


        holder.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NodalOfficerDetailActivity::class.java)
            intent.putExtra("nodalOfficer", item)
            intent.putExtra("isFrom", 2)
            holder.itemView.context.startActivity(intent)
        }
        holder.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, NodalOfficerDetailActivity::class.java)
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
