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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddDcsCenterVisitBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.AddDairyPlantRequest
import com.nlm.model.AddDcsBmcRequest
import com.nlm.model.DairyPlantVisitReportNpddScheme
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

class AddDCSCenterVisit : BaseActivity<ActivityAddDcsCenterVisitBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddDcsCenterVisitBinding? = null
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

    private var mainLogYes: String? = null
    private var tempControlYes: String? = null
    private var dgAvailableYes: String? = null

    private var tvCooperativeRegisterDate: String? = null
    private var tvFssaiDate: String? = null
    private var tvBMCMonthOfInstallation: String? = null
    private var etAMCUDate: String? = null
    private var tvTrainingDateOfTraining: String? = null
    private var tvMilkPaymentDate: String? = null
    private var tvAssetDate: String? = null

    override val layoutId: Int
        get() = R.layout.activity_add_dcs_center_visit

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

        mBinding?.tvCooperativeRegisterDate?.setOnClickListener {
            openCalendar("tvCooperativeRegisterDate", mBinding?.tvCooperativeRegisterDate!!)
        }

        mBinding?.tvFssaiDate?.setOnClickListener {
            openCalendar("tvFssaiDate", mBinding?.tvFssaiDate!!)
        }

        mBinding?.tvBMCMonthOfInstallation?.setOnClickListener {
            openCalendar("tvBMCMonthOfInstallation", mBinding?.tvBMCMonthOfInstallation!!)
        }

        mBinding?.etAMCUDate?.setOnClickListener {
            openCalendar("etAMCUDate", mBinding?.etAMCUDate!!)
        }

        mBinding?.tvTrainingDateOfTraining?.setOnClickListener {
            openCalendar("tvTrainingDateOfTraining", mBinding?.tvTrainingDateOfTraining!!)
        }

        mBinding?.tvMilkPaymentDate?.setOnClickListener {
            openCalendar("tvMilkPaymentDate", mBinding?.tvMilkPaymentDate!!)
        }

        mBinding?.tvAssetDate?.setOnClickListener {
            openCalendar("tvAssetDate", mBinding?.tvAssetDate!!)
        }

        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
