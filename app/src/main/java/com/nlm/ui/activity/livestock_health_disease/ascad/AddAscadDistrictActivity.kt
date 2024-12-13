package com.nlm.ui.activity.livestock_health_disease.ascad

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
import com.nlm.databinding.ActivityAddAscadDistrictBinding
import com.nlm.model.DistrictAscadAddRequest
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
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

class AddAscadDistrictActivity : BaseActivity<ActivityAddAscadDistrictBinding>() {
    private var mBinding: ActivityAddAscadDistrictBinding? = null
    private var viewModel = ViewModel()
    private var stateList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var uploadedDocumentName: String? = null
    private var dialogDocName: TextView? = null
    private var documentName: String? = null
    var body: MultipartBody.Part? = null
    var isFromApplication = 0
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var isFrom: String? = null
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
        get() = R.layout.activity_add_ascad_district

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
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
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name?.isNotEmpty() == true
        ) {
            mBinding?.tvState?.text = getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name
            mBinding?.tvState?.isEnabled = false
        } else {
            mBinding?.tvState?.isEnabled = true
        }
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
            mBinding?.tvChooseFileOne?.isEnabled = false
            mBinding?.tvChooseFileTwo?.isEnabled = false
            mBinding?.tvChooseFileThree?.isEnabled = false
            mBinding?.tvChooseFileFour?.isEnabled = false
            mBinding?.tvChooseFileFive?.isEnabled = false
            mBinding?.llSaveDraftAndSubmit?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
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
                            mBinding?.etChooseFileOne?.text = uploadedDocumentName
                        }

                        2 -> {
                            mBinding?.etChooseFileTwo?.text = uploadedDocumentName
                        }

                        3 -> {
                            mBinding?.etChooseFileThree?.text = uploadedDocumentName
                        }

                        4 -> {
                            mBinding?.etChooseFileFour?.text = uploadedDocumentName
                        }

                        5 -> {
                            mBinding?.etChooseFileFive?.text = uploadedDocumentName
                        }

                        else -> {
                            dialogDocName?.text = uploadedDocumentName
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
        viewModel.districtAscadAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
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
                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                            districtId = userResponseModel._result.district_code
                            mBinding?.etInputOne?.setText(userResponseModel._result.input_status_of_vaccination_against_economically)
                            mBinding?.etRemarkOne?.setText(userResponseModel._result.status_of_vaccination_against_economically_remarks)
                            mBinding?.etInputTwo?.setText(userResponseModel._result.input_status_of_vaccination_against_zoonotic)
                            mBinding?.etRemarkTwo?.setText(userResponseModel._result.status_of_vaccination_against_zoonotic_remarks)
                            mBinding?.etInputThree?.setText(userResponseModel._result.input_disease_diagnostic_labs)
                            mBinding?.etRemarkThree?.setText(userResponseModel._result.disease_diagnostic_labs_remarks)
                            mBinding?.etInputFour?.setText(userResponseModel._result.input_training_of_veterinarians_and_para_vets_last_year)
                            mBinding?.etRemarkFour?.setText(userResponseModel._result.training_of_veterinarians_and_para_vets_last_year_remarks)
                            mBinding?.etInputFive?.setText(userResponseModel._result.input_compensation_farmer_against_culling_of_animals)
                            mBinding?.etRemarkFive?.setText(userResponseModel._result.compensation_farmer_against_culling_of_animals_remarks)
                            mBinding?.etChooseFileOne?.text =
                                if (userResponseModel._result.status_of_vaccination_against_economically_input.isNullOrEmpty()) "No file chosen" else userResponseModel._result.status_of_vaccination_against_economically_input
                            mBinding?.etChooseFileTwo?.text =
                                if (userResponseModel._result.status_of_vaccination_against_zoonotic_input.isNullOrEmpty()) "No file chosen" else userResponseModel._result.status_of_vaccination_against_zoonotic_input
                            mBinding?.etChooseFileThree?.text =
                                if (userResponseModel._result.disease_diagnostic_labs_input.isNullOrEmpty()) "No file chosen" else userResponseModel._result.disease_diagnostic_labs_input
                            mBinding?.etChooseFileFour?.text =
                                if (userResponseModel._result.training_of_veterinarians_and_para_vets_last_year_input.isNullOrEmpty()) "No file chosen" else userResponseModel._result.training_of_veterinarians_and_para_vets_last_year_input
                            mBinding?.etChooseFileFive?.text =
                                if (userResponseModel._result.compensation_farmer_against_culling_of_animals_input.isNullOrEmpty()) "No file chosen" else userResponseModel._result.compensation_farmer_against_culling_of_animals_input
                        } else {
                            onBackPressedDispatcher.onBackPressed()
                            mBinding?.clParent?.let { it1 ->
                                showSnackbar(
                                    it1,
                                    userResponseModel.message
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    inner class ClickActions {
        fun state(view: View) {
            isFrom = "state"
            showBottomSheetDialog("State")
        }

        fun district(view: View) {
            isFrom = "district"
            showBottomSheetDialog("District")
        }

        fun uploadFileOne(view: View) {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }

        fun uploadFileTwo(view: View) {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }

        fun uploadFileThree(view: View) {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }

        fun uploadFileFour(view: View) {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }

        fun uploadFileFive(view: View) {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun submit(view: View) {
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

    private fun viewEditApi() {

        viewModel.getDistrictAscadAdd(
            this, true,
            DistrictAscadAddRequest(
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

    private fun valid(): Boolean {
        if (mBinding?.tvDistrict?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, getString(R.string.please_select_district))
            }
            return false
        } else if (mBinding?.etInputOne?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, getString(R.string.please_fill_all_the_input_and_remark_fields))
            }
            return false
        } else if (mBinding?.etInputTwo?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInputThree?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInputFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etInputFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemarkOne?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemarkTwo?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemarkThree?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemarkFour?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(
                    it,
                    getString(R.string.please_fill_all_the_input_and_remark_fields)
                )
            }
            return false
        } else
            return true
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

                if (hasLocationPermissions()) {
            startService(Intent(this, LocationService::class.java))
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {
        if (valid()) {
            viewModel.getDistrictAscadAdd(
                context = this@AddAscadDistrictActivity,
                loader = true,
                request = DistrictAscadAddRequest(
                    id = itemId,
                    role_id = getPreferenceOfScheme(
                        this@AddAscadDistrictActivity,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        this@AddAscadDistrictActivity,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    district_code = districtId,
                    user_id = getPreferenceOfScheme(
                        this@AddAscadDistrictActivity,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id,
                    status = draft,
                    input_status_of_vaccination_against_economically = mBinding?.etInputOne?.text.toString()
                        .trim(),
                    status_of_vaccination_against_economically_remarks = mBinding?.etRemarkOne?.text.toString()
                        .trim(),
                    input_status_of_vaccination_against_zoonotic = mBinding?.etInputTwo?.text.toString()
                        .trim(),
                    status_of_vaccination_against_zoonotic_remarks = mBinding?.etRemarkTwo?.text.toString()
                        .trim(),
                    input_disease_diagnostic_labs = mBinding?.etInputThree?.text.toString().trim(),
                    disease_diagnostic_labs_remarks = mBinding?.etRemarkThree?.text.toString()
                        .trim(),
                    input_training_of_veterinarians_and_para_vets_last_year = mBinding?.etInputFour?.text.toString()
                        .trim(),
                    training_of_veterinarians_and_para_vets_last_year_remarks = mBinding?.etRemarkFour?.text.toString()
                        .trim(),
                    input_compensation_farmer_against_culling_of_animals = mBinding?.etInputFive?.text.toString()
                        .trim(),
                    compensation_farmer_against_culling_of_animals_remarks = mBinding?.etInputFive?.text.toString()
                        .trim(),
                    status_of_vaccination_against_economically_input = mBinding?.etChooseFileOne?.text.toString()
                        .trim(),
                    status_of_vaccination_against_zoonotic_input = mBinding?.etChooseFileTwo?.text.toString()
                        .trim(),
                    disease_diagnostic_labs_input = mBinding?.etChooseFileThree?.text.toString()
                        .trim(),
                    training_of_veterinarians_and_para_vets_last_year_input = mBinding?.etChooseFileFour?.text.toString()
                        .trim(),
                    compensation_farmer_against_culling_of_animals_input = mBinding?.etChooseFileFive?.text.toString()
                        .trim(),
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
                dropDownApiCall(paginate = false, loader = true, "States", null)
                selectedList = stateList
                selectedTextView = mBinding?.tvState
            }

            "District" -> {
                dropDownApiCall(
                    paginate = false, loader = true, "Districts", getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code
                )
                selectedList = stateList
                selectedTextView = mBinding?.tvDistrict
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            if (type == "State") {
                selectedTextView?.text = selectedItem
                stateId = id
            } else if (type == "District") {
                selectedTextView?.text = selectedItem
                districtId = id
            }
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

    private fun uploadDocument(documentName: String?, uri: Uri) {
        val requestBody = convertToRequestBody(this, uri)
        body = MultipartBody.Part.createFormData(
            "document_name",
            documentName,
            requestBody
        )
        viewModel.getProfileUploadFile(
            context = this,
            document_name = body,
            user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            table_name = getString(R.string.ascad_district).toRequestBody(MultipartBody.FORM),
        )
    }

    private fun dropDownApiCall(
        paginate: Boolean,
        loader: Boolean,
        model: String,
        stateCode: Int?,
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                100,
                model,
                currentPage,
                stateCode,
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
                                if (isFrom == "State") {
                                    dropDownApiCall(paginate = true, loader = true, "States", null)
                                } else if (isFrom == "District") {
                                    dropDownApiCall(
                                        paginate = false,
                                        loader = true,
                                        "Districts",
                                        getPreferenceOfScheme(
                                            this@AddAscadDistrictActivity,
                                            AppConstants.SCHEME,
                                            Result::class.java
                                        )?.state_code
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}