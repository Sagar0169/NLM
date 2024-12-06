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
import com.nlm.databinding.ActivityAddVaccinationProgrammeFarmerLevelBinding
import com.nlm.model.FarmerVaccinationProgrammeAddRequest
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
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody

class AddVaccinationProgrammeFarmerLevel :
    BaseActivity<ActivityAddVaccinationProgrammeFarmerLevelBinding>() {

    private var mBinding: ActivityAddVaccinationProgrammeFarmerLevelBinding? = null
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
            latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_farmer_level

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
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
            mBinding?.etVillage?.isEnabled = false
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
        viewModel.farmerVaccinationProgrammerAddResult.observe(this) {
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
                        mBinding?.clParent?.let { it1 ->
                            showSnackbar(
                                it1,
                                userResponseModel.message
                            )
                        }
                    } else if (viewEdit == "view" || viewEdit == "edit") {
                        if (savedAsEdit) {
                            onBackPressedDispatcher.onBackPressed()
                            return@observe
                        }
                        mBinding?.tvState?.text = userResponseModel._result.state_name
                        mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                        districtId = userResponseModel._result.district_code
                        mBinding?.etVillage?.setText(userResponseModel._result.village_name)
                        mBinding?.etInputOne?.setText(userResponseModel._result.animal_vaccinated_inputs)
                        mBinding?.etRemarkOne?.setText(userResponseModel._result.animal_vaccinated_remarks)
                        mBinding?.etInputTwo?.setText(userResponseModel._result.vaccinator_visit_inputs)
                        mBinding?.etRemarkTwo?.setText(userResponseModel._result.vaccinator_visit_remarks)
                        mBinding?.etInputThree?.setText(userResponseModel._result.recall_vaccination_inputs)
                        mBinding?.etRemarkThree?.setText(userResponseModel._result.recall_vaccination_remarks)
                        mBinding?.etInputFour?.setText(userResponseModel._result.vaccination_carrier_inputs)
                        mBinding?.etRemarkFour?.setText(userResponseModel._result.vaccination_carrier_remarks)
                        mBinding?.etInputFive?.setText(userResponseModel._result.awarness_of_the_govt_inputs)
                        mBinding?.etRemarkFive?.setText(userResponseModel._result.awarness_of_the_govt_remarks)
                        mBinding?.etChooseFileOne?.text =
                            userResponseModel._result.animal_vaccinated_uploads
                        mBinding?.etChooseFileTwo?.text =
                            userResponseModel._result.vaccinator_visit_uploads
                        mBinding?.etChooseFileThree?.text =
                            userResponseModel._result.recall_vaccination_uploads
                        mBinding?.etChooseFileFour?.text =
                            userResponseModel._result.vaccination_carrier_uploads
                        mBinding?.etChooseFileFive?.text =
                            userResponseModel._result.awarness_of_the_govt_uploads
                    }

                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
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
            } else {
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
            } else {
                savedAsDraft = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 3)
            } else {
                saveDataApi(null, 3)
            }
        }
    }

    private fun viewEditApi() {

        viewModel.getFarmerVaccinationProgrammeAdd(
            this, true,
            FarmerVaccinationProgrammeAddRequest(
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

    private fun saveDataApi(itemId: Int?, draft: Int?) {

//        if (hasLocationPermissions()) {
//            startService(Intent(this, LocationService::class.java))
//            lifecycleScope.launch {
//                delay(1000) // Delay for 2 seconds
//                if (latitude != null && longitude != null) {

                    if (valid()) {
                        viewModel.getFarmerVaccinationProgrammeAdd(
                            context = this@AddVaccinationProgrammeFarmerLevel,
                            loader = true,
                            request = FarmerVaccinationProgrammeAddRequest(
                                id = itemId,
                                role_id = getPreferenceOfScheme(
                                    this@AddVaccinationProgrammeFarmerLevel,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                state_code = getPreferenceOfScheme(
                                    this@AddVaccinationProgrammeFarmerLevel,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.state_code,
                                district_code = districtId,
                                user_id = getPreferenceOfScheme(
                                    this@AddVaccinationProgrammeFarmerLevel,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id,
                                status = draft,
                                village_name = mBinding?.etVillage?.text.toString().trim(),
                                animal_vaccinated_inputs = mBinding?.etInputOne?.text.toString()
                                    .trim(),
                                animal_vaccinated_remarks = mBinding?.etRemarkOne?.text.toString()
                                    .trim(),
                                vaccinator_visit_inputs = mBinding?.etInputTwo?.text.toString()
                                    .trim(),
                                vaccinator_visit_remarks = mBinding?.etRemarkTwo?.text.toString()
                                    .trim(),
                                recall_vaccination_inputs = mBinding?.etInputThree?.text.toString()
                                    .trim(),
                                recall_vaccination_remarks = mBinding?.etRemarkThree?.text.toString()
                                    .trim(),
                                vaccination_carrier_inputs = mBinding?.etInputFour?.text.toString()
                                    .trim(),
                                vaccination_carrier_remarks = mBinding?.etRemarkFour?.text.toString()
                                    .trim(),
                                awarness_of_the_govt_inputs = mBinding?.etInputFive?.text.toString()
                                    .trim(),
                                awarness_of_the_govt_remarks = mBinding?.etInputFive?.text.toString()
                                    .trim(),
                                animal_vaccinated_uploads = mBinding?.etChooseFileOne?.text.toString()
                                    .trim(),
                                vaccinator_visit_uploads = mBinding?.etChooseFileTwo?.text.toString()
                                    .trim(),
                                recall_vaccination_uploads = mBinding?.etChooseFileThree?.text.toString()
                                    .trim(),
                                vaccination_carrier_uploads = mBinding?.etChooseFileFour?.text.toString()
                                    .trim(),
                                awarness_of_the_govt_uploads = mBinding?.etChooseFileFive?.text.toString()
                                    .trim()
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

    private fun valid(): Boolean {
        if (mBinding?.tvDistrict?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, getString(R.string.please_select_district))
            }
            return false
        } else if (mBinding?.etVillage?.text.toString().isEmpty()) {
            mBinding?.clParent?.let {
                showSnackbar(it, getString(R.string.please_enter_village))
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
            table_name = getString(R.string.farmer_vaccination_programme).toRequestBody(
                MultipartBody.FORM
            )
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
                                            this@AddVaccinationProgrammeFarmerLevel,
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
