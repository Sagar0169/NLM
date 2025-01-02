package com.nlm.ui.activity.national_dairy_development

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddDcsCenterVisitBinding
import com.nlm.databinding.ActivityAddMilkProcessingBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AddMilkProcessingRequest
import com.nlm.model.AddStateCenterLabRequest
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.SubTableDeleteRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.URIPathHelper
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.getFileType
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMilkProcessing : BaseActivity<ActivityAddMilkProcessingBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddMilkProcessingBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var stateAdapter: BottomSheetAdapter
    var body: MultipartBody.Part? = null
    private var viewModel = ViewModel()
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var districtId: Int? = null // Store selected state
    private var loading = true
    private var DocumentId: Int? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private var districtList = ArrayList<ResultGetDropDown>()
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var addDocumentIAAdapter: RSPSupportingDocumentIAAdapter? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var uploadData: ImageView? = null
    private var TableName: String? = null
    private var photographSite: String? = null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "LOCATION_UPDATED") {
                    // Handle the location update
                    latitude = it.getDoubleExtra("latitude", 0.0)
                    longitude = it.getDoubleExtra("longitude", 0.0)
                    Log.d("Receiver", "Location Updated: Lat = $latitude, Lon = $longitude")

                    // You can add additional handling logic here, such as updating UI or processing data.
                }
            }
        }
    }
    private var rbAsset: Int? = null
    private var rbOverall: Int? = null
    private var rbHygiene: Int? = null
    private var rbNew: Int? = null
    private var rbWhether: Int? = null
    private var rbIncreased: Int? = null
    private var rbProducts: Int? = null
    private var rbAddition: Int? = null
    private var rbIncrease: Int? = null
    private var rbOperational: Int? = null


    private var tvDateOfInspection: String? = null


    override val layoutId: Int
        get() = R.layout.activity_add_milk_processing

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))


        mBinding?.tvDateOfInspection?.setOnClickListener {
            openCalendar("tvDateOfInspection", mBinding?.tvDateOfInspection!!)
        }

        mBinding?.rbAsset?.setOnCheckedChangeListener { group, checkedId ->
            rbAsset = when (checkedId) {
                R.id.rbAssetYes -> 1
                R.id.rbAssetNo -> 0
                else -> null
            }
        }

        mBinding?.rbOverall?.setOnCheckedChangeListener { group, checkedId ->
            rbOverall = when (checkedId) {
                R.id.rbOverallYes -> 1
                R.id.rbOverallNo -> 0
                else -> null
            }
        }

        mBinding?.rbHygiene?.setOnCheckedChangeListener { group, checkedId ->
            rbHygiene = when (checkedId) {
                R.id.rbHygieneYes -> 1
                R.id.rbHygieneNo -> 0
                else -> null
            }
        }

        mBinding?.rbNew?.setOnCheckedChangeListener { group, checkedId ->
            rbNew = when (checkedId) {
                R.id.rbNewYes -> 1
                R.id.rbNewNo -> 0
                else -> null
            }
        }

        mBinding?.rbWhether?.setOnCheckedChangeListener { group, checkedId ->
            rbWhether = when (checkedId) {
                R.id.rbWhetherYes -> 1
                R.id.rbWhetherNo -> 0
                else -> null
            }
        }

        mBinding?.rbIncreased?.setOnCheckedChangeListener { group, checkedId ->
            rbIncreased = when (checkedId) {
                R.id.rbIncreasedYes -> 1
                R.id.rbIncreasedNo -> 0
                else -> null
            }
        }

        mBinding?.rbProducts?.setOnCheckedChangeListener { group, checkedId ->
            rbProducts = when (checkedId) {
                R.id.rbProductsYes -> 1
                R.id.rbProductsNo -> 0
                else -> null
            }
        }

        mBinding?.rbAddition?.setOnCheckedChangeListener { group, checkedId ->
            rbAddition = when (checkedId) {
                R.id.rbAdditionYes -> 1
                R.id.rbAdditionNo -> 0
                else -> null
            }
        }

        mBinding?.rbIncrease?.setOnCheckedChangeListener { group, checkedId ->
            rbIncrease = when (checkedId) {
                R.id.rbIncreaseYes -> 1
                R.id.rbIncreaseNo -> 0
                else -> null
            }
        }

        mBinding?.rbOperational?.setOnCheckedChangeListener { group, checkedId ->
            rbOperational = when (checkedId) {
                R.id.rbOperationalYes -> 1
                R.id.rbOperationalNo -> 0
                else -> null
            }
        }

        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))

            mBinding?.etNameOfProcessingPlant?.isEnabled = false
            mBinding?.etNameOfMilkUnion?.isEnabled = false
            mBinding?.etNameOfTehsil?.isEnabled = false
            mBinding?.tvDateOfInspection?.isEnabled = false
            mBinding?.etLat?.isEnabled = false
            mBinding?.etLong?.isEnabled = false
            mBinding?.etRemarkAsset?.isEnabled = false
            mBinding?.etOverallRemark?.isEnabled = false
            mBinding?.etHygieneRemark?.isEnabled = false
            mBinding?.etNewRemark?.isEnabled = false
            mBinding?.etWhetherRemark?.isEnabled = false
            mBinding?.etAnyOther?.isEnabled = false
            mBinding?.rbAssetYes?.isEnabled = false
            mBinding?.rbAssetNo?.isEnabled = false
            mBinding?.rbOverallYes?.isEnabled = false
            mBinding?.rbOverallNo?.isEnabled = false
            mBinding?.rbHygieneYes?.isEnabled = false
            mBinding?.rbHygieneNo?.isEnabled = false
            mBinding?.rbNewYes?.isEnabled = false
            mBinding?.rbNewNo?.isEnabled = false
            mBinding?.rbWhetherYes?.isEnabled = false
            mBinding?.rbWhetherNo?.isEnabled = false
            mBinding?.rbIncreasedYes?.isEnabled = false
            mBinding?.rbIncreasedNo?.isEnabled = false
            mBinding?.rbProductsYes?.isEnabled = false
            mBinding?.rbProductsNo?.isEnabled = false
            mBinding?.rbAdditionYes?.isEnabled = false
            mBinding?.rbAdditionNo?.isEnabled = false
            mBinding?.rbIncreaseYes?.isEnabled = false
            mBinding?.rbIncreaseNo?.isEnabled = false
            mBinding?.rbOperationalYes?.isEnabled = false
            mBinding?.rbOperationalNo?.isEnabled = false
            mBinding?.tvNLMDoc?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()


            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }

        nlmAdapter()

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

    private fun viewEditApi() {

        viewModel.getMilkProcessingAdd(
            this, true,
            AddMilkProcessingRequest(
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


    private fun openCalendar(type: String, selectedTextView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this@AddMilkProcessing,
            { _, year, month, day ->
                val calendarInstance = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                val formattedDate = sdf.format(calendarInstance.time)

                // Handle each case
                when (type) {
                    "tvDateOfInspection" -> tvDateOfInspection = formattedDate
                    else -> {
                        // Optional: Handle unknown types
                        Log.w("Calendar", "Unknown type: $type")
                    }
                }

                // Set the selected date in the TextView
                selectedTextView.text = convertDate(formattedDate)
                selectedTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.black)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddMilkProcessing)
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
                    DialogDocName?.text = userResponseModel._result.document_name
                    TableName = userResponseModel._result.table_name
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
        viewModel.getSubTableDeleteResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.status == 401) {
                    Utility.logout(this)
                } else if (userResponseModel.status != 0) {
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }

                } else {
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
        viewModel.milkProcessingAddResult.observe(this) {
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
                        TableName = userResponseModel.fileurl
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                            districtId = userResponseModel._result.district_code
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.district_name
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etNameOfProcessingPlant?.setText(userResponseModel._result.name_processing_plant)
                            mBinding?.etNameOfMilkUnion?.setText(userResponseModel._result.name_milk_union)
                            mBinding?.etNameOfTehsil?.setText(userResponseModel._result.name_of_tehsil)
                            mBinding?.etLat?.setText(userResponseModel._result.lat_processing_plant.toString())
                            mBinding?.etLong?.setText(userResponseModel._result.lat_processing_plant.toString())
                            mBinding?.etRemarkAsset?.setText(userResponseModel._result.asset_earmarked_remarks)
                            mBinding?.etOverallRemark?.setText(userResponseModel._result.overall_upkeep_asset_remarks)
                            mBinding?.etHygieneRemark?.setText(userResponseModel._result.overall_hygiene_remarks)
                            mBinding?.etNewRemark?.setText(userResponseModel._result.has_new_plant_remarks)
                            mBinding?.etWhetherRemark?.setText(userResponseModel._result.whether_there_remarks)
                            mBinding?.etAnyOther?.setText(userResponseModel._result.any_other)
                            mBinding?.tvDateOfInspection?.text =
                                convertDate(userResponseModel._result.date_of_inspection)


                            Log.d("overall_upkeep_asset",
                                userResponseModel._result.overall_upkeep_asset.toString()
                            )
                            if (userResponseModel._result.asset_earmarked_under==null ) {
                                mBinding?.rbAssetYes?.isChecked = false
                                mBinding?.rbAssetNo?.isChecked = false

                            } else if (userResponseModel._result.asset_earmarked_under == 1) {
                                mBinding?.rbAssetYes?.isChecked = true
                            } else {
                                mBinding?.rbAssetNo?.isChecked = true
                            }



                            if (userResponseModel._result.overall_upkeep_asset==null) {
                                mBinding?.rbOverallYes?.isChecked = false
                                mBinding?.rbOverallNo?.isChecked = false

                            } else if (userResponseModel._result.overall_upkeep_asset == 1) {
                                mBinding?.rbOverallYes?.isChecked = true
                            } else {
                                mBinding?.rbOverallNo?.isChecked = true
                            }

                            if (userResponseModel._result.overall_hygiene==null ) {
                                mBinding?.rbHygieneYes?.isChecked = false
                                mBinding?.rbHygieneNo?.isChecked = false

                            } else if (userResponseModel._result.overall_hygiene == 1) {
                                mBinding?.rbHygieneYes?.isChecked = true
                            } else {
                                mBinding?.rbHygieneNo?.isChecked = true
                            }

                            if (userResponseModel._result.has_new_plant==null ) {
                                mBinding?.rbNewYes?.isChecked = false
                                mBinding?.rbNewNo?.isChecked = false

                            } else if (userResponseModel._result.has_new_plant == 1) {
                                mBinding?.rbNewYes?.isChecked = true
                            } else {
                                mBinding?.rbNewNo?.isChecked = true
                            }

                            if (userResponseModel._result.whether_there==null ) {
                                mBinding?.rbWhetherYes?.isChecked = false
                                mBinding?.rbWhetherNo?.isChecked = false

                            } else if (userResponseModel._result.whether_there == 1) {
                                mBinding?.rbWhetherYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherNo?.isChecked = true
                            }

                            if (userResponseModel._result.processing_capacity_milk==null ) {
                                mBinding?.rbIncreasedYes?.isChecked = false
                                mBinding?.rbIncreasedNo?.isChecked = false

                            } else if (userResponseModel._result.processing_capacity_milk == 1) {
                                mBinding?.rbIncreasedYes?.isChecked = true
                            } else {
                                mBinding?.rbIncreasedNo?.isChecked = true
                            }

                            if (userResponseModel._result.processing_capacity_productivity==null ) {
                                mBinding?.rbProductsYes?.isChecked = false
                                mBinding?.rbProductsNo?.isChecked = false

                            } else if (userResponseModel._result.processing_capacity_productivity == 1) {
                                mBinding?.rbProductsYes?.isChecked = true
                            } else {
                                mBinding?.rbProductsNo?.isChecked = true
                            }

                            if (userResponseModel._result.addition_new_products==null ) {
                                mBinding?.rbAdditionYes?.isChecked = false
                                mBinding?.rbAdditionNo?.isChecked = false

                            } else if (userResponseModel._result.addition_new_products == 1) {
                                mBinding?.rbAdditionYes?.isChecked = true
                            } else {
                                mBinding?.rbAdditionNo?.isChecked = true
                            }

                            if (userResponseModel._result.increase_sales==null ) {
                                mBinding?.rbIncreaseYes?.isChecked = false
                                mBinding?.rbIncreaseNo?.isChecked = false

                            } else if (userResponseModel._result.increase_sales == 1) {
                                mBinding?.rbIncreaseYes?.isChecked = true
                            } else {
                                mBinding?.rbIncreaseNo?.isChecked = true
                            }

                            if (userResponseModel._result.increase_operational==null ) {
                                mBinding?.rbOperationalYes?.isChecked = false
                                mBinding?.rbOperationalNo?.isChecked = false

                            } else if (userResponseModel._result.increase_operational == 1) {
                                mBinding?.rbOperationalYes?.isChecked = true
                            } else {
                                mBinding?.rbOperationalNo?.isChecked = true
                            }




                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.milk_processing_document?.forEach { document ->
                                if (document.ia_document == null) {
                                    DocumentList.add(document)//nlm
                                } else {
                                    viewDocumentList.add(document)//ia

                                }
                            }
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


    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddMilkProcessing, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
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
    private fun saveDataApi(itemId: Int?, draft: Int?) {
        if (draft != 0) {
            viewModel.getMilkProcessingAdd(
                this@AddMilkProcessing, true,
                AddMilkProcessingRequest(
                    id = itemId,
                    district_code = districtId,
                    role_id = getPreferenceOfScheme(
                        this@AddMilkProcessing,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        this@AddMilkProcessing,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddMilkProcessing,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    status = 1,
                    is_draft = draft,
                    name_processing_plant = mBinding?.etNameOfProcessingPlant?.text.toString(),
                    name_milk_union = mBinding?.etNameOfMilkUnion?.text.toString(),
                    name_of_tehsil = mBinding?.etNameOfTehsil?.text.toString(),
                    lat_processing_plant = mBinding?.etLat?.text.toString().toDoubleOrNull(),
                    long_processing_plant = mBinding?.etLong?.text.toString().toDoubleOrNull(),
                    date_of_inspection = tvDateOfInspection,
                    asset_earmarked_remarks = mBinding?.etRemarkAsset?.text.toString(),
                    overall_upkeep_asset_remarks = mBinding?.etOverallRemark?.text.toString(),
                    overall_hygiene_remarks = mBinding?.etHygieneRemark?.text.toString(),
                    has_new_plant_remarks = mBinding?.etNewRemark?.text.toString(),
                    whether_there_remarks = mBinding?.etWhetherRemark?.text.toString(),
                    any_other = mBinding?.etAnyOther?.text.toString(),
                    asset_earmarked_under = rbAsset,
                    overall_upkeep_asset = rbOverall,
                    overall_hygiene = rbHygiene,
                    has_new_plant = rbNew,
                    whether_there = rbWhether,
                    processing_capacity_milk = rbIncreased,
                    processing_capacity_productivity = rbProducts,
                    addition_new_products = rbAddition,
                    increase_sales = rbIncrease,
                    increase_operational = rbOperational,
                    milk_processing_document = totalListDocument

                )
            )
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddMilkProcessing, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getMilkProcessingAdd(
                        this@AddMilkProcessing, true,
                        AddMilkProcessingRequest(
                            id = itemId,
                            district_code = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddMilkProcessing,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
                            state_code = getPreferenceOfScheme(
                                this@AddMilkProcessing,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddMilkProcessing,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            name_processing_plant = mBinding?.etNameOfProcessingPlant?.text.toString(),
                            name_milk_union = mBinding?.etNameOfMilkUnion?.text.toString(),
                            name_of_tehsil = mBinding?.etNameOfTehsil?.text.toString(),
                            lat_processing_plant = mBinding?.etLat?.text.toString().toDoubleOrNull(),
                            long_processing_plant = mBinding?.etLong?.text.toString().toDoubleOrNull(),
                            date_of_inspection = tvDateOfInspection,
                            asset_earmarked_remarks = mBinding?.etRemarkAsset?.text.toString(),
                            overall_upkeep_asset_remarks = mBinding?.etOverallRemark?.text.toString(),
                            overall_hygiene_remarks = mBinding?.etHygieneRemark?.text.toString(),
                            has_new_plant_remarks = mBinding?.etNewRemark?.text.toString(),
                            whether_there_remarks = mBinding?.etWhetherRemark?.text.toString(),
                            any_other = mBinding?.etAnyOther?.text.toString(),
                            asset_earmarked_under = rbAsset,
                            overall_upkeep_asset = rbOverall,
                            overall_hygiene = rbHygiene,
                            has_new_plant = rbNew,
                            whether_there = rbWhether,
                            processing_capacity_milk = rbIncreased,
                            processing_capacity_productivity = rbProducts,
                            addition_new_products = rbAddition,
                            increase_sales = rbIncrease,
                            increase_operational = rbOperational,
                            milk_processing_document = totalListDocument,
                            lat_nlm=latitude,
                            long_nlm=longitude
                        )
                    )
                } else {
                    showSnackbar(mBinding?.clParent!!, "Please wait for a sec and click again")
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
            if (id != -1) {
                districtId = id
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
        uploadData = bindingDialog.ivPic
        if (selectedItem != null) {
            bindingDialog.ivPic.showView()
            if (selectedItem.is_edit == false) {
                bindingDialog.tvSubmit.hideView()
                bindingDialog.tvChooseFile.isEnabled = false
                bindingDialog.etDescription.isEnabled = false
            }
            UploadedDocumentName = selectedItem.nlm_document
            bindingDialog.etDoc.text = selectedItem.nlm_document
            bindingDialog.etDescription.setText(selectedItem.description)

            val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
            if (isSupported) {
                val url = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                when (fileExtension) {
                    "pdf" -> {
                        val downloader = AndroidDownloader(context)
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(R.drawable.ic_pdf)
                                .placeholder(R.drawable.ic_pdf).into(
                                    it
                                )
                        }
                        bindingDialog.etDoc.setOnClickListener {
                            if (!UploadedDocumentName.isNullOrEmpty()) {
                                downloader.downloadFile(url, UploadedDocumentName!!)
                                mBinding?.let { it1 ->
                                    showSnackbar(
                                        it1.clParent,
                                        "Download started"
                                    )
                                }
                                dialog.dismiss()
                            } else {
                                mBinding?.let { it1 ->
                                    showSnackbar(
                                        it1.clParent,
                                        "No document found"
                                    )
                                }
                                dialog.dismiss()
                            }
                        }

                    }

                    "png" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url)
                                .placeholder(R.drawable.ic_image_placeholder).into(
                                    it
                                )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url)
                                .placeholder(R.drawable.ic_image_placeholder).into(
                                    it
                                )
                        }
                    }
                }
            }
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                checkStoragePermission(this)
            } else {

                mBinding?.clParent?.let { showSnackbar(it, "please enter description") }
            }
        }

        val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
        if (isSupported) {
            when (fileExtension) {
                "pdf" -> {
//                    bindingDialog.ivPic.let {
//                        Glide.with(context).load(R.drawable.ic_pdf).into(
//                            it
//                        )
//                    }
                }

                else -> {
                    bindingDialog.ivPic.setOnClickListener {
                        Utility.showImageDialog(
                            this,
                            getPreferenceOfScheme(
                                this,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                        )
                    }
                }
            }
        }
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }


        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString()
                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        DocumentList[position] =
                            ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = null,
                                nlm_document = UploadedDocumentName,
                                milk_processing_id = selectedItem.milk_processing_id,
                                id = selectedItem.id,
                            )
                        addDocumentAdapter?.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                } else {
                    DocumentList.add(
                        ImplementingAgencyDocument(
                            bindingDialog.etDescription.text.toString(),
                            nlm_document = UploadedDocumentName,
                            id = null,
                            milk_processing_id = null,
                            ia_document = null
                        )
                    )
                    DocumentList.size.minus(1).let {
                        addDocumentAdapter?.notifyItemInserted(it)
                        dialog.dismiss()
//
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

    override fun showImage(bitmap: Bitmap) {
        // Override to display the image in this activity
        uploadData?.showView()
        uploadData?.setImageBitmap(bitmap)
        val imageFile = saveImageToFile(bitmap)
        photoFile = imageFile
        photoFile?.let { uploadImage(it) }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAPTURE_IMAGE_REQUEST -> {

                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    Log.d("DOCUMENT", imageBitmap.toString())
                    uploadData?.showView()
                    uploadData?.setImageBitmap(imageBitmap)
//                    data.data?.let { startCrop(it) }
//                    fetchLocation()
                }

                PICK_IMAGE -> {
                    val selectedImageUri = data?.data
                    Log.d("DOCUMENT", selectedImageUri.toString())
                    uploadData?.showView()
                    uploadData?.setImageURI(selectedImageUri)
                    if (selectedImageUri != null) {
                        val uriPathHelper = URIPathHelper()
                        val filePath = uriPathHelper.getPath(this, selectedImageUri)
                        val fileExtension =
                            filePath?.substringAfterLast('.', "").orEmpty().lowercase()
                        // Validate file extension
                        if (fileExtension in listOf("png", "jpg", "jpeg")) {
                            uploadData?.showView()
                            uploadData?.setImageURI(selectedImageUri)
                            val file = filePath?.let { File(it) }
                            file?.let { uploadImage(it) }
                        } else {
                            Toast.makeText(this, "Format not supported", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )
                        uploadData?.showView()
                        uploadData?.setImageResource(R.drawable.ic_pdf)
                        val cursor = this.contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
//                                DialogDocName?.text=DocumentName

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
                                table_name = getString(R.string.milk_processing_document).toRequestBody(
                                    MultipartBody.FORM
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
        if (ID.toString().isNotEmpty() && viewEdit == "edit") {
            viewModel.getDeleteSubTable(
                this@AddMilkProcessing, true,
                SubTableDeleteRequest(
                    ID,
                    getString(R.string.milk_processing_document)
                )
            )
        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(this, selectedItem, position)
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

    private fun uploadImage(file: File) {
        lifecycleScope.launch {
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body =
                MultipartBody.Part.createFormData(
                    "document_name",
                    file.name, reqFile
                )
            viewModel.getProfileUploadFile(
                context = this@AddMilkProcessing,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddMilkProcessing,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.milk_processing_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
}