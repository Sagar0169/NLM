package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Gravity
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
import com.nlm.databinding.ActivityArtificialInseminationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
import com.nlm.model.ArtificialInsemenNationAddRequest
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.DocumentData
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.ObservationAIAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ArtificialInseminationForms : BaseActivity<ActivityArtificialInseminationBinding>(){
    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination
    private var mBinding: ActivityArtificialInseminationBinding? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var mObservationAIAdapter: ObservationAIAdapter
    private var DocumentName:String?=null
    private var currentPage = 1
    private var totalPage = 1
    private lateinit var stateAdapter: BottomSheetAdapter
    private var districtList = ArrayList<ResultGetDropDown>()
    val viewModel = ViewModel()
    var body: MultipartBody.Part? = null
    private lateinit var programmeList: MutableList<Array<String>>
    private lateinit var DocumentList: MutableList<DocumentData>
    private lateinit var ObservationBynlmList: MutableList<ArtificialInseminationObservationByNlm>
    private var DialogDocName:TextView?=null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private var districtId: Int? = null // Store selected state
    private var TotalNoOfGoat: String? = null // Store selected state

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        ObservationBynlmList = mutableListOf()
        ObservationAIAdapter()
        DocumentList = mutableListOf()
        AddDocumentAdapter()
        mBinding?.etState?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.etState?.isEnabled=false

        if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
        {
            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etStateIa?.isEnabled=false
            TotalNoOfGoat=mBinding?.etTotalNoOfSheepIa?.text.toString()
          mBinding?.llObservationByNlm?.hideView()
        }
        else{
            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etStateIa?.isEnabled=false
            mBinding?.etDistrictIa?.isEnabled=false
            TotalNoOfGoat=mBinding?.etTotalNoOfSheep?.text.toString()
            mBinding?.etTotalNoOfSheepIa?.isEnabled=false
            mBinding?.etLiquidNitrogen?.isEnabled=false
            mBinding?.etFrozenSemenStraws?.isEnabled=false
            mBinding?.etCryocans?.isEnabled=false

        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtList.clear()

                        val remainingCount = userResponseModel.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 10
                            count
                        } else {
                            val count = userResponseModel.total_count / 10
                            count + 1
                        }
                    }
                    districtList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()

//                    mBinding?.tvNoDataFound?.hideView()
//                    mBinding?.rvArtificialInsemination?.showView()
                } else {
//                    mBinding?.tvNoDataFound?.showView()
//                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }
        viewModel.artificialInseminationAddResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }
                else{
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }
//                else{
//                    if (savedAsDraft)
//                    {
//                        savedAsDraftClick?.onSaveAsDraft()
//                    }
//                    else{
//                        Preferences.setPreference_int(requireContext(),AppConstants.FORM_FILLED_ID,userResponseModel._result.id)
//                        listener?.onNextButtonClick()
//                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
//                    }
//
//                }
            }
        }
    }

    inner class ClickActions {
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun addDocDialog(view: View){
            AddDocumentDialog(this@ArtificialInseminationForms)
        }
        fun addMore(view:View){
            mObservationbynlmDialog(this@ArtificialInseminationForms,1)
        }
        fun saveAsDraft(view: View){}
        fun saveAndNext(view: View){
            if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
            {

            viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                ArtificialInsemenNationAddRequest(
                    state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                    district_code = districtId,
                    user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                    artificial_insemination_observation_by_nlm = ObservationBynlmList,
                    artificial_insemination_document = null,
                    is_deleted = 0,
                    is_draft_nlm = null,
                    total_sheep_goat_labs =TotalNoOfGoat?.toIntOrNull(),
                    liquid_nitrogen = mBinding?.etLiquidNitrogen?.text.toString(),
                    frozen_semen_straws = mBinding?.etFrozenSemenStraws?.text.toString(),
                    cryocans = mBinding?.etCryocans?.text.toString(),
                    exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                    role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                )
            )}
            else{


            viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                ArtificialInsemenNationAddRequest(
                    state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                    district_code = districtId,
                    user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                    artificial_insemination_observation_by_nlm = ObservationBynlmList,
                    artificial_insemination_document = null,
                    is_deleted = 0,
                    is_draft_nlm = null,
                    total_sheep_goat_labs =TotalNoOfGoat?.toIntOrNull(),
                    liquid_nitrogen = null,
                    frozen_semen_straws = null,
                    cryocans = null,
                    exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                    role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                )
            )}
        }
    }
    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                20,
                "Districts",
                currentPage,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
    }
    private fun AddDocumentDialog(context: Context) {
        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_add_document_dialog,
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
        DialogDocName=bindingDialog.etDoc
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty())
            {

                DocumentList.add(DocumentData(bindingDialog.etDescription.text.toString(),DocumentName))

                DocumentList.size.minus(1).let {
                    AddDocumentAdapter?.notifyItemInserted(it)
                    dialog.dismiss()
//
                }
            }


            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }

    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

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


            "state" -> {
                selectedList = districtList
                selectedTextView = mBinding!!.etState
            }

            "district" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                {
                    selectedTextView = mBinding!!.etDistrictIa
                }
                else{
                    selectedTextView = mBinding!!.etDistrict
                }

            }



            else -> return
        }
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }
    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )
                        val cursor = contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName=
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text=DocumentName

                                val requestBody = convertToRequestBody(this, uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
//                            viewModel.getProfileUploadFile(
//                                context = this,
//                                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
//                                table_name = getString(R.string.implementing_agency_document).toRequestBody(
//                                    MultipartBody.FORM),
//                                null,
//                                ia_document = body
//                            )
                        }
                    }
                }
            }}
    }
    private fun ObservationAIAdapter() {
        ObservationBynlmList = mutableListOf()
        mObservationAIAdapter = ObservationAIAdapter(ObservationBynlmList)
        mBinding?.rvObservationByNlm?.adapter = mObservationAIAdapter
        mBinding?.rvObservationByNlm?.layoutManager =
            LinearLayoutManager(this)
    }

    private fun AddDocumentAdapter(){
        AddDocumentAdapter=SupportingDocumentAdapterWithDialog(DocumentList)
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun mObservationbynlmDialog(context: Context,isFrom:Int) {
        val bindingDialog: ItemAiObservationBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_ai_observation,
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
            if (bindingDialog.etNameOfTheCenter.text.toString().isNotEmpty()||bindingDialog.etNumberOFAiPerformed.text.toString().isNotEmpty()||bindingDialog.etWhetherManPowerTrainedForGoatAI.text.toString().isNotEmpty()||bindingDialog.etEquipmentAvailable.text.toString().isNotEmpty())
            {
                ObservationBynlmList.add(
                ArtificialInseminationObservationByNlm(
                    bindingDialog.etNameOfTheCenter.text.toString(),
                    bindingDialog.etNumberOFAiPerformed.text.toString().toInt(),
                    bindingDialog.etWhetherManPowerTrainedForGoatAI.text.toString(),
                    bindingDialog.etEquipmentAvailable.text.toString()
                )
            )

                ObservationBynlmList.size.minus(1).let {
                    mObservationAIAdapter.notifyItemInserted(it)
                    }
                    dialog.dismiss()
                }

            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
}