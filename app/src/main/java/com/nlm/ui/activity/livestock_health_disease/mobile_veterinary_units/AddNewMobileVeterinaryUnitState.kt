package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitStateBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.StateMobileVeterinaryUnitAddRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNewMobileVeterinaryUnitState : BaseActivity<ActivityAddNewMobileVeterinaryUnitStateBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitStateBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var viewModel = ViewModel()
    private var stateList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var stateId: Int? = null // Store selected state
    private var uploadedDocumentName: String? = null
    private var dialogDocName: TextView? = null
    private var documentName: String? = null
    var body: MultipartBody.Part? = null
    private var isFromApplication = 0
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "LOCATION_UPDATED") {
                    // Handle the location update
                    latitude = it.getDoubleExtra("latitude", 0.0)
                    longitude = it.getDoubleExtra("longitude", 0.0)
                    Log.d("Receiver", "Location Updated: Lat = $latitude, Lon = $longitude")
                }
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_add_new_mobile_veterinary_unit_state

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.etInputOne?.isEnabled = false
            mBinding?.etRemarkOne?.isEnabled = false
            mBinding?.etInputTwo?.isEnabled = false
            mBinding?.etRemarkTwo?.isEnabled = false
            mBinding?.etInputThree?.isEnabled = false
            mBinding?.etRemarkThree?.isEnabled = false
            mBinding?.etInputFour?.isEnabled = false
            mBinding?.etRemarkFour?.isEnabled = false
            mBinding?.etInputFive?.isEnabled = false
            mBinding?.etRemarkFive?.isEnabled = false
            mBinding?.etInputSix?.isEnabled = false
            mBinding?.etRemarkSix?.isEnabled = false
            mBinding?.etInputSeven?.isEnabled = false
            mBinding?.etRemarkSeven?.isEnabled = false
            mBinding?.etInputA?.isEnabled = false
            mBinding?.etRemarkA?.isEnabled = false
            mBinding?.etInputB?.isEnabled = false
            mBinding?.etRemarkB?.isEnabled = false
            mBinding?.etInputC?.isEnabled = false
            mBinding?.etRemarkC?.isEnabled = false
            mBinding?.etInputD?.isEnabled = false
            mBinding?.etRemarkD?.isEnabled = false
            mBinding?.etInputE?.isEnabled = false
            mBinding?.etRemarkE?.isEnabled = false
            mBinding?.etInputF?.isEnabled = false
            mBinding?.etRemarkF?.isEnabled = false
            mBinding?.etChooseOne?.isEnabled = false
            mBinding?.etChooseTwo?.isEnabled = false
            mBinding?.etChooseThree?.isEnabled = false
            mBinding?.etChooseFour?.isEnabled = false
            mBinding?.etChooseFive?.isEnabled = false
            mBinding?.etChooseSix?.isEnabled = false
            mBinding?.etChooseSeven?.isEnabled = false
            mBinding?.etChooseA?.isEnabled = false
            mBinding?.etChooseB?.isEnabled = false
            mBinding?.etChooseC?.isEnabled = false
            mBinding?.etChooseD?.isEnabled = false
            mBinding?.etChooseE?.isEnabled = false
            mBinding?.etChooseF?.isEnabled = false
            mBinding?.llSaveDraftAndSubmit?.hideView()

            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
