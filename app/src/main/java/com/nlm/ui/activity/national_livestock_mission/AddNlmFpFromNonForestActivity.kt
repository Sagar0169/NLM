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
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackFspCommentNlm
import com.nlm.callBack.CallBackFspNonNlm
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.CallBackSemenDose
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.databinding.ActivityAddNewFspPlantStorageBinding
import com.nlm.databinding.ActivityAddNlmFpFromNonForestBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemFspNonPlantNlmBinding
import com.nlm.databinding.ItemFspPlantStorageBinding
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.FodderProductionFromNonForestRequest
import com.nlm.model.FpFromNonForestFilledByNlmTeam
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.NlmFpFromNonForestAddRequest
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspAddAverage
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.FspNonPlantStorageNLMAdapter
import com.nlm.ui.adapter.FspPlantStorageNLMAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
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

class AddNlmFpFromNonForestActivity(
) : BaseActivity<ActivityAddNlmFpFromNonForestBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackFspNonNlm, CallBackDeleteFSPAtId {
    private var mBinding: ActivityAddNlmFpFromNonForestBinding? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var stateId: Int? = null // Store selected state
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var districtIdIA: Int? = null // Store selected state
    private var districtIdNlm: Int? = null // Store selected state
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var Model:String? = null // Store selected state
    private var stateList = ArrayList<ResultGetDropDown>()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var isFrom: Int = 0
    private var img: Int = 0
    private var viewModel = ViewModel()
    private var districtId: Int? = null // Store selected state
    private var districtIdDropdown: Int? = null // Store selected state
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null
    private lateinit var plantStorageList: ArrayList<FpFromNonForestFilledByNlmTeam>
    private var plantStorageAdapter
            : FspNonPlantStorageNLMAdapter? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false


    private val agency = listOf(
        ResultGetDropDown(0, "GOVT"),
        ResultGetDropDown(0, "Outsourced Agency")
    )

    private val land = listOf(
        ResultGetDropDown(0, "CPR"),
        ResultGetDropDown(0, "GOCHAR"),
        ResultGetDropDown(0, "Government Farm"),
        ResultGetDropDown(0, "Community Land"),
        ResultGetDropDown(0, "Individual Land"),
        ResultGetDropDown(0, "Waste Land"),
    )


    private val typeOfAgency = listOf(
        ResultGetDropDown(0, "State Government"),
        ResultGetDropDown(0, "Gaushala"),
        ResultGetDropDown(0, "Outsourced Agent"),
    )


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun quality(view: View) {
            showBottomSheetDialog("Variety")
        }

        fun agency(view: View) {
            mBinding?.tvTypeOfAgency?.let { showBottomSheetDialogNonDis("typeOfAgency", it) }
        }

        fun land(view: View) {
            mBinding?.tvLand?.let { showBottomSheetDialogNonDis("land", it) }
        }

        fun chooseFile(view: View) {
            openOnlyPdfAccordingToPosition()
            toast("Hi")
            img = 1
        }

        fun district(view: View) {
            showBottomSheetDialog2("District",1,null)
        }

        fun districtNLM(view: View) {
            showBottomSheetDialog2("District",2,null)
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddNlmFpFromNonForestActivity, null, null)
            img = 0
        }

        fun semenDose(view: View) {
            semenDoseDialog(this@AddNlmFpFromNonForestActivity, 1, null, null)
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
        get() = R.layout.activity_add_nlm_fp_from_non_forest

    override fun initView() {
        when (isFrom) {
            1 -> {
                mBinding!!.tvHeading.text = "Add New Fpfrom Non Forest"
            }
        }

        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        plantStorageList = arrayListOf()
        isFrom = intent?.getIntExtra("isFrom", 0)!!
        mBinding?.tvStateIa?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvStateIa?.isEnabled = false

        mBinding?.tvStateNlm?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvStateNlm?.isEnabled = false
        mBinding?.tvStateNlm?.setTextColor(Color.parseColor("#000000"))
        mBinding?.tvStateIa?.setTextColor(Color.parseColor("#000000"))
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
            mBinding?.tvAddDocumentsIa?.hideView()
            nlmAdapter()
        }
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {

            mBinding?.tvAddDocumentsNlm?.hideView()
            mBinding?.llNlm?.hideView()
            mBinding?.tvFilledByNlm?.hideView()
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
                mBinding?.tvStateIa?.isEnabled = false
                mBinding?.tvDistrictIa?.isEnabled = false
                mBinding?.etImplementingAgencyIa?.isEnabled = false
                mBinding?.etLocation?.isEnabled = false
                mBinding?.etAreaCovered?.isEnabled = false
                mBinding?.tvLand?.isEnabled = false
                mBinding?.tvTypeOfAgency?.isEnabled = false
                mBinding?.etVarietyOfFodder?.isEnabled = false
                mBinding?.etSchemeGuidelines?.isEnabled = false
                mBinding?.etGrantReceived?.isEnabled = false
                mBinding?.etTarget?.isEnabled = false
                mBinding?.tvAddDocumentsIa?.hideView()
            }
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24
            ) {
                mBinding?.tvStateNlm?.isEnabled = false
                mBinding?.tvDistrictNlm?.isEnabled = false
                mBinding?.etImplementingAgencyNlm?.isEnabled = false
                mBinding?.tvAddAgency?.isEnabled = false
                mBinding?.tvAddDocumentsNlm?.hideView()
                mBinding?.tvAddAgency?.hideView()
                mBinding?.llNlm?.hideView()
                mBinding?.tvFilledByNlm?.hideView()
            }
            if (viewEdit == "view") {
                viewEditApi()
                mBinding?.tvStateNlm?.isEnabled = false
                mBinding?.tvDistrictNlm?.isEnabled = false
                mBinding?.etImplementingAgencyNlm?.isEnabled = false
                mBinding?.tvAddAgency?.isEnabled = false
                mBinding?.tvAddDocumentsNlm?.hideView()
                mBinding?.tvAddAgency?.hideView()
                mBinding?.tvStateIa?.isEnabled = false
                mBinding?.tvDistrictIa?.isEnabled = false
                mBinding?.etImplementingAgencyIa?.isEnabled = false
                mBinding?.etLocation?.isEnabled = false
                mBinding?.etAreaCovered?.isEnabled = false
                mBinding?.tvLand?.isEnabled = false
                mBinding?.tvTypeOfAgency?.isEnabled = false
                mBinding?.etVarietyOfFodder?.isEnabled = false
                mBinding?.etSchemeGuidelines?.isEnabled = false
                mBinding?.etGrantReceived?.isEnabled = false
                mBinding?.etTarget?.isEnabled = false
                mBinding?.tvAddDocumentsIa?.hideView()
                mBinding?.tvSaveDraft?.hideView()
                mBinding?.tvSendOtp?.hideView()
            }
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
        semenDoseAdapter()
    }

    private fun viewEditApi() {

        viewModel.getNlmFpFromNonForestAddEdit(
            this, true,
            NlmFpFromNonForestAddRequest(
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

    private fun semenDoseAdapter() {
        plantStorageAdapter =
            FspNonPlantStorageNLMAdapter(
                this@AddNlmFpFromNonForestActivity,
                plantStorageList,
                viewEdit,
                this@AddNlmFpFromNonForestActivity,
                this, this
            )
        mBinding?.rvAgency?.adapter = plantStorageAdapter
        mBinding?.rvAgency?.layoutManager =
            LinearLayoutManager(this@AddNlmFpFromNonForestActivity)
    }

    private fun nlmAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.rvSupportDocumentNlm?.adapter = addDocumentAdapter
        mBinding?.rvSupportDocumentNlm?.layoutManager = LinearLayoutManager(this)
    }

    private fun iaAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            viewDocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.ShowDocumentRv?.adapter = addDocumentAdapter
        mBinding?.ShowDocumentRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

        viewModel.getNlmFpFromNonForestAddEdit(
            this@AddNlmFpFromNonForestActivity, true,
            NlmFpFromNonForestAddRequest(
                id = itemId,
                district_code = districtId,
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
                name_implementing_agency = if (mBinding?.etImplementingAgencyNlm?.text.isNullOrEmpty()) {
                    mBinding?.etImplementingAgencyIa?.text.toString()
                } else {
                    mBinding?.etImplementingAgencyNlm?.text.toString()
                },
                location = mBinding?.etLocation?.text.toString(),
                area_covered = mBinding?.etAreaCovered?.text.toString().toDoubleOrNull(),
                type_of_land = mBinding?.tvLand?.text.toString(),
                type_of_agency = mBinding?.tvTypeOfAgency?.text.toString(),
                variety_of_fodder = mBinding?.etVarietyOfFodder?.text.toString(),
                scheme_guidelines = mBinding?.etSchemeGuidelines?.text.toString(),
                grant_received = mBinding?.etGrantReceived?.text.toString(),
                target_achievement = mBinding?.etTarget?.text.toString(),
                fp_from_non_forest_document= totalListDocument,
                fp_from_non_forest_filled_by_nlm_team= plantStorageList,
            )
        )
    }


    private fun semenDoseDialog(
        context: Context,
        isFrom: Int,
        selectedItem: FpFromNonForestFilledByNlmTeam?,
        position: Int?
    ) {
        val bindingDialog: ItemFspNonPlantNlmBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_fsp_non_plant_nlm,
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
        bindingDialog.tvAgency.setOnClickListener {
            showBottomSheetDialogNonDis("agency", bindingDialog.tvAgency)
        }
        bindingDialog.tvDistrict.setOnClickListener { showBottomSheetDialog2("District",3,bindingDialog.tvDistrict) }
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.tvDistrict.text = selectedItem.district?.name
            bindingDialog.etBlock.setText(selectedItem.block_name)
            bindingDialog.etVillage.setText(selectedItem.village_name)
            bindingDialog.etArea.setText(selectedItem.area_covered)
            bindingDialog.etEstimated.setText(selectedItem.estimated_quantity)
            bindingDialog.etConsumer.setText(selectedItem.consumer_fodder)
            bindingDialog.tvAgency.text = selectedItem.agency_involved
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.tvDistrict.text.toString().isNotEmpty()
                || bindingDialog.etBlock.text.toString().isNotEmpty()
                || bindingDialog.etVillage.text.toString().isNotEmpty()
                || bindingDialog.etArea.text.toString().isNotEmpty()
                || bindingDialog.etEstimated.text.toString().isNotEmpty()
                || bindingDialog.etConsumer.text.toString().isNotEmpty()
                || bindingDialog.tvAgency.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        if (districtIdNlm==null)
                        {
                            districtIdNlm=selectedItem.district_code
                        }
                        plantStorageList[position] =
                            FpFromNonForestFilledByNlmTeam(
                                selectedItem.id,
                                districtIdNlm,
                                null,
                                bindingDialog.tvDistrict.text.toString(),
                                bindingDialog.etBlock.text.toString(),
                                bindingDialog.etVillage.text.toString(),
                                bindingDialog.etArea.text.toString(),
                                bindingDialog.etEstimated.text.toString(),
                                bindingDialog.etConsumer.text.toString(),
                                bindingDialog.tvAgency.text.toString(),
                                selectedItem.fp_from_non_forest_id
                            )
                        plantStorageAdapter
                            ?.notifyItemChanged(position)
                    }

                } else {
                    plantStorageList.add(
                        FpFromNonForestFilledByNlmTeam(
                            null,
                            districtIdNlm,
                            null,
                            bindingDialog.tvDistrict.text.toString(),
                            bindingDialog.etBlock.text.toString(),
                            bindingDialog.etVillage.text.toString(),
                            bindingDialog.etArea.text.toString(),
                            bindingDialog.etEstimated.text.toString(),
                            bindingDialog.etConsumer.text.toString(),
                            bindingDialog.tvAgency.text.toString(),
                            null
                        )
                    )
                    plantStorageList.size.minus(1).let {
                        plantStorageAdapter
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
    private fun showBottomSheetDialog2(type: String,isFrom: Int,TextView:TextView?) {
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
                Model="State"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvStateNlm!!
            }

            "District" -> {
                Model="Districts"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                if (isFrom==2)
                {
                    selectedTextView = mBinding?.tvDistrictNlm!!}
                else if (isFrom==3 && TextView != null){

                    selectedTextView = TextView
                }
                else{
                    selectedTextView = mBinding?.tvDistrictIa!!
                }
            }
//            "agency_involved" -> {
//                Model="agency_involved"
//                getNlmDropDownApi(Model)
//                selectedList = stateList
//                selectedTextView=TextView
//            }
//
//            "type_of_land" -> {
//                Model="type_of_land"
//                getNlmDropDownApi(Model)
//                selectedList = stateList
//                selectedTextView = mBinding!!.tvLand
//            }
//
//            "type_of_agency" -> {
//                Model="type_of_agency"
//                getNlmDropDownApi(Model)
//                selectedList = stateList
//                selectedTextView = mBinding!!.tvAgency
//            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView?.text = selectedItem


            if (Model=="Districts")
            {
                if (isFrom==2)
                {
                    districtIdIA=id
                }
                else if (isFrom==3 && TextView != null)
                {
                    districtIdNlm=id
                }
                else{

                    districtId=id}
            }
//            else if (Model=="agency_involved")
//            {
//                AgencyInvolvedName  =selectedItem
//                AgencyInvolvedId=id
//            }
//            else if (Model=="type_of_land")
//            {
//                TypeOfLandName  =selectedItem
//                TypeOfLandId=id
//            }
//            else if (Model=="type_of_agency")
//            {
//                TypeOfAgencyName  =selectedItem
//                TypeOfAgencyId=id
//            }
            else{
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
    private fun showBottomSheetDialogNonDis(type: String, full: TextView) {
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
//            "typeSemen" -> {
//                selectedList = typeSemen
//                selectedTextView = mBinding!!.tvSemenStation
//            }
//
//            "StateNDD" -> {
//                selectedList = stateList
//                selectedTextView = binding!!.tvStateNDD
//            }
//
//            "District" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = districtList
//                selectedTextView = mBinding!!.tvDistrict
//            }

//            "DistrictNLM" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = districtList
//                selectedTextView = mBinding!!.tvDistrictNlm
//            }

            "agency" -> {
                img = 2
                selectedList = agency
                selectedTextView = full
            }


            "typeOfAgency" -> {
                selectedList = typeOfAgency
                selectedTextView = full
            }

            "land" -> {
                selectedList = land
                selectedTextView = full
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)


        // Rotate drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
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

        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                bindingDialog.etDoc.text = selectedItem.nlm_document
                bindingDialog.etDescription.setText(selectedItem.description)
            } else {
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
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {
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
                                    fsp_plant_storage_id = selectedItem.fsp_plant_storage_id,
                                    id = selectedItem.id,
                                )
                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    fsp_plant_storage_id = selectedItem.fsp_plant_storage_id,
                                    id = selectedItem.id,
                                )
                        }

                        addDocumentAdapter?.notifyItemChanged(position)
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
                                nlm_document = DocumentName,
                                id = null,
                                fsp_plant_storage_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = DocumentName,
                                id = null,
                                fsp_plant_storage_id = null,
                                nlm_document = null
                            )
                        )
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
                            addDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }
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
                                table_name = getString(R.string.fp_from_non_forest_document).toRequestBody(
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
//            "typeSemen" -> {
//                selectedList = typeSemen
//                selectedTextView = mBinding!!.tvSemenStation
//            }
//
//            "StateNDD" -> {
//                selectedList = stateList
//                selectedTextView = binding!!.tvStateNDD
//            }

            "District" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrictIa
            }

            "DistrictNLM" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrictNlm
            }


