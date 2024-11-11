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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenManpowerBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.databinding.ItemStateSemenManpowerBinding
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.model.StateSemenBankOtherManpower
import com.nlm.model.StateSemenManPower
import com.nlm.ui.activity.national_livestock_mission.StateSemenBankForms
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.NlmIAProjectMonitoringCommitteeAdapter
import com.nlm.ui.adapter.RspManPowerAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class StateSemenManpowerFragment : BaseFragment<FragmentStateSemenManpowerBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_state_semen_manpower
    private var mBinding: FragmentStateSemenManpowerBinding? = null
    private var viewModel = ViewModel()

    private lateinit var stateSemenManPowerList: MutableList<StateSemenBankOtherAddManpower>
    private var stateSemenManPowerAdapter: RspManPowerAdapter? = null
    private lateinit var nlmIAProjectMonitoringCommitteeList: MutableList<ImplementingAgencyProjectMonitoring>

    private var savedAsDraft: Boolean = false
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        stateSemenManPowerAdapter()
    }

    private fun stateSemenManPowerAdapter() {
        stateSemenManPowerList = mutableListOf()
        stateSemenManPowerAdapter =
            RspManPowerAdapter(stateSemenManPowerList,"view")
        mBinding?.recyclerView1?.adapter = stateSemenManPowerAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(requireContext())
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

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.stateSemenBankAddResult.observe(viewLifecycleOwner){
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
                    }
                    else{
                        Preferences.setPreference_int(requireContext(),AppConstants.FORM_FILLED_ID,userResponseModel._result.id)
                        listener?.onNextButtonClick()
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                    }

                }
            }
        }

    }

    private fun compositionOfGoverningNlmIaDialog(context: Context, isFrom: Int) {
        val bindingDialog: ItemStateSemenManpowerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_state_semen_manpower,
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
            if (bindingDialog.etDesignation.text.toString()
                    .isNotEmpty() || bindingDialog.etQualification.text.toString()
                    .isNotEmpty() || bindingDialog.etExperience.text.toString()
                    .isNotEmpty() || bindingDialog.etTrainingStatus.text.toString().isNotEmpty()
            ) {
                stateSemenManPowerList.add(
                    StateSemenBankOtherAddManpower(
                        bindingDialog.etDesignation.text.toString(),
                        bindingDialog.etQualification.text.toString(),
                        bindingDialog.etExperience.text.toString(),
                        bindingDialog.etTrainingStatus.text.toString(),
                    )
                )
                stateSemenManPowerList.size.minus(1).let {
                    stateSemenManPowerAdapter?.notifyItemInserted(it)
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

    inner class ClickActions {
        fun saveAsDraft(view: View) {
            viewModel.getStateSemenAddBankApi(
                requireContext(), true,
                StateSemenBankNLMRequest(
                    role_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    manpower_no_of_people = mBinding!!.etManPower.text.toString().toIntOrNull(),
                    officer_in_charge_name = mBinding!!.etOfficerInCharge.text.toString(),
                    state_semen_bank_other_manpower = stateSemenManPowerList,
                    is_draft = 1,

                    )
            )
            savedAsDraft = true
        }

        fun save(view: View) {
            viewModel.getStateSemenAddBankApi(
                requireContext(), true,
                StateSemenBankNLMRequest(
                    role_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    manpower_no_of_people = mBinding!!.etManPower.text.toString().toIntOrNull(),
                    officer_in_charge_name = mBinding!!.etOfficerInCharge.text.toString(),
                    state_semen_bank_other_manpower = stateSemenManPowerList,
                    )
            )
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1)
        }

    }
}