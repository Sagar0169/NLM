package com.nlm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.ui.activity.RegistrationActivity
import nlm.R

class DistrictAdapter(private val districtList: List<String>, private val callBackItem: RegistrationActivity, private val callBackItemDistrict: RegistrationActivity) :
    RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>() {

    // ViewHolder class to hold the view elements
    class DistrictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvStateName)
    }

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        return DistrictViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        val stateName = districtList[position]
        holder.tvStateName.text = stateName

        holder.tvStateName.setOnClickListener {
            callBackItemDistrict.onClickItemDistrict(stateName)
        }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return districtList.size
    }
}
