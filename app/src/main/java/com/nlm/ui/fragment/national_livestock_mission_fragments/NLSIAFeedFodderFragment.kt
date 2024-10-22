package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAFeedFodderFragment : BaseFragment<FragmentNLSIAFeedFodderBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__feed_fodder

    private var mBinding: FragmentNLSIAFeedFodderBinding?=null
    val viewModel = ViewModel()

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            }
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
        fun saveAndNext(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    part = "part7",
                    assessments_of_green = mBinding?.etAssessmentOfGreen?.text.toString(),
                    availability_of_green_area = mBinding?.etAvailabilityOfGreen?.text.toString(),
                    availability_of_dry = mBinding?.etAvailibilityOfDry?.text.toString(),
                    availability_of_concentrate = mBinding?.AvailabilityOfConcentrate?.text.toString(),
                    availability_of_common = mBinding?.etAvailabilityCommon?.text.toString(),
                    efforts_of_state = mBinding?.etEffortsOfState?.text.toString(),
                    name_of_the_agency = mBinding?.etNameOfAgency?.text.toString(),
                    quantity_of_fodder = mBinding?.etQuantityOfFodder?.text.toString(),
                    distribution_channel =mBinding?.etDistributionChannel?.text.toString() ,
                    number_of_fodder = mBinding?.etNumberOfFodder?.text.toString(),

                )
            )
        }
    }
}