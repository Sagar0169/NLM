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
import com.nlm.model.RgmAi
import com.nlm.ui.activity.AddRGMVitroFertilizatonActivity
import com.nlm.ui.activity.AddRgmAiCenterAcitivity


class RgmAiAdapter(private val onlyCreated: List<RgmAi>, private val isFrom: Int) :
    RecyclerView.Adapter<RgmAiAdapter.RgmAiAdapterViewholder>() {

    // ViewHolder class to hold the view elements
    class RgmAiAdapterViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvState)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        val tvSubmit: TextView = itemView.findViewById(R.id.tvSubmitAs)
        val tvLocAiCenter: TextView = itemView.findViewById(R.id.tvLocAiCenter)
        val tvNameAiCenter: TextView = itemView.findViewById(R.id.tvNameAiCenter)
        val tvDateOfVisit: TextView = itemView.findViewById(R.id.tvDateOfVisit)
        val tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
        val tvDistrict: TextView = itemView.findViewById(R.id.tvDistrict)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RgmAiAdapterViewholder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rgm_ai_center, parent, false)
        return RgmAiAdapterViewholder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: RgmAiAdapterViewholder, position: Int) {
        val item = onlyCreated[position]
        holder.tvStateName.text = item.state
        holder.tvCreated.text = item.created
        holder.tvSubmit.text = item.submit
        holder.tvDistrict.text = item.district
        holder.tvNameAiCenter.text = item.name
        holder.tvLocAiCenter.text = item.location
        holder.tvDateOfVisit.text = item.dateOfVisit
        holder.tvCreatedBy.text = item.createdBy


        holder.ivView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddRgmAiCenterAcitivity::class.java
            ).putExtra("isFrom", isFrom)
            holder.itemView.context.startActivity(intent)
        }
        holder.ivEdit.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                AddRgmAiCenterAcitivity::class.java
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