//            "Status" -> {
//                selectedList = status
//                selectedTextView = binding!!.tvStatus
//            }
//
//            "Reading" -> {
//                selectedList = reading
//                selectedTextView = binding!!.tvReadingMaterial
//            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)


        // Rotate drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                rotatedDrawable,
                null
            )
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
    }

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
        if (Model=="Districts") {
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
        else{
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    20,
                    Model,
                    currentPage,
                    null,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddNlmFpFromNonForestActivity)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtList.clear()
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
                    districtList.addAll(userResponseModel._result)
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

        viewModel.nlmFpFromNonForestAddEditResult.observe(this) {
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
//                                listener?.onNextButtonClick()
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            districtId = userResponseModel._result.district_code
                            mBinding?.tvStateIa?.text = userResponseModel._result.state_name
                            mBinding?.tvStateNlm?.text = userResponseModel._result.state_name
                            mBinding?.tvDistrictIa?.text = userResponseModel._result.district_name
                            mBinding?.tvDistrictNlm?.text = userResponseModel._result.district_name
                            mBinding?.tvDistrictIa?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.tvStateIa?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.tvStateNlm?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.tvDistrictNlm?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etImplementingAgencyIa?.setText(userResponseModel._result.name_implementing_agency)
                            mBinding?.etImplementingAgencyNlm?.setText(userResponseModel._result.name_implementing_agency)
                            mBinding?.etLocation?.setText(userResponseModel._result.location)
                            mBinding?.etAreaCovered?.setText(userResponseModel._result.area_covered.toString())
                            mBinding?.tvLand?.text = userResponseModel._result.type_of_land
                            mBinding?.tvTypeOfAgency?.setText(userResponseModel._result.type_of_agency)
                            mBinding?.etVarietyOfFodder?.setText(userResponseModel._result.variety_of_fodder)
                            mBinding?.etSchemeGuidelines?.setText(userResponseModel._result.scheme_guidelines)
                            mBinding?.etGrantReceived?.setText(userResponseModel._result.grant_received)
                            mBinding?.etTarget?.setText(userResponseModel._result.target_achievement)

                            plantStorageList.clear()
                            val comments =
                                userResponseModel._result.fp_from_non_forest_filled_by_nlm_team
                                    ?: emptyList()

                            if (comments.isEmpty() && viewEdit == "view") {
                                val dummyData = FpFromNonForestFilledByNlmTeam(
                                  null,
                                  null,
                                    null,
                                    "",
                                  "",
                                  "",
                                  "",
                                  "",
                                  "",
                                  "",
                                  null,
                                )
                                plantStorageList.add(dummyData)
                            } else {
                                plantStorageList.addAll(comments)
                            }


                            plantStorageAdapter?.notifyDataSetChanged()
                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()
                            val dummyData = ImplementingAgencyDocument(
                                id = 0, // Or null, depending on your use case
                                description = "",
                                ia_document = "",
                                nlm_document = "",
                                fsp_plant_storage_id = 0 // Or null, depending on your use case
                            )
                            if (userResponseModel._result.fp_from_non_forest_document.isEmpty() && viewEdit == "view") {
                                DocumentList.add(dummyData)
                                viewDocumentList.add(dummyData)
                            } else {
                                userResponseModel._result.fp_from_non_forest_document.forEach { document ->
                                    if (document.ia_document == null) {
                                        DocumentList.add(document)//nlm
                                    } else {
                                        viewDocumentList.add(document)//ia

                                    }
                                }
                                // Check if viewDocumentList is empty after the loop
                                if (viewDocumentList.isEmpty() && viewEdit == "view") {
                                    viewDocumentList.add(dummyData)
                                }
                                if (DocumentList.isEmpty() && viewEdit == "view") {
                                    DocumentList.add(dummyData)
                                }
                            }
                            iaAdapter()
                            nlmAdapter()
                            addDocumentAdapter?.notifyDataSetChanged()

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
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(this@AddNlmFpFromNonForestActivity, selectedItem, position)
    }


    override fun onClickItem(
        selectedItem: FpFromNonForestFilledByNlmTeam,
        position: Int,
        isFrom: Int
    ) {
        semenDoseDialog(this, isFrom, selectedItem, position)
    }

    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> plantStorageAdapter?.onDeleteButtonClick(it1) }
    }

}