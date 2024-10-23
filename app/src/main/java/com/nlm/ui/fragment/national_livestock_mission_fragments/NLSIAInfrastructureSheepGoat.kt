package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.content.Context
import android.util.Log
import android.view.View
import com.nlm.R
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAInfrastructureSheepGoat() : BaseFragment<FragmentNLSIAInfrastructureSheepGoatBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__infrastructure__sheep_goat
    val viewModel = ViewModel()
    private var listener: OnNextButtonClickListener? = null
    private var mBinding: FragmentNLSIAInfrastructureSheepGoatBinding?=null
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
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
                else{
                    listener?.onNextButtonClick()
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }
    inner class ClickActions {
        fun save(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    part = "part2",
                    frozen_semen_goat_number = mBinding?.etFrozenSemenNumber?.text.toString().toIntOrNull(),
                    frozen_semen_goat_location = mBinding?.etFrozenSemenLocation?.text.toString(),
                    liquid_semen_sheep_number = mBinding?.etLiquidSemenNumber?.text.toString().toIntOrNull(),
                    liquid_semen_sheep_location = mBinding?.etLiquidSemenLocation?.text.toString(),
                    production_capacity_number = mBinding?.etProductionCapacityNumber?.text.toString().toIntOrNull(),
                    production_capacity_location = mBinding?.etProductionCapacityLocation?.text.toString(),
                    actual_production_number = mBinding?.etActualProductionNumber?.text.toString().toIntOrNull(),
                    actual_production_location = mBinding?.etActualProductionLocation?.text.toString(),
                    no_semen_distributed_number = mBinding?.etNoOfDosesOfSemenNumber?.text.toString().toIntOrNull(),
                    no_semen_distributed_location = mBinding?.etNoOfDosesOfSemenLocation?.text.toString(),
                    no_semen_neighboring_number = mBinding?.etNoOfDosesOfSemenNeighbouringNumber?.text.toString().toIntOrNull(),
                    no_semen_neighboring_location = mBinding?.etNoOfDosesOfSemenNeighbouringnLocation?.text.toString(),
                    availability_liquid_nitrogen_number = mBinding?.etAvailabilityOfLiquidNitrogenNumber?.text.toString().toIntOrNull(),
                    availability_liquid_nitrogen_location = mBinding?.etAvailabilityOfLiquidNitrogenLocation?.text.toString(),
                    breeding_farms_number = mBinding?.etBreedingFramsNumber?.text.toString().toIntOrNull(),
                    breeding_farms_location = mBinding?.etBreedingFramsLocation?.text.toString(),
                    goat_breeding_farm_number = mBinding?.etGoatBreedingFramsNumber?.text.toString().toIntOrNull(),
                    goat_breeding_farm_location = mBinding?.etGoatBreedingFramsLocation?.text.toString(),
                    training_centers_number = mBinding?.etTrainingCentersNumber?.text.toString().toIntOrNull(),
                    training_centers_location = mBinding?.etTrainingCentersLocation?.text.toString(),
                    number_of_cattle_ai_number = mBinding?.etCatelAiNumber?.text.toString().toIntOrNull(),
                    number_of_cattle_ai_location = mBinding?.etCatelAiNumber?.text.toString(),
                    total_ai_performed_number = mBinding?.etTotalAiNumber?.text.toString().toIntOrNull(),
                    total_ai_performed_location = mBinding?.etTotalAiLocation?.text.toString(),
                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    is_deleted = 0
                    )
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}