package com.nlm.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit
import com.nlm.callBack.CallBackItemTypeRGMStateIACompositionList
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentRGMCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.RgmImplementingAgencyCompositionOfGoverningBody
import com.nlm.model.RgmImplementingAgencyProjectMonitoring
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.rgm.CompositionOFGoverningAdapter
import com.nlm.ui.adapter.rgm.ProjectMonitoringCommitteeAdapterRGM
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class RGMCompositionOfGoverningFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentRGMCompositionOfGoverningBinding>(),
    CallBackItemTypeRGMStateIACompositionList, CallBackDeleteAtId {
private var mBinding: FragmentRGMCompositionOfGoverningBinding? = null
override val layoutId: Int
    get() = R.layout.fragment_r_g_m__composition__of_governing
    private lateinit var RGMCompositionOFGoverningAdapter: CompositionOFGoverningAdapter
    private lateinit var RGMProjectMonitoringAdapter: ProjectMonitoringCommitteeAdapterRGM
    private lateinit var RGMCompositionOFGoverningList: MutableList<RgmImplementingAgencyCompositionOfGoverningBody>
    private lateinit var RGMProjectMonitoringList: MutableList<RgmImplementingAgencyProjectMonitoring>
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override fun init() {
        mBinding=viewDataBinding

        mBinding?.clickAction = ClickActions()
        RGMCompositionOFGoverningAdapter()
        RGMProjectMonitoringAdapter()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    private fun RGMCompositionOFGoverningAdapter() {
        RGMCompositionOFGoverningList = mutableListOf()
        RGMCompositionOFGoverningAdapter =
            CompositionOFGoverningAdapter(RGMCompositionOFGoverningList,requireActivity(),viewEdit,this,this)
        mBinding?.rvRGMCompositionOFGoverning?.adapter = RGMCompositionOFGoverningAdapter
        mBinding?.rvRGMCompositionOFGoverning?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    private fun RGMProjectMonitoringAdapter() {
        RGMProjectMonitoringList = mutableListOf()
        RGMProjectMonitoringAdapter =
            ProjectMonitoringCommitteeAdapterRGM(RGMProjectMonitoringList,requireActivity(),viewEdit,this,this)
        mBinding?.rvRGMProjectMonitoring?.adapter = RGMProjectMonitoringAdapter
        mBinding?.rvRGMProjectMonitoring?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    inner class ClickActions {
    fun compositionOfGoverningNlmIaDialog(view: View) {

        compositionOfGoverningRGMDialog(requireContext(),1,null,null)
    }
    fun nlmIAProjectMonitoringCommitteeDialog(view: View) {

        compositionOfGoverningRGMDialog(requireContext(),2, null,null)
    }
        fun SaveAndNext(view: View) {
            listener?.onNextButtonClick()

        }
        fun SaveAsDraft(view: View) {
            savedAsDraftClick?.onSaveAsDraft()
        }
}

fun compositionOfGoverningRGMDialog(context: Context, isFrom:Int, selectedItem: RgmImplementingAgencyCompositionOfGoverningBody?, position: Int?) {
    val bindingDialog: ItemCompositionOfGoverningBinding = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.item_composition_of_governing,
        null,
        false
    )
    val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(bindingDialog.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setGravity(Gravity.CENTER)
    val lp: WindowManager.LayoutParams = dialog.window!!.attributes
    lp.dimAmount = 0.5f
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    bindingDialog.btnDelete.hideView()
    bindingDialog.tvSubmit.showView()
    Log.d("ISFROM",isFrom.toString())
    if(selectedItem!=null && isFrom == 1)
    {
        bindingDialog.etDesignation.setText(selectedItem.designations)
        bindingDialog.etOrganization.setText(selectedItem.organization)
    }
    if(selectedItem!=null && isFrom == 2)
    {
        bindingDialog.etDesignation.setText(selectedItem.designations)
        bindingDialog.etOrganization.setText(selectedItem.organization)
    }
    bindingDialog.tvSubmit.setOnClickListener {
        if (bindingDialog.etDesignation.text.toString().isNotEmpty()||bindingDialog.etOrganization.text.toString().isNotEmpty())
        {
            if(isFrom == 2) {
                if(selectedItem!=null)
                {
                    if (position != null) {
                        RGMProjectMonitoringList[position] =
                            RgmImplementingAgencyProjectMonitoring(
                                designation = bindingDialog.etDesignation.text.toString(),
                               organization =  bindingDialog.etOrganization.text.toString(),
                               rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                               id= selectedItem.id
                            )
                        RGMProjectMonitoringAdapter.notifyItemChanged(position)
                    }

                } else{
                    RGMProjectMonitoringList.add(
                        RgmImplementingAgencyProjectMonitoring(
                            designation = bindingDialog.etDesignation.text.toString(),
                            organization =  bindingDialog.etOrganization.text.toString(),
                            null,
                            null,
                        )
                    )
                    RGMProjectMonitoringList.size.minus(1).let {
                        RGMProjectMonitoringAdapter.notifyItemInserted(it)
                    }}
                dialog.dismiss()
            }
            else if(isFrom == 1){
                if(selectedItem!=null)
                {
                    if (position != null) {
                        RGMCompositionOFGoverningList[position] =
                            RgmImplementingAgencyCompositionOfGoverningBody(
                                designations = bindingDialog.etDesignation.text.toString(),
                                organization =  bindingDialog.etOrganization.text.toString(),
                                rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                                id= selectedItem.id
                            )
                        RGMCompositionOFGoverningAdapter.notifyItemChanged(position)
                    }

                }
                else{
                    RGMCompositionOFGoverningList.add(
                        RgmImplementingAgencyCompositionOfGoverningBody(
                            designations = bindingDialog.etDesignation.text.toString(),
                            organization =  bindingDialog.etOrganization.text.toString(),
                            null,
                            null
                        )
                    )
                    RGMCompositionOFGoverningList.size.minus(1).let {
                        RGMCompositionOFGoverningAdapter.notifyItemInserted(it)
                    }

                }
                dialog.dismiss()
            }
        }

        else {
            showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
        }
    }
    dialog.show()
}
    override fun onClickItem(
        selectedItem: RgmImplementingAgencyCompositionOfGoverningBody,
        position: Int,
        isFrom: Int
    ) {
        compositionOfGoverningRGMDialog(requireContext(),isFrom,selectedItem,position)
    }//edit

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> RGMCompositionOFGoverningAdapter.onDeleteButtonClick(it1) }
        }
        else if(isFrom==2){
            position.let { it1 -> RGMProjectMonitoringAdapter.onDeleteButtonClick(it1) }
        }
    }//delete
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }
}