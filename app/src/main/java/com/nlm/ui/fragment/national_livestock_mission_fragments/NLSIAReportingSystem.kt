package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.CallBackItemFundsReceivedListEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIAFundsRecievedAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLSIAReportingSystem (private val viewEdit: String?,private val itemId:Int?): BaseFragment<FragmentNLSIAReportingSystemBinding>(),
    CallBackItemFundsReceivedListEdit {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__reporting__system

    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAReportingSystemBinding?=null
    private var savedAsDraft:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var nlmIAFundsRecievedAdapter: NlmIAFundsRecievedAdapter? = null
    private lateinit var nlmIAFundsRecievedList: MutableList<ImplementingAgencyFundsReceived>
    private var listener: OnNextButtonClickListener? = null
    private var savedAsEdit:Boolean=false

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        if (viewEdit=="view")
        {mBinding?.tvAddMore?.hideView()
            mBinding?.etFrequencyOfMonitoring1?.isEnabled=false
            mBinding?.etFrequencyOfMonitoring2?.isEnabled=false
            mBinding?.etReportingMechanismToStateGovt1?.isEnabled=false
            mBinding?.etReportingMechanismToStateGovt2?.isEnabled=false
            mBinding?.etRegularity1?.isEnabled=false
            mBinding?.etRegularity2?.isEnabled=false
            mBinding?.etSubmission1?.isEnabled=false
            mBinding?.etSubmission2?.isEnabled=false
            mBinding?.etStudiesConducted?.isEnabled=false
            ViewEditApi()
        }
        else if (viewEdit=="edit"){
            ViewEditApi()

        }
        nlmIAFundsRecievedAdapter()
    }

    override fun setVariables() {}

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
                    }else

                    {
                        if (viewEdit=="view"||viewEdit=="edit")
                        {
                            if (savedAsEdit)
                            {
                                listener?.onNextButtonClick()
                            }
                            else {
                            mBinding?.etFrequencyOfMonitoring1?.setText(userResponseModel._result.frequency_of_monitoring_1)
                            mBinding?.etFrequencyOfMonitoring2?.setText(userResponseModel._result.frequency_of_monitoring_2)
                            mBinding?.etReportingMechanismToStateGovt1?.setText(userResponseModel._result.reporting_mechanism_1)
                            mBinding?.etReportingMechanismToStateGovt2?.setText(userResponseModel._result.reporting_mechanism_2)
                            mBinding?.etRegularity1?.setText(userResponseModel._result.regularity_1)
                            mBinding?.etRegularity2?.setText(userResponseModel._result.regularity_2)
                            mBinding?.etSubmission1?.setText(userResponseModel._result.submission_of_quarterly_1)
                            mBinding?.etSubmission2?.setText(userResponseModel._result.submission_of_quarterly_2)
                            mBinding?.etStudiesConducted?.setText(userResponseModel._result.studies_surveys_conducted)
                            nlmIAFundsRecievedList.clear()
                            userResponseModel._result.implementing_agency_funds_received?.let { it1 ->
                                nlmIAFundsRecievedList.addAll(
                                    it1
                                )
                                nlmIAFundsRecievedAdapter?.notifyDataSetChanged()
                            }
                        }
                        }
                        else{


                    listener?.onNextButtonClick()
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)}
                }}
            }
        }
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
        }}

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
        fun nlmIAFundsRecievedDialog(view: View) {
            nlmIAFundsRecievedDialog(requireContext(),null,null)
        }
    }

    private fun nlmIAFundsRecievedAdapter() {
        nlmIAFundsRecievedList = mutableListOf()
        nlmIAFundsRecievedAdapter =
            NlmIAFundsRecievedAdapter(nlmIAFundsRecievedList,viewEdit,this)
        mBinding?.rvFundRecieved?.adapter = nlmIAFundsRecievedAdapter
        mBinding?.rvFundRecieved?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun nlmIAFundsRecievedDialog(context: Context, selectedItem: ImplementingAgencyFundsReceived?, position: Int?) {
        val bindingDialog: ItemFundsReceivedNlsiaBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_funds_received_nlsia,
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
        bindingDialog.btnEdit.hideView()
        bindingDialog.tvSubmit.showView()
        if(selectedItem!=null )
        {
            selectedItem.from_dahd?.toString().let { bindingDialog.etFormDahd.setText(it) }
            selectedItem.state_govt?.toString().let { bindingDialog.etStateGovt.setText(it) }
            selectedItem.any_other?.toString().let { bindingDialog.etAnyOther.setText(it) }
            selectedItem.physical_progress?.toString().let { bindingDialog.etPhysicalProgress.setText(it) }
            bindingDialog.etYear.setText(selectedItem.year.toString())
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etYear.text.toString()
                    .isNotEmpty() || bindingDialog.etFormDahd.text.toString()
                    .isNotEmpty() || bindingDialog.etStateGovt.text.toString()
                    .isNotEmpty() || bindingDialog.etAnyOther.text.toString().isNotEmpty()
                                  || bindingDialog.etPhysicalProgress.text.toString().isNotEmpty()
            ) {
                if(selectedItem!=null)
                {
                    if (position != null) {

                        nlmIAFundsRecievedList[position] = ImplementingAgencyFundsReceived(
                            bindingDialog.etYear.text.toString(),
                            bindingDialog.etFormDahd.text.toString().toIntOrNull(),
                            bindingDialog.etStateGovt.text.toString().toIntOrNull(),
                            bindingDialog.etAnyOther.text.toString().toIntOrNull(),
                            bindingDialog.etPhysicalProgress.text.toString().toIntOrNull(),
                            selectedItem.id,
                            selectedItem.implementing_agency_id,

                            )
                        nlmIAFundsRecievedAdapter?.notifyItemChanged(position)
                    }  }
                    else{
                nlmIAFundsRecievedList.add(
                    ImplementingAgencyFundsReceived(
                        bindingDialog.etYear.text.toString(),
                        bindingDialog.etFormDahd.text.toString().toIntOrNull(),
                        bindingDialog.etStateGovt.text.toString().toIntOrNull(),
                        bindingDialog.etAnyOther.text.toString().toIntOrNull(),
                        bindingDialog.etPhysicalProgress.text.toString().toIntOrNull(),
                        null,
                        null,

                    )
                )
                nlmIAFundsRecievedList.size.minus(1).let {
                    nlmIAFundsRecievedAdapter?.notifyItemInserted(it)
                }}
                dialog.dismiss()
            } else {
                showSnackbar(
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
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
                part = "part4",
                id = itemId,
                state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }
    private fun saveDataApi(){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part4",
                frequency_of_monitoring_1 =  mBinding?.etFrequencyOfMonitoring1?.text.toString(),
                frequency_of_monitoring_2 = mBinding?.etFrequencyOfMonitoring2?.text.toString(),
                reporting_mechanism_1 = mBinding?.etReportingMechanismToStateGovt1?.text.toString(),
                reporting_mechanism_2 = mBinding?.etReportingMechanismToStateGovt2?.text.toString(),
                regularity_1 = mBinding?.etRegularity1?.text.toString(),
                regularity_2 = mBinding?.etRegularity2?.text.toString(),
                submission_of_quarterly_1 = mBinding?.etSubmission1?.text.toString(),
                submission_of_quarterly_2 = mBinding?.etSubmission2?.text.toString(),
                studies_surveys_conducted = mBinding?.etStudiesConducted?.text.toString(),
                implementing_agency_funds_received = nlmIAFundsRecievedList,
                id = itemId,
                is_draft = 1,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString()
            )
        )
    }

    override fun onClickItem(selectedItem: ImplementingAgencyFundsReceived, position: Int) {
        nlmIAFundsRecievedDialog(requireContext(),selectedItem,position)
    }

}