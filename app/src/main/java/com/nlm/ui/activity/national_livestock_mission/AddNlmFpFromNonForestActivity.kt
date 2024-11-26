package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddNlmFpFromNonForestBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.model.FpFromNonForestFilledByNlmTeam
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.model.NlmFpFromNonForestAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterViewOnly
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.rotateDrawable
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNlmFpFromNonForestActivity : BaseActivity<ActivityAddNlmFpFromNonForestBinding>(),
    CallBackDeleteAtId, CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddNlmFpFromNonForestBinding? = null
    private var savedAsDraft: Boolean = false
    private var formId: Int? = null
    private var currentPage = 1
    private var totalPage = 1
    private var layoutManager: LinearLayoutManager? = null
    private var stateList = ArrayList<ResultGetDropDown>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private  var fpFromNonForestTeamList: MutableList<FpFromNonForestFilledByNlmTeam>? =null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private var ViewDocumentAdapter: SupportingDocumentAdapterViewOnly? = null
    private var documentListNlm = ArrayList<ImplementingAgencyDocument>()
    private var documentListIa = ArrayList<ImplementingAgencyDocument>()
    private var documentList = ArrayList<ImplementingAgencyDocument>()
    private lateinit var stateAdapter: BottomSheetAdapter
    private var DocumentName: String? = null
    private var DocumentId: Int? = null
    private var DialogDocName: TextView? = null
    private var UploadedDocumentName: String? = null
    var body: MultipartBody.Part? = null
    val viewModel = ViewModel()
    var selectedValue: Int = 1
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var model: String? = null // Store selected state
    private var loading = true
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
//                                dropDownApiCall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }


    override val layoutId: Int
        get() = R.layout.activity_add_nlm_fp_from_non_forest

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        if (viewEdit == "edit") {

//            ViewEditApi(viewEdit)
        }
        addDocumentAdapterNlm()
        addDocumentAdapterIa()
    }

    override fun setVariables() {

        mBinding?.tvStateIa?.text =
            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.tvStateIa?.isEnabled = false
        mBinding?.tvStateNlm?.text =
            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.tvStateNlm?.isEnabled = false
        if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id == 24) {
            mBinding?.llNlm?.hideView()
            mBinding?.tvFilledByNlm?.hideView()
        } else {
            mBinding?.llIa?.hideView()
            mBinding?.tvFilledByIa?.hideView()
//            ViewEditApi("view")
        }
        if (viewEdit == "view") {
            mBinding?.etLocation?.isEnabled = false
            mBinding?.etImplementingAgencyIa?.isEnabled = false
            mBinding?.etAreaCovered?.isEnabled = false
            mBinding?.etVarietyOfFodder?.isEnabled = false
            mBinding?.etSchemeGuidelines?.isEnabled = false
            mBinding?.etGrantReceived?.isEnabled = false
            mBinding?.etTarget?.isEnabled = false
            mBinding?.etImplementingAgencyNlm?.isEnabled = false
            mBinding?.tvAddAgency?.hideView()
            mBinding?.tvAddDocumentsNlm?.hideView()
            mBinding?.tvAddDocumentsIa?.hideView()
//            ViewEditApi(viewEdit)
        }
    }

    inner class ClickActions {
        fun state(view: View) {
//            showBottomSheetDialog("state")
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun saveAndNext(view: View) {
            if (viewEdit == "view") {
                onBackPressedDispatcher.onBackPressed()
            } else {
                saveDataApi(0)
            }
            savedAsDraft = true
        }

        fun saveAsDraft(view: View) {
            if (viewEdit == "view") {
                onBackPressedDispatcher.onBackPressed()
            } else {
                saveDataApi(1)
            }
            savedAsDraft = true
        }

        fun addDocumentIa(view: View) {
            addDocumentDialog(this@AddNlmFpFromNonForestActivity, null, null)
        }

        fun addDocumentNlm(view: View) {
            addDocumentDialog(this@AddNlmFpFromNonForestActivity, null, null)
        }

        fun addAgency(view: View) {

        }
    }

    override fun setObservers() {

        viewModel.nlmFpFromNonForestAddEditResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.llParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
                else {
                    if (viewEdit == "view") {

                        formId = userResponseModel._result.id
                        documentListNlm.clear()
                        documentListIa.clear()

                        userResponseModel._result.fp_from_non_forest_document.forEach { document ->
                            if (document.nlm_document == null) {
                                if (getPreferenceOfScheme(
                                        this,
                                        AppConstants.SCHEME,
                                        Result::class.java
                                    )?.role_id == 24
                                ) {

                                    documentListIa.add(document)
                                } else {
                                    documentListNlm.add(document)
                                }
                            } else {
                                documentList.add(document)
                            }
                        }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()

//                        userResponseModel._result.import_of_exotic_goat_achievement?.let { it1 ->
//                            AchievementList?.addAll(it1)
//                        } ?: run {
//                            // Add an item with empty fields if data is null
//                            AchievementList?.add(
//                                ImportOfExoticGoatAchievement(
//                                    number_of_animals = null,
//                                    f1_generation_produced = "",
//                                    f2_generation_produced = "",
//                                    no_of_animals_f1 = null,
//                                    no_of_animals_f2 = null,
//                                    balance = "",
//                                    performance_animals_doorstep = "",
//                                    import_of_exotic_goat_id = null,
//                                    id = null,
//                                )
//                            )
//                        }
//                        AchievementAdapter?.notifyDataSetChanged()

                    } else if (viewEdit == "edit") {
                        documentListIa.clear()
                        documentListNlm.clear()
                        userResponseModel._result.fp_from_non_forest_document?.forEach { document ->
                            if (document.nlm_document == null) {
                                if (getPreferenceOfScheme(
                                        this,
                                        AppConstants.SCHEME,
                                        Result::class.java
                                    )?.role_id == 24
                                ) {
                                    documentListIa.add(document)
                                } else {
                                    documentListNlm.add(document)
                                }
                            } else {
                                documentList.add(document)
                            }
                        }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()
                        formId = userResponseModel._result.id
//                        AchievementList?.clear()
//
//                        userResponseModel._result.import_of_exotic_goat_achievement?.let { it1 ->
//                            AchievementList?.addAll(
//                                it1
//                            )
//                        }
//                        AchievementAdapter?.notifyDataSetChanged()

                    } else {
                        mBinding?.llParent?.let { it1 ->
                            showSnackbar(
                                it1,
                                userResponseModel.message
                            )
                        }
                    }
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                        mBinding?.llParent?.let { it1 ->
                            showSnackbar(
                                it1,
                                userResponseModel.message
                            )
                        }
                    }
                }
            }
        }

        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.llParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }

                } else {
                    DocumentId = userResponseModel._result.id
                    UploadedDocumentName = userResponseModel._result.document_name
                    mBinding?.llParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
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
                                document_name = body,
                                user_id = getPreferenceOfScheme(
                                    this,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id,
                                table_name = getString(R.string.artificial_insemination_document).toRequestBody(
                                    MultipartBody.FORM
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addDocumentAdapterNlm() {
        AddDocumentAdapter =
            SupportingDocumentAdapterWithDialog(this, documentListNlm, viewEdit, this, this)
        mBinding?.rvSupportDocumentNlm?.adapter = AddDocumentAdapter
        mBinding?.rvSupportDocumentNlm?.layoutManager = LinearLayoutManager(this)
    }

    private fun addDocumentAdapterIa() {
        ViewDocumentAdapter = SupportingDocumentAdapterViewOnly(documentListNlm, "viewEdit")
        mBinding?.ShowDocumentRv?.adapter = ViewDocumentAdapter
        mBinding?.ShowDocumentRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun saveDataApi(isDraft: Int?) {
        viewModel.getNlmFpFromNonForestAddEdit(
            this, true,
            NlmFpFromNonForestAddRequest(
                area_covered = mBinding?.etAreaCovered?.text.toString().toDoubleOrNull(),
                created_by = null,
                district_code = districtId,
                fp_from_non_forest_document = documentListNlm,
                fp_from_non_forest_filled_by_nlm_team = null,
                grant_received = mBinding?.etGrantReceived?.text.toString(),
                id = formId,
                is_deleted = null,
                is_draft = isDraft,
                location = mBinding?.etLocation?.text.toString(),
                name_implementing_agency = mBinding?.etImplementingAgencyNlm?.text.toString(),
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                scheme_guidelines = mBinding?.etSchemeGuidelines?.text.toString(),
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                status = null,
                target_achievement = mBinding?.etTarget?.text.toString(),
                type_of_agency = mBinding?.tvTypeOfAgency?.text.toString(),
                type_of_land = mBinding?.tvLand?.text.toString(),
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                variety_of_fodder = mBinding?.etVarietyOfFodder?.text.toString(),
            )
        )
    }

    private fun addImportDetailDialog(context: Context,selectedItem: ImportOfExoticGoatDetailImport?, position: Int?) {
        val bindingDialog: ItemImportExoticGermplasmBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_import_exotic_germplasm,
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
        bindingDialog.tvSubmit.showView()
        bindingDialog.btnDelete.hideView()
        if(selectedItem!=null )
        {
            bindingDialog.etSpeciesBreed.setText(selectedItem.species_breed)
            bindingDialog.etUnit.setText(selectedItem.unit)
            bindingDialog.etYear.setText(selectedItem.year.toString())
            bindingDialog.etProcurementCost.setText(selectedItem.procurement_cost.toString())
            bindingDialog.etPlaceOfProcurement.setText(selectedItem.place_of_procurement)
            bindingDialog.etPlaceOfInduction.setText(selectedItem.place_of_induction)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etSpeciesBreed.text.toString().isNotEmpty()||bindingDialog.etUnit.text.toString().isNotEmpty()||bindingDialog.etYear.text.toString().isNotEmpty()||bindingDialog.etProcurementCost.text.toString().isNotEmpty()||bindingDialog.etPlaceOfProcurement.text.toString().isNotEmpty()||bindingDialog.etPlaceOfInduction.text.toString().isNotEmpty())
            {
                if(selectedItem!=null)
                {
                    if (position != null) {
//                        DetailOfImportList?.set(position,ImportOfExoticGoatDetailImport(
//                            species_breed =bindingDialog.etSpeciesBreed.text.toString(),
//                            unit = bindingDialog.etUnit.text.toString(),
//                            year = bindingDialog.etYear.text.toString(),
//                            procurement_cost = bindingDialog.etProcurementCost.text.toString().toIntOrNull(),
//                            place_of_procurement = bindingDialog.etPlaceOfProcurement.text.toString(),
//                            place_of_induction = bindingDialog.etPlaceOfInduction.text.toString(),
//                            import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
//                            id = selectedItem.id,
//                        ))
//                        DetailOfImportAdapter?.notifyItemChanged(position)
                    } }
                else{
//                    DetailOfImportList?.add(ImportOfExoticGoatDetailImport(
//                        species_breed =bindingDialog.etSpeciesBreed.text.toString(),
//                        unit = bindingDialog.etUnit.text.toString(),
//                        year = bindingDialog.etYear.text.toString(),
//                        procurement_cost = bindingDialog.etProcurementCost.text.toString().toIntOrNull(),
//                        place_of_procurement = bindingDialog.etPlaceOfProcurement.text.toString(),
//                        place_of_induction = bindingDialog.etPlaceOfInduction.text.toString(),
//                        import_of_exotic_goat_id = null,
//                        id = null,
//                    ))
//                    DetailOfImportList?.size?.minus(1).let {
//                        if (it != null) {
//                            DetailOfImportAdapter?.notifyItemInserted(it)
//                        }
//                    }
                }
                dialog.dismiss()
            }
            else {
                mBinding?.llParent?.let { it1 -> showSnackbar(it1, getString(R.string.please_enter_atleast_one_field)) }
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
        val selectedTextView: TextView?

        // Initialize based on type
        when (type) {
            "State" -> {
                model = "State"
//                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvStateNlm
            }

            "District" -> {
//                dropDownApiCallDistrict(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                model = "Districts"
                selectedTextView = mBinding?.tvDistrictNlm
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView?.text = selectedItem

            // Store the appropriate ID based on the type
            if (type == "State") {
                stateId = id  // Save the selected state ID
            } else if (type == "District") {
                districtName = selectedItem
                districtId = id  // Save the selected district ID
            }

            if (model == "Districts") {
                districtName = selectedItem
                districtId = id
            } else {
                stateId = id
            }
            selectedTextView?.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)

        // Rotate drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView?.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView?.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                rotatedDrawable,
                null
            )
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
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
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }
        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24
            ) {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document
            } else {
                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
            }
            bindingDialog.etDescription.setText(selectedItem.description)

        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                openOnlyPdfAccordingToPosition()
            } else {
                mBinding?.llParent?.let { showSnackbar(it, "please enter description") }
            }
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {
                if (getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id == 24
                ) {
                    if (selectedItem != null) {
                        if (position != null) {
                            documentListIa[position] = ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                nlm_document = null,
                                import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
                                id = selectedItem.id,
                            )
                            AddDocumentAdapter?.notifyItemChanged(position)
                            dialog.dismiss()
                        }

                    } else {
                        documentListIa.add(
                            ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                nlm_document = null,
                            )
                        )

                        documentListNlm.size.minus(1).let {
                            AddDocumentAdapter?.notifyItemInserted(it)
                            Log.d("DOCUMENTLIST", documentListIa.toString())
                            dialog.dismiss()
                        }
                    }
                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        if (selectedItem != null) {
                            if (position != null) {
                                documentListNlm[position] = ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    fp_from_non_forest_id = selectedItem.fp_from_non_forest_id,
                                    id = selectedItem.id,
                                )
                                AddDocumentAdapter?.notifyItemChanged(position)
                                dialog.dismiss()
                            }

                        } else {
                            documentListNlm.add(
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,

                                    )
                            )
                            documentListNlm.size.minus(1).let {
                                AddDocumentAdapter?.notifyItemInserted(it)
                                dialog.dismiss()
                            }
                        }
                    }
                }
            } else {
                showSnackbar(
                    mBinding!!.rlToolbar,
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

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom == 1) {
            position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(this, selectedItem, position)
    }
}