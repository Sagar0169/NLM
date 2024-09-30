package com.nlm.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.model.RGMVitro
import com.nlm.ui.activity.rashtriya_gokul_mission.AddRGMVitroFertilizatonActivity
import com.nlm.utilities.AppConstants
import com.nlm.utilities.hideView


class RGMVItroAdapter(private val onlyCreated: List<RGMVitro>, private val isFrom: Int,val Role_name:String) :
    RecyclerView.Adapter<RGMVItroAdapter.RGMVItroAdapterViewholder>() {

    // ViewHolder class to hold the view elements
    class RGMVItroAdapterViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvState)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusData)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RGMVItroAdapterViewholder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rgm_vitro, parent, false)
        return RGMVItroAdapterViewholder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: RGMVItroAdapterViewholder, position: Int) {
        val item = onlyCreated[position]
        holder.tvStateName.text = item.state
        holder.tvCreated.text = item.created
        holder.tvStatus.text = item.status

        if (Role_name== AppConstants.SUPER_ADMIN||Role_name== AppConstants.Nodal_Officer||Role_name== AppConstants.ADMIN)
        {

            holder.ivEdit.hideView()
            holder.ivDelete.hideView()
        }
        holder.ivView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddRGMVitroFertilizatonActivity::class.java
            ).putExtra("isFrom", isFrom)
            holder.itemView.context.startActivity(intent)
        }
        holder.ivEdit.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddRGMVitroFertilizatonActivity::class.java
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
