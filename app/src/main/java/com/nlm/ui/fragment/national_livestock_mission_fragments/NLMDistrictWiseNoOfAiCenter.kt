package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyInvolvedDistrictWise
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIADistrictWiseNoAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.rotateDrawable
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLMDistrictWiseNoOfAiCenter:
    BaseFragment<FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__agencies_involved_in_genetic_improvement_goat_sheep
    private lateinit var stateAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var savedAsDraft:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    val viewModel = ViewModel()
    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )
    private var listener: OnNextButtonClickListener? = null
    private var mBinding: FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding?=null

    private lateinit var mNlmIADistrictWiseNoAdapter: NlmIADistrictWiseNoAdapter
    private lateinit var mNlmIADistrictWiseNoList: MutableList<ImplementingAgencyInvolvedDistrictWise>
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        NlmIADistrictWiseNoAdapterFun()
    }
   private fun NlmIADistrictWiseNoAdapterFun() {
       mBinding?.recyclerViewDistrictWiseOfAi?.layoutManager = LinearLayoutManager(requireContext())
       mNlmIADistrictWiseNoList = mutableListOf()
       mNlmIADistrictWiseNoAdapter = NlmIADistrictWiseNoAdapter  (mNlmIADistrictWiseNoList)
       mBinding?.recyclerViewDistrictWiseOfAi?.adapter = mNlmIADistrictWiseNoAdapter
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
                    if (savedAsDraft)
                    {
                        savedAsDraftClick?.onSaveAsDraft()
                    }else
                    {
                    listener?.onNextButtonClick()
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }}
            }
        }
    }

    inner class ClickActions {
      fun AddDistrictWiseNoAiDialog(view: View){
          compositionOfGoverningNlmIaDialog(requireContext())
      }
        fun saveAndNext(view: View) {
            viewModel.getImplementingAgencyAddApi(
                context = requireContext(), loader = true,
                request = ImplementingAgencyAddRequest(
                    part = "part5",
                    no_of_al_technicians = mBinding?.etNoOfAiTechnician?.text.toString().toIntOrNull(),
                    number_of_ai = mBinding?.etNumberOfAiTechnicianTrained?.text.toString().toIntOrNull(),
                    total_paravet_trained = mBinding?.etTotalNoOfParavetTrained?.text.toString().toIntOrNull(),
                    implementing_agency_involved_district_wise = mNlmIADistrictWiseNoList,
                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    implementing_agency_document = null,
                    is_deleted = 0,
                    id =Preferences.getPreference_int(requireContext(),AppConstants.FORM_FILLED_ID),
                )
            )
        }
        fun saveAsDraft(view: View) {
            viewModel.getImplementingAgencyAddApi(
                context = requireContext(), loader = true,
                request = ImplementingAgencyAddRequest(
                    part = "part5",
                    no_of_al_technicians = mBinding?.etNoOfAiTechnician?.text.toString().toIntOrNull(),
                    number_of_ai = mBinding?.etNumberOfAiTechnicianTrained?.text.toString().toIntOrNull(),
                    total_paravet_trained = mBinding?.etTotalNoOfParavetTrained?.text.toString().toIntOrNull(),
                    implementing_agency_involved_district_wise = mNlmIADistrictWiseNoList,
                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    implementing_agency_document = null,
                    is_deleted = 0,
                    id =Preferences.getPreference_int(requireContext(),AppConstants.FORM_FILLED_ID),
                    is_draft = 1
                )
            )
            savedAsDraft=true
        }
    }
    private fun showBottomSheetDialog(type: String,textView: TextView) {
        // Initialize the BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(requireContext())

        // Use LayoutInflater.from(context) to get the layout inflater
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set up RecyclerView and Close button in the bottom sheet
        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Define a variable for the selected list and TextView
        val selectedList: List<String>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {
            "district" -> {
                selectedList = district
                selectedTextView = textView
            }

            else -> return
        }

        // Set up the adapter for the bottom sheet
        stateAdapter = StateAdapter(selectedList, requireContext()) { selectedItem ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter

        bottomSheetDialog.setContentView(view)

        // Rotate drawable when the bottom sheet is shown
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Reset drawable when the bottom sheet is dismissed
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
    }
    private fun compositionOfGoverningNlmIaDialog(context: Context) {
        val bindingDialog: ItemDistrictWiseNoNlsiaBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_district_wise_no_nlsia,
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
            if (bindingDialog.etState.text.toString().isNotEmpty()||bindingDialog.etAiPerformed.text.toString().isNotEmpty()||bindingDialog.etLocationOfAi.text.toString().isNotEmpty())
            {

                    mNlmIADistrictWiseNoList.add(
                        ImplementingAgencyInvolvedDistrictWise(
                            null,
                            bindingDialog.etLocationOfAi.text.toString(),
                            bindingDialog.etAiPerformed.text.toString(),
                            null,
                            null,
                        )
                    )
                    mNlmIADistrictWiseNoList.size.minus(1).let {
                        mNlmIADistrictWiseNoAdapter.notifyItemInserted(it)
                    }
                    dialog.dismiss()


            }
            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
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
}