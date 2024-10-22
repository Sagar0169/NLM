package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel

class NLSIAConstraintsFacedByIAFragment : BaseFragment<FragmentNLSIAConstraintsFacedByIABinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__constraints_faced_by__i_a

    private var mBinding: FragmentNLSIAConstraintsFacedByIABinding?=null
    private var viewModel = ViewModel()
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()

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
      fun saveAndNext(view:View){
          viewModel.getImplementingAgencyAddApi(requireContext(),true,
              ImplementingAgencyAddRequest(
                  user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                  part = "part6",
                  infrastructural =mBinding?.etInfrastructural?.text.toString() ,
                  organizational =mBinding?.etOrganizational?.text.toString() ,
                  funds = mBinding?.etFunds?.text.toString() ,
                  any_other = mBinding?.etAnyOther?.text.toString() ,
                  any_assets_created = mBinding?.etAnyOfTheAsset?.text.toString(),
              )
          )
      }
    }
}