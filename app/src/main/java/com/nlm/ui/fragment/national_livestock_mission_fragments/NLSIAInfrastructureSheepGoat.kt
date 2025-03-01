package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.viewModel.ViewModel


class NLSIAInfrastructureSheepGoat(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAInfrastructureSheepGoatBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__infrastructure__sheep_goat
    val viewModel = ViewModel()

    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var mBinding: FragmentNLSIAInfrastructureSheepGoatBinding?=null
    private var savedAsEdit:Boolean=false
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()

        if(viewEdit=="view"){
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etFrozenSemenNumber?.isEnabled=false
            mBinding?.etFrozenSemenLocation?.isEnabled=false
            mBinding?.etLiquidSemenNumber?.isEnabled=false
            mBinding?.etLiquidSemenLocation?.isEnabled=false
            mBinding?.etProductionCapacityNumber?.isEnabled=false
            mBinding?.etProductionCapacityLocation?.isEnabled=false
            mBinding?.etActualProductionNumber?.isEnabled=false
            mBinding?.etActualProductionLocation?.isEnabled=false
            mBinding?.etNoOfDosesOfSemenNumber?.isEnabled=false
            mBinding?.etNoOfDosesOfSemenLocation?.isEnabled=false
            mBinding?.etNoOfDosesOfSemenNeighbouringNumber?.isEnabled=false
            mBinding?.etNoOfDosesOfSemenNeighbouringnLocation?.isEnabled=false
            mBinding?.etAvailabilityOfLiquidNitrogenNumber?.isEnabled=false
            mBinding?.etAvailabilityOfLiquidNitrogenLocation?.isEnabled=false
            mBinding?.etBreedingFramsNumber?.isEnabled=false
            mBinding?.etBreedingFramsLocation?.isEnabled=false
            mBinding?.etGoatBreedingFramsNumber?.isEnabled=false
            mBinding?.etGoatBreedingFramsLocation?.isEnabled=false
            mBinding?.etSheepBreedingFramsNumber?.isEnabled=false
            mBinding?.etSheepBreedingFramsLocation?.isEnabled=false
            mBinding?.etTrainingCentersNumber?.isEnabled=false
            mBinding?.etTrainingCentersLocation?.isEnabled=false
            mBinding?.etCatelAiNumber?.isEnabled=false
            mBinding?.etCatelAiLocation?.isEnabled=false
            mBinding?.etTotalAiNumber?.isEnabled=false
            mBinding?.etTotalAiLocation?.isEnabled=false
            mBinding?.etTotalNoOfReginalSemenBank?.isEnabled=false

            ViewEditApi(viewEdit)
        }
        else if(viewEdit=="edit"){
            ViewEditApi(viewEdit)
        }
        else{
            ViewEditApi("edit")
        }


    }
    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            } else {
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    if (userResponseModel.statuscode==202)
                    {
                        activity?.supportFragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        // Notify listener to go back to the first fragment and change the tab
                        listener?.onNavigateToFirstFragment()
                    }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
                else{
                    if (savedAsDraft)
                    {
                        Utility.clearAllFormFilledID(requireContext())

//                        savedAsDraftClick?.onSaveAsDraft()
                        startActivity(Intent(requireContext(),NationalLiveStockMissionIAList::class.java))

                    }else
                    {
                        if (viewEdit == "view" || viewEdit == "edit" || viewEdit=="add") {
                            if (savedAsEdit)
                            {
                                listener?.onNextButtonClick()
                            }
                            else{
                            mBinding?.etFrozenSemenNumber?.setText(userResponseModel._result.frozen_semen_goat_number?.toString() ?: "")
                            mBinding?.etFrozenSemenLocation?.setText(userResponseModel._result.frozen_semen_goat_location ?: "")
                            mBinding?.etLiquidSemenNumber?.setText(userResponseModel._result.liquid_semen_sheep_number?.toString() ?: "")
                            mBinding?.etLiquidSemenLocation?.setText(userResponseModel._result.liquid_semen_sheep_location ?: "")
                            mBinding?.etProductionCapacityNumber?.setText(userResponseModel._result.production_capacity_number?.toString() ?: "")
                            mBinding?.etProductionCapacityLocation?.setText(userResponseModel._result.production_capacity_location ?: "")
                            mBinding?.etActualProductionNumber?.setText(userResponseModel._result.actual_production_number?.toString() ?: "")
                            mBinding?.etActualProductionLocation?.setText(userResponseModel._result.actual_production_location ?: "")
                            mBinding?.etNoOfDosesOfSemenNumber?.setText(userResponseModel._result.no_semen_distributed_number?.toString() ?: "")
                            mBinding?.etNoOfDosesOfSemenLocation?.setText(userResponseModel._result.no_semen_distributed_location ?: "")
                            mBinding?.etNoOfDosesOfSemenNeighbouringNumber?.setText(userResponseModel._result.no_semen_neighboring_number?.toString() ?: "")
                            mBinding?.etNoOfDosesOfSemenNeighbouringnLocation?.setText(userResponseModel._result.no_semen_neighboring_location ?: "")
                            mBinding?.etAvailabilityOfLiquidNitrogenNumber?.setText(userResponseModel._result.availability_liquid_nitrogen_number?.toString() ?: "")
                            mBinding?.etAvailabilityOfLiquidNitrogenLocation?.setText(userResponseModel._result.availability_liquid_nitrogen_location ?: "")
                                mBinding?.etTotalNoOfReginalSemenBank?.setText(userResponseModel._result.total_reginal_semen_bank?.toString() ?: "")
                                mBinding?.etBreedingFramsNumber?.setText(userResponseModel._result.breeding_farms_number?.toString() ?: "")
                                mBinding?.etBreedingFramsLocation?.setText(userResponseModel._result.breeding_farms_location ?: "")
                                mBinding?.etGoatBreedingFramsNumber?.setText(userResponseModel._result.goat_breeding_farm_number?.toString() ?: "")
                                mBinding?.etGoatBreedingFramsLocation?.setText(userResponseModel._result.goat_breeding_farm_location ?: "")
                                mBinding?.etSheepBreedingFramsNumber?.setText(userResponseModel._result.sheep_breeding_farms_number?.toString() ?: "")
                                mBinding?.etSheepBreedingFramsLocation?.setText(userResponseModel._result.sheep_breeding_farms_location ?: "")
                                mBinding?.etTrainingCentersNumber?.setText(userResponseModel._result.training_centers_number?.toString() ?: "")
                                mBinding?.etTrainingCentersLocation?.setText(userResponseModel._result.training_centers_location ?: "")
                                mBinding?.etCatelAiNumber?.setText(userResponseModel._result.number_of_cattle_ai_number?.toString() ?: "")
                                mBinding?.etCatelAiLocation?.setText(userResponseModel._result.number_of_cattle_ai_location ?: "")
                                mBinding?.etTotalAiNumber?.setText(userResponseModel._result.total_ai_performed_number?.toString() ?: "")
                                mBinding?.etTotalAiLocation?.setText(userResponseModel._result.total_ai_performed_location ?: "")

                            }}
                        else{
                            Utility.clearAllFormFilledID(requireContext())
                            listener?.onNextButtonClick()
                        }
                        if(viewEdit!="add")
                        {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)}
                }}
            }
        }}
    }
    inner class ClickActions {
        fun save(view: View){
            savedAsEdit=true
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
            else{
                if (viewEdit=="edit")
                {
                    savedAsEdit=true
                }
                saveDataApi()
            }
        }
        fun saveAsDraft(view: View){
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

            savedAsDraft=true}
        }
    }
    private fun ViewEditApi(viewEdit: String?){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part2",
                id = itemId,
                state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick=null
    }
    private  fun saveDataApi(){
        viewModel.getImplementingAgencyAddApi(
            requireContext(), true,
            ImplementingAgencyAddRequest(
                part = "part2",
                frozen_semen_goat_number = mBinding?.etFrozenSemenNumber?.text.toString()
                    .toIntOrNull(),
                frozen_semen_goat_location = mBinding?.etFrozenSemenLocation?.text.toString(),
                liquid_semen_sheep_number = mBinding?.etLiquidSemenNumber?.text.toString()
                    .toIntOrNull(),
                liquid_semen_sheep_location = mBinding?.etLiquidSemenLocation?.text.toString(),
                production_capacity_number = mBinding?.etProductionCapacityNumber?.text.toString()
                    .toIntOrNull(),
                production_capacity_location = mBinding?.etProductionCapacityLocation?.text.toString(),
                actual_production_number = mBinding?.etActualProductionNumber?.text.toString()
                    .toIntOrNull(),
                actual_production_location = mBinding?.etActualProductionLocation?.text.toString(),
                no_semen_distributed_number = mBinding?.etNoOfDosesOfSemenNumber?.text.toString()
                    .toIntOrNull(),
                no_semen_distributed_location = mBinding?.etNoOfDosesOfSemenLocation?.text.toString(),
                no_semen_neighboring_number = mBinding?.etNoOfDosesOfSemenNeighbouringNumber?.text.toString()
                    .toIntOrNull(),
                no_semen_neighboring_location = mBinding?.etNoOfDosesOfSemenNeighbouringnLocation?.text.toString(),
                availability_liquid_nitrogen_number = mBinding?.etAvailabilityOfLiquidNitrogenNumber?.text.toString()
                    .toIntOrNull(),
                availability_liquid_nitrogen_location = mBinding?.etAvailabilityOfLiquidNitrogenLocation?.text.toString(),
                breeding_farms_number = mBinding?.etBreedingFramsNumber?.text.toString()
                    .toIntOrNull(),
                breeding_farms_location = mBinding?.etBreedingFramsLocation?.text.toString(),
                goat_breeding_farm_number = mBinding?.etGoatBreedingFramsNumber?.text.toString()
                    .toIntOrNull(),
                goat_breeding_farm_location = mBinding?.etGoatBreedingFramsLocation?.text.toString(),
                sheep_breeding_farms_number = mBinding?.etSheepBreedingFramsNumber?.text.toString().toIntOrNull(),
                sheep_breeding_farms_location = mBinding?.etSheepBreedingFramsLocation?.text.toString(),
                training_centers_number = mBinding?.etTrainingCentersNumber?.text.toString()
                    .toIntOrNull(),
                training_centers_location = mBinding?.etTrainingCentersLocation?.text.toString(),
                number_of_cattle_ai_number = mBinding?.etCatelAiNumber?.text.toString()
                    .toIntOrNull(),
                number_of_cattle_ai_location = mBinding?.etCatelAiLocation?.text.toString(),
                total_ai_performed_number = mBinding?.etTotalAiNumber?.text.toString()
                    .toIntOrNull(),
                total_ai_performed_location = mBinding?.etTotalAiLocation?.text.toString(),
                total_reginal_semen_bank = mBinding?.etTotalNoOfReginalSemenBank?.text.toString().toIntOrNull(),
                user_id = getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                is_deleted = 0,
                id = itemId,
                is_draft = 1

            )

        )
    }
}