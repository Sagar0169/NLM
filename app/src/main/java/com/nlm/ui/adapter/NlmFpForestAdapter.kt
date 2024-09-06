package com.nlm.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.model.NlmEdp
import com.nlm.model.NlmFpForest
import com.nlm.ui.activity.AddNewAssistanceForEaActivity
import com.nlm.ui.activity.AddNewFspPlantStorageActivity
import com.nlm.ui.activity.AddNlmAssistanceForQFSPActivity
import com.nlm.ui.activity.AddNlmEdpActivity
import com.nlm.ui.activity.AddNlmFpForestLandActivity


class NlmFpForestAdapter(private val onlyCreated: List<NlmFpForest>, private val isFrom: Int) :
    RecyclerView.Adapter<NlmFpForestAdapter.NlmFpForestViewHolder>() {

    // ViewHolder class to hold the view elements
    class NlmFpForestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvState)
        val tvDistricts: TextView = itemView.findViewById(R.id.tvDistricts)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvArea: TextView = itemView.findViewById(R.id.tvAreaCovered)
        val tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
        val tvCapacityPlant: TextView = itemView.findViewById(R.id.tvArea)
        val tvNameOfOrganisation: TextView = itemView.findViewById(R.id.tvAgencyTitle)
        val tvOrganogam: TextView = itemView.findViewById(R.id.tvOrganogam)
        val tvAgencyName: TextView = itemView.findViewById(R.id.tvAgencyName)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmFpForestViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fp_forest, parent, false)
        return NlmFpForestViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: NlmFpForestViewHolder, position: Int) {
        val item = onlyCreated[position]
        holder.tvCreatedBy.text = item.createdBy


        when (isFrom) {
            1 -> {
                holder.tvNameOfOrganisation.text = "Name of Agency"
                holder.ivView.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNlmFpForestLandActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNlmFpForestLandActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivEdit.context.startActivity(intent)

                }
            }

            3 -> {
                holder.tvCapacityPlant.text = "Capacity Of Plant"
                holder.tvNameOfOrganisation.text = "Name Of Organization"
                holder.tvStateName.text = item.state
                holder.tvLocation.text = item.location
                holder.tvArea.text = item.areaCovered
                holder.tvAgencyName.text = item.agencyName
                holder.tvDistricts.text = item.district
                holder.tvCreated.text = item.created
                holder.ivView.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNewFspPlantStorageActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNewFspPlantStorageActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivEdit.context.startActivity(intent)

                }
            }

            4 -> {
                holder.tvCapacityPlant.text = "Technical Competance"
                holder.tvOrganogam.text = "Organogram"
                holder.tvNameOfOrganisation.text = "Name of Organization"
                holder.tvStateName.text = item.state
                holder.tvAgencyName.text = item.agencyName
                holder.tvDistricts.text = item.district
                holder.tvCreated.text = item.created
                holder.tvLocation.text = item.organogram
                holder.tvArea.text = item.technicalCompetence
                holder.ivView.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNlmAssistanceForQFSPActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNlmAssistanceForQFSPActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivEdit.context.startActivity(intent)

                }
            }

            else -> {
                holder.tvCapacityPlant.text = "Area Covered"
                holder.tvNameOfOrganisation.text = "Name Implementing Agency"
                holder.tvStateName.text = item.state
                holder.tvLocation.text = item.location
                holder.tvArea.text = item.areaCovered
                holder.tvAgencyName.text = item.agencyName
                holder.tvDistricts.text = item.district
                holder.tvCreated.text = item.created
                holder.ivView.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNewFspPlantStorageActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(
                        holder.itemView.context,
                        AddNewFspPlantStorageActivity::class.java
                    ).putExtra("isFrom", isFrom)
                    holder.ivEdit.context.startActivity(intent)

                }
            }
        }


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
