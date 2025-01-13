package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.nlm.R
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.StateVaccinationProgrammeAddRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.URIPathHelper
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.getFileType
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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
    private var TableName: String? = null
    var body: MultipartBody.Part? = null
    var isFromApplication = 0
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
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

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API level 26
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(locationReceiver)
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
            mBinding?.ivDelete1a?.hideView()
            mBinding?.ivDelete1b?.hideView()
            mBinding?.ivDelete1c?.hideView()
            mBinding?.ivDelete1d?.hideView()
            mBinding?.ivDelete1e?.hideView()
            mBinding?.ivDelete2?.hideView()
            mBinding?.ivDelete3?.hideView()
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
                    TableName=userResponseModel.fileurl
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


                            if (userResponseModel._result.schedule_vaccination_focal_point_upload.isNullOrEmpty()) {
                                mBinding?.etChooseFile1a?.text = "No file chosen"
                            } else {
                                mBinding?.llUpload1a?.showView()
                                mBinding?.tvDocumentName1a?.text = userResponseModel._result.schedule_vaccination_focal_point_upload
                                mBinding?.ivPic1a?.let { it1 -> GlideImage(it1,userResponseModel._result.schedule_vaccination_focal_point_upload)
                                    mBinding?.etChooseFile1a?.text = "Uploaded"}
                            }

                            if (userResponseModel._result.schedule_vaccination_timeline_upload.isNullOrEmpty()) {
                                mBinding?.etChooseFile1b?.text ="No file chosen"
                            } else {
                                mBinding?.llUpload1b?.showView()
                                mBinding?.tvDocumentName1b?.text = userResponseModel._result.schedule_vaccination_timeline_upload
                                mBinding?.ivPic1b?.let { it1 -> GlideImage(it1,userResponseModel._result.schedule_vaccination_timeline_upload)
                                    mBinding?.etChooseFile1b?.text ="Uploaded"}
                            }

                            if (userResponseModel._result.schedule_vaccination_arrangement_upload.isNullOrEmpty()) {
                                mBinding?.etChooseFile1c?.text =   "No file chosen"
                            }else {
                                mBinding?.llUpload1c?.showView()
                                mBinding?.tvDocumentName1c?.text = userResponseModel._result.schedule_vaccination_arrangement_upload
                                mBinding?.ivPic1c?.let { it1 -> GlideImage(it1,userResponseModel._result.schedule_vaccination_arrangement_upload)
                                    mBinding?.etChooseFile1c?.text ="Uploaded"}
                            }

                            if (userResponseModel._result.schedule_vaccination_cold_chain_avail_upload.isNullOrEmpty()) {
                                mBinding?.etChooseFile1d?.text =  "No file chosen"
                            } else{
                                mBinding?.llUpload1d?.showView()
                                mBinding?.tvDocumentName1d?.text = userResponseModel._result.schedule_vaccination_cold_chain_avail_upload
                                mBinding?.ivPic1d?.let { it1 -> GlideImage(it1,userResponseModel._result.schedule_vaccination_cold_chain_avail_upload)
                                    mBinding?.etChooseFile1d?.text =  "Uploaded"}
                            }

                            if (userResponseModel._result.schedule_vaccination_assign_areas_upload.isNullOrEmpty()){
                                mBinding?.etChoosefile1e?.text =   "No file chosen"
                            } else {
                                mBinding?.llUpload1e?.showView()
                                mBinding?.tvDocumentName1e?.text =  userResponseModel._result.schedule_vaccination_assign_areas_upload
                                mBinding?.ivPic1e?.let { it1 -> GlideImage(it1,userResponseModel._result.schedule_vaccination_assign_areas_upload)
                                    mBinding?.etChoosefile1e?.text =   "Uploaded"}
                            }

                            if (userResponseModel._result.seromonitoring_facilities_upload.isNullOrEmpty()) {
                                mBinding?.etChooseFile2?.text =  "No file chosen"
                            } else{
                                mBinding?.llUpload2?.showView()
                                mBinding?.tvDocumentName2?.text = userResponseModel._result.seromonitoring_facilities_upload
                                mBinding?.ivPic2?.let { it1 -> GlideImage(it1,userResponseModel._result.seromonitoring_facilities_upload)
                                    mBinding?.etChooseFile2?.text =  "Uploaded"}
                            }

                            if (userResponseModel._result.process_plan_monitoring_upload.isNullOrEmpty()){
                                mBinding?.etChooseFile3?.text =   "No file chosen"
                            } else {
                                mBinding?.llUpload3?.showView()
                                mBinding?.tvDocumentName3?.text =  userResponseModel._result.process_plan_monitoring_upload
                                mBinding?.ivPic3?.let { it1 -> GlideImage(it1, userResponseModel._result.process_plan_monitoring_upload)
                                    mBinding?.etChooseFile3?.text =   "Uploaded"}
                            }
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
                        mBinding?.llUpload1a?.hideView()
                        mBinding?.llUpload1b?.hideView()
                        mBinding?.llUpload1c?.hideView()
                        mBinding?.llUpload1d?.hideView()
                        mBinding?.llUpload1e?.hideView()
                        mBinding?.llUpload2?.hideView()
                        mBinding?.llUpload3?.hideView()

                    }

                } else {
                    uploadedDocumentName = userResponseModel._result.document_name
                    TableName=userResponseModel._result.table_name
                    dialogDocName?.text = userResponseModel._result.document_name

                    when (isFromApplication) {
                        1 -> {
                            mBinding?.llUpload1a?.showView()
                            mBinding?.tvDocumentName1a?.text = uploadedDocumentName
                            mBinding?.etChooseFile1a?.text = "Uploaded"

                        }

                        2 -> {
                            mBinding?.tvDocumentName1b?.text = uploadedDocumentName
                            mBinding?.etChooseFile1b?.text = "Uploaded"

                        }

                        3 -> {
                            mBinding?.tvDocumentName1c?.text = uploadedDocumentName
                            mBinding?.etChooseFile1c?.text = "Uploaded"

                        }

                        4 -> {
                            mBinding?.tvDocumentName1d?.text = uploadedDocumentName
                            mBinding?.etChooseFile1d?.text = "Uploaded"

                        }

                        5 -> {
                            mBinding?.tvDocumentName1e?.text = uploadedDocumentName
                            mBinding?.etChoosefile1e?.text = "Uploaded"

                        }

                        6 -> {
                            mBinding?.tvDocumentName2?.text = uploadedDocumentName
                            mBinding?.etChooseFile2?.text = "Uploaded"

                        }

                        7 -> {
                            mBinding?.tvDocumentName3?.text = uploadedDocumentName
                            mBinding?.etChooseFile3?.text = "Uploaded"
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
private fun GlideImage(imageView:ShapeableImageView,   uploadedDocumentName:String?){
    val url=getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(uploadedDocumentName)
    val (isSupported, fileExtension) = getFileType(uploadedDocumentName.toString())

    if (isSupported) {


        when (fileExtension) {
            "pdf" -> {
                val downloader = AndroidDownloader(this)
                imageView.let {
                    Glide.with(this).load(R.drawable.ic_pdf).placeholder(R.drawable.ic_pdf).into(
                        it
                    )

                }
                imageView.setOnClickListener {
                    if (!uploadedDocumentName.isNullOrEmpty()) {
                        downloader.downloadFile(url, uploadedDocumentName!!)
                        mBinding?.let { it1 -> showSnackbar(it1.main,"Download started") }

                    }
                    else{
                        mBinding?.let { it1 -> showSnackbar(it1.main,"No document found") }
                    }
                }

            }

            "png" -> {
                imageView.let {
                    Glide.with(this).load(url).placeholder(R.drawable.ic_image_placeholder).into(
                        it
                    )
                    imageView.setOnClickListener {
                        Utility.showImageDialog(
                            this,
                            url
                        )
                    }

                }
            }

            "jpg" -> {
                imageView.let {
                    Glide.with(this).load(url).placeholder(R.drawable.ic_image_placeholder).into(
                        it
                    )
                    imageView.setOnClickListener {
                        Utility.showImageDialog(
                            this,
                            url
                        )
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

        fun deleteDocumentOneA(view: View){
            mBinding?.llUpload1a?.hideView()
            mBinding?.tvDocumentName1a?.text = null
            mBinding?.etChooseFile1a?.text = ""
            body = null
        }

        fun chooseFile1b(view: View) {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentOneB(view: View){
            mBinding?.llUpload1b?.hideView()
            mBinding?.tvDocumentName1b?.text = null
            mBinding?.tvDocumentName1b?.text = ""
            body = null
        }

        fun chooseFile1c(view: View) {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentOneC(view: View){
            mBinding?.llUpload1c?.hideView()
            mBinding?.tvDocumentName1c?.text = null
            mBinding?.tvDocumentName1c?.text = ""
            body = null
        }

        fun chooseFile1d(view: View) {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentOneD(view: View){
            mBinding?.llUpload1d?.hideView()
            mBinding?.tvDocumentName1d?.text = null
            mBinding?.tvDocumentName1d?.text = ""
            body = null
        }

        fun chooseFile1e(view: View) {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentOneE(view: View){
            mBinding?.llUpload1e?.hideView()
            mBinding?.tvDocumentName1e?.text = null
            mBinding?.tvDocumentName1e?.text = ""
            body = null
        }

        fun chooseFile2(view: View) {
            isFromApplication = 6
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentTwo(view: View){
            mBinding?.llUpload2?.hideView()
            mBinding?.tvDocumentName2?.text = null
            mBinding?.tvDocumentName2?.text = ""
            body = null
        }

        fun chooseFile3(view: View) {
            isFromApplication = 7
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentThree(view: View){
            mBinding?.llUpload3?.hideView()
            mBinding?.tvDocumentName3?.text = null
            mBinding?.tvDocumentName3?.text = ""
            body = null
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

        if (hasLocationPermissions()) {
            val intent = Intent(this, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {

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
                                schedule_vaccination_focal_point_upload = mBinding?.tvDocumentName1a?.text.toString(),
                                schedule_vaccination_timeline_inputs = mBinding?.etInput1b?.text.toString(),
                                schedule_vaccination_timeline_remarks = mBinding?.etRemark1b?.text.toString(),
                                schedule_vaccination_timeline_upload = mBinding?.tvDocumentName1b?.text.toString(),
                                schedule_vaccination_arrangement_inputs = mBinding?.etInput1c?.text.toString(),
                                schedule_vaccination_arrangement_remarks = mBinding?.etRemark1c?.text.toString(),
                                schedule_vaccination_arrangement_upload = mBinding?.tvDocumentName1c?.text.toString(),
                                schedule_vaccination_cold_chain_avail_inputs = mBinding?.etInput1d?.text.toString(),
                                schedule_vaccination_cold_chain_avail_remarks = mBinding?.etRemark1d?.text.toString(),
                                schedule_vaccination_cold_chain_avail_upload = mBinding?.tvDocumentName1d?.text.toString(),
                                schedule_vaccination_assign_areas_inputs = mBinding?.etInput1e?.text.toString(),
                                schedule_vaccination_assign_areas_remarks = mBinding?.etRemark1e?.text.toString(),
                                schedule_vaccination_assign_areas_upload = mBinding?.tvDocumentName1e?.text.toString(),
                                seromonitoring_facilities_input = mBinding?.etInput2?.text.toString(),
                                seromonitoring_facilitie_remarks = mBinding?.etRemark2?.text.toString(),
                                seromonitoring_facilities_upload = mBinding?.tvDocumentName2?.text.toString(),
                                process_plan_monitoring_inputs = mBinding?.etInput3?.text.toString(),
                                process_plan_monitoring_remarks = mBinding?.etRemark3?.text.toString(),
                                process_plan_monitoring_upload = mBinding?.tvDocumentName3?.text.toString(),
                                id = itemId,
                                longitude = longitude,
                                latitude = latitude
                            )
                        )
                    }
                }
                else {
                    mBinding?.main?.let {
                        showSnackbar(
                            it,
                            "Please wait for a sec and click again"
                        )
                    }
                }
            }
        } else {
            showLocationAlertDialog()
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
        if (mBinding?.etInput3?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(it, getString(R.string.please_fill_all_the_input_and_remark_fields))
            }
            return false
        } else if (mBinding?.etInput2?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(it, getString(R.string.please_fill_all_the_input_and_remark_fields))
            }
            return false
        } else if (mBinding?.etInput1a?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInput1b?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInput1c?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInput1d?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInput1e?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemark3?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemark2?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemark1a?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemark1b?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        }
        else if (mBinding?.etRemark1c?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        }
        else if (mBinding?.etRemark1d?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        }
        else if (mBinding?.etRemark1e?.text.toString().isEmpty()) {
            mBinding?.main?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        }

        else
            return true
    }
    override fun showImage(bitmap: Bitmap) {
        // Override to display the image in this activity
        Log.d("TAG", isFromApplication.toString())
        when (isFromApplication) {
            1 -> {
                mBinding?.llUpload1a?.showView()
                mBinding?.ivPic1a?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic1a?.setOnClickListener {

                        Utility.showImageDialogFileUrl(
                            this,
                            imageFile
                        )

                }

                uploadImage(imageFile)
            }

            2 -> {
                mBinding?.llUpload1b?.showView()
                mBinding?.ivPic1b?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic1b?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }

            3 -> {
                mBinding?.llUpload1c?.showView()
                mBinding?.ivPic1c?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic1c?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }

            4 -> {
                mBinding?.llUpload1d?.showView()
                mBinding?.ivPic1d?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic1d?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }

            5 -> {
                mBinding?.llUpload1e?.showView()
                mBinding?.ivPic1e?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic1e?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }

            6 -> {
                mBinding?.llUpload2?.showView()
                mBinding?.ivPic2?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic2?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }

            7 -> {
                mBinding?.llUpload3?.showView()
                mBinding?.ivPic3?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPic3?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )

                }
                uploadImage(imageFile)
            }
        }
    }
    private fun openOnlyPdfAccordingToPosition() {
        checkStoragePermission(this@AddVaccinationProgrammeStateLevel)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAPTURE_IMAGE_REQUEST -> {
                    Log.d("ISFROM", isFromApplication.toString())
                    val bitmap = data?.extras?.get("data") as Bitmap
                }

                PICK_IMAGE -> {
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        val uriPathHelper = URIPathHelper()
                        val filePath = uriPathHelper.getPath(this, selectedImageUri)

                        val fileExtension =
                            filePath?.substringAfterLast('.', "").orEmpty().lowercase()
                        // Validate file extension
                        if (fileExtension in listOf("png", "jpg", "jpeg")) {
                            val file = filePath?.let { File(it) }

                            // Check file size (5 MB = 5 * 1024 * 1024 bytes)
                            file?.let {
                                val fileSizeInMB = it.length() / (1024 * 1024.0) // Convert to MB
                                if (fileSizeInMB <= 5) {
                                    when (isFromApplication) {
                                        1 -> {
                                            mBinding?.llUpload1a?.showView()
                                            mBinding?.ivPic1a?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic1a?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        2 -> {
                                            mBinding?.llUpload1b?.showView()
                                            mBinding?.ivPic1b?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic1b?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        3 -> {
                                            mBinding?.llUpload1c?.showView()
                                            mBinding?.ivPic1c?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic1c?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        4 -> {
                                            mBinding?.llUpload1d?.showView()
                                            mBinding?.ivPic1d?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic1d?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        5 -> {
                                            mBinding?.llUpload1e?.showView()
                                            mBinding?.ivPic1e?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic1e?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        6 -> {
                                            mBinding?.llUpload2?.showView()
                                            mBinding?.ivPic2?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic2?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        7 -> {
                                            mBinding?.llUpload3?.showView()
                                            mBinding?.ivPic3?.setImageURI(selectedImageUri)
                                            mBinding?.ivPic3?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }
                                        else -> {}
                                    }
                                } else {
                                    mBinding?.let { showSnackbar(it.main,"File size exceeds 5 MB") }
                                }
                            }
                        } else {
                            mBinding?.let { showSnackbar(it.main,"Format not supported") }
                        }
                    }
                }

                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )


                        val cursor = contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                val documentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                val fileSizeInBytes =
                                    it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                                val fileSizeInMB = fileSizeInBytes / (1024 * 1024.0) // Convert to MB

                                // Validate file size (5 MB = 5 * 1024 * 1024 bytes)
                                if (fileSizeInMB <= 5) {
                                    when (isFromApplication) {
                                        1 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload1a?.showView()
                                            mBinding?.ivPic1a?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        2 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload1b?.showView()
                                            mBinding?.ivPic1b?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        3 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload1c?.showView()
                                            mBinding?.ivPic1c?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        4 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload1d?.showView()
                                            mBinding?.ivPic1d?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        5 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload1e?.showView()
                                            mBinding?.ivPic1e?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        6 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload2?.showView()
                                            mBinding?.ivPic2?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        7 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUpload3?.showView()
                                            mBinding?.ivPic3?.setImageResource(R.drawable.ic_pdf)
                                        }
                                        else -> {
                                            uploadDocument(documentName, uri)
                                        }

                                    }
//                                    DocumentName = documentName
//                                    val requestBody = convertToRequestBody(this, uri)
//                                    body = MultipartBody.Part.createFormData(
//                                        "document_name",
//                                        documentName,
//                                        requestBody
//                                    )
//                                    viewModel.getProfileUploadFile(
//                                        context = this,
//                                        document_name = body,
//                                        user_id = getPreferenceOfScheme(
//                                            this,
//                                            AppConstants.SCHEME,
//                                            Result::class.java
//                                        )?.user_id,
//                                        table_name = getString(R.string.import_of_exotic_goat).toRequestBody(
//                                            MultipartBody.FORM
//                                        ),
//                                    )
                                } else {
                                    mBinding?.let { showSnackbar(it.main,"File size exceeds 5 MB") }
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

    private fun uploadImage(file: File) {
        lifecycleScope.launch {
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body =
                MultipartBody.Part.createFormData(
                    "document_name",
                    file.name, reqFile
                )
            viewModel.getProfileUploadFile(
                context = this@AddVaccinationProgrammeStateLevel,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddVaccinationProgrammeStateLevel,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.state_vaccination_programme).toRequestBody(MultipartBody.FORM),
            )
        }
    }

}