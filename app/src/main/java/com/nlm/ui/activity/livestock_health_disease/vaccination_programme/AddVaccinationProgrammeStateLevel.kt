package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.StateVaccinationProgrammeAddRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddVaccinationProgrammeStateLevel :
    BaseActivity<ActivityAddVaccinationProgrammeStateLevelBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_state_level
    private var mBinding: ActivityAddVaccinationProgrammeStateLevelBinding? = null
    private var viewModel = ViewModel()
    private var stateList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var totalPage = 1
    private var loading = true
    private var itemId: Int? = null
    private var stateId: Int? = null // Store selected state
    private var viewEdit: String? = null
    private var uploadedDocumentName: String? = null
    private var dialogDocName: TextView? = null
    private var documentName: String? = null
    var body: MultipartBody.Part? = null
    var isFromApplication = 0
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        }
    }

    override fun onResume() {
        super.onResume()
//        this.registerReceiver(
//            locationReceiver,
//            IntentFilter("LOCATION_UPDATED")
//        )
    }

    override fun onPause() {
        super.onPause()
//        this.unregisterReceiver(locationReceiver)
    }

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
    }

    override fun setVariables() {
        if (viewEdit == "view") {
            mBinding?.etInput1a?.isEnabled = false
            mBinding?.etInput1b?.isEnabled = false
            mBinding?.etInput1c?.isEnabled = false
            mBinding?.etInput1d?.isEnabled = false
            mBinding?.etInput1e?.isEnabled = false
            mBinding?.etInput2?.isEnabled = false
            mBinding?.etInput3?.isEnabled = false
            mBinding?.etRemark1a?.isEnabled = false
            mBinding?.etRemark1b?.isEnabled = false
            mBinding?.etRemark1c?.isEnabled = false
            mBinding?.etRemark1d?.isEnabled = false
            mBinding?.etRemark1e?.isEnabled = false
            mBinding?.etRemark2?.isEnabled = false
            mBinding?.etRemark3?.isEnabled = false
            mBinding?.tvChooseFile1a?.isEnabled = false
            mBinding?.tvChooseFile1b?.isEnabled = false
            mBinding?.tvChooseFile1c?.isEnabled = false
            mBinding?.tvChooseFile1d?.isEnabled = false
            mBinding?.tvChoosefile1e?.isEnabled = false
            mBinding?.tvChooseFile2?.isEnabled = false
            mBinding?.tvChooseFile3?.isEnabled = false
            mBinding?.llSaveDraftAndSubmit?.hideView()
            viewEditApi(viewEdit)
        }
        if (viewEdit == "edit") {
            viewEditApi(viewEdit)
        }
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name?.isNotEmpty() == true
        ) {
            stateId = getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_code
            mBinding?.tvState?.text = getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name
            mBinding?.tvState?.isEnabled = false
        }
    }

    override fun setObservers() {
        viewModel.stateVaccinationProgrammerAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.main?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                        mBinding?.main?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                    }
                    else if (viewEdit == "view" || viewEdit == "edit") {
                        if (savedAsEdit) {
                            onBackPressedDispatcher.onBackPressed()
                            return@observe
                        }
                        mBinding?.etInput1a?.setText(userResponseModel._result.schedule_vaccination_focal_point_input)
                        mBinding?.etInput1b?.setText(userResponseModel._result.schedule_vaccination_timeline_inputs)
                        mBinding?.etInput1c?.setText(userResponseModel._result.schedule_vaccination_arrangement_inputs)
                        mBinding?.etInput1d?.setText(userResponseModel._result.schedule_vaccination_cold_chain_avail_inputs)
                        mBinding?.etInput1e?.setText(userResponseModel._result.schedule_vaccination_assign_areas_inputs)
                        mBinding?.etInput2?.setText(userResponseModel._result.seromonitoring_facilities_input)
                        mBinding?.etInput3?.setText(userResponseModel._result.process_plan_monitoring_inputs)
                        mBinding?.etRemark1a?.setText(userResponseModel._result.schedule_vaccination_focal_point_remark)
                        mBinding?.etRemark1b?.setText(userResponseModel._result.schedule_vaccination_timeline_remarks)
                        mBinding?.etRemark1c?.setText(userResponseModel._result.schedule_vaccination_arrangement_remarks)
                        mBinding?.etRemark1d?.setText(userResponseModel._result.schedule_vaccination_cold_chain_avail_remarks)
                        mBinding?.etRemark1e?.setText(userResponseModel._result.schedule_vaccination_assign_areas_remarks)
                        mBinding?.etRemark2?.setText(userResponseModel._result.seromonitoring_facilitie_remarks)
                        mBinding?.etRemark3?.setText(userResponseModel._result.process_plan_monitoring_remarks)

                        mBinding?.etChooseFile1a?.text =
                            if (userResponseModel._result.schedule_vaccination_focal_point_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.schedule_vaccination_focal_point_upload
                        mBinding?.etChooseFile1b?.text =
                            if (userResponseModel._result.schedule_vaccination_timeline_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.schedule_vaccination_timeline_upload
                        mBinding?.etChooseFile1c?.text =
                            if (userResponseModel._result.schedule_vaccination_arrangement_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.schedule_vaccination_arrangement_upload
                        mBinding?.etChooseFile1d?.text =
                            if (userResponseModel._result.schedule_vaccination_cold_chain_avail_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.schedule_vaccination_cold_chain_avail_upload
                        mBinding?.etChoosefile1e?.text =
                            if (userResponseModel._result.schedule_vaccination_assign_areas_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.schedule_vaccination_assign_areas_upload
                        mBinding?.etChooseFile2?.text =
                            if (userResponseModel._result.seromonitoring_facilities_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.seromonitoring_facilities_upload
                        mBinding?.etChooseFile3?.text =
                            if (userResponseModel._result.process_plan_monitoring_upload.isNullOrEmpty()) "No file chosen" else userResponseModel._result.process_plan_monitoring_upload
                    }

                    mBinding?.main?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
            }
        }
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateList.clear()

                        val remainingCount = userResponseModel.total_count % 100
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 100
                            count
                        } else {
                            val count = userResponseModel.total_count / 100
                            count + 1
                        }
                    }
                    stateList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()
                }
            }
        }
        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.main?.let { it1 ->
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
                            mBinding?.etChooseFile1a?.text = uploadedDocumentName

                        }

                        2 -> {
                            mBinding?.etChooseFile1b?.text = uploadedDocumentName

                        }

                        3 -> {
                            mBinding?.etChooseFile1c?.text = uploadedDocumentName

                        }

                        4 -> {
                            mBinding?.etChooseFile1d?.text = uploadedDocumentName

                        }

                        5 -> {
                            mBinding?.etChoosefile1e?.text = uploadedDocumentName

                        }

                        6 -> {
                            mBinding?.etChooseFile2?.text = uploadedDocumentName

                        }

                        7 -> {
                            mBinding?.etChooseFile3?.text = uploadedDocumentName

                        }

                        else -> {
                            dialogDocName?.text = uploadedDocumentName

                        }
                    }
                    mBinding?.main?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
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
            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            else{
                savedAsDraft = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 2)
            } else {
                saveDataApi(null, 2)
            }
        }

        fun saveAsDraft(view: View) {
            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            else{
                savedAsDraft = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 3)
            } else {
                saveDataApi(null, 3)
            }
        }

        fun chooseFile1a(view: View) {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile1b(view: View) {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile1c(view: View) {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile1d(view: View) {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile1e(view: View) {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile2(view: View) {
            isFromApplication = 6
            openOnlyPdfAccordingToPosition()
        }

        fun chooseFile3(view: View) {
            isFromApplication = 7
            openOnlyPdfAccordingToPosition()
        }

    }

    private fun viewEditApi(viewEdit: String?) {
        viewModel.getStateVaccinationProgrammeAdd(
            this@AddVaccinationProgrammeStateLevel, true,
            StateVaccinationProgrammeAddRequest(
                state_code = getPreferenceOfScheme(
                    this@AddVaccinationProgrammeStateLevel,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this@AddVaccinationProgrammeStateLevel,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                role_id = getPreferenceOfScheme(
                    this@AddVaccinationProgrammeStateLevel,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit,
                id = itemId,
            )
        )
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

//        if (hasLocationPermissions()) {
//            val intent = Intent(this, LocationService::class.java)
//            startService(intent)
//            lifecycleScope.launch {
//                delay(1000) // Delay for 2 seconds
//                if (latitude != null && longitude != null) {
                    if (valid()) {
                        viewModel.getStateVaccinationProgrammeAdd(
                            this@AddVaccinationProgrammeStateLevel, true,
                            StateVaccinationProgrammeAddRequest(
                                status = draft,
                                state_code = stateId,
                                user_id = getPreferenceOfScheme(
                                    this@AddVaccinationProgrammeStateLevel,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id,
                                role_id = getPreferenceOfScheme(
                                    this@AddVaccinationProgrammeStateLevel,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                schedule_vaccination_focal_point_input = mBinding?.etInput1a?.text.toString(),
                                schedule_vaccination_focal_point_remark = mBinding?.etRemark1a?.text.toString(),
                                schedule_vaccination_focal_point_upload = mBinding?.etChooseFile1a?.text.toString(),
                                schedule_vaccination_timeline_inputs = mBinding?.etInput1b?.text.toString(),
                                schedule_vaccination_timeline_remarks = mBinding?.etRemark1b?.text.toString(),
                                schedule_vaccination_timeline_upload = mBinding?.etChooseFile1b?.text.toString(),
                                schedule_vaccination_arrangement_inputs = mBinding?.etInput1c?.text.toString(),
                                schedule_vaccination_arrangement_remarks = mBinding?.etRemark1c?.text.toString(),
                                schedule_vaccination_arrangement_upload = mBinding?.etChooseFile1c?.text.toString(),
                                schedule_vaccination_cold_chain_avail_inputs = mBinding?.etInput1d?.text.toString(),
                                schedule_vaccination_cold_chain_avail_remarks = mBinding?.etRemark1d?.text.toString(),
                                schedule_vaccination_cold_chain_avail_upload = mBinding?.etChooseFile1d?.text.toString(),
                                schedule_vaccination_assign_areas_inputs = mBinding?.etInput1e?.text.toString(),
                                schedule_vaccination_assign_areas_remarks = mBinding?.etRemark1e?.text.toString(),
                                schedule_vaccination_assign_areas_upload = mBinding?.etChoosefile1e?.text.toString(),
                                seromonitoring_facilities_input = mBinding?.etInput2?.text.toString(),
                                seromonitoring_facilitie_remarks = mBinding?.etRemark2?.text.toString(),
                                seromonitoring_facilities_upload = mBinding?.etChooseFile2?.text.toString(),
                                process_plan_monitoring_inputs = mBinding?.etInput3?.text.toString(),
                                process_plan_monitoring_remarks = mBinding?.etRemark3?.text.toString(),
                                process_plan_monitoring_upload = mBinding?.etChooseFile3?.text.toString(),
                                id = itemId,
                                longitude = longitude,
                                latitude = latitude
                            )
                        )
                    }
//                }
//                else {
//                    mBinding?.main?.let {
//                        showSnackbar(
//                            it,
//                            getString(R.string.no_location_fetched)
//                        )
//                    }
//                }
//            }
//        } else {
//            showLocationAlertDialog()
//        }
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
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvState
            }


            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView?.text = selectedItem
            stateId = id
            selectedTextView?.setTextColor(ContextCompat.getColor(this, R.color.black))
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

    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
    }

    private fun valid(): Boolean {
        if (mBinding?.etInput3?.text.toString()
                .isEmpty() && mBinding?.etInput2?.text.toString()
                .isEmpty() && mBinding?.etInput1a?.text.toString()
                .isEmpty() && mBinding?.etInput1b?.text.toString()
                .isEmpty() && mBinding?.etInput1c?.text.toString()
                .isEmpty() && mBinding?.etInput1d?.text.toString()
                .isEmpty() && mBinding?.etInput1e?.text.toString()
                .isEmpty() && mBinding?.etRemark3?.text.toString()
                .isEmpty() && mBinding?.etRemark2?.text.toString()
                .isEmpty() && mBinding?.etRemark1a?.text.toString()
                .isEmpty() && mBinding?.etRemark1b?.text.toString()
                .isEmpty() && mBinding?.etRemark1c?.text.toString()
                .isEmpty() && mBinding?.etRemark1d?.text.toString()
                .isEmpty() && mBinding?.etRemark1e?.text.toString().isEmpty()
        ) {
            mBinding?.main?.let {
                showSnackbar(it, getString(R.string.please_fill_all_the_input_and_remark_fields))
            }
            return false
        }
        return true
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
            document_name = body,
            user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            table_name = getString(R.string.state_vaccination_programme).toRequestBody(MultipartBody.FORM),
        )
    }

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                100,
                "States",
                currentPage,
                null,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
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
}