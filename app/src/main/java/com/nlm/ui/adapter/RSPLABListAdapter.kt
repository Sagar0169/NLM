package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemRspLabListBinding
import com.nlm.model.RSPLabListData
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class RSPLABListAdapter(
    val context: Context,
    private val implementingAgencyList: ArrayList<RSPLabListData>,
    private val callBackDeleteAtId: CallBackDeleteAtId
) :
    RecyclerView.Adapter<RSPLABListAdapter.RSPLabViewHolder>() {

    // ViewHolder class to hold the view elements
    class RSPLabViewHolder(val mBinding: ItemRspLabListBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RSPLabViewHolder {
        val mBinding: ItemRspLabListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rsp_lab_list,
            parent,
            false
        )
        return RSPLabViewHolder(mBinding)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: RSPLabViewHolder, position: Int) {

    val item = implementingAgencyList[position]

    holder.mBinding.etState.text = item.state_name
    holder.mBinding.etDistricts.text = item.district_name
    holder.mBinding.etPhone.text = item.phone_no.toString()
    holder.mBinding.etEstablishment.text = item.year_of_establishment
    holder.mBinding.etCreated.text = item.created_at
    holder.mBinding.etStatus.text = item.is_draft_ia
    holder.mBinding.etNlmStatus.text = item.is_draft_nlm

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

//    holder.mBinding.ivView.setOnClickListener {
//        val intent = Intent(holder.itemView.context, RspLabSemenForms::class.java)
//        intent.putExtra("nodalOfficer", item)
//        intent.putExtra("isFrom", 2)
//        holder.itemView.context.startActivity(intent)
//    }
//    holder.mBinding.ivEdit.setOnClickListener {
//        val intent = Intent(holder.itemView.context, RspLabSemenForms::class.java)
//        intent.putExtra("nodalOfficer", item)
//        intent.putExtra("isFrom", 3)
//        holder.itemView.context.startActivity(intent)
//    }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return implementingAgencyList.size
    }
}
