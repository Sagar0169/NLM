package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeIACompositionListEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.NlmIAProjectMonitoringCommitteeAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.setSafeOnClickListener
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLSIAGoverningBodyBoardOfDirectorsFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(),
    CallBackItemTypeIACompositionListEdit, CallBackDeleteAtId
{
    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAGoverningBodyBoardOfDirectorsBinding?=null
    private var nlmIACompositionOFGoverningAdapter: NlmIACompositionOFGoverningAdapter ?= null
    private lateinit var nlmIACompositionOFGoverningList: MutableList<ImplementingAgencyAdvisoryCommittee>
    private var nlmIAProjectMonitoringCommitteeAdapter: NlmIAProjectMonitoringCommitteeAdapter ?= null
    private lateinit var nlmIAProjectMonitoringCommitteeList: MutableList<ImplementingAgencyProjectMonitoring>
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft:Boolean=false
    private var savedAsEdit:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__governing_body__board__of__directors


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        if (viewEdit=="view")
        {mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
             mBinding?.tvAddMore1?.hideView()
             mBinding?.tvAddMore2?.hideView()
            ViewEditApi()

        }
        else   if (viewEdit=="edit")
        {

            ViewEditApi()

        }
        nlmIACompositionOFGoverningAdapter()
        nlmIAProjectMonitoringCommitteeAdapter()

    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){

            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            } else {
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
                else{

                    if (savedAsDraft)
                    {
                        savedAsDraftClick?.onSaveAsDraft()
                    }
                    else
                    {
                        if (viewEdit=="view")
                        {
                            if (savedAsEdit)
                            {
                                listener?.onNextButtonClick()
                            }
                            else {
                                nlmIACompositionOFGoverningList.clear()
                                nlmIAProjectMonitoringCommitteeList.clear()
                                // Add data to nlmIACompositionOFGoverningList if it's null or empty
                                if (userResponseModel._result.implementing_agency_advisory_committee.isNullOrEmpty()) {
                                    nlmIACompositionOFGoverningList.add(
                                        ImplementingAgencyAdvisoryCommittee(
                                            name_of_the_official = "",
                                            designation = "",
                                            organization = "",
                                            implementing_agency_id = null,
                                            id = null
                                        )
                                    )
                                } else {
                                    nlmIACompositionOFGoverningList.addAll(userResponseModel._result.implementing_agency_advisory_committee)
                                }
                                if (userResponseModel._result.implementing_agency_project_monitoring.isNullOrEmpty()) {
                                    nlmIAProjectMonitoringCommitteeList.add(
                                        ImplementingAgencyProjectMonitoring(
                                            name_of_official = "",
                                            designation = "",
                                            organization = "",
                                            id = null,
                                            implementing_agency_id = null
                                        )
                                    )
                                } else {
                                    nlmIAProjectMonitoringCommitteeList.addAll(userResponseModel._result.implementing_agency_project_monitoring)
                                }

                                nlmIACompositionOFGoverningAdapter?.notifyDataSetChanged()
                                    nlmIAProjectMonitoringCommitteeAdapter?.notifyDataSetChanged()

                            }
                        }
                       else if (viewEdit=="edit")
                        {
                            if (savedAsEdit)
                            {
                                listener?.onNextButtonClick()
                            }
                            else {
                                nlmIACompositionOFGoverningList.clear()
                                nlmIAProjectMonitoringCommitteeList.clear()

                                userResponseModel._result.implementing_agency_advisory_committee?.let { it1 ->
                                    nlmIACompositionOFGoverningList.addAll(
                                        it1
                                    )
                                }


                                userResponseModel._result.implementing_agency_project_monitoring?.let { it1 ->
                                    nlmIAProjectMonitoringCommitteeList.addAll(
                                        it1
                                    )
                                }


                                nlmIACompositionOFGoverningAdapter?.notifyDataSetChanged()
                                nlmIAProjectMonitoringCommitteeAdapter?.notifyDataSetChanged()

                            }
                        }
                        else{
                            listener?.onNextButtonClick()
                        }

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }}
            }
        }}
    }

    private fun nlmIACompositionOFGoverningAdapter() {
        nlmIACompositionOFGoverningList = mutableListOf()
        nlmIACompositionOFGoverningAdapter =
            NlmIACompositionOFGoverningAdapter(requireActivity(),nlmIACompositionOFGoverningList,viewEdit,this,this)
        mBinding?.rvNlmIACompositionOFGoverning?.adapter = nlmIACompositionOFGoverningAdapter
        mBinding?.rvNlmIACompositionOFGoverning?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun nlmIAProjectMonitoringCommitteeAdapter() {
        nlmIAProjectMonitoringCommitteeList = mutableListOf()
        nlmIAProjectMonitoringCommitteeAdapter =
            NlmIAProjectMonitoringCommitteeAdapter(requireContext(),nlmIAProjectMonitoringCommitteeList,viewEdit,this,this
            )
        mBinding?.rvNlmIAProjectMonitoringCommittee?.adapter = nlmIAProjectMonitoringCommitteeAdapter
        mBinding?.rvNlmIAProjectMonitoringCommittee?.layoutManager = LinearLayoutManager(requireContext())
    }


    inner class ClickActions {
        fun saveAndNext(view: View) {
            if (viewEdit=="view")
            {
                listener?.onNextButtonClick()
            }
            if (itemId==0)
            {
                activity?.supportFragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                showSnackbar(mBinding!!.clParent, "Please fill the mandatory field and save the data")
                listener?.onNavigateToFirstFragment()

            }
            else {
                if (viewEdit=="edit")
                {
                    savedAsEdit=true
                }
                saveDataApi()
            }
   }
        fun saveAsDraft(view: View) {
            if (itemId==0)
            {
                activity?.supportFragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                showSnackbar(mBinding!!.clParent, "Please fill the mandatory field and save the data")
                listener?.onNavigateToFirstFragment()

            }
            else {
                if (viewEdit=="edit")
                {
                    savedAsEdit=true
                }
                saveDataApi()
            savedAsDraft=true

        }}

        fun compositionOfGoverningNlmIaDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(),1,null,null)
        }
        fun nlmIAProjectMonitoringCommitteeDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(),2, null,null)
        }
    }

     fun compositionOfGoverningNlmIaDialog(context: Context,isFrom:Int,selectedItem: IdAndDetails?,position: Int?) {
        val bindingDialog: ItemCompositionOfGoverningNlmIaBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_composition_of_governing_nlm_ia,
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
        if(selectedItem!=null && isFrom == 1)
        {
            bindingDialog.nameOfOfficial.setText(selectedItem.name_of_the_official)
            bindingDialog.nameOfDesignation.setText(selectedItem.designation)
            bindingDialog.nameOfOrganization.setText(selectedItem.organization)
        }
         if(selectedItem!=null && isFrom == 2)
         {
             bindingDialog.nameOfOfficial.setText(selectedItem.name_of_the_official)
             bindingDialog.nameOfDesignation.setText(selectedItem.designation)
             bindingDialog.nameOfOrganization.setText(selectedItem.organization)
         }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.nameOfOfficial.text.toString().isNotEmpty()||bindingDialog.nameOfDesignation.text.toString().isNotEmpty()||bindingDialog.nameOfOrganization.text.toString().isNotEmpty())
            {
                if(isFrom == 2) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            nlmIAProjectMonitoringCommitteeList[position] =
                                ImplementingAgencyProjectMonitoring(
                                    bindingDialog.nameOfOfficial.text.toString(),
                                    bindingDialog.nameOfDesignation.text.toString(),
                                    bindingDialog.nameOfOrganization.text.toString(),
                                    selectedItem.implementing_agency_id,
                                    selectedItem.id
                                )
                            nlmIAProjectMonitoringCommitteeAdapter?.notifyItemChanged(position)
                        }

                    } else{
                    nlmIAProjectMonitoringCommitteeList.add(
                        ImplementingAgencyProjectMonitoring(
                            bindingDialog.nameOfOfficial.text.toString(),
                            bindingDialog.nameOfDesignation.text.toString(),
                            bindingDialog.nameOfOrganization.text.toString(),
                            null,
                            null,
                        )
                    )
                    nlmIAProjectMonitoringCommitteeList.size.minus(1).let {
                        nlmIAProjectMonitoringCommitteeAdapter?.notifyItemInserted(it)
                    }}
                    dialog.dismiss()
                }
                else if(isFrom == 1){
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            nlmIACompositionOFGoverningList[position] =
                                ImplementingAgencyAdvisoryCommittee(
                                    bindingDialog.nameOfOfficial.text.toString(),
                                    bindingDialog.nameOfDesignation.text.toString(),
                                    bindingDialog.nameOfOrganization.text.toString(),
                                    selectedItem.implementing_agency_id,
                                    selectedItem.id
                                )
                            nlmIACompositionOFGoverningAdapter?.notifyItemChanged(position)
                        }

                    }
                    else{
                        nlmIACompositionOFGoverningList.add(
                            ImplementingAgencyAdvisoryCommittee(
                                bindingDialog.nameOfOfficial.text.toString(),
                                bindingDialog.nameOfDesignation.text.toString(),
                                bindingDialog.nameOfOrganization.text.toString(),
                                null,
                                null
                            )
                        )
                        nlmIACompositionOFGoverningList.size.minus(1).let {
                            nlmIACompositionOFGoverningAdapter?.notifyItemInserted(it)
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
    private fun ViewEditApi(){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part3",
                id = itemId,
                state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }

    override fun onClickItem(selectedItem: IdAndDetails,position:Int,isFrom: Int) {
        compositionOfGoverningNlmIaDialog(requireContext(),isFrom,selectedItem,position)
    }
    private fun saveDataApi(){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part3",
                implementing_agency_advisory_committee = nlmIACompositionOFGoverningList,
                implementing_agency_project_monitoring = nlmIAProjectMonitoringCommitteeList,
                id = itemId,
                is_draft = 1,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString()
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> nlmIACompositionOFGoverningAdapter?.onDeleteButtonClick(it1) }
        }
        else if(isFrom==2){
            position.let { it1 -> nlmIAProjectMonitoringCommitteeAdapter?.onDeleteButtonClick(it1) }
        }


    }
}