//        viewEdit?.let { toast(it) }
        mBinding?.etChooseOne?.setOnClickListener {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseTwo?.setOnClickListener {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseThree?.setOnClickListener {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseFour?.setOnClickListener {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseFive?.setOnClickListener {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseSix?.setOnClickListener {
            isFromApplication = 6
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseSeven?.setOnClickListener {
            isFromApplication = 7
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseA?.setOnClickListener {
            isFromApplication = 8
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseB?.setOnClickListener {
            isFromApplication = 9
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseC?.setOnClickListener {
            isFromApplication = 10
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseD?.setOnClickListener {
            isFromApplication = 11
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseE?.setOnClickListener {
            isFromApplication = 12
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseF?.setOnClickListener {
            isFromApplication = 13
            openOnlyPdfAccordingToPosition()
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, intentFilter)
        }
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
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
                    uploadedDocumentName = userResponseModel._result.document_name
                    dialogDocName?.text = userResponseModel._result.document_name

                    when (isFromApplication) {
                        1 -> {
                            mBinding?.tvNoFileOne?.text = uploadedDocumentName
                        }

                        2 -> {
                            mBinding?.tvNoFileTwo?.text = uploadedDocumentName
                        }

                        3 -> {
                            mBinding?.tvNoFileThree?.text = uploadedDocumentName
                        }

                        4 -> {
                            mBinding?.tvNoFileFour?.text = uploadedDocumentName
                        }

                        5 -> {
                            mBinding?.tvNoFileFive?.text = uploadedDocumentName
                        }

                        6 -> {
                            mBinding?.tvNoFileSix?.text = uploadedDocumentName
                        }

                        7 -> {
                            mBinding?.tvNoFileSeven?.text = uploadedDocumentName
                        }

                        8 -> {
                            mBinding?.tvNoFileA?.text = uploadedDocumentName
                        }

                        9 -> {
                            mBinding?.tvNoFileB?.text = uploadedDocumentName
                        }

                        10 -> {
                            mBinding?.tvNoFileC?.text = uploadedDocumentName
                        }

                        11 -> {
                            mBinding?.tvNoFileD?.text = uploadedDocumentName
                        }

                        12 -> {
                            mBinding?.tvNoFileE?.text = uploadedDocumentName
                        }

                        13 -> {
                            mBinding?.tvNoFileF?.text = uploadedDocumentName
                        }

                        else -> {
                            dialogDocName?.text = documentName
                        }

                    }
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }

        viewModel.stateMobileVeterinaryUnitsAddResult.observe(this) {
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
                            toast(viewEdit.toString())
                            mBinding?.tvState?.text = userResponseModel._result.state_name
                            mBinding?.etInputOne?.setText(userResponseModel._result.input_mechanism_operation)
                            mBinding?.etRemarkOne?.setText(userResponseModel._result.mechanism_operation_remarks)
                            mBinding?.etInputTwo?.setText(userResponseModel._result.input_engagement_indicators)
                            mBinding?.etRemarkTwo?.setText(userResponseModel._result.engagement_indicators_remarks)
                            mBinding?.etInputThree?.setText(userResponseModel._result.input_procurement_procedure)
                            mBinding?.etRemarkThree?.setText(userResponseModel._result.procurement_procedure_remarks)
                            mBinding?.etInputFour?.setText(userResponseModel._result.input_supply_procedure)
                            mBinding?.etRemarkFour?.setText(userResponseModel._result.supply_procedure_remarks)
                            mBinding?.etInputFive?.setText(userResponseModel._result.input_is_monitoring_supervision_medic_equip)
                            mBinding?.etRemarkFive?.setText(userResponseModel._result.is_monitoring_supervision_medic_equip_remarks)
                            mBinding?.etInputSix?.setText(userResponseModel._result.input_is_monitoring_supervision_fuel)
                            mBinding?.etRemarkSix?.setText(userResponseModel._result.is_monitoring_supervision_fuel_remarks)
                            mBinding?.etInputSeven?.setText(userResponseModel._result.input_call_center)
                            mBinding?.etRemarkSeven?.setText(userResponseModel._result.call_center_remarks)
                            mBinding?.etInputA?.setText(userResponseModel._result.input_is_service_provider_engaged)
                            mBinding?.etRemarkA?.setText(userResponseModel._result.is_service_provider_engaged_remaks)
                            mBinding?.etInputB?.setText(userResponseModel._result.input_is_building_provided_operation_seats)
                            mBinding?.etRemarkB?.setText(userResponseModel._result.is_building_provided_operation_seats_remarks)
                            mBinding?.etInputC?.setText(userResponseModel._result.input_are_operators_engaged)
                            mBinding?.etRemarkC?.setText(userResponseModel._result.are_operators_engaged_remarks)
                            mBinding?.etInputD?.setText(userResponseModel._result.input_is_app_crm_place)
                            mBinding?.etRemarkD?.setText(userResponseModel._result.is_app_crm_place_remarks)
                            mBinding?.etInputE?.setText(userResponseModel._result.input_are_adequate_staff)
                            mBinding?.etRemarkE?.setText(userResponseModel._result.are_adequate_staff_remarks)
                            mBinding?.etInputF?.setText(userResponseModel._result.input_data_compilation_analysis_done)
                            mBinding?.etRemarkF?.setText(userResponseModel._result.data_compilation_analysis_done_remarks)

                            mBinding?.tvNoFileOne?.text = if (userResponseModel._result.mechanism_operation_inputs.isNullOrEmpty()) {
                                "No file chosen"
                            } else {
                                userResponseModel._result.mechanism_operation_inputs
                            }

                            mBinding?.tvNoFileTwo?.text = if (userResponseModel._result.engagement_indicators_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.engagement_indicators_inputs
                            mBinding?.tvNoFileThree?.text = if (userResponseModel._result.procurement_procedure_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.procurement_procedure_inputs
                            mBinding?.tvNoFileFour?.text = if (userResponseModel._result.supply_procedure_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.supply_procedure_inputs
                            mBinding?.tvNoFileFive?.text = if (userResponseModel._result.is_monitoring_supervision_medic_equip_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.is_monitoring_supervision_medic_equip_inputs
                            mBinding?.tvNoFileSix?.text = if (userResponseModel._result.is_monitoring_supervision_fuel_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.is_monitoring_supervision_fuel_inputs
                            mBinding?.tvNoFileSeven?.text = if (userResponseModel._result.call_center_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.call_center_inputs
                            mBinding?.tvNoFileA?.text = if (userResponseModel._result.is_service_provider_engaged_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.is_service_provider_engaged_inputs
                            mBinding?.tvNoFileB?.text = if (userResponseModel._result.is_building_provided_operation_seats_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.is_building_provided_operation_seats_inputs
                            mBinding?.tvNoFileC?.text = if (userResponseModel._result.are_operators_engaged_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.are_operators_engaged_inputs
                            mBinding?.tvNoFileD?.text = if (userResponseModel._result.is_app_crm_place_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.is_app_crm_place_inputs
                            mBinding?.tvNoFileE?.text = if (userResponseModel._result.are_adequate_staff_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.are_adequate_staff_inputs
                            mBinding?.tvNoFileF?.text = if (userResponseModel._result.data_compilation_analysis_done_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.data_compilation_analysis_done_inputs



                        } else {
                            onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }
                    }
                }

            }
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            showBottomSheetDialog("State")
        }

        fun submit(view: View) {
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 2)
            } else {
                saveDataApi(null, 2)
            }

        }

        fun saveAsDraft(view: View) {
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 3)
            } else {
                saveDataApi(null, 3)
            }
        }
    }

    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }

    @SuppressLint("Range")
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
                                documentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                when (isFromApplication) {
                                    1 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    2 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    3 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    4 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    5 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    6 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    7 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    8 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    9 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    10 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    11 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    12 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    13 -> {
                                        uploadDocument(documentName, uri)
                                    }

                                    else -> {
                                        uploadDocument(documentName, uri)
                                    }

                                }


                            }
                        }
                    }
                }
            }
        }
    }

    private fun uploadDocument(DocumentName: String?, uri: Uri) {
        val requestBody = convertToRequestBody(this, uri)
        body = MultipartBody.Part.createFormData(
            "document_name",
            DocumentName,
            requestBody
        )

        viewModel.getProfileUploadFile(
            context = this,
            table_name = getString(R.string.mobile_veterinary_unit_state).toRequestBody(
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

    private fun valid(): Boolean{
        if (mBinding?.etInputOne?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, "Please Fill All The Input and Remark Fields")

            }
            return false
        }
        else if (mBinding?.etInputTwo?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputThree?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputSix?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputSeven?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputA?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputB?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputC?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputD?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputE?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etInputF?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkOne?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkTwo?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkThree?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkSix?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkSeven?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkA?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkB?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkC?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkD?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkE?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else if (mBinding?.etRemarkF?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else {return true}
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

        if (hasLocationPermissions()) {
            startService(Intent(this, LocationService::class.java))
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {

        if (valid()) {
            viewModel.getStateMobileVeterinaryUnitsAdd(
                this@AddNewMobileVeterinaryUnitState, true,
                StateMobileVeterinaryUnitAddRequest(
                    id = itemId,
                    role_id = getPreferenceOfScheme(
                        this@AddNewMobileVeterinaryUnitState,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        this@AddNewMobileVeterinaryUnitState,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddNewMobileVeterinaryUnitState,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id,
                    status = draft,
                    input_mechanism_operation = mBinding?.etInputOne?.text.toString(),
                    mechanism_operation_remarks = mBinding?.etRemarkOne?.text.toString(),
                    input_engagement_indicators = mBinding?.etInputTwo?.text.toString(),
                    engagement_indicators_remarks = mBinding?.etRemarkTwo?.text.toString(),
                    input_procurement_procedure = mBinding?.etInputThree?.text.toString(),
                    procurement_procedure_remarks = mBinding?.etRemarkThree?.text.toString(),
                    input_supply_procedure = mBinding?.etInputFour?.text.toString(),
                    supply_procedure_remarks = mBinding?.etRemarkFour?.text.toString(),
                    input_is_monitoring_supervision_medic_equip = mBinding?.etInputFive?.text.toString(),
                    is_monitoring_supervision_medic_equip_remarks = mBinding?.etRemarkFive?.text.toString(),
                    input_is_monitoring_supervision_fuel = mBinding?.etInputSix?.text.toString(),
                    is_monitoring_supervision_fuel_remarks = mBinding?.etRemarkSix?.text.toString(),
                    input_call_center = mBinding?.etInputSeven?.text.toString(),
                    call_center_remarks = mBinding?.etRemarkSeven?.text.toString(),
                    input_is_service_provider_engaged = mBinding?.etInputA?.text.toString(),
                    is_service_provider_engaged_remaks = mBinding?.etRemarkA?.text.toString(),
                    input_is_building_provided_operation_seats = mBinding?.etInputB?.text.toString(),
                    is_building_provided_operation_seats_remarks = mBinding?.etRemarkB?.text.toString(),
                    input_are_operators_engaged = mBinding?.etInputC?.text.toString(),
                    are_operators_engaged_remarks = mBinding?.etRemarkC?.text.toString(),
                    input_is_app_crm_place = mBinding?.etInputD?.text.toString(),
                    is_app_crm_place_remarks = mBinding?.etRemarkD?.text.toString(),
                    input_are_adequate_staff = mBinding?.etInputE?.text.toString(),
                    are_adequate_staff_remarks = mBinding?.etRemarkE?.text.toString(),
                    input_data_compilation_analysis_done = mBinding?.etInputF?.text.toString(),
                    data_compilation_analysis_done_remarks = mBinding?.etRemarkF?.text.toString(),
                    mechanism_operation_inputs = mBinding?.tvNoFileOne?.text.toString(),
                    engagement_indicators_inputs = mBinding?.tvNoFileTwo?.text.toString(),
                    procurement_procedure_inputs = mBinding?.tvNoFileThree?.text.toString(),
                    supply_procedure_inputs = mBinding?.tvNoFileFour?.text.toString(),
                    is_monitoring_supervision_medic_equip_inputs = mBinding?.tvNoFileFive?.text.toString(),
                    is_monitoring_supervision_fuel_inputs = mBinding?.tvNoFileSix?.text.toString(),
                    call_center_inputs = mBinding?.tvNoFileSeven?.text.toString(),
                    is_service_provider_engaged_inputs = mBinding?.tvNoFileA?.text.toString(),
                    is_building_provided_operation_seats_inputs = mBinding?.tvNoFileB?.text.toString(),
                    are_operators_engaged_inputs = mBinding?.tvNoFileC?.text.toString(),
                    is_app_crm_place_inputs = mBinding?.tvNoFileD?.text.toString(),
                    are_adequate_staff_inputs = mBinding?.tvNoFileE?.text.toString(),
                    data_compilation_analysis_done_inputs = mBinding?.tvNoFileF?.text.toString(),
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }
                } else {
                    showSnackbar(mBinding!!.clParent, "Please wait for a sec and click again")
                }
            }
        } else {
            showLocationAlertDialog()
        }
    }


    private fun viewEditApi() {

        viewModel.getStateMobileVeterinaryUnitsAdd(
            this@AddNewMobileVeterinaryUnitState, true,
            StateMobileVeterinaryUnitAddRequest(
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
                )?.user_id,
                is_type = viewEdit
            )
        )
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

            "State" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding!!.tvState
            }


            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            stateId = id
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
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            rotatedDrawable,
            null
        )

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
                    val pastVisiblesItems: Int? =
                        layoutManager?.findFirstVisibleItemPosition()
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
}