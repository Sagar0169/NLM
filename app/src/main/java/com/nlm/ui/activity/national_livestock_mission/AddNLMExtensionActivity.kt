package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackAssistanceEANlm
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.databinding.ActivityAddNlmextensionActivitiesBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemNlmTrainingInstituteBinding
import com.nlm.model.AddAssistanceEARequest
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.AssistanceEAAdapter
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNLMExtensionActivity(
) : BaseActivity<ActivityAddNlmextensionActivitiesBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackAssistanceEANlm, CallBackDeleteFSPAtId {
    private var mBinding: ActivityAddNlmextensionActivitiesBinding? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var addDocumentIAAdapter: RSPSupportingDocumentIAAdapter? = null

    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var isFrom: Int = 0
    private var img: Int = 0
    private var viewModel = ViewModel()
    private var districtId: Int? = null // Store selected state
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null
    private lateinit var ea_training_institute: ArrayList<AssistanceForEaTrainingInstitute>
    private var assistanceEAAdapter
            : AssistanceEAAdapter? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false


    private val variety = listOf(
        ResultGetDropDown(0, "Class Wise"),
        ResultGetDropDown(0, "Variety Wise")
    )

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
//            showBottomSheetDialog("state")
        }

        fun quality(view: View) {
//            showBottomSheetDialog("Variety")
        }

        fun chooseFile(view: View) {
            openOnlyPdfAccordingToPosition()
            toast("Hi")
            img = 1
        }

        fun district(view: View) {
//            showBottomSh/eetDialog("District")
        }

        fun districtNLM(view: View) {
//            showBottomSheetDialog("DistrictNLM")
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddNLMExtensionActivity, null, null)
            img = 0
        }

        fun trainingInstitute(view: View) {
            trainingInstitute(this@AddNLMExtensionActivity, 1, null, null)
        }

        fun save(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            isSubmitted = true
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 0)
            } else {
                saveDataApi(null, 0)
            }


        }


        fun saveAsDraft(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 1)
            } else {
                saveDataApi(null, 1)
            }
            savedAsDraft = true
        }

    }

    override val layoutId: Int
        get() = R.layout.activity_add_nlmextension_activities

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        ea_training_institute = arrayListOf()
        isFrom = intent?.getIntExtra("isFrom", 0)!!
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false

        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            mBinding?.tvIADoc?.hideView()
            nlmAdapter()
        }
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {

            mBinding?.tvNLMDoc?.hideView()
            mBinding?.llNLM?.hideView()
            iaAdapter()
        }




        if (viewEdit == "view" || (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
                    )
            ||
            (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
                    )
        ) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                mBinding?.tvState?.isEnabled = false
                mBinding?.etNoa?.isEnabled = false
                mBinding?.etParticipant?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
            }
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24
            ) {
                mBinding?.etEOA?.isEnabled = false
                mBinding?.etParticipateNlm?.isEnabled = false

                mBinding?.tvNLMComment?.isEnabled = false
                mBinding?.tvNLMComment?.hideView()
                mBinding?.tvNLMDoc?.hideView()
            }
            if (viewEdit == "view") {
                viewEditApi()
                mBinding?.tvState?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
                mBinding?.etEOA?.isEnabled = false
                mBinding?.etParticipateNlm?.isEnabled = false
                mBinding?.etNoa?.isEnabled = false
                mBinding?.etModule?.isEnabled = false
                mBinding?.etTrainer?.isEnabled = false
                mBinding?.etDetails?.isEnabled = false
                mBinding?.etParticipant?.isEnabled = false
                mBinding?.tvNLMComment?.isEnabled = false
                mBinding?.tvNLMComment?.hideView()
                mBinding?.tvNLMDoc?.hideView()
                mBinding?.tvSaveDraft?.hideView()
                mBinding?.tvSendOtp?.hideView()
            }
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
        assistanceEAAdapter()
    }

    private fun viewEditApi() {

        viewModel.getAssistanceForEaADD(
            this, true,
            AddAssistanceEARequest(
                id = itemId,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                district_code = dId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit
            )
        )
    }

    private fun assistanceEAAdapter() {
        assistanceEAAdapter =
            AssistanceEAAdapter(
                this@AddNLMExtensionActivity,
                ea_training_institute,
                viewEdit,
                this@AddNLMExtensionActivity,
                this
            )
        mBinding?.recyclerView1?.adapter = assistanceEAAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(this@AddNLMExtensionActivity)
    }

    private fun nlmAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(this)
    }

    private fun iaAdapter() {
        addDocumentIAAdapter = RSPSupportingDocumentIAAdapter(
            this,
            viewDocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView?.adapter = addDocumentIAAdapter
        mBinding?.recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
        val state = mBinding?.tvState?.text.toString()
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {

            if (mBinding?.etEOA?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it, "No of camps is required") }
                return
            }
            if (mBinding?.etParticipateNlm?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "No. of participants trained is required"
                    )
                }
                return
            }
            if (mBinding?.etModule?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Whether the state has developed Training Materials/ Module is required"
                    )
                }
                return
            }
            if (mBinding?.etTrainer?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Whether the state has master trainer is required"
                    )
                }
                return
            }
            if (mBinding?.etDetails?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Details of training programmes? If so, give details"
                    )
                }
                return
            }


        } else {
            if (state == "Please Select") {
                mBinding?.clParent?.let { showSnackbar(it, "State Name is required") }
                return
            }
            if (mBinding?.etNoa?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it, "No of camps is required") }
                return
            }
            if (mBinding?.etParticipant?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "No. of participants trained is required"
                    )
                }
                return
            }


        }
        viewModel.getAssistanceForEaADD(
            this@AddNLMExtensionActivity, true,
            AddAssistanceEARequest(
                id = itemId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                is_draft = draft,
                no_of_camps =
                if (mBinding?.etNoa?.text.isNullOrEmpty()) {
                    mBinding?.etEOA?.text.toString().toIntOrNull()
                } else {
                    mBinding?.etNoa?.text.toString().toIntOrNull()
                },
                no_of_participants =
                if (mBinding?.etParticipateNlm?.text.isNullOrEmpty()) {
                    mBinding?.etParticipant?.text.toString().toIntOrNull()
                } else {
                    mBinding?.etParticipateNlm?.text.toString().toIntOrNull()
                },
                whether_the_state_developed = mBinding?.etModule?.text.toString(),
                whether_the_state_trainers = mBinding?.etTrainer?.text.toString(),
                details_of_training_programmes = mBinding?.etDetails?.text.toString(),
                assistance_for_ea_document = totalListDocument,
                assistance_for_ea_training_institute = ea_training_institute
            )
        )
    }


    private fun trainingInstitute(
        context: Context,
        isFrom: Int,
        selectedItem: AssistanceForEaTrainingInstitute?,
        position: Int?
    ) {
        val bindingDialog: ItemNlmTrainingInstituteBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_nlm_training_institute,
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
        bindingDialog.btnEdit.hideView()
        bindingDialog.tvSubmit.showView()
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etNameInstitute.setText(selectedItem.name_of_institute)
            bindingDialog.etAddress.setText(selectedItem.address_for_training)
            bindingDialog.etTraining.setText(selectedItem.training_courses_run)
            bindingDialog.etNoParticipants.setText(selectedItem.no_of_participants_trained.toString())
            bindingDialog.etNoProvide.setText(selectedItem.no_of_provide_information.toString())
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNameInstitute.text.toString().isNotEmpty()
                || bindingDialog.etAddress.text.toString().isNotEmpty()
                || bindingDialog.etTraining.text.toString().isNotEmpty()
                || bindingDialog.etNoParticipants.text.toString().isNotEmpty()
                || bindingDialog.etNoProvide.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        ea_training_institute[position] =
                            AssistanceForEaTrainingInstitute(
                                selectedItem.id,
                                bindingDialog.etNameInstitute.text.toString(),
                                bindingDialog.etAddress.text.toString(),
                                bindingDialog.etTraining.text.toString(),
                                if (bindingDialog.etNoParticipants.text.isNullOrEmpty()) null else bindingDialog.etNoParticipants.text.toString()
                                    .toIntOrNull(),
                                if (bindingDialog.etNoProvide.text.isNullOrEmpty()) null else bindingDialog.etNoProvide.text.toString()
                                    .toIntOrNull(),
                                selectedItem.assistance_for_ea_id
                            )
                        assistanceEAAdapter
                            ?.notifyItemChanged(position)
                    }

                } else {
                    ea_training_institute.add(
                        AssistanceForEaTrainingInstitute(
                            id = null,
                            name_of_institute = bindingDialog.etNameInstitute.text.toString(),
                            address_for_training = bindingDialog.etAddress.text.toString(),
                            training_courses_run = bindingDialog.etTraining.text.toString(),
                            no_of_participants_trained = if (bindingDialog.etNoParticipants.text.isNullOrEmpty()) null else bindingDialog.etNoParticipants.text.toString()
                                .toIntOrNull(),
                            no_of_provide_information = if (bindingDialog.etNoProvide.text.isNullOrEmpty()) null else bindingDialog.etNoProvide.text.toString()
                                .toIntOrNull(),
                            null
                        )
                    )

                    ea_training_institute.size.minus(1).let {
                        assistanceEAAdapter
                            ?.notifyItemInserted(it)
                    }
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


    private fun addDocumentDialog(
        context: Context,
        selectedItem: ImplementingAgencyDocument?,
        position: Int?
    ) {
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
        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
        lp.dimAmount = 0.5f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        DialogDocName = bindingDialog.etDoc

        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
                bindingDialog.etDescription.setText(selectedItem.description)
            } else {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document
                bindingDialog.etDescription.setText(selectedItem.description)
            }

        }
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }


        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString()
                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        if (getPreferenceOfScheme(
                                this,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id == 8
                        ) {
                            DocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    assistance_for_ea_id = selectedItem.assistance_for_ea_id,
                                    id = selectedItem.id,
                                )
                            addDocumentAdapter?.notifyItemChanged(position)

                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    assistance_for_ea_id = selectedItem.assistance_for_ea_id,
                                    id = selectedItem.id,
                                )
                            addDocumentIAAdapter?.notifyItemChanged(position)
                        }

                        dialog.dismiss()
                    }

                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                nlm_document = UploadedDocumentName,
                                id = null,
                                assistance_for_ea_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                id = null,
                                assistance_for_ea_id = null,
                                nlm_document = null
                            )
                        )
                        Log.d("Debug", "viewDocumentList: ${viewDocumentList.size}")

                    }

                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.size.minus(1).let {
                            addDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
//
                        }
                    } else {

                        viewDocumentList.size.minus(1).let {
                            addDocumentIAAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }
                        toast("else")
                    }
                }
            } else {
                showSnackbar(
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }
        dialog.show()
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
                        val cursor = this.contentResolver.query(
                            uri,
                            projection,
                            null,
                            null,
                            null
                        )
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text = DocumentName


                                val requestBody = convertToRequestBody(this, uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
                            viewModel.getProfileUploadFile(
                                context = this,
                                table_name = getString(R.string.assistance_for_ea_document).toRequestBody(
                                    MultipartBody.FORM
                                ),
                                document_name = body,
                                user_id = getPreferenceOfScheme(
                                    this,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id,
                            )
                        }
                    }
                }
            }
        }
    }


