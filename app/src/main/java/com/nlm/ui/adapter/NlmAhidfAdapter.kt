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
import com.nlm.databinding.ItemNlmEdpBinding
import com.nlm.model.NlmAhidfData
import com.nlm.model.NlmEdpData
import com.nlm.ui.activity.national_livestock_mission.AddAnimalHusbandryActivity
import com.nlm.ui.activity.national_livestock_mission.AddNewFspPlantStorageActivity
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.hideView
import com.nlm.utilities.showView


class NlmAhidfAdapter(
    val context: Context,
    private val list: ArrayList<NlmAhidfData>,
    private val callBackDeleteAtId: CallBackDeleteAtId
) :
    RecyclerView.Adapter<NlmAhidfAdapter.NlmAhidfAdapterViewHolder>() {

    // ViewHolder class to hold the view elements
    class NlmAhidfAdapterViewHolder(val mBinding: ItemNlmEdpBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmAhidfAdapterViewHolder {
        val mBinding: ItemNlmEdpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_nlm_edp, parent, false
        )
        return NlmAhidfAdapterViewHolder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(
        holder: NlmAhidfAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = list[position]

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

        holder.mBinding.etState.text = item.state_name
        holder.mBinding.etCreatedBy.text = item.created_by
        holder.mBinding.etCreated.text = convertDate(item.created)
        holder.mBinding.etNlmStatus.text = item.is_draft_nlm.toString()
        holder.mBinding.etIAStatus.text = item.is_draft_ia.toString()
        holder.mBinding.ivView.setOnClickListener {
            context.startActivity(
                Intent(context, AddAnimalHusbandryActivity::class.java).putExtra(
                    "View/Edit",
                    "view"
                )
                    .putExtra("itemId", item.id)
            )
        }
        holder.mBinding.ivEdit.setOnClickListener {
            context.startActivity(
                Intent(context, AddAnimalHusbandryActivity::class.java).putExtra(
                    "View/Edit",
                    "edit"
                )
                    .putExtra("itemId", item.id)
            )
        }

        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        if (item != null) {
                            callBackDeleteAtId.onClickItem(item.id, position, 0)
                        }
                    }
                },
                context.getString(R.string.are_you_sure_want_to_delete_your_post)
            )
        }

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

    fun onDeleteButtonClick(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}
