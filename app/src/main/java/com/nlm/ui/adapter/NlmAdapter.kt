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
import com.nlm.databinding.ItemFpForestBinding
import com.nlm.model.NlmAssistanceForQFSPData
import com.nlm.model.NlmFpForest
import com.nlm.ui.activity.national_livestock_mission.AddNlmAssistanceForQFSPActivity
import com.nlm.ui.activity.national_livestock_mission.ImportOfExoticGoatForms
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmAdapter(
    val context: Context,
    private val list: ArrayList<NlmAssistanceForQFSPData>,
    private val callBackDeleteAtId: CallBackDeleteAtId
) : RecyclerView.Adapter<NlmAdapter.NlmViewHolder>() {

    // ViewHolder class to hold the view elements
    class NlmViewHolder(val mBinding: ItemFpForestBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmViewHolder {
        val mBinding: ItemFpForestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_fp_forest, parent, false
        )
        return NlmViewHolder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: NlmViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = list[position]

        if(item.is_view){
            holder.mBinding.ivView.showView()
        }
        else{
            holder.mBinding.ivView.hideView()
        }
        if(item.is_delete){
            holder.mBinding.ivDelete.showView()
        }
        else{
            holder.mBinding.ivDelete.hideView()
        }
        if(item.is_edit){
            holder.mBinding.ivEdit.showView()
        }
        else{
            holder.mBinding.ivEdit.hideView()
        }

        holder.mBinding.etOrganizationName.text = item.name_of_organization
        holder.mBinding.etState.text = item.state_name
        holder.mBinding.etDistrict.text = item.district_name
        holder.mBinding.etOrganogram.text = item.organogram
        holder.mBinding.etCreatedAt.text = convertDate(item.created)
        holder.mBinding.etNlmStatus.text = item.is_draft
        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddNlmAssistanceForQFSPActivity::class.java)
            intent.putExtra("View/Edit", "view")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddNlmAssistanceForQFSPActivity::class.java)
            intent.putExtra("View/Edit", "edit")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)

        }

        holder.mBinding.ivDelete.setOnClickListener {
            Utility.showConfirmationAlertDialog(
                context,
                object :
                    DialogCallback {
                    override fun onYes() {
                        if (item != null) {
                            callBackDeleteAtId.onClickItem(item.id,position,0)
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
}
