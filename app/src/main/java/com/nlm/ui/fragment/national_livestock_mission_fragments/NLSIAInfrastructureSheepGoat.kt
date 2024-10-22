package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.util.Log
import android.view.View
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAInfrastructureSheepGoat(val fragmentId:Int) : BaseFragment<FragmentNLSIAInfrastructureSheepGoatBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__infrastructure__sheep_goat
    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAInfrastructureSheepGoatBinding?=null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
       Log.d("ID", fragmentId.toString())
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
        fun save(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    "part2",
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
                    mBinding?.etFrozenSemenNumber?.text.toString().toInt(),
                    mBinding?.etFrozenSemenLocation?.text.toString(),
                    mBinding?.etLiquidSemenNumber?.text.toString().toInt(),
                    mBinding?.etLiquidSemenLocation?.text.toString(),
                    mBinding?.etProductionCapacityNumber?.text.toString().toInt(),
                    mBinding?.etProductionCapacityLocation?.text.toString(),
                    mBinding?.etActualProductionNumber?.text.toString().toInt(),
                    mBinding?.etActualProductionLocation?.text.toString(),
                    mBinding?.etNoOfDosesOfSemenNumber?.text.toString().toInt(),
                    mBinding?.etNoOfDosesOfSemenLocation?.text.toString(),
                    mBinding?.etNoOfDosesOfSemenNeighbouringNumber?.text.toString().toInt(),
                    mBinding?.etNoOfDosesOfSemenNeighbouringnLocation?.text.toString(),
                    mBinding?.etAvailabilityOfLiquidNitrogenNumber?.text.toString().toInt(),
                    mBinding?.etAvailabilityOfLiquidNitrogenLocation?.text.toString(),
                    mBinding?.etBreedingFramsNumber?.text.toString().toInt(),
                    mBinding?.etBreedingFramsLocation?.text.toString(),
                    mBinding?.etGoatBreedingFramsNumber?.text.toString().toInt(),
                    mBinding?.etGoatBreedingFramsLocation?.text.toString(),
                    mBinding?.etTrainingCentersNumber?.text.toString().toInt(),
                    mBinding?.etTrainingCentersLocation?.text.toString(),
                    mBinding?.etCatelAiNumber?.text.toString().toInt(),
                    mBinding?.etCatelAiNumber?.text.toString(),
                    mBinding?.etTotalAiNumber?.text.toString().toInt(),
                    mBinding?.etTotalAiLocation?.text.toString(),
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
                    null,
                    null,
                    null,

                    )
            )
        }



    }

}