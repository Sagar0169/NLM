package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteAtIdString
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemVaccinationProgrammerBinding
import com.nlm.model.MobileVeterinaryUnitsListData
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitBlock
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitDistrict
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitState
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitVillage
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast


class MobileVeterinaryAdapter(
    val context: Context,
    private val list: ArrayList<MobileVeterinaryUnitsListData>,
    private val isFrom: String?,
    private val callBackDeleteAtId: CallBackDeleteAtIdString
) :
    RecyclerView.Adapter<MobileVeterinaryAdapter.MobileVeterinaryAdapterViewHolder>() {

    // ViewHolder class to hold the view elements
    class MobileVeterinaryAdapterViewHolder(val mBinding: ItemVaccinationProgrammerBinding) :
        RecyclerView.ViewHolder(mBinding.root)


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MobileVeterinaryAdapterViewHolder {
        val mBinding = ItemVaccinationProgrammerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MobileVeterinaryAdapterViewHolder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(
        holder: MobileVeterinaryAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = list[position]
        when (isFrom) {
            context.getString(R.string.state) -> {
                holder.mBinding.llDistrict.hideView()
                holder.mBinding.llFarmer.hideView()
                holder.mBinding.llBlock.hideView()
            }

            context.getString(R.string.district) -> {
                holder.mBinding.llFarmer.hideView()
                holder.mBinding.llBlock.hideView()
                holder.mBinding.llDistrict.showView()
            }

            context.getString(R.string.block_level) -> {
                holder.mBinding.llFarmer.hideView()
                holder.mBinding.llBlock.showView()
                holder.mBinding.llDistrict.showView()
            }

            context.getString(R.string.farmer_level) -> {
                holder.mBinding.llBlock.showView()
                holder.mBinding.llDistrict.showView()
                holder.mBinding.llFarmer.showView()
            }
        }
        holder.mBinding.etState.text = item.state_name
        holder.mBinding.etDistrict.text = item.district_name
        holder.mBinding.etFarmer.text = item.village_name
        holder.mBinding.etBlockName.text = item.block_name
        holder.mBinding.etNlmStatus.text = item.status
        holder.mBinding.etCreatedAt.text = convertDate(item.created)
        holder.mBinding.etCreatedBy.text = item.created_by

        if (item.is_view) {
            holder.mBinding.ivView.showView()
        } else {
            holder.mBinding.ivView.hideView()
        }
        if (item.is_delete) {
            holder.mBinding.ivDelete.showView()
        } else {
            holder.mBinding.ivDelete.hideView()
        }
        if (item.is_edit) {
            holder.mBinding.ivEdit.showView()
        } else {
            holder.mBinding.ivEdit.hideView()
        }

        holder.mBinding.ivView.setOnClickListener {

            when (isFrom) {
                context.getString(R.string.state) -> context.startActivity(
                    Intent(context, AddNewMobileVeterinaryUnitState::class.java)
                        .putExtra("View/Edit", "view")
                        .putExtra("itemId", item.id)
                )

                context.getString(R.string.district) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitDistrict::class.java)
                            .putExtra("View/Edit", "view")
                            .putExtra("itemId", item.id)
                    )
                }

                context.getString(R.string.block_level) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitBlock::class.java)
                            .putExtra("View/Edit", "view")
                            .putExtra("itemId", item.id)
                    )
                }

                context.getString(R.string.farmer_level) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitVillage::class.java)
                            .putExtra("View/Edit", "view")
                            .putExtra("itemId", item.id)
                    )
                }

                else -> {
                    if (isFrom != null) {
                        context.toast(isFrom)
                    }
                }
            }
        }

        holder.mBinding.ivEdit.setOnClickListener {
            when (isFrom) {
                context.getString(R.string.state) -> context.startActivity(
                    Intent(context, AddNewMobileVeterinaryUnitState::class.java)
                        .putExtra("View/Edit", "edit")
                        .putExtra("itemId", item.id)
                )

                context.getString(R.string.district) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitDistrict::class.java)
                            .putExtra("View/Edit", "edit")
                            .putExtra("itemId", item.id)
                    )
                }

                context.getString(R.string.block_level) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitBlock::class.java)
                            .putExtra("View/Edit", "edit")
                            .putExtra("itemId", item.id)
                    )
                }

                context.getString(R.string.farmer_level) -> {
                    context.startActivity(
                        Intent(context, AddNewMobileVeterinaryUnitVillage::class.java)
                            .putExtra("View/Edit", "edit")
                            .putExtra("itemId", item.id)
                    )
                }
            }
        }

        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        if (item != null) {
                            if (isFrom != null) {
                                callBackDeleteAtId.onClickItem(item.id, position, isFrom)
                            }
                        }
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
    }

    fun onDeleteButtonClick(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