//    private fun showBottomSheetDialog(type: String) {
//        bottomSheetDialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
//        view.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
//        val close = view.findViewById<TextView>(R.id.tvClose)
//
//        close.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        // Define a variable for the selected list and TextView
//        val selectedList: List<ResultGetDropDown>
//        val selectedTextView: TextView
//
//        // Initialize based on type
//        when (type) {
////            "typeSemen" -> {
////                selectedList = typeSemen
////                selectedTextView = mBinding!!.tvSemenStation
////            }
////
////            "StateNDD" -> {
////                selectedList = stateList
////                selectedTextView = binding!!.tvStateNDD
////            }
////
////            "District" -> {
////                dropDownApiCall(paginate = false, loader = true)
////                selectedList = districtList
////                selectedTextView = mBinding!!.tvDistrict
////            }
//
////            "DistrictNLM" -> {
////                dropDownApiCall(paginate = false, loader = true)
////                selectedList = districtList
////                selectedTextView = mBinding!!.tvDistrictNlm
////            }
//
////            "Variety" -> {
////                img = 2
////                selectedList = variety
////                selectedTextView = mBinding!!.tvQuality
////            }
//
////            "Status" -> {
////                selectedList = status
////                selectedTextView = binding!!.tvStatus
////            }
////
////            "Reading" -> {
////                selectedList = reading
////                selectedTextView = binding!!.tvReadingMaterial
////            }
//
//            else -> return
//        }
//
//        // Set up the adapter
//        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
//            // Handle state item click
//            selectedTextView.text = selectedItem
//            districtId = id
//            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
//            bottomSheetDialog.dismiss()
//        }
//
//
//
//        layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rvBottomSheet.layoutManager = layoutManager
//        rvBottomSheet.adapter = stateAdapter
//        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
//        bottomSheetDialog.setContentView(view)
//
//
//        // Rotate drawable
//        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
//        var rotatedDrawable = rotateDrawable(drawable, 180f)
//        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)
//
//        // Set a dismiss listener to reset the view visibility
//        bottomSheetDialog.setOnDismissListener {
//            rotatedDrawable = rotateDrawable(drawable, 0f)
//            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
//                null,
//                null,
//                rotatedDrawable,
//                null
//            )
//        }
//
//        // Show the bottom sheet
//        bottomSheetDialog.show()
//    }

    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
    }

    private var recyclerScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount: Int? = layoutManager?.childCount
                    val totalItemCount: Int? = layoutManager?.itemCount
                    val pastVisiblesItems: Int? = layoutManager?.findFirstVisibleItemPosition()
                    if (loading) {
                        if ((visibleItemCount!! + pastVisiblesItems!!) >= totalItemCount!!) {
                            loading = false
                            if (currentPage < totalPage) {
                                //Call API here
                                dropDownApiCall(paginate = true, loader = true)
                            }
                        }
                    }
                }
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
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
    }

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddNLMExtensionActivity)
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
        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }

                } else {
                    DocumentId = userResponseModel._result.id
                    UploadedDocumentName = userResponseModel._result.document_name
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }

        viewModel.assistanceForEaADDResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            mBinding?.etNoa?.setText(userResponseModel._result?.no_of_camps.toString())
                            mBinding?.etParticipant?.setText(userResponseModel._result?.no_of_participants.toString())
                            mBinding?.etEOA?.setText(userResponseModel._result?.no_of_camps.toString())
                            mBinding?.etParticipateNlm?.setText(userResponseModel._result?.no_of_participants.toString())
                            mBinding?.etModule?.setText(userResponseModel._result?.whether_the_state_developed)
                            mBinding?.etTrainer?.setText(userResponseModel._result?.whether_the_state_trainers)
                            mBinding?.etDetails?.setText(userResponseModel._result?.details_of_training_programmes)

                            ea_training_institute.clear()
                            val comments =
                                userResponseModel._result?.assistance_for_ea_training_institute
                                    ?: emptyList()

                            if (comments.isEmpty() && viewEdit == "view") {
                                val dummyData = AssistanceForEaTrainingInstitute(
                                    id = 0,
                                    name_of_institute = "",
                                    address_for_training = "",
                                    training_courses_run = "",
                                    null, null
                                )
                                ea_training_institute.add(dummyData)
                            } else {
                                ea_training_institute.addAll(comments)
                            }


                            assistanceEAAdapter?.notifyDataSetChanged()
                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()
                            val dummyData = ImplementingAgencyDocument(
                                id = 0, // Or null, depending on your use case
                                description = "",
                                ia_document = "",
                                nlm_document = "",
                                assistance_for_ea_id = 0 // Or null, depending on your use case
                            )
                            if (userResponseModel._result?.assistance_for_ea_document?.isEmpty() == true && viewEdit == "view") {
                                // Add dummy data with default values

//
//                                DocumentList.add(dummyData)
//                                viewDocumentList.add(dummyData)

                            } else {
                                userResponseModel._result?.assistance_for_ea_document?.forEach { document ->
                                    if (document.ia_document == null) {
                                        DocumentList.add(document)//nlm
                                    } else {
                                        viewDocumentList.add(document)//ia

                                    }
                                }
                                // Check if viewDocumentList is empty after the loop
                                if (viewDocumentList.isEmpty() && viewEdit == "view") {
//                                    viewDocumentList.add(dummyData)
                                }
                                if (DocumentList.isEmpty() && viewEdit == "view") {
//                                    DocumentList.add(dummyData)
                                }
                            }

//                            if(getPreferenceOfScheme(context, AppConstants.SCHEME, Result::class.java)?.role_id==8){
                            nlmAdapter()
                            iaAdapter()
//                            }
//                            else{
//                                iaAdapter()
//                            }
                            addDocumentAdapter?.notifyDataSetChanged()
                            addDocumentIAAdapter?.notifyDataSetChanged()

                        } else {
                            onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }

                    }


                }
            }
        }

    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom == 10) {
            position.let { it1 -> addDocumentIAAdapter?.onDeleteButtonClick(it1) }

        } else {
            position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }

        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(this@AddNLMExtensionActivity, selectedItem, position)
    }


    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> assistanceEAAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItem(
        selectedItem: AssistanceForEaTrainingInstitute,
        position: Int,
        isFrom: Int
    ) {
        trainingInstitute(this@AddNLMExtensionActivity, isFrom, selectedItem, position)
    }

}