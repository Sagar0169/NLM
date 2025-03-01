package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.annotation.SuppressLint
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
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitDistrictBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.DistrictMobileVeterinaryUnitAddRequest
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
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

class AddNewMobileVeterinaryUnitDistrict : BaseActivity<ActivityAddNewMobileVeterinaryUnitDistrictBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitDistrictBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var viewModel = ViewModel()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var districtId: Int? = null // Store selected state
    private var isFromApplication = 0
    private var uploadedDocumentName: String? = null
    private var dialogDocName: TextView? = null
    private var documentName: String? = null
    private var TableName: String? = null
    var body: MultipartBody.Part? = null
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
        get() = R.layout.activity_add_new_mobile_veterinary_unit_district

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
            mBinding?.tvDistrict?.isEnabled = false
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


            mBinding?.etChooseOne?.isEnabled = false
            mBinding?.etChooseTwo?.isEnabled = false
            mBinding?.etChooseThree?.isEnabled = false
            mBinding?.etChooseFour?.isEnabled = false
            mBinding?.etChooseFive?.isEnabled = false
            mBinding?.llSaveDraftAndSubmit?.hideView()

            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
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
        unregisterReceiver(locationReceiver)
    }


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun state(view: View) {
            showBottomSheetDialog("State")
        }
        fun district(view: View) {
            showBottomSheetDialog("District")
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
        fun uploadFileOne(view: View) {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentOne(view: View){
            mBinding?.llUploadOne?.hideView()
            mBinding?.tvDocumentNameOne?.text = null
            mBinding?.tvNoFileOne?.text = "No File Chosen"
            body = null
        }

        fun uploadFileTwo(view: View) {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentTwo(view: View){
            mBinding?.llUploadTwo?.hideView()
            mBinding?.tvDocumentNameTwo?.text = null
            mBinding?.tvNoFileTwo?.text = "No File Chosen"
            body = null
        }

        fun uploadFileThree(view: View) {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentThree(view: View){
            mBinding?.llUploadThree?.hideView()
            mBinding?.tvDocumentNameThree?.text = null
            mBinding?.tvNoFileThree?.text = "No File Chosen"
            body = null
        }

        fun uploadFileFour(view: View) {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentFour(view: View){
            mBinding?.llUploadFour?.hideView()
            mBinding?.tvDocumentNameFour?.text = null
            mBinding?.tvNoFileFour?.text = "No File Chosen"
            body = null
        }

        fun uploadFileFive(view: View) {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }

        fun deleteDocumentFive(view: View){
            mBinding?.llUploadFive?.hideView()
            mBinding?.tvDocumentNameFive?.text = null
            mBinding?.tvNoFileFive?.text = "No File Chosenn"
            body = null
        }

    }

    private fun valid(): Boolean{
        if (mBinding?.tvDistrict?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, "Please Select District")

            }
            return false
        }
        else if (mBinding?.etInputOne?.text.toString().isEmpty()) {
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
        else  if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        }
        else {
            return true
        }
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
        if (hasLocationPermissions()) {
            startService(Intent(this, LocationService::class.java))
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {



         if(valid()) {
             viewModel.getDistrictMobileVeterinaryUnitsAdd(
                 this@AddNewMobileVeterinaryUnitDistrict, true,
                 DistrictMobileVeterinaryUnitAddRequest(
                     id = itemId,
                     role_id = getPreferenceOfScheme(
                         this@AddNewMobileVeterinaryUnitDistrict,
                         AppConstants.SCHEME,
                         Result::class.java
                     )?.role_id,
                     state_code = getPreferenceOfScheme(
                         this@AddNewMobileVeterinaryUnitDistrict,
                         AppConstants.SCHEME,
                         Result::class.java
                     )?.state_code,
                     user_id = getPreferenceOfScheme(
                         this@AddNewMobileVeterinaryUnitDistrict,
                         AppConstants.SCHEME,
                         Result::class.java
                     )?.user_id,
                     status = draft,
                     district_code = districtId,
                     input_mechanism_medicines = mBinding?.etInputOne?.text.toString(),
                     mechanism_medicines_remaks = mBinding?.etRemarkOne?.text.toString(),
                     input_organize_awareness_camp = mBinding?.etInputTwo?.text.toString(),
                     organize_awareness_camp_remarks = mBinding?.etRemarkTwo?.text.toString(),
                     input_distribution_medicines_role = mBinding?.etInputThree?.text.toString(),
                     distribution_medicines_role_remaks = mBinding?.etRemarkThree?.text.toString(),
                     input_distribution_fuel_role = mBinding?.etInputFour?.text.toString(),
                     distribution_fuel_role_inputs_remarks = mBinding?.etRemarkFour?.text.toString(),
                     input_medicine_requirement = mBinding?.etInputFive?.text.toString(),
                     medicine_requirement_remarks = mBinding?.etRemarkFive?.text.toString(),

                     mechanism_medicines_inputs = mBinding?.tvDocumentNameOne?.text.toString(),
                     organize_awareness_camp_inputs = mBinding?.tvDocumentNameTwo?.text.toString(),
                     distribution_medicines_role_inputs = mBinding?.tvDocumentNameThree?.text.toString(),
                     distribution_fuel_role_inputs = mBinding?.tvDocumentNameFour?.text.toString(),
                     medicine_requirement_inputs = mBinding?.tvDocumentNameFive?.text.toString(),
                     latitude = latitude,
                     longitude = longitude,

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

        viewModel.getDistrictMobileVeterinaryUnitsAdd(
            this@AddNewMobileVeterinaryUnitDistrict, true,
            DistrictMobileVeterinaryUnitAddRequest(
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

    private fun uploadImage(file: File) {
        lifecycleScope.launch {
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body =
                MultipartBody.Part.createFormData(
                    "document_name",
                    file.name, reqFile
                )
            viewModel.getProfileUploadFile(
                context = this@AddNewMobileVeterinaryUnitDistrict,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddNewMobileVeterinaryUnitDistrict,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.mobile_veterinary_unit_district).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
    private fun GlideImage(imageView: ShapeableImageView, uploadedDocumentName:String?){
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
                            mBinding?.let { it1 -> showSnackbar(it1.clParent,"Download started") }

                        }
                        else{
                            mBinding?.let { it1 -> showSnackbar(it1.clParent,"No document found") }
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
                "jpeg" -> {
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
    private fun openOnlyPdfAccordingToPosition() {
        checkStoragePermission(this)
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
                                            mBinding?.llUploadOne?.showView()
                                            mBinding?.ivPicOne?.setImageURI(selectedImageUri)
                                            mBinding?.ivPicOne?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        2 -> {
                                            mBinding?.llUploadTwo?.showView()
                                            mBinding?.ivPicTwo?.setImageURI(selectedImageUri)
                                            mBinding?.ivPicTwo?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        3 -> {
                                            mBinding?.llUploadThree?.showView()
                                            mBinding?.ivPicThree?.setImageURI(selectedImageUri)
                                            mBinding?.ivPicThree?.setOnClickListener {

                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )

                                            }
                                            uploadImage(it)
                                        }

                                        4 -> {
                                            mBinding?.llUploadFour?.showView()
                                            mBinding?.ivPicFour?.setImageURI(selectedImageUri)
                                            mBinding?.ivPicFour?.setOnClickListener {
                                                Utility.showImageDialog(
                                                    this,
                                                    filePath
                                                )
                                            }
                                            uploadImage(it)
                                        }

                                        5 -> {
                                            mBinding?.llUploadFive?.showView()
                                            mBinding?.ivPicFive?.setImageURI(selectedImageUri)
                                            mBinding?.ivPicFive?.setOnClickListener {
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
                                    mBinding?.let { showSnackbar(it.clParent,"File size exceeds 5 MB") }
                                }
                            }
                        } else {
                            mBinding?.let { showSnackbar(it.clParent,"Format not supported") }
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
                                            mBinding?.llUploadOne?.showView()
                                            mBinding?.ivPicOne?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        2 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUploadTwo?.showView()
                                            mBinding?.ivPicTwo?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        3 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUploadThree?.showView()
                                            mBinding?.ivPicThree?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        4 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUploadFour?.showView()
                                            mBinding?.ivPicFour?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        5 -> {
                                            uploadDocument(documentName, uri)
                                            mBinding?.llUploadFive?.showView()
                                            mBinding?.ivPicFive?.setImageResource(R.drawable.ic_pdf)
                                        }

                                        else -> {
                                            uploadDocument(documentName, uri)
                                        }

                                    }
                                } else {
                                    mBinding?.let { showSnackbar(it.clParent,"File size exceeds 5 MB") }
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
            table_name = getString(R.string.mobile_veterinary_unit_district).toRequestBody(
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
                    mBinding?.llUploadOne?.hideView()
                    mBinding?.llUploadTwo?.showView()
                    mBinding?.llUploadThree?.showView()
                    mBinding?.llUploadFour?.showView()
                    mBinding?.llUploadFive?.showView()

                } else {
                    uploadedDocumentName = userResponseModel._result.document_name
                    dialogDocName?.text = userResponseModel._result.document_name
                    TableName=userResponseModel._result.table_name
                    when (isFromApplication) {
                        1 -> {
                            mBinding?.tvDocumentNameOne?.text = uploadedDocumentName
                            mBinding?.tvNoFileOne?.text ="Uploaded"
                        }

                        2 -> {
                            mBinding?.tvNoFileTwo?.text = "Uploaded"
                            mBinding?.tvDocumentNameTwo?.text=  uploadedDocumentName
                        }

                        3 -> {
                            mBinding?.tvNoFileThree?.text ="Uploaded"
                            mBinding?.tvDocumentNameThree?.text=uploadedDocumentName
                        }

                        4 -> {
                            mBinding?.tvNoFileFour?.text ="Uploaded"
                            mBinding?.tvDocumentNameFour?.text = uploadedDocumentName
                        }

                        5 -> {
                            mBinding?.tvNoFileFive?.text ="Uploaded"
                            mBinding?.tvDocumentNameFive?.text = uploadedDocumentName
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

        viewModel.districtMobileVeterinaryUnitsAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {
                    TableName=userResponseModel.fileurl
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
//                            toast(viewEdit.toString())
                            mBinding?.etInputOne?.setText(userResponseModel._result.input_mechanism_medicines)
                            mBinding?.etRemarkOne?.setText(userResponseModel._result.mechanism_medicines_remaks)
                            mBinding?.etInputTwo?.setText(userResponseModel._result.input_organize_awareness_camp)
                            mBinding?.etRemarkTwo?.setText(userResponseModel._result.organize_awareness_camp_remarks)
                            mBinding?.etInputThree?.setText(userResponseModel._result.input_distribution_medicines_role)
                            mBinding?.etRemarkThree?.setText(userResponseModel._result.distribution_medicines_role_remaks)
                            mBinding?.etInputFour?.setText(userResponseModel._result.input_distribution_fuel_role)
                            mBinding?.etRemarkFour?.setText(userResponseModel._result.distribution_fuel_role_inputs_remarks)
                            mBinding?.etInputFive?.setText(userResponseModel._result.input_medicine_requirement)
                            mBinding?.etRemarkFive?.setText(userResponseModel._result.medicine_requirement_remarks)
                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                            if (userResponseModel._result.mechanism_medicines_inputs.isNullOrEmpty()) {
                                mBinding?.tvNoFileOne?.text =  "No file chosen"
                            } else {
                                mBinding?.llUploadOne?.showView()
                                mBinding?.tvNoFileOne?.text ="Uploaded"
                                mBinding?.tvDocumentNameOne?.text=   userResponseModel._result.mechanism_medicines_inputs
                                mBinding?.ivPicOne?.let { it1 -> GlideImage(it1,userResponseModel._result.mechanism_medicines_inputs)}
                            }

                            if (userResponseModel._result.organize_awareness_camp_inputs.isNullOrEmpty()) {
                                mBinding?.tvNoFileTwo?.text = "No file chosen"
                            } else{
                                mBinding?.llUploadTwo?.showView()
                                mBinding?.tvNoFileTwo?.text ="Uploaded"
                                mBinding?.tvDocumentNameTwo?.text=  userResponseModel._result.organize_awareness_camp_inputs
                                mBinding?.ivPicTwo?.let { it1 -> GlideImage(it1,userResponseModel._result.organize_awareness_camp_inputs)}
                            }
                            if (userResponseModel._result.distribution_medicines_role_inputs.isNullOrEmpty()){
                                mBinding?.tvNoFileThree?.text ="No file chosen"
                            } else{
                                mBinding?.llUploadThree?.showView()
                                mBinding?.tvNoFileThree?.text ="Uploaded"
                                mBinding?.tvDocumentNameThree?.text= userResponseModel._result.distribution_medicines_role_inputs
                                mBinding?.ivPicThree?.let { it1 -> GlideImage(it1,userResponseModel._result.distribution_medicines_role_inputs)}
                            }
                            if (userResponseModel._result.distribution_fuel_role_inputs.isNullOrEmpty()){
                                mBinding?.tvNoFileFour?.text =  "No file chosen"
                            } else{
                                mBinding?.llUploadFour?.showView()
                                mBinding?.tvNoFileFour?.text ="Uploaded"
                                mBinding?.tvDocumentNameFour?.text= userResponseModel._result.distribution_fuel_role_inputs
                                mBinding?.ivPicFour?.let { it1 -> GlideImage(it1,userResponseModel._result.distribution_fuel_role_inputs)}

                            }
                            if (userResponseModel._result.medicine_requirement_inputs.isNullOrEmpty()) {
                                mBinding?.tvNoFileFive?.text = "No file chosen"
                            } else {
                                mBinding?.llUploadFive?.showView()
                                mBinding?.tvDocumentNameFive?.text=userResponseModel._result.medicine_requirement_inputs
                                mBinding?.tvNoFileFive?.text = "Upload"
                                mBinding?.ivPicFive?.let { it1 -> GlideImage(it1,userResponseModel._result.medicine_requirement_inputs)}
                            }
                        }
                        else{
                            onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
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
    override fun showImage(bitmap: Bitmap) {
        when (isFromApplication) {
            1 -> {
                mBinding?.llUploadOne?.showView()
                mBinding?.ivPicOne?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPicOne?.setOnClickListener {
                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )
                }
                uploadImage(imageFile)
            }
            2 -> {
                mBinding?.llUploadTwo?.showView()
                mBinding?.ivPicTwo?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPicTwo?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )
                }
                uploadImage(imageFile)
            }
            3 -> {
                mBinding?.llUploadThree?.showView()
                mBinding?.ivPicThree?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPicThree?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )
                }
                uploadImage(imageFile)
            }
            4 -> {
                mBinding?.llUploadFour?.showView()
                mBinding?.ivPicFour?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPicFour?.setOnClickListener {

                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )
                }
                uploadImage(imageFile)
            }
            5 -> {
                mBinding?.llUploadFive?.showView()
                mBinding?.ivPicFive?.setImageBitmap(bitmap)
                val imageFile = saveImageToFile(bitmap)
                mBinding?.ivPicFive?.setOnClickListener {
                    Utility.showImageDialogFileUrl(
                        this,
                        imageFile
                    )
                }
                uploadImage(imageFile)
            }
        }
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
}