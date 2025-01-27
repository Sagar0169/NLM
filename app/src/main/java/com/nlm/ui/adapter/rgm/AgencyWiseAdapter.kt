package com.nlm.ui.adapter.rgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeRGMStateIACompositionList
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.ItemAnyOfAssetsRgmBinding
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyAgencyWiseCalfBorn

class AgencyWiseAdapter(
    private val RGMAgencyWiseAiList: MutableList<RgmImplementingAgencyAgencyWiseAiDone>,
    private val RGMAgencyWiseCalfList: MutableList<RgmImplementingAgencyAgencyWiseCalfBorn>,
    private val isFrom: Int,
    private val isNOOfCamps: Boolean,
    private val callBackEdit: CallBackItemTypeRGMStateIACompositionList,
    private val callBackDeleteAtId: CallBackDeleteAtId,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View type constants
    private val TYPE_AGENCY_WISE = 0
    private val TYPE_ASSETS_RGM = 1

    // Get the view type based on the value of isFrom
    override fun getItemViewType(position: Int): Int {
        return if (isFrom == 1) TYPE_ASSETS_RGM else TYPE_AGENCY_WISE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ASSETS_RGM -> {
                val binding = ItemAnyOfAssetsRgmBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AssetsRGMViewHolder(binding)
            }
            else -> {
                val binding = ItemAgencyWiseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AgencyWiseViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AssetsRGMViewHolder -> {
                // Bind data for the AssetsRGMViewHolder
                if (isNOOfCamps) {
                    holder.binding.tv11.text = "No. of Fertility camps"
                    holder.binding.tv12.text = "No. of Animals Treated"
                } else {
                    holder.binding.tv11.text = "Assets /Components"
                    holder.binding.tv12.text = "Reasons"
                }

                // Handle visibility of add/delete buttons


                // Add new row for this layout

            }

            is AgencyWiseViewHolder -> {

                // Handle visibility of add/delete buttons


                // Delete row for this layout

            }
        }
    }

    override fun getItemCount(): Int = RGMAgencyWiseAiList.size

    // Helper method to manage button visibility
    private fun handleButtonVisibility(btnAdd: ImageButton, btnDelete: ImageButton, position: Int) {
        if (position == RGMAgencyWiseAiList.size - 1) {
            // Last item, show Add button, hide Delete button
            btnAdd.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
        } else {
            // Hide Add button, show Delete button for other items
            btnAdd.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }
    }

    // ViewHolder for the AgencyWise layout using View Binding
    inner class AgencyWiseViewHolder(val binding: ItemAgencyWiseBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ViewHolder for the AssetsRGM layout using View Binding
    inner class AssetsRGMViewHolder(val binding: ItemAnyOfAssetsRgmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
