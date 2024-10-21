package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAReportingSystem : BaseFragment<FragmentNLSIAReportingSystemBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__reporting__system
    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAReportingSystemBinding?=null

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
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
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }
    inner class ClickActions {
        fun save2(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
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
                    mBinding?.etStudiesConducted1?.text.toString(),
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

                    )
            )
        }



    }
}