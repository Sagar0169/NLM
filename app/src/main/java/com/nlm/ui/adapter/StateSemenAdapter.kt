package com.nlm.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.databinding.ItemStateSemenBinding
import com.nlm.model.DataSemen
import com.nlm.model.NLMIA_data
import com.nlm.model.State_Semen_Bank
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm

import com.nlm.ui.activity.national_livestock_mission.RspLabSemenForms
import com.nlm.ui.activity.national_livestock_mission.StateSemenBankForms
import com.nlm.utilities.Utility

import com.nlm.utilities.hideView
import com.nlm.utilities.showView


class StateSemenAdapter(
    val context: Context,
    private val implementingAgencyList: ArrayList<DataSemen>,
    val isFrom: Int,
    val Role_name: String,
    private val callBackDeleteAtId: CallBackDeleteAtId
) :
    RecyclerView.Adapter<StateSemenAdapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding: ItemStateSemenBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImplementingAgencyViewholder {
        val mBinding: ItemStateSemenBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_state_semen,
            parent,
            false
        )
        return ImplementingAgencyViewholder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, position: Int) {

        val item = implementingAgencyList[position]
        if (Role_name == "Super Admin") {
            holder.mBinding.ivView.hideView()
            holder.mBinding.ivEdit.hideView()
            holder.mBinding.ivDelete.hideView()
        }



        holder.mBinding.etState.text = item.state_name
        holder.mBinding.etDistricts.text = item.district_name
        holder.mBinding.etEstablishment.text = item.year_of_establishment
        holder.mBinding.etCreated.text = item.created
        holder.mBinding.etStatus.text = item.is_draft

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
        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        if (item != null) {
                            callBackDeleteAtId.onClickItem(item.id, position)
                        }
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }

        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StateSemenBankForms::class.java)
            intent.putExtra("View/Edit", "view")
            intent.putExtra("itemId", item.id)
            intent.putExtra("dId", item.district_code)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, StateSemenBankForms::class.java)
            intent.putExtra("View/Edit", "edit")
            intent.putExtra("itemId", item.id)
            intent.putExtra("dId", item.district_code)
            holder.itemView.context.startActivity(intent)
        }

    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun onDeleteButtonClick(position: Int) {
        implementingAgencyList.removeAt(position)
        notifyItemRemoved(position)
    }
}
