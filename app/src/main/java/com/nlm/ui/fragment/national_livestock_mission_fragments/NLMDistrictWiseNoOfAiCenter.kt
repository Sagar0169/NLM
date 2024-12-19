package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemNLMDistrictWiseListEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding
import com.nlm.databinding.ItemDistrictWiseNoNlsiaBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyInvolvedDistrictWise
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.NlmIADistrictWiseNoAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreference
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.rotateDrawable
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLMDistrictWiseNoOfAiCenter(private val viewEdit: String?,private val itemId:Int?):
    BaseFragment<FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding>(),CallBackItemNLMDistrictWiseListEdit,
    CallBackDeleteAtId {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__agencies_involved_in_genetic_improvement_goat_sheep
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var currentPage = 1
    private var totalPage = 1
    private var stateList = ArrayList<ResultGetDropDown>()
    private var savedAsDraft:Boolean=false
    private var savedAsEdit:Boolean=false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var Model:String? = null // Store selected state
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
        if (viewEdit=="view")
        {mBinding?.tvAddMore1?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etNoOfAiTechnician?.isEnabled=false
            mBinding?.etNumberOfAiTechnicianTrained?.isEnabled=false
            mBinding?.etTotalNoOfParavetTrained?.isEnabled=false

            ViewEditApi()
        }
        else if (viewEdit=="edit"){
            ViewEditApi()

        }
        NlmIADistrictWiseNoAdapterFun()
    }
   private fun NlmIADistrictWiseNoAdapterFun() {
       mBinding?.recyclerViewDistrictWiseOfAi?.layoutManager = LinearLayoutManager(requireContext())
       mNlmIADistrictWiseNoList = mutableListOf()
       mNlmIADistrictWiseNoAdapter = NlmIADistrictWiseNoAdapter  (requireContext(),mNlmIADistrictWiseNoList,viewEdit,this,this)
       mBinding?.recyclerViewDistrictWiseOfAi?.adapter = mNlmIADistrictWiseNoAdapter
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
                            else{
                            userResponseModel._result.no_of_al_technicians?.toString().let { it1 ->
                                mBinding?.etNoOfAiTechnician?.setText(
                                    it1
                                )
                            }
                            userResponseModel._result.number_of_ai?.toString().let { it1 ->
                                mBinding?.etNumberOfAiTechnicianTrained?.setText(
                                    it1
                                )
                            }
                            userResponseModel._result.total_paravet_trained?.toString().let { it1 ->
                                mBinding?.etTotalNoOfParavetTrained?.setText(
                                    it1
                                )
                            }
                                mBinding?.etPresentSystem?.setText(userResponseModel._result.present_system.toString())
                            mNlmIADistrictWiseNoList.clear()
                                if (userResponseModel._result.implementing_agency_involved_district_wise.isNullOrEmpty()) {
                                    mNlmIADistrictWiseNoList.add(
                                        ImplementingAgencyInvolvedDistrictWise(
                                            name_of_district = "",
                                            location_of_ai_centre = "",
                                            ai_performed = "",
                                            id = null,
                                            year = null,
                                            implementing_agency_id = null
                                        )
                                    )
                                } else {
                                    mNlmIADistrictWiseNoList.addAll(userResponseModel._result.implementing_agency_involved_district_wise)
                                }
                                mNlmIADistrictWiseNoAdapter.notifyDataSetChanged()

                        }}
                       else if (viewEdit=="edit")
                        {
                            if (savedAsEdit)
                            {
                                listener?.onNextButtonClick()
                            }
                            else{
                                userResponseModel._result.no_of_al_technicians?.toString().let { it1 ->
                                    mBinding?.etNoOfAiTechnician?.setText(
                                        it1
                                    )
                                }
                                userResponseModel._result.number_of_ai?.toString().let { it1 ->
                                    mBinding?.etNumberOfAiTechnicianTrained?.setText(
                                        it1
                                    )
                                }
                                userResponseModel._result.total_paravet_trained?.toString().let { it1 ->
                                    mBinding?.etTotalNoOfParavetTrained?.setText(
                                        it1
                                    )
                                }
                                mNlmIADistrictWiseNoList.clear()

                                userResponseModel._result.implementing_agency_involved_district_wise?.let { it1 ->
                                    mNlmIADistrictWiseNoList.addAll(
                                        it1
                                    )
                                }

                                mNlmIADistrictWiseNoAdapter.notifyDataSetChanged()

                            }}
                        else{
                    listener?.onNextButtonClick()
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }}}
            }}
        }
        viewModel.getDropDownResult.observe(viewLifecycleOwner) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateList.clear()

                        val remainingCount = userResponseModel.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 10
                            count
                        } else {
                            val count = userResponseModel.total_count / 10
                            count + 1
                        }
                    }
                    stateList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()

