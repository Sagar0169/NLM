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
import com.nlm.databinding.ItemNlspFormsBinding
import com.nlm.model.DataImplementingAgency
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView


class NationalLiveStockMissionIAAdapter(
    val context:Context,
    private val implementingAgencyList: ArrayList<DataImplementingAgency>,
    val isFrom: Int,
    val Role_name: String,
    private val callBackDeleteAtId: CallBackDeleteAtId
) : RecyclerView.Adapter<NationalLiveStockMissionIAAdapter.ImplementingAgencyViewHolder>() {

    class ImplementingAgencyViewHolder(val mBinding: ItemNlspFormsBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ImplementingAgencyViewHolder {
        val mBinding: ItemNlspFormsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_nlsp_forms,
            viewGroup,
            false
        )
        return ImplementingAgencyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ImplementingAgencyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = implementingAgencyList[position]

        holder.mBinding.etState.text = item.state_name
        holder.mBinding.etCreatedBy.text = item.name_location_of_ai
        holder.mBinding.etCreated.text = item.created
        holder.mBinding.etStatus.text = item.is_draft
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
         holder.mBinding.ivDelete.setOnClickListener {
             Utility.showConfirmationAlertDialog(
                 context,
                 object :
                     DialogCallback {
                     override fun onYes() {
                         if (item != null) {
                             callBackDeleteAtId.onClickItem(item.id,position)
                         }
                     }
                 },
                 context.getString(R.string.are_you_sure_want_to_delete_your_post)
             )
         }

        holder.mBinding.ivView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NLMIAForm::class.java)
            intent.putExtra("View/Edit", "view")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
        holder.mBinding.ivEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, NLMIAForm::class.java)
            intent.putExtra("View/Edit", "edit")
            intent.putExtra("itemId", item.id)
            holder.itemView.context.startActivity(intent)
        }
    }

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