//
//            mBinding?.etLocationDairyPlant?.isEnabled = false
//            mBinding?.etFSSAILicenseNo?.isEnabled = false
//            mBinding?.etCapacityDairyPlant?.isEnabled = false
//            mBinding?.etYearEstablishment?.isEnabled = false
//            mBinding?.etTotalDcsCovered?.isEnabled = false
//            mBinding?.etTotalBMCNoCapacity?.isEnabled = false
//            mBinding?.etBMCNoOfMilkRoutes?.isEnabled = false
//            mBinding?.etNonBMCNoOfMilkRoutes?.isEnabled = false
//            mBinding?.etTotalRoutesNoOfMilkRoutes?.isEnabled = false
//            mBinding?.etLiquidMilkDailyAverage?.isEnabled = false
//            mBinding?.etGheeDailyAverage?.isEnabled = false
//            mBinding?.etCurdDailyAverage?.isEnabled = false
//            mBinding?.etLassiDailyAverage?.isEnabled = false
//            mBinding?.etTestsPerformedChemicalMicrobiological?.isEnabled = false
//            mBinding?.etFrequencyChemicalMicrobiological?.isEnabled = false
//            mBinding?.etTestsPerformed2ChemicalMicrobiological?.isEnabled = false
//            mBinding?.etFrequency2ChemicalMicrobiological?.isEnabled = false
//            mBinding?.etETP?.isEnabled = false
//            mBinding?.etCapacity?.isEnabled = false
//            mBinding?.etRemarksNlM?.isEnabled = false
//            mBinding?.rbIsTheDairyYes?.isEnabled = false
//            mBinding?.rbIsTheDairyNo?.isEnabled = false
//            mBinding?.rbQualityYes?.isEnabled = false
//            mBinding?.rbQualityNo?.isEnabled = false
//            mBinding?.tvChooseFile?.isEnabled = false
//            mBinding?.tvNLMDoc?.hideView()
//            mBinding?.tvScheme?.hideView()
//            mBinding?.tvSaveDraft?.hideView()
//            mBinding?.tvSendOtp?.hideView()


            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }

        mBinding?.rgMaintenance?.setOnCheckedChangeListener { group, checkedId ->
            mainLogYes = when (checkedId) {
                R.id.rbMainYes -> "Yes"
                R.id.rbMainNo -> "No"
                else -> null
            }
        }

        mBinding?.rgTemp?.setOnCheckedChangeListener { group, checkedId ->
            tempControlYes = when (checkedId) {
                R.id.rbTempYes -> "Yes"
                R.id.rbTempNo -> "No"
                else -> null
            }
        }

        mBinding?.rgDGAvailable?.setOnCheckedChangeListener { group, checkedId ->
            dgAvailableYes = when (checkedId) {
                R.id.rbAvailableYes -> "Yes"
                R.id.rbAvailableNo -> "No"
                else -> null
            }
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

        viewModel.getDcsBmcAdd(
            this, true,
            AddDcsBmcRequest(
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
            this@AddDCSCenterVisit,
            { _, year, month, day ->
                val calendarInstance = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                val formattedDate = sdf.format(calendarInstance.time)

                // Handle each case
                when (type) {
                    "tvCooperativeRegisterDate" -> tvCooperativeRegisterDate = formattedDate
                    "tvFssaiDate" -> tvFssaiDate = formattedDate
                    "tvBMCMonthOfInstallation" -> tvBMCMonthOfInstallation = formattedDate
                    "etAMCUDate" -> etAMCUDate = formattedDate
                    "tvTrainingDateOfTraining" -> tvTrainingDateOfTraining = formattedDate
                    "tvMilkPaymentDate" -> tvMilkPaymentDate = formattedDate
                    "tvAssetDate" -> tvAssetDate = formattedDate
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
                Utility.logout(this@AddDCSCenterVisit)
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
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }

        viewModel.dcsBmcAddResult.observe(this) {
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
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                            districtId = userResponseModel._result.districts
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.district_name
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etNameOfDCS?.setText(userResponseModel._result.name_of_dcs)
                            mBinding?.etVillage?.setText(userResponseModel._result.village)
                            mBinding?.etCooperativeRegisterNo?.setText(userResponseModel._result.cooperative_society_reg_num)
                            mBinding?.tvCooperativeRegisterDate?.setText(userResponseModel._result.cooperative_society_reg_date)
                            mBinding?.etFssaiNo?.setText(userResponseModel._result.fssai_lic_no)

                            mBinding?.tvFssaiDate?.setText(userResponseModel._result.fssai_lic_validity_date)
                            mBinding?.etYearEstablishment?.setText(userResponseModel._result.year_of_estb)
                            mBinding?.etTotalRegisterMemeber?.setText(userResponseModel._result.total_reg_numbers)
                            mBinding?.etActualPourer?.setText(userResponseModel._result.actual_pourer_members)
                            mBinding?.etMembersAC?.setText(userResponseModel._result.member_bank_ac.toString())
                            mBinding?.etDailyAVGLastYear?.setText(userResponseModel._result.daily_avg_procurement_milk_last_year.toString())
                            mBinding?.etDailyAVGCurrentYear?.setText(userResponseModel._result.daily_avg_procurement_milk_current_year.toString())
                            mBinding?.etCowMilkLastYear?.setText(userResponseModel._result.procurement_price_cow_milk_last_year)
                            mBinding?.etCowMilkCurrentYear?.setText(
                                userResponseModel._result.procurement_price_cow_milk_current_year
                            )
                            mBinding?.etBuffaloMilkLastYear?.setText(userResponseModel._result.procurement_price_buffalo_milk_last_year)
                            mBinding?.etBuffaloMilkCurrentYear?.setText(
                                userResponseModel._result.procurement_price_buffalo_milk_current_year
                            )
                            mBinding?.etIncentiveCowMilk?.setText(userResponseModel._result.incentive_procurement_price_cow.toString())
                            mBinding?.etIncentiveBuffaloMilk?.setText(userResponseModel._result.incentive_procurement_price_buffalo.toString())
                            mBinding?.etAvgFat?.setText(userResponseModel._result.avg_composition_milk_fat.toString())
                            mBinding?.etAvgSNF?.setText(userResponseModel._result.avg_composition_milk_snf.toString())
                            mBinding?.etQualityMilkProduced?.setText(userResponseModel._result.avg_procurement_price_qmp.toString())
                            mBinding?.etTotalAmountPaid?.setText(userResponseModel._result.avg_procurement_price_total_ap.toString())
                            mBinding?.etHoursSupply?.setText(userResponseModel._result.condition_electricity_supply_hrs_sply.toString())
                            mBinding?.BMCNpddNo?.setText(userResponseModel._result.bmc_details_npdd_prj_no)
                            mBinding?.etBMCCapacity?.setText(userResponseModel._result.bmc_details_capacity)
                            mBinding?.tvBMCMonthOfInstallation?.setText(userResponseModel._result.bmc_details_date_of_install)
                            mBinding?.etBMCAvgMilk?.setText(userResponseModel._result.bmc_details_avg_milk_collection.toString())
                            mBinding?.BMCAvgChilling?.setText(userResponseModel._result.bmc_details_avg_chil_cost_ltr.toString())
                            mBinding?.etTempControl?.setText(userResponseModel._result.bmc_details_temp_control_log_book_temp)
                            mBinding?.etDgAvRunTime?.setText(userResponseModel._result.bmc_details_dg_set_av_rt)
                            mBinding?.etOtherRegister?.setText(userResponseModel._result.bmc_details_other_reg)
                            mBinding?.etSpareParts?.setText(userResponseModel._result.bmc_details_spare_parts)
                            mBinding?.etAMCUNPDDProjectNo?.setText(userResponseModel._result.amcu_npdd_project_no)
                            mBinding?.etAMCUDate?.setText(userResponseModel._result.amcu_npdd_date_install)
                            mBinding?.etDCSInstallation?.setText(userResponseModel._result.dcs_bmc_installation)
                            mBinding?.etDCSMake?.setText(userResponseModel._result.dcs_bmc_make)
                            mBinding?.etDCSCalibrationStatus?.setText(userResponseModel._result.dcs_bmc_calibration_status)
                            mBinding?.etLastDate?.setText(userResponseModel._result.dcs_bmc_last_date)
                            mBinding?.etDCSFrequency?.setText(userResponseModel._result.dcs_bmc_fre_interval)
                            mBinding?.etAMCEquipement?.setText(userResponseModel._result.amc_equip_covered)
                            mBinding?.etAMCYear?.setText(userResponseModel._result.amc_equip_valid_years)
                            mBinding?.etAMCFeedback?.setText(userResponseModel._result.amc_equip_quality)
                            mBinding?.etAdditionalFacilities?.setText(userResponseModel._result.additional_facilities)
                            mBinding?.etChemicalTests?.setText(userResponseModel._result.chemical_tests_performed)
                            mBinding?.etChemicalFrequency?.setText(userResponseModel._result.chemical_frequency_tests)
                            mBinding?.etStaffSecretary?.setText(userResponseModel._result.staff_strength_at_dcs_secretary)
                            mBinding?.etStaffMilkTester?.setText(userResponseModel._result.staff_strength_at_dcs_milk_tester)
                            mBinding?.etStaffBMCOperator?.setText(userResponseModel._result.staff_strength_at_dcs_bmc_operator)
                            mBinding?.etStaffCleaner?.setText(userResponseModel._result.staff_strength_at_dcs_cleaner)
                            mBinding?.etDCSLastAuditYear?.setText(userResponseModel._result.dcs_last_audit_year)
                            mBinding?.etDCSProfitLoss?.setText(userResponseModel._result.dcs_profit_loss)
                            mBinding?.etDCSBonus?.setText(userResponseModel._result.dsc_bonus_distribution)
                            mBinding?.etDCSStatusElections?.setText(userResponseModel._result.dsc_bonus_distribution)
                            mBinding?.etDCSGeneralBody?.setText(userResponseModel._result.dsc_general)
                            mBinding?.etDCSAnyOther?.setText(userResponseModel._result.dsc_any_other)
                            mBinding?.tvTrainingDateOfTraining?.setText(userResponseModel._result.bmc_operator_secretary_date_of_training)
                            mBinding?.etTrainingPlace?.setText(userResponseModel._result.bmc_operator_secretary_place_of_training)
                            mBinding?.etTrainingNoDays?.setText(userResponseModel._result.bmc_operator_secretary_no_of_days)
                            mBinding?.etMilkPayment?.setText(userResponseModel._result.milk_payment_cycle)
                            mBinding?.tvMilkPaymentDate?.setText(userResponseModel._result.milk_payment_last_date)
                            mBinding?.etCommentOfNlm?.setText(userResponseModel._result.farmer_interaction_feedback)
                            mBinding?.etRemarkOfNlm?.setText(userResponseModel._result.overall_remarks)
                            mBinding?.etAssetSiteAssets?.setText(userResponseModel._result.photographs_of_site_assets)
                            mBinding?.tvAssetDate?.setText(userResponseModel._result.photographs_of_site_assets_date)
                            mBinding?.etAssetLocation?.setText(userResponseModel._result.photographs_of_site_assets_location)

                            if (userResponseModel._result.bmc_details_maintenance_log_book.isNullOrEmpty()) {
                                mBinding?.rbMainYes?.isChecked = false
                                mBinding?.rbMainNo?.isChecked = false

                            } else if (userResponseModel._result.bmc_details_maintenance_log_book == "1") {
                                mBinding?.rbMainYes?.isChecked = true
                            } else {
                                mBinding?.rbMainNo?.isChecked = true
                            }

                            if (userResponseModel._result.bmc_details_temp_control_log_book.isNullOrEmpty()) {
                                mBinding?.rbTempYes?.isChecked = false
                                mBinding?.rbTempNo?.isChecked = false

                            } else if (userResponseModel._result.bmc_details_temp_control_log_book == "Yes") {
                                mBinding?.rbTempYes?.isChecked = true
                            } else {
                                mBinding?.rbTempNo?.isChecked = true
                            }

                            if (userResponseModel._result.bmc_details_dg_set_available.isNullOrEmpty()) {
                                mBinding?.rbAvailableYes?.isChecked = false
                                mBinding?.rbAvailableNo?.isChecked = false

                            } else if (userResponseModel._result.bmc_details_dg_set_available == "Yes") {
                                mBinding?.rbAvailableYes?.isChecked = true
                            } else {
                                mBinding?.rbAvailableNo?.isChecked = true
                            }


                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.onsite_dcs_center_visit_document.forEach { document ->
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
            addDocumentDialog(this@AddDCSCenterVisit, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
        }

        fun photoSite(view: View) {
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
        viewModel.getDcsBmcAdd(
            this@AddDCSCenterVisit, true,
            AddDcsBmcRequest(
                id = itemId,
                districts = districtId,
                role_id = getPreferenceOfScheme(
                    this@AddDCSCenterVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this@AddDCSCenterVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this@AddDCSCenterVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                status = 1,
                is_draft = draft,
                name_of_dcs = mBinding?.etNameOfDCS?.text.toString(),
                village = mBinding?.etVillage?.text.toString(),
                cooperative_society_reg_num = mBinding?.etCooperativeRegisterNo?.text.toString().toIntOrNull(),
                cooperative_society_reg_date = tvCooperativeRegisterDate,
                fssai_lic_no = mBinding?.etFssaiNo?.text.toString().toIntOrNull(),
                fssai_lic_validity_date = tvFssaiDate,
                year_of_estb = mBinding?.etYearEstablishment?.text.toString(),
                total_reg_numbers = mBinding?.etTotalRegisterMemeber?.text.toString().toIntOrNull(),
                actual_pourer_members = mBinding?.etActualPourer?.text.toString(),
                member_bank_ac = mBinding?.etMembersAC?.text.toString().toLongOrNull(),
                daily_avg_procurement_milk_last_year = mBinding?.etDailyAVGLastYear?.text.toString().toDoubleOrNull(),
                daily_avg_procurement_milk_current_year = mBinding?.etDailyAVGCurrentYear?.text.toString().toDoubleOrNull(),
                procurement_price_cow_milk_last_year = mBinding?.etCowMilkLastYear?.text.toString().toIntOrNull(),
                procurement_price_cow_milk_current_year = mBinding?.etCowMilkCurrentYear?.text.toString().toIntOrNull(),
                procurement_price_buffalo_milk_last_year = mBinding?.etBuffaloMilkLastYear?.text.toString().toIntOrNull(),
                procurement_price_buffalo_milk_current_year = mBinding?.etBuffaloMilkCurrentYear?.text.toString().toIntOrNull(),
                incentive_procurement_price_cow = mBinding?.etIncentiveCowMilk?.text.toString().toDoubleOrNull(),
                incentive_procurement_price_buffalo = mBinding?.etIncentiveBuffaloMilk?.text.toString().toDoubleOrNull(),
                avg_composition_milk_fat = mBinding?.etAvgFat?.text.toString().toDoubleOrNull(),
                avg_composition_milk_snf = mBinding?.etAvgSNF?.text.toString().toDoubleOrNull(),
                avg_procurement_price_qmp = mBinding?.etQualityMilkProduced?.text.toString().toDoubleOrNull(),
                avg_procurement_price_total_ap = mBinding?.etTotalAmountPaid?.text.toString().toDoubleOrNull(),
                condition_electricity_supply_hrs_sply = mBinding?.etHoursSupply?.text.toString().toDoubleOrNull(),
                bmc_details_npdd_prj_no = mBinding?.BMCNpddNo?.text.toString(),
                bmc_details_capacity = mBinding?.etBMCCapacity?.text.toString(),
                bmc_details_date_of_install = tvBMCMonthOfInstallation,
                bmc_details_avg_milk_collection = mBinding?.etBMCAvgMilk?.text.toString().toDoubleOrNull(),
                bmc_details_avg_chil_cost_ltr = mBinding?.BMCAvgChilling?.text.toString().toDoubleOrNull(),
                bmc_details_maintenance_log_book = mainLogYes,
                bmc_details_temp_control_log_book = tempControlYes,
                bmc_details_temp_control_log_book_temp = mBinding?.etTempControl?.text.toString(),
                bmc_details_dg_set_available = dgAvailableYes,
                bmc_details_dg_set_av_rt = mBinding?.etDgAvRunTime?.text.toString(),
                bmc_details_other_reg = mBinding?.etOtherRegister?.text.toString(),
                bmc_details_spare_parts = mBinding?.etSpareParts?.text.toString(),
                amcu_npdd_project_no = mBinding?.etAMCUNPDDProjectNo?.text.toString(),
                amcu_npdd_date_install = etAMCUDate,
                dcs_bmc_installation = mBinding?.etDCSInstallation?.text.toString(),
                dcs_bmc_make = mBinding?.etDCSMake?.text.toString(),
                dcs_bmc_calibration_status = mBinding?.etDCSCalibrationStatus?.text.toString(),
                dcs_bmc_last_date = mBinding?.etLastDate?.text.toString(),
                dcs_bmc_fre_interval = mBinding?.etDCSFrequency?.text.toString(),
                amc_equip_covered = mBinding?.etAMCEquipement?.text.toString(),
                amc_equip_valid_years = mBinding?.etAMCYear?.text.toString(),
                amc_equip_quality = mBinding?.etAMCFeedback?.text.toString(),
                additional_facilities = mBinding?.etAdditionalFacilities?.text.toString(),
                chemical_tests_performed = mBinding?.etChemicalTests?.text.toString(),
                chemical_frequency_tests = mBinding?.etChemicalFrequency?.text.toString(),
                staff_strength_at_dcs_secretary = mBinding?.etStaffSecretary?.text.toString(),
                staff_strength_at_dcs_milk_tester = mBinding?.etStaffMilkTester?.text.toString(),
                staff_strength_at_dcs_bmc_operator = mBinding?.etStaffBMCOperator?.text.toString(),
                staff_strength_at_dcs_cleaner = mBinding?.etStaffCleaner?.text.toString(),
                dcs_last_audit_year = mBinding?.etDCSLastAuditYear?.text.toString(),
                dcs_profit_loss = mBinding?.etDCSProfitLoss?.text.toString(),
                dsc_bonus_distribution = mBinding?.etDCSBonus?.text.toString(),
                dsc_status_held = mBinding?.etDCSStatusElections?.text.toString(),
                dsc_general = mBinding?.etDCSGeneralBody?.text.toString(),
                dsc_any_other = mBinding?.etDCSAnyOther?.text.toString(),
                bmc_operator_secretary_date_of_training = tvTrainingDateOfTraining,
                bmc_operator_secretary_place_of_training = mBinding?.etTrainingPlace?.text.toString(),
                bmc_operator_secretary_no_of_days = mBinding?.etTrainingNoDays?.text.toString().toIntOrNull(),
                milk_payment_cycle = mBinding?.etMilkPayment?.text.toString(),
                milk_payment_last_date = tvMilkPaymentDate,
                farmer_interaction_feedback = mBinding?.etCommentOfNlm?.text.toString(),
                overall_remarks = mBinding?.etRemarkOfNlm?.text.toString(),
                photographs_of_site_assets = mBinding?.etAssetSiteAssets?.text.toString(),
                photographs_of_site_assets_date = tvAssetDate,
                photographs_of_site_assets_location = mBinding?.etAssetLocation?.text.toString(),
                photographs_of_site = photographSite,
                onsite_dcs_center_visit_document = totalListDocument,
            )
        )
        return
    }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddDCSCenterVisit, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getDcsBmcAdd(
                        this@AddDCSCenterVisit, true,
                        AddDcsBmcRequest(
                            id = itemId,
                            districts = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddDCSCenterVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
                            state_code = getPreferenceOfScheme(
                                this@AddDCSCenterVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddDCSCenterVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            name_of_dcs = mBinding?.etNameOfDCS?.text.toString(),
                            village = mBinding?.etVillage?.text.toString(),
                            cooperative_society_reg_num = mBinding?.etCooperativeRegisterNo?.text.toString().toIntOrNull(),
                            cooperative_society_reg_date = tvCooperativeRegisterDate,
                            fssai_lic_no = mBinding?.etFssaiNo?.text.toString().toIntOrNull(),
                            fssai_lic_validity_date = tvFssaiDate,
                            year_of_estb = mBinding?.etYearEstablishment?.text.toString(),
                            total_reg_numbers = mBinding?.etTotalRegisterMemeber?.text.toString().toIntOrNull(),
                            actual_pourer_members = mBinding?.etActualPourer?.text.toString(),
                            member_bank_ac = mBinding?.etMembersAC?.text.toString().toLongOrNull(),
                            daily_avg_procurement_milk_last_year = mBinding?.etDailyAVGLastYear?.text.toString().toDoubleOrNull(),
                            daily_avg_procurement_milk_current_year = mBinding?.etDailyAVGCurrentYear?.text.toString().toDoubleOrNull(),
                            procurement_price_cow_milk_last_year = mBinding?.etCowMilkLastYear?.text.toString().toIntOrNull(),
                            procurement_price_cow_milk_current_year = mBinding?.etCowMilkCurrentYear?.text.toString().toIntOrNull(),
                            procurement_price_buffalo_milk_last_year = mBinding?.etBuffaloMilkLastYear?.text.toString().toIntOrNull(),
                            procurement_price_buffalo_milk_current_year = mBinding?.etBuffaloMilkCurrentYear?.text.toString().toIntOrNull(),
                            incentive_procurement_price_cow = mBinding?.etIncentiveCowMilk?.text.toString().toDoubleOrNull(),
                            incentive_procurement_price_buffalo = mBinding?.etIncentiveBuffaloMilk?.text.toString().toDoubleOrNull(),
                            avg_composition_milk_fat = mBinding?.etAvgFat?.text.toString().toDoubleOrNull(),
                            avg_composition_milk_snf = mBinding?.etAvgSNF?.text.toString().toDoubleOrNull(),
                            avg_procurement_price_qmp = mBinding?.etQualityMilkProduced?.text.toString().toDoubleOrNull(),
                            avg_procurement_price_total_ap = mBinding?.etTotalAmountPaid?.text.toString().toDoubleOrNull(),
                            condition_electricity_supply_hrs_sply = mBinding?.etHoursSupply?.text.toString().toDoubleOrNull(),
                            bmc_details_npdd_prj_no = mBinding?.BMCNpddNo?.text.toString(),
                            bmc_details_capacity = mBinding?.etBMCCapacity?.text.toString(),
                            bmc_details_date_of_install = tvBMCMonthOfInstallation,
                            bmc_details_avg_milk_collection = mBinding?.etBMCAvgMilk?.text.toString().toDoubleOrNull(),
                            bmc_details_avg_chil_cost_ltr = mBinding?.BMCAvgChilling?.text.toString().toDoubleOrNull(),
                            bmc_details_maintenance_log_book = mainLogYes,
                            bmc_details_temp_control_log_book = tempControlYes,
                            bmc_details_temp_control_log_book_temp = mBinding?.etTempControl?.text.toString(),
                            bmc_details_dg_set_available =dgAvailableYes ,
                            bmc_details_dg_set_av_rt = mBinding?.etDgAvRunTime?.text.toString(),
                            bmc_details_other_reg = mBinding?.etOtherRegister?.text.toString(),
                            bmc_details_spare_parts = mBinding?.etSpareParts?.text.toString(),
                            amcu_npdd_project_no = mBinding?.etAMCUNPDDProjectNo?.text.toString(),
                            amcu_npdd_date_install = etAMCUDate,
                            dcs_bmc_installation = mBinding?.etDCSInstallation?.text.toString(),
                            dcs_bmc_make = mBinding?.etDCSMake?.text.toString(),
                            dcs_bmc_calibration_status = mBinding?.etDCSCalibrationStatus?.text.toString(),
                            dcs_bmc_last_date = mBinding?.etLastDate?.text.toString(),
                            dcs_bmc_fre_interval = mBinding?.etDCSFrequency?.text.toString(),
                            amc_equip_covered = mBinding?.etAMCEquipement?.text.toString(),
                            amc_equip_valid_years = mBinding?.etAMCYear?.text.toString(),
                            amc_equip_quality = mBinding?.etAMCFeedback?.text.toString(),
                            additional_facilities = mBinding?.etAdditionalFacilities?.text.toString(),
                            chemical_tests_performed = mBinding?.etChemicalTests?.text.toString(),
                            chemical_frequency_tests = mBinding?.etChemicalFrequency?.text.toString(),
                            staff_strength_at_dcs_secretary = mBinding?.etStaffSecretary?.text.toString(),
                            staff_strength_at_dcs_milk_tester = mBinding?.etStaffMilkTester?.text.toString(),
                            staff_strength_at_dcs_bmc_operator = mBinding?.etStaffBMCOperator?.text.toString(),
                            staff_strength_at_dcs_cleaner = mBinding?.etStaffCleaner?.text.toString(),
                            dcs_last_audit_year = mBinding?.etDCSLastAuditYear?.text.toString(),
                            dcs_profit_loss = mBinding?.etDCSProfitLoss?.text.toString(),
                            dsc_bonus_distribution = mBinding?.etDCSBonus?.text.toString(),
                            dsc_status_held = mBinding?.etDCSStatusElections?.text.toString(),
                            dsc_general = mBinding?.etDCSGeneralBody?.text.toString(),
                            dsc_any_other = mBinding?.etDCSAnyOther?.text.toString(),
                            bmc_operator_secretary_date_of_training = tvTrainingDateOfTraining,
                            bmc_operator_secretary_place_of_training = mBinding?.etTrainingPlace?.text.toString(),
                            bmc_operator_secretary_no_of_days = mBinding?.etTrainingNoDays?.text.toString().toIntOrNull(),
                            milk_payment_cycle = mBinding?.etMilkPayment?.text.toString(),
                            milk_payment_last_date = tvMilkPaymentDate,
                            farmer_interaction_feedback = mBinding?.etCommentOfNlm?.text.toString(),
                            overall_remarks = mBinding?.etRemarkOfNlm?.text.toString(),
                            photographs_of_site_assets = mBinding?.etAssetSiteAssets?.text.toString(),
                            photographs_of_site_assets_date = tvAssetDate,
                            photographs_of_site_assets_location = mBinding?.etAssetLocation?.text.toString(),
                            photographs_of_site = photographSite,
                            onsite_dcs_center_visit_document = totalListDocument,
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
            UploadedDocumentName = selectedItem.nlm_document
            bindingDialog.etDoc.text = selectedItem.nlm_document
            bindingDialog.etDescription.setText(selectedItem.description)
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                checkStoragePermission(this)
            } else {

                mBinding?.clParent?.let { showSnackbar(it, "please enter description") }
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
                                dairy_plant_visit_report_id = selectedItem.dairy_plant_visit_report_id,
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
                            dairy_plant_visit_report_id = null,
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
                                table_name = getString(R.string.onsite_dcs_center_visit_document).toRequestBody(
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
                context = this@AddDCSCenterVisit,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddDCSCenterVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.onsite_dcs_center_visit_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }

}