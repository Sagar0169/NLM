package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitVillageBinding
import com.nlm.model.FarmerMobileVeterinaryUnitsAddRequest
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNewMobileVeterinaryUnitVillage :
    BaseActivity<ActivityAddNewMobileVeterinaryUnitVillageBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitVillageBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null
    private var viewModel = ViewModel()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var districtId: Int? = null // Store selected state
    var isFromApplication = 0
    private var uploadedDocumentName: String? = null
    private var dialogDocName: TextView? = null
    private var documentName: String? = null
    var body: MultipartBody.Part? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_add_new_mobile_veterinary_unit_village

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)

        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.etBlock?.isEnabled = false
            mBinding?.etFarmer?.isEnabled = false
            mBinding?.rbYes?.isEnabled = false
            mBinding?.rbNo?.isEnabled = false
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
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false

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
//        registerReceiver(
//            locationReceiver,
//            IntentFilter("LOCATION_UPDATED")
//        )
    }


    override fun onPause() {
        super.onPause()
//        unregisterReceiver(locationReceiver)
    }

    private fun valid() : Boolean{
        if (mBinding?.tvDistrict?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Select District"
                )
            }
            return false
        }
        else if (mBinding?.etBlock?.text.toString().isEmpty()) {

            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please enter Block Name"
                )
            }
            return false
        }
        else if (mBinding?.etFarmer?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please enter Village Name"
                )
            }
            return false
        } else if (mBinding?.etInputTwo?.text.toString().isEmpty()) {
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
        } else if (mBinding?.etInputFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etInputFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etRemarkOne?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etRemarkTwo?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etRemarkThree?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etRemarkFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    "Please Fill All The Input and Remark Fields"
                )
            }
            return false
        } else {
            return true
        }
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
            savedAsDraft = true

        }
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

//        if (hasLocationPermissions()) {
//            startService(Intent(this, LocationService::class.java))
//            lifecycleScope.launch {
//                delay(1000) // Delay for 2 seconds
//                if (latitude != null && longitude != null) {

        if (valid()) {
            viewModel.getFarmerMobileVeterinaryUnitsAdd(
                this@AddNewMobileVeterinaryUnitVillage, true,
                FarmerMobileVeterinaryUnitsAddRequest(
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
                    status = draft,
                    attended_call = when {
                        mBinding?.rbYes?.isChecked == true -> 1
                        mBinding?.rbNo?.isChecked == true -> 0
                        else -> -1 // Optional: Add a default value for cases where neither is selected
                    },
                    attended_call_remarks = mBinding?.etRemarkOne?.text.toString(),
                    input_come_know_about = mBinding?.etInputTwo?.text.toString(),
                    come_know_about_remarks = mBinding?.etRemarkTwo?.text.toString(),
                    input_services_mvu = mBinding?.etInputThree?.text.toString(),
                    services_mvu_remarks = mBinding?.etRemarkThree?.text.toString(),
                    input_mvu_arrive_call = mBinding?.etInputFour?.text.toString(),
                    mvu_arrive_call_remarks = mBinding?.etRemarkFour?.text.toString(),
                    input_services_offered_by_mvu = mBinding?.etInputFive?.text.toString(),
                    services_offered_by_mvu_remarks = mBinding?.etRemarkFive?.text.toString(),
                    district_code = districtId,
                    block_name = mBinding?.etBlock?.text.toString(),
                    village_name = mBinding?.etFarmer?.text.toString(),

                    attended_call_inputs = mBinding?.tvNoFileOne?.text.toString(),
                    come_know_about_inputs = mBinding?.tvNoFileTwo?.text.toString(),
                    services_mvu_inputs = mBinding?.tvNoFileThree?.text.toString(),
                    mvu_arrive_call_inputs = mBinding?.tvNoFileFour?.text.toString(),
                    services_offered_by_mvu_inputs = mBinding?.tvNoFileFive?.text.toString(),

                    )
            )
        }
//                } else {
//                    showSnackbar(mBinding!!.clParent, "No Location fetched")
//                }
//            }
//        } else {
//            showLocationAlertDialog()
//        }

    }

    private fun viewEditApi() {

        viewModel.getFarmerMobileVeterinaryUnitsAdd(
            this@AddNewMobileVeterinaryUnitVillage, true,
            FarmerMobileVeterinaryUnitsAddRequest(
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


    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
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

                                    else -> {
                                        uploadDocument(documentName, uri)
                                    }
                                }


//                                use this code to add new view with image name and uri
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
            table_name = getString(R.string.mobile_veterinary_unit_farmer).toRequestBody(
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

        viewModel.farmerMobileVeterinaryUnitsAddResult.observe(this) {
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

                            if (userResponseModel._result.attended_call == 1) {
                                mBinding?.rbYes?.isChecked = true
                            } else if (userResponseModel._result.attended_call == 0) {
                                mBinding?.rbNo?.isChecked = true
                            }
                            mBinding?.etRemarkOne?.setText(userResponseModel._result.attended_call_remarks)
                            mBinding?.etInputTwo?.setText(userResponseModel._result.input_come_know_about)
                            mBinding?.etRemarkTwo?.setText(userResponseModel._result.come_know_about_remarks)
                            mBinding?.etInputThree?.setText(userResponseModel._result.input_services_mvu)
                            mBinding?.etRemarkThree?.setText(userResponseModel._result.services_mvu_remarks)
                            mBinding?.etInputFour?.setText(userResponseModel._result.input_mvu_arrive_call)
                            mBinding?.etRemarkFour?.setText(userResponseModel._result.mvu_arrive_call_remarks)
                            mBinding?.etInputFive?.setText(userResponseModel._result.input_services_offered_by_mvu)
                            mBinding?.etRemarkFive?.setText(userResponseModel._result.services_offered_by_mvu_remarks)
                            mBinding?.etBlock?.setText(userResponseModel._result.block_name)
                            mBinding?.etFarmer?.setText(userResponseModel._result.village_name)
                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name

                            mBinding?.tvNoFileOne?.text =
                                if (userResponseModel._result.attended_call_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.attended_call_inputs
                            mBinding?.tvNoFileTwo?.text =
                                if (userResponseModel._result.come_know_about_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.come_know_about_inputs
                            mBinding?.tvNoFileThree?.text =
                                if (userResponseModel._result.services_mvu_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.services_mvu_inputs
                            mBinding?.tvNoFileFour?.text =
                                if (userResponseModel._result.mvu_arrive_call_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.mvu_arrive_call_inputs
                            mBinding?.tvNoFileFive?.text =
                                if (userResponseModel._result.services_offered_by_mvu_inputs.isNullOrEmpty()) "No file chosen" else userResponseModel._result.services_offered_by_mvu_inputs

                        } else {
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