//                    mBinding?.tvNoDataFound?.hideView()
//                    mBinding?.rvArtificialInsemination?.showView()
                } else {
//                    mBinding?.tvNoDataFound?.showView()
//                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }
    }

    inner class ClickActions {
      fun AddDistrictWiseNoAiDialog(view: View){
          compositionOfGoverningNlmIaDialog(requireContext(),null,null)
      }
        fun saveAndNext(view: View) {
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
        fun saveAsDraft(view: View) {
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
        val selectedList: List<ResultGetDropDown>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {
            "District" -> {
                dropDownApiCallDistrict(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                Model="Districts"
                selectedTextView = textView
            }

            else -> return
        }

        // Set up the adapter for the bottom sheet
         stateAdapter = BottomSheetAdapter(requireContext(), selectedList) { selectedItem, id ->
                    // Handle item click
                    selectedTextView.text = selectedItem



                        districtName = selectedItem
                        districtId = id  // Save the selected district ID


                    if (Model=="Districts")
                    {
                        districtName=selectedItem
                        districtId=id
                    }
                    else{
                        stateId = id
                    }
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
    private fun compositionOfGoverningNlmIaDialog(context: Context, selectedItem: ImplementingAgencyInvolvedDistrictWise?, position: Int?) {
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
        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
        lp.dimAmount = 0.5f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.showView()
        bindingDialog.etState.setOnClickListener { showBottomSheetDialog("District",bindingDialog.etState) }
        if(selectedItem!=null)
        {
            bindingDialog.etState.text = selectedItem.name_of_district
            bindingDialog.etAiPerformed.setText(selectedItem.ai_performed)
            bindingDialog.etLocationOfAi.setText(selectedItem.location_of_ai_centre)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etState.text.toString().isNotEmpty()||bindingDialog.etAiPerformed.text.toString().isNotEmpty()||bindingDialog.etLocationOfAi.text.toString().isNotEmpty())
            {
                if(selectedItem!=null)
                {
                    if (position != null) {
                        mNlmIADistrictWiseNoList.set(position,
                            ImplementingAgencyInvolvedDistrictWise(
                               name_of_district = districtName,
                                location_of_ai_centre = bindingDialog.etLocationOfAi.text.toString(),
                               ai_performed =  bindingDialog.etAiPerformed.text.toString(),
                                selectedItem.id,
                                selectedItem.year,
                                selectedItem.implementing_agency_id,
                            )
                        )
                        mNlmIADistrictWiseNoAdapter.notifyItemChanged(position)

                    }                    }
                    else{
                    mNlmIADistrictWiseNoList.add(
                        ImplementingAgencyInvolvedDistrictWise(
                            districtName,
                            bindingDialog.etLocationOfAi.text.toString(),
                            bindingDialog.etAiPerformed.text.toString(),
                            null,
                            null,
                            null,
                        )
                    )
                    mNlmIADistrictWiseNoList.size.minus(1).let {
                        mNlmIADistrictWiseNoAdapter.notifyItemInserted(it)
                    }}
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
    private fun ViewEditApi(){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part5",
                id = itemId,
                state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }
   private fun saveDataApi(){
       viewModel.getImplementingAgencyAddApi(
           context = requireContext(), loader = true,
           request = ImplementingAgencyAddRequest(
               part = "part5",
               no_of_al_technicians = mBinding?.etNoOfAiTechnician?.text.toString().toIntOrNull(),
               present_system = mBinding?.etPresentSystem?.text.toString(),
               number_of_ai = mBinding?.etNumberOfAiTechnicianTrained?.text.toString().toIntOrNull(),
               total_paravet_trained = mBinding?.etTotalNoOfParavetTrained?.text.toString().toIntOrNull(),
               implementing_agency_involved_district_wise = mNlmIADistrictWiseNoList,
               user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
               implementing_agency_document = null,
               is_deleted = 0,
               id =itemId,
               is_draft = 1,
           )
       )
   }

    override fun onClickItem(selectedItem: ImplementingAgencyInvolvedDistrictWise, position: Int) {
        compositionOfGoverningNlmIaDialog(requireContext(),selectedItem,position)
    }

    private fun dropDownApiCallDistrict(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            requireContext(), loader, GetDropDownRequest(
                20,
                "Districts",
                currentPage,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> mNlmIADistrictWiseNoAdapter.onDeleteButtonClick(it1) }
        }
        else if(isFrom==2){
            position.let { it1 -> mNlmIADistrictWiseNoAdapter.onDeleteButtonClick(it1) }
        }


    }
}