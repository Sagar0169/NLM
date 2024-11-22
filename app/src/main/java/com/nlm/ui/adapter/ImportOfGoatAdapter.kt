package com.nlm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemImportOfExoticGoatBinding
import com.nlm.model.DataIE
import com.nlm.ui.activity.national_livestock_mission.ImportOfExoticGoatForms
import com.nlm.utilities.Utility

class ImportOfGoatAdapter(private val context: Context,
                          private val callBackDeleteAtId: CallBackDeleteAtId,
                          private val implementingAgencyList: ArrayList<DataIE>,
) :
    RecyclerView.Adapter<ImportOfGoatAdapter.ImplementingAgencyViewholder>() {

    // ViewHolder class to hold the view elements
    class ImplementingAgencyViewholder(val mBinding:ItemImportOfExoticGoatBinding) : RecyclerView.ViewHolder(mBinding.root) {

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
    override fun onBindViewHolder(holder: ImplementingAgencyViewholder, @SuppressLint("RecyclerView") position: Int) {

        val item = implementingAgencyList[position]
    holder.mBinding.etState.text = item.state_name
    holder.mBinding.etCreatedBy.text = item.created_by
    holder.mBinding.etCreated.text = item.created_at
    holder.mBinding.etStatus.text = item.is_draft_ia
    holder.mBinding.etStatusNlm.text = item.is_draft_nlm

        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        callBackDeleteAtId.onClickItem(item.id,position,0)
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }
    holder.mBinding.ivView.setOnClickListener {
        val intent = Intent(holder.itemView.context, ImportOfExoticGoatForms::class.java)
        intent.putExtra("View/Edit", "view")
        intent.putExtra("itemId", item.id)
        holder.itemView.context.startActivity(intent)
    }
    holder.mBinding.ivEdit.setOnClickListener {
        val intent = Intent(holder.itemView.context, ImportOfExoticGoatForms::class.java)
        intent.putExtra("View/Edit", "edit")
        intent.putExtra("itemId", item.id)
        holder.itemView.context.startActivity(intent)

    }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }
    fun onDeleteButtonClick(position: Int) {
        implementingAgencyList.removeAt(position)
        notifyItemRemoved(position)
    }
}
