package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackFspCommentNlm
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.CallBackSemenDose
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.databinding.ActivityAddNewFspPlantStorageBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemFspPlantStorageBinding
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspAddAverage
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.FspPlantStorageNLMAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNewFspPlantStorageActivity(
) : BaseActivity<ActivityAddNewFspPlantStorageBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackFspCommentNlm, CallBackDeleteFSPAtId {
    private var mBinding: ActivityAddNewFspPlantStorageBinding? = null
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
    private lateinit var plantStorageList: ArrayList<FspPlantStorageCommentsOfNlm>
    private var plantStorageAdapter
            : FspPlantStorageNLMAdapter? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var latitude:Double?=null
    private var longitude:Double?=null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        }
    }

    private val variety = listOf(
        ResultGetDropDown(-1, "Class Wise"),
        ResultGetDropDown(-1, "Variety Wise")
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

        fun chooseFile(view: View) {
            openOnlyPdfAccordingToPosition()
            toast("Hi")
            img = 1
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun districtNLM(view: View) {
            showBottomSheetDialog("DistrictNLM")
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddNewFspPlantStorageActivity, null, null)
            img = 0
        }

        fun semenDose(view: View) {
            semenDoseDialog(this@AddNewFspPlantStorageActivity, 1, null, null)
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
        get() = R.layout.activity_add_new_fsp_plant_storage

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
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false

        mBinding?.tvStateNlm?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvStateNlm?.isEnabled = false
        mBinding?.tvStateNlm?.setTextColor(Color.parseColor("#000000"))
        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))
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
                mBinding?.tvDistrict?.isEnabled = false
                mBinding?.etNoa?.isEnabled = false
                mBinding?.etLoc?.isEnabled = false
                mBinding?.etPurposeOfEst?.isEnabled = false
                mBinding?.etCapacityofPlant?.isEnabled = false
                mBinding?.etMachinery?.isEnabled = false
                mBinding?.llApplicantPhoto?.isEnabled = false
                mBinding?.tvChooseFile?.isEnabled = false
                mBinding?.etQuality?.isEnabled = false
                mBinding?.tvQuality?.isEnabled = false
                mBinding?.etTechnical?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
            }
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24
            ) {
                mBinding?.tvStateNlm?.isEnabled = false
                mBinding?.tvDistrictNlm?.isEnabled = false
                mBinding?.etEOA?.isEnabled = false
                mBinding?.tvNLMComment?.isEnabled = false
                mBinding?.tvNLMComment?.hideView()
                mBinding?.tvNLMDoc?.hideView()
            }
            if (viewEdit == "view") {
                viewEditApi()
                mBinding?.tvState?.isEnabled = false
                mBinding?.tvDistrict?.isEnabled = false
                mBinding?.etNoa?.isEnabled = false
                mBinding?.etLoc?.isEnabled = false
                mBinding?.etPurposeOfEst?.isEnabled = false
                mBinding?.etCapacityofPlant?.isEnabled = false
                mBinding?.etMachinery?.isEnabled = false
                mBinding?.llApplicantPhoto?.isEnabled = false
                mBinding?.tvChooseFile?.isEnabled = false
                mBinding?.etQuality?.isEnabled = false
                mBinding?.tvQuality?.isEnabled = false
                mBinding?.etTechnical?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
                mBinding?.tvStateNlm?.isEnabled = false
                mBinding?.tvDistrictNlm?.isEnabled = false
                mBinding?.etEOA?.isEnabled = false
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
        semenDoseAdapter()
    }

    private fun viewEditApi() {

        viewModel.getFpsPlantStorageADD(
            this, true,
            AddFspPlantStorageRequest(
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
            FspPlantStorageNLMAdapter(
                this@AddNewFspPlantStorageActivity,
                plantStorageList,
                viewEdit,
                this@AddNewFspPlantStorageActivity,
                this, this
            )
        mBinding?.recyclerView1?.adapter = plantStorageAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(this@AddNewFspPlantStorageActivity)
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
        val district = mBinding?.tvDistrict?.text.toString()
        val tvStateNlm = mBinding?.tvStateNlm?.text.toString()
        val tvDistrictNlm = mBinding?.tvDistrictNlm?.text.toString()
        val iaName = mBinding?.etNoa?.text.toString()
        val nLMName = mBinding?.etEOA?.text.toString()

        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            if (tvStateNlm == "Please Select") {
                mBinding?.clParent?.let { showSnackbar(it,"State Name is required") }
                return
            }
            if (tvDistrictNlm == "Please Select") {
                mBinding?.clParent?.let { showSnackbar(it,"District Name is required") }
                return
            }
            if (nLMName.isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Name is required") }
                return
            }
            if (plantStorageList.isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"At least one plant storage nlm comment is required") }
                return
            }
            if (hasLocationPermissions()) {
                val intent = Intent(this@AddNewFspPlantStorageActivity, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    Log.d("Scope","out")
                    delay(1000) // Delay for 2 seconds
                    Log.d("Scope","In")
                    Log.d("Scope", latitude.toString())
                    Log.d("Scope", longitude.toString())

                    if(latitude!=null&&longitude!=null)
                    {
                        toast("hi")
                        viewModel.getFpsPlantStorageADD(
                            this@AddNewFspPlantStorageActivity, true,
                            AddFspPlantStorageRequest(
                                id = itemId,
                                district_code = districtId,
                                role_id = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                state_code = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.state_code,
                                user_id = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id.toString(),
                                is_draft = draft,
                                name_of_organization = if (mBinding?.etNoa?.text.isNullOrEmpty()) {
                                    mBinding?.etEOA?.text.toString()
                                } else {
                                    mBinding?.etNoa?.text.toString()
                                },
                                location_address = mBinding?.etLoc?.text.toString(),
                                purpose_of_establishment = mBinding?.etPurposeOfEst?.text.toString(),
                                capacity_of_plant = mBinding?.etCapacityofPlant?.text.toString(),
                                machinery_equipment_available = mBinding?.etMachinery?.text.toString(),
                                quantity_fodder_seed_class = mBinding?.etQuality?.text.toString(),
                                quantity_fodder_seed_variety = mBinding?.tvQuality?.text.toString(),
                                technical_expertise = mBinding?.etTechnical?.text.toString(),
                                certification_recognition = chooseDocName,
                                fsp_plant_storage_comments_of_nlm = plantStorageList,
                                fsp_plant_storage_document = totalListDocument,
                                lattitude_nlm=latitude,
                                longitude_nlm= longitude
                            )
                        )
                    }
                    else{
                        showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
                    }
                }}


        }
        else{
            if (hasLocationPermissions()) {
                val intent = Intent(this@AddNewFspPlantStorageActivity, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    delay(1000) // Delay for 2 seconds
                    if(latitude!=null&&longitude!=null)
                    {
                        viewModel.getFpsPlantStorageADD(
                            this@AddNewFspPlantStorageActivity, true,
                            AddFspPlantStorageRequest(
                                id = itemId,
                                district_code = districtId,
                                role_id = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                state_code = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.state_code,
                                user_id = getPreferenceOfScheme(
                                    this@AddNewFspPlantStorageActivity,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id.toString(),
                                is_draft = draft,
                                name_of_organization = if (mBinding?.etNoa?.text.isNullOrEmpty()) {
                                    mBinding?.etEOA?.text.toString()
                                } else {
                                    mBinding?.etNoa?.text.toString()
                                },
                                location_address = mBinding?.etLoc?.text.toString(),
                                purpose_of_establishment = mBinding?.etPurposeOfEst?.text.toString(),
                                capacity_of_plant = mBinding?.etCapacityofPlant?.text.toString(),
                                machinery_equipment_available = mBinding?.etMachinery?.text.toString(),
                                quantity_fodder_seed_class = mBinding?.etQuality?.text.toString(),
                                quantity_fodder_seed_variety = mBinding?.tvQuality?.text.toString(),
                                technical_expertise = mBinding?.etTechnical?.text.toString(),
                                certification_recognition = chooseDocName,
                                fsp_plant_storage_comments_of_nlm = plantStorageList,
                                fsp_plant_storage_document = totalListDocument,
                                lattitude_ia=latitude,
                                longitude_ia= longitude
                            )
                        )
                    }
                    else{
                        showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
                    }
                }}
            if (state == "Please Select") {
                mBinding?.clParent?.let { showSnackbar(it,"State Name is required") }
                return
            }
            if (district == "Please Select") {
                mBinding?.clParent?.let { showSnackbar(it,"District Name is required") }
                return
            }
            if (iaName.isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Name is required") }
                return
            }
            if (mBinding?.etLoc?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Location is required") }
                return
            }
            if (mBinding?.etPurposeOfEst?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Purpose of establishment is required") }
                return
            }
            if (mBinding?.etCapacityofPlant?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Capacity of Plant is required") }
                return
            }
            if (mBinding?.etMachinery?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Machinery/Equipment available is required") }
                return
            }
            if (mBinding?.etTechnical?.text.toString().isEmpty()) {
                mBinding?.clParent?.let { showSnackbar(it,"Technical Expertise is required") }
                return
            }


        }
    }


    private fun semenDoseDialog(
        context: Context,
        isFrom: Int,
        selectedItem: FspPlantStorageCommentsOfNlm?,
        position: Int?
    ) {
        val bindingDialog: ItemFspPlantStorageBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_fsp_plant_storage,
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
            bindingDialog.etNameOfAgency.setText(selectedItem.name_of_agency)
            bindingDialog.etAddress.setText(selectedItem.address)
            bindingDialog.etQuantity.setText(selectedItem.quantity_of_seed_graded)
            bindingDialog.etInfra.setText(selectedItem.infrastructure_available)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNameOfAgency.text.toString().isNotEmpty()
                && bindingDialog.etAddress.text.toString().isNotEmpty()
                && bindingDialog.etQuantity.text.toString().isNotEmpty()
                && bindingDialog.etInfra.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        plantStorageList[position] =
                            FspPlantStorageCommentsOfNlm(
                                selectedItem.id,
                                bindingDialog.etNameOfAgency.text.toString(),
                                bindingDialog.etAddress.text.toString(),
                                bindingDialog.etQuantity.text.toString(),
                                bindingDialog.etInfra.text.toString(),
                                selectedItem.fsp_plant_storage_id
                            )
                        plantStorageAdapter
                            ?.notifyItemChanged(position)
                    }

                } else {
                    plantStorageList.add(
                        FspPlantStorageCommentsOfNlm(
                            null,
                            bindingDialog.etNameOfAgency.text.toString(),
                            bindingDialog.etAddress.text.toString(),
                            bindingDialog.etQuantity.text.toString(),
                            bindingDialog.etInfra.text.toString(),
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
            if (bindingDialog.etDescription.text.toString().isNotEmpty()&& bindingDialog.etDoc.text.toString().isNotEmpty()) {
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
                            addDocumentAdapter?.notifyItemChanged(position)

                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    fsp_plant_storage_id = selectedItem.fsp_plant_storage_id,
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
                                fsp_plant_storage_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                id = null,
                                fsp_plant_storage_id = null,
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
                                if (img == 1) {
                                    mBinding?.tvDoc?.text = DocumentName
                                    chooseDocName = DocumentName
                                } else {
                                    DialogDocName?.text = DocumentName
                                }


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
                                table_name = getString(R.string.fsp_plant_storage_document).toRequestBody(
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
                selectedTextView = mBinding!!.tvDistrict
            }

            "DistrictNLM" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrictNlm
            }

            "Variety" -> {
                selectedList = variety
                selectedTextView = mBinding!!.tvQuality
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
            if (id != -1) {
                districtId = id
                toast(districtId.toString())
            }
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
                Utility.logout(this@AddNewFspPlantStorageActivity)
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
                    chooseDocName = userResponseModel._result.document_name
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }

        viewModel.fpsPlantStorageADDResult.observe(this) {
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
                            mBinding?.etLoc?.setText(userResponseModel._result.location_address)
                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                            mBinding?.tvDistrictNlm?.text = userResponseModel._result.district_name
                            mBinding?.tvDoc?.text =
                                userResponseModel._result.certification_recognition
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.tvDistrictNlm?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etNoa?.setText(userResponseModel._result.name_of_organization)
                            mBinding?.etEOA?.setText(userResponseModel._result.name_of_organization)
                            mBinding?.etPurposeOfEst?.setText(userResponseModel._result.purpose_of_establishment)
                            mBinding?.etCapacityofPlant?.setText(userResponseModel._result.capacity_of_plant)
                            mBinding?.etMachinery?.setText(userResponseModel._result.machinery_equipment_available)
                            mBinding?.etQuality?.setText(userResponseModel._result.quantity_fodder_seed_class)
                            mBinding?.tvQuality?.text =
                                userResponseModel._result.quantity_fodder_seed_variety
                            mBinding?.etTechnical?.setText(userResponseModel._result.technical_expertise)

                            plantStorageList.clear()
                            val comments =
                                userResponseModel._result.fsp_plant_storage_comments_of_nlm
                                    ?: emptyList()

                            if (comments.isEmpty() && viewEdit == "view") {
                                val dummyData = FspPlantStorageCommentsOfNlm(
                                    id = 0,
                                    name_of_agency = "",
                                    address = "",
                                    quantity_of_seed_graded = "",
                                    infrastructure_available = "",
                                    fsp_plant_storage_id = 0
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
                                assistance_for_ea_id = 0 // Or null, depending on your use case
                            )
                            if (userResponseModel._result?.fsp_plant_storage_document?.isEmpty() == true && viewEdit == "view") {
//                                // Add dummy data with default values
//
//
//                                DocumentList.add(dummyData)
//                                viewDocumentList.add(dummyData)

                            } else {
                                userResponseModel._result?.fsp_plant_storage_document?.forEach { document ->
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
        addDocumentDialog(this@AddNewFspPlantStorageActivity, selectedItem, position)
    }


    override fun onClickItem(
        selectedItem: FspPlantStorageCommentsOfNlm,
        position: Int,
        isFrom: Int
    ) {
        semenDoseDialog(this@AddNewFspPlantStorageActivity, isFrom, selectedItem, position)
    }

    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> plantStorageAdapter?.onDeleteButtonClick(it1) }
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33
            registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(locationReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
    }

}