package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
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
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.setSafeOnClickListener
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLSIAGoverningBodyBoardOfDirectorsFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(),
    CallBackItemTypeIACompositionListEdit {
    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAGoverningBodyBoardOfDirectorsBinding?=null
    private var nlmIACompositionOFGoverningAdapter: NlmIACompositionOFGoverningAdapter ?= null
    private lateinit var nlmIACompositionOFGoverningList: MutableList<ImplementingAgencyAdvisoryCommittee>
    private var nlmIAProjectMonitoringCommitteeAdapter: NlmIAProjectMonitoringCommitteeAdapter ?= null
    private lateinit var nlmIAProjectMonitoringCommitteeList: MutableList<ImplementingAgencyProjectMonitoring>
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__governing_body__board__of__directors


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        if (viewEdit=="view")
        {
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
                        if (viewEdit=="view"||viewEdit=="edit")
                        {
                            nlmIACompositionOFGoverningList.clear()
                            nlmIAProjectMonitoringCommitteeList.clear()
                            userResponseModel._result.implementing_agency_advisory_committee?.let { it1 ->
                                nlmIACompositionOFGoverningList.addAll(
                                    it1
                                )
                                userResponseModel._result.implementing_agency_project_monitoring?.let { it2 ->
                                    nlmIAProjectMonitoringCommitteeList.addAll(
                                        it2
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
        }
    }

    private fun nlmIACompositionOFGoverningAdapter() {
        nlmIACompositionOFGoverningList = mutableListOf()
        nlmIACompositionOFGoverningAdapter =
            NlmIACompositionOFGoverningAdapter(nlmIACompositionOFGoverningList,viewEdit,this)
        mBinding?.rvNlmIACompositionOFGoverning?.adapter = nlmIACompositionOFGoverningAdapter
        mBinding?.rvNlmIACompositionOFGoverning?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun nlmIAProjectMonitoringCommitteeAdapter() {
        nlmIAProjectMonitoringCommitteeList = mutableListOf()
        nlmIAProjectMonitoringCommitteeAdapter =
            NlmIAProjectMonitoringCommitteeAdapter(nlmIAProjectMonitoringCommitteeList,viewEdit)
        mBinding?.rvNlmIAProjectMonitoringCommittee?.adapter = nlmIAProjectMonitoringCommitteeAdapter
        mBinding?.rvNlmIAProjectMonitoringCommittee?.layoutManager = LinearLayoutManager(requireContext())
    }


    inner class ClickActions {
        fun saveAndNext(view: View) {
       viewModel.getImplementingAgencyAddApi(requireContext(),true,
           ImplementingAgencyAddRequest(
               part = "part3",
               implementing_agency_advisory_committee = nlmIACompositionOFGoverningList,
               implementing_agency_project_monitoring = nlmIAProjectMonitoringCommitteeList,
               id = Preferences.getPreference_int(requireContext(),AppConstants.FORM_FILLED_ID),
               )
       )
   }
        fun saveAsDraft(view: View) {
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    part = "part3",
                    implementing_agency_advisory_committee = nlmIACompositionOFGoverningList,
                    implementing_agency_project_monitoring = nlmIAProjectMonitoringCommitteeList,
                    id = Preferences.getPreference_int(requireContext(),AppConstants.FORM_FILLED_ID),
                )
            )
            savedAsDraft=true

        }

        fun compositionOfGoverningNlmIaDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(),1,null)
        }
        fun nlmIAProjectMonitoringCommitteeDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(),2, null)
        }
    }

    private fun compositionOfGoverningNlmIaDialog(context: Context,isFrom:Int,selectedItem: IdAndDetails?) {
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
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.showView()
        if(selectedItem!=null && isFrom == 1)
        {
            bindingDialog.nameOfOfficial.setText(selectedItem.name_of_the_official)
            bindingDialog.nameOfDesignation.setText(selectedItem.designation)
            bindingDialog.nameOfOrganization.setText(selectedItem.organization)

        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.nameOfOfficial.text.toString().isNotEmpty()||bindingDialog.nameOfDesignation.text.toString().isNotEmpty()||bindingDialog.nameOfOrganization.text.toString().isNotEmpty())
            {
                if(isFrom == 2) {
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
                    }
                    dialog.dismiss()
                }
                else if(isFrom == 1){
                    if(selectedItem!=null)
                    {
                        nlmIACompositionOFGoverningList.add(
                            ImplementingAgencyAdvisoryCommittee(
                                bindingDialog.nameOfOfficial.text.toString(),
                                bindingDialog.nameOfDesignation.text.toString(),
                                bindingDialog.nameOfOrganization.text.toString(),
                                selectedItem.implementing_agency_id,
                                selectedItem.id
                            )
                        )
                    }
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

    override fun onClickItem(selectedItem: IdAndDetails) {
        compositionOfGoverningNlmIaDialog(requireContext(),1,selectedItem)
    }
}