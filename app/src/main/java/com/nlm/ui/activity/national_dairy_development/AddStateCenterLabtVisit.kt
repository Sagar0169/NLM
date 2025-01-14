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
import com.nlm.databinding.ActivityAddStateCenterLabVisitBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AddDcsBmcRequest
import com.nlm.model.AddStateCenterLabRequest
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
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

class AddStateCenterLabtVisit : BaseActivity<ActivityAddStateCenterLabVisitBinding>(),
    CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddStateCenterLabVisitBinding? = null
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
    private lateinit var skillEmpDegree: ArrayList<String>
    private lateinit var skillEmpNo: ArrayList<String>
    private var rgNabl: String? = null
    private var rgCivilWork: Int? = null
    private var rgLCMS: Int? = null
    private var rgGCMS: Int? = null
    private var rgInduced: Int? = null
    private var rgFTIR: Int? = null
    private var rgBacterial: Int? = null
    private var rgWhether: Int? = null
    private var rgWhetherAntibiotic: Int? = null
    private var rgWhetherHeavy: Int? = null
    private var rgWhetherMicro: Int? = null
    private var rgWhetherFatty: Int? = null
    private var rgWhetherProtein: Int? = null
    private var rgWhetherAflat: Int? = null
    private var rgWhetherOthers: Int? = null
    private var rgCivilWorkStatus: Int? = null
    private var rgLCMSStatus: Int? = null
    private var rgGCMSStatus: Int? = null
    private var rgInducedStatus: Int? = null
    private var rgFTIRStatus: Int? = null
    private var rgBacterialStatus: Int? = null

    private var tvNablDate: String? = null
    private var tvCivilWorkDate: String? = null
    private var tvLCMSDate: String? = null
    private var tvGCMSDate: String? = null
    private var tvInducedDate: String? = null
    private var tvFTIRDate: String? = null
    private var tvBacterialDate: String? = null

    override val layoutId: Int
        get() = R.layout.activity_add_state_center_lab_visit

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
        skillEmpDegree = arrayListOf()
        skillEmpNo = arrayListOf()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))

        mBinding?.tvNablDate?.setOnClickListener {
            openCalendar("tvNablDate", mBinding?.tvNablDate!!)
        }

        mBinding?.tvCivilWorkDate?.setOnClickListener {
            openCalendar("tvCivilWorkDate", mBinding?.tvCivilWorkDate!!)
        }

        mBinding?.tvLCMSDate?.setOnClickListener {
            openCalendar("tvLCMSDate", mBinding?.tvLCMSDate!!)
        }

        mBinding?.tvGCMSDate?.setOnClickListener {
            openCalendar("tvGCMSDate", mBinding?.tvGCMSDate!!)
        }

        mBinding?.tvInducedDate?.setOnClickListener {
            openCalendar("tvInducedDate", mBinding?.tvInducedDate!!)
        }

        mBinding?.tvFTIRDate?.setOnClickListener {
            openCalendar("tvFTIRDate", mBinding?.tvFTIRDate!!)
        }

        mBinding?.tvBacterialDate?.setOnClickListener {
            openCalendar("tvBacterialDate", mBinding?.tvBacterialDate!!)
        }


        mBinding?.rgNabl?.setOnCheckedChangeListener { group, checkedId ->
            rgNabl = when (checkedId) {
                R.id.rbNablYes -> "Yes"
                R.id.rbNablNo -> "No"
                else -> null
            }
        }

        mBinding?.rgCivilWork?.setOnCheckedChangeListener { group, checkedId ->
            rgCivilWork = when (checkedId) {
                R.id.rbCivilWorkYes -> 1
                R.id.rbCivilWorkNo -> 0
                else -> null
            }
        }

        mBinding?.rgLCMS?.setOnCheckedChangeListener { group, checkedId ->
            rgLCMS = when (checkedId) {
                R.id.rgLCMSYes -> 1
                R.id.rgLCMSNo -> 0
                else -> null
            }
        }

        mBinding?.rgGCMS?.setOnCheckedChangeListener { group, checkedId ->
            rgGCMS = when (checkedId) {
                R.id.rbGCMSYes -> 1
                R.id.rbGCMSNo -> 0
                else -> null
            }
        }

        mBinding?.rgInduced?.setOnCheckedChangeListener { group, checkedId ->
            rgInduced = when (checkedId) {
                R.id.rbInducedYes -> 1
                R.id.rbInducedNo -> 0
                else -> null
            }
        }

        mBinding?.rgFTIR?.setOnCheckedChangeListener { group, checkedId ->
            rgFTIR = when (checkedId) {
                R.id.rbFTIRYes -> 1
                R.id.rbFTIRNo -> 0
                else -> null
            }
        }

        mBinding?.rgBacterial?.setOnCheckedChangeListener { group, checkedId ->
            rgBacterial = when (checkedId) {
                R.id.rbBacterialYes -> 1
                R.id.rbBacterialNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhether?.setOnCheckedChangeListener { group, checkedId ->
            rgWhether = when (checkedId) {
                R.id.rbWhetherYes -> 1
                R.id.rbWhetherNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherAntibiotic?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherAntibiotic = when (checkedId) {
                R.id.rbWhetherAntibioticYes -> 1
                R.id.rbWhetherAntibioticNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherHeavy?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherHeavy = when (checkedId) {
                R.id.rbWhetherHeavyYes -> 1
                R.id.rbWhetherHeavyNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherMicro?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherMicro = when (checkedId) {
                R.id.rbWhetherMicroYes -> 1
                R.id.rbWhetherMicroNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherFatty?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherFatty = when (checkedId) {
                R.id.rbWhetherFattyYes -> 1
                R.id.rbWhetherFattyNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherProtein?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherProtein = when (checkedId) {
                R.id.rbWhetherProteinYes -> 1
                R.id.rbWhetherProteinNo -> 0
                else -> null
            }
        }


        mBinding?.rgWhetherAflat?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherAflat = when (checkedId) {
                R.id.rbWhetherAflatYes -> 1
                R.id.rbWhetherAflatNo -> 0
                else -> null
            }
        }

        mBinding?.rgWhetherOthers?.setOnCheckedChangeListener { group, checkedId ->
            rgWhetherOthers = when (checkedId) {
                R.id.rbWhetherOthersYes -> 1
                R.id.rbWhetherOthersNo -> 0
                else -> null
            }
        }



        mBinding?.rgCivilWorkStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgCivilWorkStatus = when (checkedId) {
                R.id.rbCivilWorkOperational -> 1
                R.id.rbCivilWorkNonOperational -> 0
                else -> null
            }
        }

        mBinding?.rgLCMSStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgLCMSStatus = when (checkedId) {
                R.id.rbLCMSStatusOperational -> 1
                R.id.rbLCMSStatusNonOperational -> 0
                else -> null
            }
        }

        mBinding?.rgGCMSStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgGCMSStatus = when (checkedId) {
                R.id.rbGCMSStatusOperational -> 1
                R.id.rbGCMSStatusNonOperational -> 0
                else -> null
            }
        }


        mBinding?.rgInducedStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgInducedStatus = when (checkedId) {
                R.id.rbInducedStatusOperational -> 1
                R.id.rbInducedStatusNonOperational -> 0
                else -> null
            }
        }

        mBinding?.rgFTIRStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgFTIRStatus = when (checkedId) {
                R.id.rbFTIROperational -> 1
                R.id.rbFTIRNonOperational -> 0
                else -> null
            }
        }


        mBinding?.rgBacterialStatus?.setOnCheckedChangeListener { group, checkedId ->
            rgBacterialStatus = when (checkedId) {
                R.id.rbBacterialStatusOperational -> 1
                R.id.rbBacterialStatusNonOperational -> 0
                else -> null
            }
        }



        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))

            mBinding?.etLocationStateCentralLab?.isEnabled = false
            mBinding?.etYearEst?.isEnabled = false
            mBinding?.tvCivilWorkDate?.isEnabled = false
            mBinding?.tvNablDate?.isEnabled = false
            mBinding?.tvLCMSDate?.isEnabled = false
            mBinding?.tvCivilWorkDate?.isEnabled = false
            mBinding?.tvGCMSDate?.isEnabled = false
            mBinding?.tvInducedDate?.isEnabled = false
            mBinding?.tvFTIRDate?.isEnabled = false
            mBinding?.tvBacterialDate?.isEnabled = false
            mBinding?.etFrequency?.isEnabled = false
            mBinding?.etAntibiotic?.isEnabled = false
            mBinding?.etHeavy?.isEnabled = false
            mBinding?.etMicro?.isEnabled = false
            mBinding?.etFatty?.isEnabled = false
            mBinding?.etProtein?.isEnabled = false
            mBinding?.etAflat?.isEnabled = false
            mBinding?.etOthers?.isEnabled = false
            mBinding?.etLoss?.isEnabled = false
            mBinding?.etProfit?.isEnabled = false
            mBinding?.etFuturePlans?.isEnabled = false
            mBinding?.etCommentsNlm?.isEnabled = false
            mBinding?.rbNablYes?.isEnabled = false
            mBinding?.rbNablNo?.isEnabled = false
            mBinding?.rbCivilWorkYes?.isEnabled = false
            mBinding?.rbCivilWorkNo?.isEnabled = false
            mBinding?.rgLCMSYes?.isEnabled = false
            mBinding?.rgLCMSNo?.isEnabled = false
            mBinding?.rbGCMSYes?.isEnabled = false
            mBinding?.rbGCMSNo?.isEnabled = false
            mBinding?.rbInducedYes?.isEnabled = false
            mBinding?.rbInducedNo?.isEnabled = false
            mBinding?.rbFTIRYes?.isEnabled = false
            mBinding?.rbFTIRNo?.isEnabled = false
            mBinding?.rbBacterialYes?.isEnabled = false
            mBinding?.rbBacterialNo?.isEnabled = false
            mBinding?.rbWhetherYes?.isEnabled = false
            mBinding?.rbWhetherNo?.isEnabled = false
            mBinding?.rbWhetherAntibioticYes?.isEnabled = false
            mBinding?.rbWhetherAntibioticNo?.isEnabled = false
            mBinding?.rbWhetherHeavyYes?.isEnabled = false
            mBinding?.rbWhetherHeavyNo?.isEnabled = false
            mBinding?.rbWhetherMicroYes?.isEnabled = false
            mBinding?.rbWhetherMicroNo?.isEnabled = false
            mBinding?.rbWhetherFattyYes?.isEnabled = false
            mBinding?.rbWhetherFattyNo?.isEnabled = false
            mBinding?.rbWhetherProteinYes?.isEnabled = false
            mBinding?.rbWhetherProteinNo?.isEnabled = false
            mBinding?.rbWhetherAflatYes?.isEnabled = false
            mBinding?.rbWhetherAflatNo?.isEnabled = false
            mBinding?.rbWhetherOthersYes?.isEnabled = false
            mBinding?.rbWhetherOthersNo?.isEnabled = false
            mBinding?.rbCivilWorkOperational?.isEnabled = false
            mBinding?.rbCivilWorkNonOperational?.isEnabled = false
            mBinding?.rbLCMSStatusOperational?.isEnabled = false
            mBinding?.rbLCMSStatusNonOperational?.isEnabled = false
            mBinding?.rbGCMSStatusOperational?.isEnabled = false
            mBinding?.rbGCMSStatusNonOperational?.isEnabled = false
            mBinding?.rbInducedStatusOperational?.isEnabled = false
            mBinding?.rbInducedStatusNonOperational?.isEnabled = false
            mBinding?.rbFTIROperational?.isEnabled = false
            mBinding?.rbFTIRNonOperational?.isEnabled = false
            mBinding?.rbBacterialStatusOperational?.isEnabled = false
            mBinding?.rbBacterialStatusNonOperational?.isEnabled = false
            mBinding?.etDegreeOne?.isEnabled = false
            mBinding?.etDegreeThree?.isEnabled = false
            mBinding?.etDegreeTwo?.isEnabled = false
            mBinding?.etDegreeFour?.isEnabled = false
            mBinding?.etNoEmpOne?.isEnabled = false
            mBinding?.etNoEmpThree?.isEnabled = false
            mBinding?.etNoEmpTwo?.isEnabled = false
            mBinding?.etNoEmpFour?.isEnabled = false
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

        viewModel.getStateCenterLabAdd(
            this, true,
            AddStateCenterLabRequest(
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
                districts = dId,
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
            this@AddStateCenterLabtVisit,
            { _, year, month, day ->
                val calendarInstance = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                val formattedDate = sdf.format(calendarInstance.time)

                // Handle each case
                when (type) {
                    "tvNablDate" -> tvNablDate = formattedDate
                    "tvCivilWorkDate" -> tvCivilWorkDate = formattedDate
                    "tvLCMSDate" -> tvLCMSDate = formattedDate
                    "tvGCMSDate" -> tvGCMSDate = formattedDate
                    "tvInducedDate" -> tvInducedDate = formattedDate
                    "tvFTIRDate" -> tvFTIRDate = formattedDate
                    "tvBacterialDate" -> tvBacterialDate = formattedDate
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
                Utility.logout(this@AddStateCenterLabtVisit)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtList.clear()

                        val remainingCount = userResponseModel.total_count % 100
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 100
                            count
                        } else {
                            val count = userResponseModel.total_count / 100
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

        viewModel.stateCenterLabAddResult.observe(this) {
            val userResponseModel = it
            toast(savedAsDraft.toString())
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {

                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {
//                    toast(savedAsDraft.toString())
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        TableName = userResponseModel.fileurl
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
//                                listener?.onNextButtonClick()
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                            districtId = userResponseModel._result.districts
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.district_name
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etLocationStateCentralLab?.setText(userResponseModel._result.location_state_central_lab)
                            mBinding?.etYearEst?.setText(userResponseModel._result.year_of_establishment)
                            mBinding?.tvNablDate?.text =
                                convertDate(userResponseModel._result.nabl_certified_date.toString())
                            mBinding?.tvCivilWorkDate?.text =
                                convertDate(userResponseModel._result.equipments_civil_work_month_install)
                            mBinding?.tvLCMSDate?.text =
                                convertDate(userResponseModel._result.equipments_lcms_month_install)
                            mBinding?.tvGCMSDate?.text =
                                convertDate(userResponseModel._result.equipments_lcms_month_install)
                            mBinding?.tvInducedDate?.text =
                                convertDate(userResponseModel._result.equipments_induced_couple_month_install)
                            mBinding?.tvFTIRDate?.text =
                                convertDate(userResponseModel._result.equipments_ftir_month_install)
                            mBinding?.tvBacterialDate?.text =
                                convertDate(userResponseModel._result.equipments_bacterial_month_install)




                            mBinding?.etFrequency?.setText(userResponseModel._result.chemical_test_pesticide_frequency)
                            mBinding?.etAntibiotic?.setText(userResponseModel._result.chemical_test_pesticide_frequency)
                            mBinding?.etHeavy?.setText(userResponseModel._result.chemical_test_metal_frequency)
                            mBinding?.etMicro?.setText(userResponseModel._result.chemical_test_microbiological_frequency)
                            mBinding?.etFatty?.setText(userResponseModel._result.chemical_test_acid_profile_frequency)
                            mBinding?.etProtein?.setText(userResponseModel._result.chemical_test_protein_frequency)
                            mBinding?.etAflat?.setText(userResponseModel._result.chemical_test_aflatoxins_frequency)
                            mBinding?.etOthers?.setText(userResponseModel._result.chemical_test_others_frequency)
                            mBinding?.etLoss?.setText(
                                userResponseModel._result.financial_viability_cost_loss.toString()
                            )
                            mBinding?.etProfit?.setText(userResponseModel._result.financial_viability_cost_profit.toString())
                            mBinding?.etFuturePlans?.setText(
                                userResponseModel._result.future_plans.toString()
                            )
                            mBinding?.etCommentsNlm?.setText(userResponseModel._result.comments_of_nlm)

                            if (userResponseModel._result.nabl_certified.isNullOrEmpty()) {
                                mBinding?.rbNablYes?.isChecked = false
                                mBinding?.rbNablNo?.isChecked = false

                            } else if (userResponseModel._result.nabl_certified == "Yes") {
                                mBinding?.rbNablYes?.isChecked = true
                            } else {
                                mBinding?.rbNablNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_civil_work_avail_site.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbCivilWorkYes?.isChecked = false
                                mBinding?.rbCivilWorkNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_civil_work_avail_site == 1) {
                                mBinding?.rbCivilWorkYes?.isChecked = true
                            } else {
                                mBinding?.rbCivilWorkNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_lcms_avail_site.isNullOrEmpty()) {
                                mBinding?.rgLCMSYes?.isChecked = false
                                mBinding?.rgLCMSNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_lcms_avail_site == "1") {
                                mBinding?.rgLCMSYes?.isChecked = true
                            } else {
                                mBinding?.rgLCMSNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_gcms_avail_site.isNullOrEmpty()) {
                                mBinding?.rbGCMSYes?.isChecked = false
                                mBinding?.rbGCMSNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_gcms_avail_site == "1") {
                                mBinding?.rbGCMSYes?.isChecked = true
                            } else {
                                mBinding?.rbGCMSNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_induced_couple_avail_site.isNullOrEmpty()) {
                                mBinding?.rbInducedYes?.isChecked = false
                                mBinding?.rbInducedNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_induced_couple_avail_site == "1") {
                                mBinding?.rbInducedYes?.isChecked = true
                            } else {
                                mBinding?.rbInducedNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_ftir_avail_site.isNullOrEmpty()) {
                                mBinding?.rbFTIRYes?.isChecked = false
                                mBinding?.rbFTIRNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_ftir_avail_site == "1") {
                                mBinding?.rbFTIRYes?.isChecked = true
                            } else {
                                mBinding?.rbFTIRNo?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_bacterial_avail_site.isNullOrEmpty()) {
                                mBinding?.rbBacterialYes?.isChecked = false
                                mBinding?.rbBacterialNo?.isChecked = false

                            } else if (userResponseModel._result.equipments_bacterial_avail_site == "1") {
                                mBinding?.rbBacterialYes?.isChecked = true
                            } else {
                                mBinding?.rbBacterialNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_pesticide_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherYes?.isChecked = false
                                mBinding?.rbWhetherNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_pesticide_result == "1") {
                                mBinding?.rbWhetherYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_antibiotic_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherAntibioticYes?.isChecked = false
                                mBinding?.rbWhetherAntibioticNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_antibiotic_result == "1") {
                                mBinding?.rbWhetherAntibioticYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherAntibioticNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_metal_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherHeavyYes?.isChecked = false
                                mBinding?.rbWhetherHeavyNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_metal_result == "1") {
                                mBinding?.rbWhetherHeavyYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherHeavyNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_microbiological_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherMicroYes?.isChecked = false
                                mBinding?.rbWhetherMicroNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_microbiological_result == "1") {
                                mBinding?.rbWhetherMicroYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherMicroNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_acid_profile_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherFattyYes?.isChecked = false
                                mBinding?.rbWhetherFattyNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_acid_profile_result == "1") {
                                mBinding?.rbWhetherFattyYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherFattyNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_protein_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherProteinYes?.isChecked = false
                                mBinding?.rbWhetherProteinNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_protein_result == "1") {
                                mBinding?.rbWhetherProteinYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherProteinNo?.isChecked = true
                            }

                            if (userResponseModel._result.chemical_test_aflatoxins_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherAflatYes?.isChecked = false
                                mBinding?.rbWhetherAflatNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_aflatoxins_result == "1") {
                                mBinding?.rbWhetherAflatYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherAflatNo?.isChecked = true
                            }


                            if (userResponseModel._result.chemical_test_others_result.isNullOrEmpty()) {
                                mBinding?.rbWhetherOthersYes?.isChecked = false
                                mBinding?.rbWhetherOthersNo?.isChecked = false

                            } else if (userResponseModel._result.chemical_test_others_result == "1") {
                                mBinding?.rbWhetherOthersYes?.isChecked = true
                            } else {
                                mBinding?.rbWhetherOthersNo?.isChecked = true
                            }


                            if (userResponseModel._result.equipments_civil_work_status.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbCivilWorkOperational?.isChecked = false
                                mBinding?.rbCivilWorkNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_civil_work_status == 1) {
                                mBinding?.rbCivilWorkOperational?.isChecked = true
                            } else {
                                mBinding?.rbCivilWorkNonOperational?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_lcms_status.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbLCMSStatusOperational?.isChecked = false
                                mBinding?.rbLCMSStatusNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_lcms_status == 1) {
                                mBinding?.rbLCMSStatusOperational?.isChecked = true
                            } else {
                                mBinding?.rbLCMSStatusNonOperational?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_gcms_status.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbGCMSStatusOperational?.isChecked = false
                                mBinding?.rbGCMSStatusNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_gcms_status == 1) {
                                mBinding?.rbGCMSStatusOperational?.isChecked = true
                            } else {
                                mBinding?.rbGCMSStatusNonOperational?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_induced_couple_status.isNullOrEmpty()) {
                                mBinding?.rbInducedStatusOperational?.isChecked = false
                                mBinding?.rbInducedStatusNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_induced_couple_status == "1") {
                                mBinding?.rbInducedStatusOperational?.isChecked = true
                            } else {
                                mBinding?.rbInducedStatusNonOperational?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_ftir_status.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbFTIROperational?.isChecked = false
                                mBinding?.rbFTIRNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_ftir_status == 1) {
                                mBinding?.rbFTIROperational?.isChecked = true
                            } else {
                                mBinding?.rbFTIRNonOperational?.isChecked = true
                            }

                            if (userResponseModel._result.equipments_bacterial_status.toString()
                                    .isNullOrEmpty()
                            ) {
                                mBinding?.rbBacterialStatusOperational?.isChecked = false
                                mBinding?.rbBacterialStatusNonOperational?.isChecked = false

                            } else if (userResponseModel._result.equipments_bacterial_status == 1) {
                                mBinding?.rbBacterialStatusOperational?.isChecked = true
                            } else {
                                mBinding?.rbBacterialStatusNonOperational?.isChecked = true
                            }

                            if (!userResponseModel._result.skill_emp_details_qualification.isNullOrEmpty()) {
                                mBinding?.etDegreeOne?.setText(userResponseModel._result.skill_emp_details_qualification[0])
                                mBinding?.etDegreeThree?.setText(userResponseModel._result.skill_emp_details_qualification[2])
                                mBinding?.etDegreeTwo?.setText(userResponseModel._result.skill_emp_details_qualification[1])
                                mBinding?.etDegreeFour?.setText(userResponseModel._result.skill_emp_details_qualification[3])
                            }

                            if (!userResponseModel._result.skill_emp_details_no.isNullOrEmpty()) {
                                mBinding?.etNoEmpOne?.setText(userResponseModel._result.skill_emp_details_no[0])
                                mBinding?.etNoEmpThree?.setText(userResponseModel._result.skill_emp_details_no[2])
                                mBinding?.etNoEmpTwo?.setText(userResponseModel._result.skill_emp_details_no[1])
                                mBinding?.etNoEmpFour?.setText(userResponseModel._result.skill_emp_details_no[3])
                            }


                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.state_central_lab_visit_document.forEach { document ->
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
            addDocumentDialog(this@AddStateCenterLabtVisit, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
        }

        fun save(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            skillEmpDegree.clear()
            skillEmpNo.clear()
            skillEmpDegree.add(mBinding?.etDegreeOne?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeTwo?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeThree?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeFour?.text?.toString() ?: "")


            skillEmpNo.add(mBinding?.etNoEmpOne?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpTwo?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpThree?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpFour?.text.toString())
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
            skillEmpDegree.clear()
            skillEmpNo.clear()
            skillEmpDegree.add(mBinding?.etDegreeOne?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeTwo?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeThree?.text?.toString() ?: "")
            skillEmpDegree.add(mBinding?.etDegreeFour?.text?.toString() ?: "")


            skillEmpNo.add(mBinding?.etNoEmpOne?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpTwo?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpThree?.text.toString())
            skillEmpNo.add(mBinding?.etNoEmpFour?.text.toString())
            Log.d("ListCheck", skillEmpDegree.toString())
            Log.d("ListCheck", skillEmpNo.toString())
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
            viewModel.getStateCenterLabAdd(
                this@AddStateCenterLabtVisit, true,
                AddStateCenterLabRequest(
                    id = itemId,
                    districts = districtId,
                    role_id = getPreferenceOfScheme(
                        this@AddStateCenterLabtVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        this@AddStateCenterLabtVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddStateCenterLabtVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    status = 1,
                    is_draft = draft,
                    location_state_central_lab = mBinding?.etLocationStateCentralLab?.text.toString(),
                    year_of_establishment = mBinding?.etYearEst?.text.toString(),
                    nabl_certified = rgNabl,
                    nabl_certified_date = tvNablDate,
                    equipments_civil_work_avail_site = rgCivilWork,
                    equipments_civil_work_month_install = tvCivilWorkDate,
                    equipments_civil_work_status = rgCivilWorkStatus,
                    equipments_lcms_avail_site = rgLCMS,
                    equipments_lcms_month_install = tvLCMSDate,
                    equipments_lcms_status = rgLCMSStatus,
                    equipments_gcms_avail_site = rgGCMS,
                    equipments_gcms_month_install = tvGCMSDate,
                    equipments_gcms_status = rgGCMSStatus,
                    equipments_induced_couple_avail_site = rgInduced,
                    equipments_induced_couple_month_install = tvInducedDate,
                    equipments_induced_couple_status = rgInducedStatus,
                    equipments_ftir_avail_site = rgFTIR,
                    equipments_ftir_month_install = tvFTIRDate,
                    equipments_ftir_status = rgFTIRStatus,
                    equipments_bacterial_avail_site = rgBacterial,
                    equipments_bacterial_month_install = tvBacterialDate,
                    equipments_bacterial_status = rgBacterialStatus,
                    chemical_test_pesticide_result = rgWhether,
                    chemical_test_pesticide_frequency = mBinding?.etFrequency?.text.toString(),
                    chemical_test_antibiotic_result = rgWhetherAntibiotic,
                    chemical_test_antibiotic_frequency = mBinding?.etAntibiotic?.text.toString(),
                    chemical_test_metal_result = rgWhetherHeavy,
                    chemical_test_metal_frequency = mBinding?.etHeavy?.text.toString(),
                    chemical_test_microbiological_result = rgWhetherMicro,
                    chemical_test_microbiological_frequency = mBinding?.etMicro?.text.toString(),
                    chemical_test_acid_profile_result = rgWhetherFatty,
                    chemical_test_acid_profile_frequency = mBinding?.etFatty?.text.toString(),
                    chemical_test_protein_result = rgWhetherProtein,
                    chemical_test_protein_frequency = mBinding?.etProtein?.text.toString(),
                    chemical_test_aflatoxins_result = rgWhetherAflat,
                    chemical_test_aflatoxins_frequency = mBinding?.etAflat?.text.toString(),
                    chemical_test_others_result = rgWhetherOthers,
                    chemical_test_others_frequency = mBinding?.etOthers?.text.toString(),
                    skill_emp_details_qualification = skillEmpDegree,
                    skill_emp_details_no = skillEmpNo,
                    financial_viability_cost_loss = mBinding?.etLoss?.text.toString(),
                    financial_viability_cost_profit = mBinding?.etProfit?.text.toString(),
                    future_plans = mBinding?.etFuturePlans?.text.toString(),
                    comments_of_nlm = mBinding?.etCommentsNlm?.text.toString(),
                    state_central_lab_visit_document = totalListDocument,
                )
            )
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddStateCenterLabtVisit, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getStateCenterLabAdd(
                        this@AddStateCenterLabtVisit, true,
                        AddStateCenterLabRequest(
                            id = itemId,
                            districts = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddStateCenterLabtVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
                            state_code = getPreferenceOfScheme(
                                this@AddStateCenterLabtVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddStateCenterLabtVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            location_state_central_lab = mBinding?.etLocationStateCentralLab?.text.toString(),
                            year_of_establishment = mBinding?.etYearEst?.text.toString(),
                            nabl_certified = rgNabl,
                            nabl_certified_date = tvNablDate,
                            equipments_civil_work_avail_site = rgCivilWork,
                            equipments_civil_work_month_install = tvCivilWorkDate,
                            equipments_civil_work_status = rgCivilWorkStatus,
                            equipments_lcms_avail_site = rgLCMS,
                            equipments_lcms_month_install = tvLCMSDate,
                            equipments_lcms_status = rgLCMSStatus,
                            equipments_gcms_avail_site = rgGCMS,
                            equipments_gcms_month_install = tvGCMSDate,
                            equipments_gcms_status = rgGCMSStatus,
                            equipments_induced_couple_avail_site = rgInduced,
                            equipments_induced_couple_month_install = tvInducedDate,
                            equipments_induced_couple_status = rgInducedStatus,
                            equipments_ftir_avail_site = rgFTIR,
                            equipments_ftir_month_install = tvFTIRDate,
                            equipments_ftir_status = rgFTIRStatus,
                            equipments_bacterial_avail_site = rgBacterial,
                            equipments_bacterial_month_install = tvBacterialDate,
                            equipments_bacterial_status = rgBacterialStatus,
                            chemical_test_pesticide_result = rgWhether,
                            chemical_test_pesticide_frequency = mBinding?.etFrequency?.text.toString(),
                            chemical_test_antibiotic_result = rgWhetherAntibiotic,
                            chemical_test_antibiotic_frequency = mBinding?.etAntibiotic?.text.toString(),
                            chemical_test_metal_result = rgWhetherHeavy,
                            chemical_test_metal_frequency = mBinding?.etHeavy?.text.toString(),
                            chemical_test_microbiological_result = rgWhetherMicro,
                            chemical_test_microbiological_frequency = mBinding?.etMicro?.text.toString(),
                            chemical_test_acid_profile_result = rgWhetherFatty,
                            chemical_test_acid_profile_frequency = mBinding?.etFatty?.text.toString(),
                            chemical_test_protein_result = rgWhetherProtein,
                            chemical_test_protein_frequency = mBinding?.etProtein?.text.toString(),
                            chemical_test_aflatoxins_result = rgWhetherAflat,
                            chemical_test_aflatoxins_frequency = mBinding?.etAflat?.text.toString(),
                            chemical_test_others_result = rgWhetherOthers,
                            chemical_test_others_frequency = mBinding?.etOthers?.text.toString(),
                            skill_emp_details_qualification = skillEmpDegree,
                            skill_emp_details_no = skillEmpNo,
                            financial_viability_cost_loss = mBinding?.etLoss?.text.toString(),
                            financial_viability_cost_profit = mBinding?.etProfit?.text.toString(),
                            future_plans = mBinding?.etFuturePlans?.text.toString(),
                            comments_of_nlm = mBinding?.etCommentsNlm?.text.toString(),
                            state_central_lab_visit_document = totalListDocument,
                            lat_nlm = latitude,
                            long_nlm = longitude

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
                100,
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
                                state_central_lab_visit_id = selectedItem.state_central_lab_visit_id,
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
                            state_central_lab_visit_id = null,
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
                                table_name = getString(R.string.state_central_lab_visit_document).toRequestBody(
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
                context = this@AddStateCenterLabtVisit,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddStateCenterLabtVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.state_central_lab_visit_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
}