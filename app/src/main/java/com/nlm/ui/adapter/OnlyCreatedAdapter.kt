package com.nlm.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.model.OnlyCreated
import com.nlm.ui.activity.AddNewMobileVeterinaryUnit
import com.nlm.utilities.showView


class OnlyCreatedAdapter(private val onlyCreated: List<OnlyCreated>, private val isFrom: Int) :
    RecyclerView.Adapter<OnlyCreatedAdapter.OnlyCreatedAdapterViewholder>() {

    // ViewHolder class to hold the view elements
    class OnlyCreatedAdapterViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvState)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        val tvDistrict: TextView = itemView.findViewById(R.id.district_name)
        val tvBlock: TextView = itemView.findViewById(R.id.block_name)
        val tvFarmer: TextView = itemView.findViewById(R.id.farmer_name)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val llDistrict: LinearLayout = itemView.findViewById(R.id.llDistrict)
        val llBlock: LinearLayout = itemView.findViewById(R.id.llBlock)
        val llFarmer: LinearLayout = itemView.findViewById(R.id.llFarmer)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnlyCreatedAdapterViewholder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_only_created, parent, false)
        return OnlyCreatedAdapterViewholder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: OnlyCreatedAdapterViewholder, position: Int) {
        val item = onlyCreated[position]
        holder.tvStateName.text = item.state
        holder.tvCreated.text = item.created

        when (isFrom) {
            2 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
            }

            3 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
                holder.llBlock.showView()
                holder.tvBlock.text = item.block
            }

            4 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
                holder.llBlock.showView()
                holder.tvBlock.text = item.block
                holder.llFarmer.showView()
                holder.tvFarmer.text = item.village
            }
            6 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
            }
            7 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
                holder.llFarmer.showView()
                holder.tvFarmer.text = item.village
            }
            9 -> {
                holder.llDistrict.showView()
                holder.tvDistrict.text = item.district
            }


        }

        holder.ivView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddNewMobileVeterinaryUnit::class.java
            ).putExtra("isFrom", isFrom)
            holder.itemView.context.startActivity(intent)
        }
        holder.ivEdit.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddNewMobileVeterinaryUnit::class.java
            ).putExtra("isFrom", isFrom)
            holder.itemView.context.startActivity(intent)
        }
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, NodalOfficerDetailActivity::class.java)
//            intent.putExtra("nodalOfficer", item)
//            holder.itemView.context.startActivity(intent)
//        }

    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return onlyCreated.size
    }
}
