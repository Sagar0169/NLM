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
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.databinding.ItemFundsReceivedNlsiaBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIAFundsRecievedAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLSIAReportingSystem : BaseFragment<FragmentNLSIAReportingSystemBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__reporting__system

    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAReportingSystemBinding?=null
    private var nlmIAFundsRecievedAdapter: NlmIAFundsRecievedAdapter? = null
    private lateinit var nlmIAFundsRecievedList: MutableList<ImplementingAgencyFundsReceived>


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
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
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }
    inner class ClickActions {
        fun saveAndNext(view: View) {
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    "part4",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    mBinding?.etFrequencyOfMonitoring1?.text.toString(),
                    mBinding?.etFrequencyOfMonitoring2?.text.toString(),
                    mBinding?.etReportingMechanismToStateGovt1?.text.toString(),
                    mBinding?.etReportingMechanismToStateGovt2?.text.toString(),
                    mBinding?.etRegularity1?.text.toString(),
                    mBinding?.etRegularity2?.text.toString(),
                    mBinding?.etSubmission1?.text.toString(),
                    mBinding?.etSubmission2?.text.toString(),
                    mBinding?.etStudiesConducted?.text.toString(),
                    nlmIAFundsRecievedList,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,

                    )
            )
        }

        fun nlmIAFundsRecievedDialog(view: View) {
            nlmIAFundsRecievedDialog(requireContext())
        }
    }

    private fun nlmIAFundsRecievedAdapter() {
        nlmIAFundsRecievedList = mutableListOf()
        nlmIAFundsRecievedAdapter =
            NlmIAFundsRecievedAdapter(nlmIAFundsRecievedList)
        mBinding?.rvFundRecieved?.adapter = nlmIAFundsRecievedAdapter
        mBinding?.rvFundRecieved?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun nlmIAFundsRecievedDialog(context: Context) {
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
        bindingDialog.tvSubmit.showView()

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etYear.text.toString()
                    .isNotEmpty() || bindingDialog.etFormDahd.text.toString()
                    .isNotEmpty() || bindingDialog.etStateGovt.text.toString()
                    .isNotEmpty() || bindingDialog.etAnyOther.text.toString().isNotEmpty()
                                  || bindingDialog.etPhysicalProgress.text.toString().isNotEmpty()
            ) {
                nlmIAFundsRecievedList.add(
                    ImplementingAgencyFundsReceived(
                        bindingDialog.etYear.text.toString().toInt(),
                        bindingDialog.etFormDahd.text.toString().toDoubleOrNull(),
                        bindingDialog.etStateGovt.text.toString().toDoubleOrNull(),
                        bindingDialog.etAnyOther.text.toString().toDoubleOrNull(),
                        bindingDialog.etPhysicalProgress.text.toString().toDoubleOrNull(),
                        null
                    )
                )
                nlmIAFundsRecievedList.size.minus(1).let {
                    nlmIAFundsRecievedAdapter?.notifyItemInserted(it)
                }
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
}