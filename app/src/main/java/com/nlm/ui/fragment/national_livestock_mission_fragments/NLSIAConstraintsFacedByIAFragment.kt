package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.viewModel.ViewModel

class NLSIAConstraintsFacedByIAFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAConstraintsFacedByIABinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__constraints_faced_by__i_a

    private var mBinding: FragmentNLSIAConstraintsFacedByIABinding?=null
    private var viewModel = ViewModel()
    private var savedAsDraft:Boolean=false
    private var savedAsEdit:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var listener: OnNextButtonClickListener? = null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        if (viewEdit=="view")
        {
            mBinding?.etInfrastructural?.isEnabled=false
            mBinding?.etOrganizational?.isEnabled=false
            mBinding?.etFunds?.isEnabled=false
            mBinding?.etAnyOther?.isEnabled=false
            mBinding?.etAnyOfTheAsset?.isEnabled=false

            ViewEditApi()
        }
        else if (viewEdit=="edit"){
            ViewEditApi()

        }

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
                                mBinding?.etInfrastructural?.setText(userResponseModel._result.infrastructural)
                                mBinding?.etOrganizational?.setText(userResponseModel._result.organizational)
                                mBinding?.etFunds?.setText(userResponseModel._result.funds)
                                mBinding?.etAnyOther?.setText(userResponseModel._result.any_other)
                                mBinding?.etAnyOfTheAsset?.setText(userResponseModel._result.any_assets_created)
                            }
                        }
                        else{

                            listener?.onNextButtonClick()
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                    }}}
            }
        }
    }
    inner class ClickActions {
      fun saveAndNext(view:View){
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
        fun saveAsDraft(view:View){
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
                part = "part6",
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
               user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
               part = "part6",
               infrastructural =mBinding?.etInfrastructural?.text.toString() ,
               organizational =mBinding?.etOrganizational?.text.toString() ,
               funds = mBinding?.etFunds?.text.toString() ,
               any_other = mBinding?.etAnyOther?.text.toString() ,
               any_assets_created = mBinding?.etAnyOfTheAsset?.text.toString(),
               id = itemId,
               is_draft = 1
           )
       )
   }
}