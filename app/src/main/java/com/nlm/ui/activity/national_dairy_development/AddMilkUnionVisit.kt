package com.nlm.ui.activity.national_dairy_development

import android.annotation.SuppressLint
import android.app.Activity
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
import com.nlm.databinding.ActivityAddMilkUnionVisitBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AddDairyPlantRequest
import com.nlm.model.DairyPlantVisitReportNpddScheme
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.MilkUnionAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterNDD
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
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddMilkUnionVisit : BaseActivity<ActivityAddMilkUnionVisitBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddMilkUnionVisitBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var stateAdapter: BottomSheetAdapter
    var body: MultipartBody.Part? = null
    private var viewModel = ViewModel()
    private var viewEdit: String? = null
    var itemId: Int? = null
    private lateinit var milkLocation: ArrayList<String>
    private lateinit var milkCapacity: ArrayList<String>
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
    private var photographSite: String? = null
    private var totalPage = 1
    private var uploadData: ImageView? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var TableName: String? = null
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
    override val layoutId: Int
        get() = R.layout.activity_add_milk_union_visit

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
        milkLocation = arrayListOf()
        milkCapacity = arrayListOf()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))

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

        viewModel.getMilkUnionAdd(
            this, true,
            MilkUnionAddRequest(
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
//                districts_covered = dId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit
            )
        )
    }


    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddMilkUnionVisit)
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
                    TableName=userResponseModel._result.table_name
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



        viewModel.milkUnionAddResult.observe(this) {
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
                        TableName=userResponseModel.fileurl
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
//                                listener?.onNextButtonClick()
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
//                            districtId = userResponseModel._result.districts
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.districts_covered
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etNameOfMilkUnion?.setText(userResponseModel._result.name_of_milk_union)
                            mBinding?.etTotalNoOfVillages?.setText(userResponseModel._result.no_of_villages)
                            mBinding?.etNoOfVillagesCovered?.setText(userResponseModel._result.no_of_villages_covered)
                            mBinding?.etNoOfMilkProducers?.setText(userResponseModel._result.no_of_milk_producers_enr)
                            mBinding?.etOrganised?.setText(userResponseModel._result.no_of_dairy_organised)
                            mBinding?.etFunctional?.setText(userResponseModel._result.no_of_dairy_functional)

                            mBinding?.tvTotalLocationCapacity?.setText(userResponseModel._result.milk_processing_plant_total)
                            mBinding?.etBulkMilkChillersNumber?.setText(userResponseModel._result.no_of_milk_chillers)
                            mBinding?.etBulkMilkChillersTotalCapacity?.setText(userResponseModel._result.capacity_of_milk_chillers)
                            mBinding?.etMilkProcurementAverageLastYear?.setText(userResponseModel._result.last_year_avg_milk)
                            mBinding?.etMilkProcurementAverageCurrentYear?.setText(userResponseModel._result.current_year_avg_milk)
                            mBinding?.etMilkProcurementAverageCurrentMonth?.setText(userResponseModel._result.current_month_avg_milk)
                            mBinding?.etMilkProductSaleLiquidMilk?.setText(userResponseModel._result.daily_liquid_milk_sale)
                            mBinding?.etMilkProductSaleBulkSale?.setText(userResponseModel._result.daily_bulk_sale_to_other)
                            mBinding?.etMilkProductSaleValueAdded?.setText(
                                userResponseModel._result.daily_value_added_product
                            )
                            mBinding?.etAPPCowLastYear?.setText(userResponseModel._result.last_year_cow_milk_price)
                            mBinding?.etAPPCowCurrentYear?.setText(
                                userResponseModel._result.current_year_cow_milk_price
                            )
                            mBinding?.etAPPBuffaloLastYear?.setText(userResponseModel._result.last_year_buffalo_milk_price)
                            mBinding?.etAPPBuffaloCurrentYear?.setText(userResponseModel._result.current_year_buffalo_milk_price)
                            mBinding?.etIPCowLastYear?.setText(userResponseModel._result.cow_milk_last_year_incentives)
                            mBinding?.etIPCowCurrentYear?.setText(userResponseModel._result.cow_milk_current_year_incentives)
                            mBinding?.etIPBuffaloLastYear?.setText(userResponseModel._result.buffalo_milk_last_year_incentives)
                            mBinding?.etIPBuffaloCurrentYear?.setText(userResponseModel._result.buffalo_milk_current_year_incentives)
                            mBinding?.etFPTurnOver?.setText(userResponseModel._result.last_fy_turnover)
                            mBinding?.etFPCumulativeProfit?.setText(userResponseModel._result.last_fy_cumulative_profit_loss)
                            mBinding?.etFPNetProfit?.setText(userResponseModel._result.last_fy_net_profit_loss)
                            mBinding?.etStaffPositionSanctioned?.setText(userResponseModel._result.staff_sanctioned)
                            mBinding?.etStaffPositionAvailable?.setText(userResponseModel._result.staff_available)
                            mBinding?.etFuturePlans?.setText(userResponseModel._result.future_plan_action)
                            mBinding?.etRemarks?.setText(userResponseModel._result.remarks_by_nlm)


                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.milk_union_visit_report_document.forEach { document ->
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
            addDocumentDialog(this@AddMilkUnionVisit, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
        }
        fun save(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            milkLocation.clear()
            milkCapacity.clear()
            milkLocation.add(mBinding?.location1?.text?.toString() ?: "")
            milkLocation.add(mBinding?.location2?.text?.toString() ?: "")
            milkLocation.add(mBinding?.location3?.text?.toString() ?: "")


            milkCapacity.add(mBinding?.capacity1?.text.toString())
            milkCapacity.add(mBinding?.capacity2?.text.toString())
            milkCapacity.add(mBinding?.capacity3?.text.toString())
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
            milkLocation.clear()
            milkCapacity.clear()
            milkLocation.add(mBinding?.location1?.text?.toString() ?: "")
            milkLocation.add(mBinding?.location2?.text?.toString() ?: "")
            milkLocation.add(mBinding?.location3?.text?.toString() ?: "")


            milkCapacity.add(mBinding?.capacity1?.text.toString())
            milkCapacity.add(mBinding?.capacity2?.text.toString())
            milkCapacity.add(mBinding?.capacity3?.text.toString())
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
            viewModel.getMilkUnionAdd(
                this, true,
                MilkUnionAddRequest(
                    id = itemId,
//                    districts = districtId,
                    role_id = getPreferenceOfScheme(
                        this@AddMilkUnionVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
//                            state_code = getPreferenceOfScheme(
//                                this@AddDairyPlantVisit,
//                                AppConstants.SCHEME,
//                                Result::class.java
//                            )?.state_code,
                    state_code = getPreferenceOfScheme(
                        this@AddMilkUnionVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddMilkUnionVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    status = 1,
                    is_draft = draft,
                    name_of_milk_union = mBinding?.etNameOfMilkUnion?.text.toString(),
                    no_of_villages = mBinding?.etTotalNoOfVillages?.text.toString().toIntOrNull(),
                    no_of_villages_covered = mBinding?.etNoOfVillagesCovered?.text.toString().toIntOrNull(),
                    no_of_milk_producers_enr = mBinding?.etNoOfMilkProducers?.text.toString().toIntOrNull(),
                    no_of_dairy_organised = mBinding?.etOrganised?.text.toString().toIntOrNull(),
                    no_of_dairy_functional = mBinding?.etFunctional?.text.toString().toIntOrNull(),
                    milk_processing_plant_location = "",//milkLocation.toString(),
                    milk_processing_plant_capacity = "",//milkCapacity.toString(),
                    milk_processing_plant_total = mBinding?.tvTotalLocationCapacity?.text.toString().toIntOrNull(),
                    no_of_milk_chillers = mBinding?.etBulkMilkChillersNumber?.text.toString().toIntOrNull(),
                    capacity_of_milk_chillers = mBinding?.etBulkMilkChillersTotalCapacity?.text.toString().toIntOrNull(),
                    last_year_avg_milk = mBinding?.etMilkProcurementAverageLastYear?.text.toString().toIntOrNull(),
                    current_year_avg_milk = mBinding?.etMilkProcurementAverageCurrentYear?.text.toString().toIntOrNull(),
                    current_month_avg_milk = mBinding?.etMilkProcurementAverageCurrentMonth?.text.toString().toIntOrNull(),
                    daily_liquid_milk_sale = mBinding?.etMilkProductSaleLiquidMilk?.text.toString(),
                    daily_bulk_sale_to_other = mBinding?.etMilkProductSaleBulkSale?.text.toString(),
                    daily_value_added_product = mBinding?.etMilkProductSaleValueAdded?.text.toString(),
                    last_year_cow_milk_price = mBinding?.etAPPCowLastYear?.text.toString().toIntOrNull(),
                    current_year_cow_milk_price = mBinding?.etAPPCowCurrentYear?.text.toString().toIntOrNull(),
                    last_year_buffalo_milk_price = mBinding?.etAPPBuffaloLastYear?.text.toString().toIntOrNull(),
                    current_year_buffalo_milk_price = mBinding?.etAPPBuffaloCurrentYear?.text.toString().toIntOrNull(),
                    cow_milk_last_year_incentives = mBinding?.etIPCowLastYear?.text.toString().toIntOrNull(),
                    cow_milk_current_year_incentives = mBinding?.etIPCowCurrentYear?.text.toString().toIntOrNull(),
                    buffalo_milk_last_year_incentives = mBinding?.etIPBuffaloLastYear?.text.toString().toIntOrNull(),
                    buffalo_milk_current_year_incentives = mBinding?.etIPBuffaloCurrentYear?.text.toString().toIntOrNull(),
                    last_fy_turnover = mBinding?.etFPTurnOver?.text.toString(),
                    last_fy_cumulative_profit_loss = mBinding?.etFPCumulativeProfit?.text.toString(),
                    last_fy_net_profit_loss = mBinding?.etFPNetProfit?.text.toString(),
                    staff_sanctioned = mBinding?.etStaffPositionSanctioned?.text.toString(),
                    staff_available = mBinding?.etStaffPositionAvailable?.text.toString(),
                    future_plan_action = mBinding?.etFuturePlans?.text.toString(),
                    remarks_by_nlm = mBinding?.etRemarks?.text.toString(),
                    photographs_of_site = photographSite,
                    milk_union_visit_report_document = totalListDocument
                )
            )
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddMilkUnionVisit, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getMilkUnionAdd(
                        this@AddMilkUnionVisit, true,
                        MilkUnionAddRequest(
                            id = itemId,
//                    districts = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddMilkUnionVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
//                            state_code = getPreferenceOfScheme(
//                                this@AddDairyPlantVisit,
//                                AppConstants.SCHEME,
//                                Result::class.java
//                            )?.state_code,
                            state_code = getPreferenceOfScheme(
                                this@AddMilkUnionVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddMilkUnionVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            name_of_milk_union = mBinding?.etNameOfMilkUnion?.text.toString(),
                            no_of_villages = mBinding?.etTotalNoOfVillages?.text.toString().toIntOrNull(),
                            no_of_villages_covered = mBinding?.etNoOfVillagesCovered?.text.toString().toIntOrNull(),
                            no_of_milk_producers_enr = mBinding?.etNoOfMilkProducers?.text.toString().toIntOrNull(),
                            no_of_dairy_organised = mBinding?.etOrganised?.text.toString().toIntOrNull(),
                            no_of_dairy_functional = mBinding?.etFunctional?.text.toString().toIntOrNull(),
                            milk_processing_plant_location = "",
                            milk_processing_plant_capacity = "",//Array me change krvana hai,
                            milk_processing_plant_total = mBinding?.tvTotalLocationCapacity?.text.toString().toIntOrNull(),
                            no_of_milk_chillers = mBinding?.etBulkMilkChillersNumber?.text.toString().toIntOrNull(),
                            capacity_of_milk_chillers = mBinding?.etBulkMilkChillersTotalCapacity?.text.toString().toIntOrNull(),
                            last_year_avg_milk = mBinding?.etMilkProcurementAverageLastYear?.text.toString().toIntOrNull(),
                            current_year_avg_milk = mBinding?.etMilkProcurementAverageCurrentYear?.text.toString().toIntOrNull(),
                            current_month_avg_milk = mBinding?.etMilkProcurementAverageCurrentMonth?.text.toString().toIntOrNull(),
                            daily_liquid_milk_sale = mBinding?.etMilkProductSaleLiquidMilk?.text.toString(),
                            daily_bulk_sale_to_other = mBinding?.etMilkProductSaleBulkSale?.text.toString(),
                            daily_value_added_product = mBinding?.etMilkProductSaleValueAdded?.text.toString(),
                            last_year_cow_milk_price = mBinding?.etAPPCowLastYear?.text.toString().toIntOrNull(),
                            current_year_cow_milk_price = mBinding?.etAPPCowCurrentYear?.text.toString().toIntOrNull(),
                            last_year_buffalo_milk_price = mBinding?.etAPPBuffaloLastYear?.text.toString().toIntOrNull(),
                            current_year_buffalo_milk_price = mBinding?.etAPPBuffaloCurrentYear?.text.toString().toIntOrNull(),
                            cow_milk_last_year_incentives = mBinding?.etIPCowLastYear?.text.toString().toIntOrNull(),
                            cow_milk_current_year_incentives = mBinding?.etIPCowCurrentYear?.text.toString().toIntOrNull(),
                            buffalo_milk_last_year_incentives = mBinding?.etIPBuffaloLastYear?.text.toString().toIntOrNull(),
                            buffalo_milk_current_year_incentives = mBinding?.etIPBuffaloCurrentYear?.text.toString().toIntOrNull(),
                            last_fy_turnover = mBinding?.etFPTurnOver?.text.toString(),
                            last_fy_cumulative_profit_loss = mBinding?.etFPCumulativeProfit?.text.toString(),
                            last_fy_net_profit_loss = mBinding?.etFPNetProfit?.text.toString(),
                            staff_sanctioned = mBinding?.etStaffPositionSanctioned?.text.toString(),
                            staff_available = mBinding?.etStaffPositionAvailable?.text.toString(),
                            future_plan_action = mBinding?.etFuturePlans?.text.toString(),
                            remarks_by_nlm = mBinding?.etRemarks?.text.toString(),
                            photographs_of_site = photographSite,
                            milk_union_visit_report_document = totalListDocument,
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
            if (selectedItem.is_edit==false)
            {
                bindingDialog.tvSubmit.hideView()
                bindingDialog.tvChooseFile.isEnabled=false
                bindingDialog.etDescription.isEnabled=false
            }
            UploadedDocumentName = selectedItem.nlm_document
            bindingDialog.etDoc.text = selectedItem.nlm_document
            bindingDialog.etDescription.setText(selectedItem.description)

            val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
            if (isSupported) {
                val url=getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                when (fileExtension) {
                    "pdf" -> {
                        val downloader = AndroidDownloader(context)
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(R.drawable.ic_pdf).placeholder(R.drawable.ic_pdf).into(
                                it
                            )
                        }
                        bindingDialog.etDoc.setOnClickListener {
                            if (!UploadedDocumentName.isNullOrEmpty()) {
                                downloader.downloadFile(url, UploadedDocumentName!!)
                                mBinding?.let { it1 -> showSnackbar(it1.clParent,"Download started") }
                                dialog.dismiss()
                            }
                            else{
                                mBinding?.let { it1 -> showSnackbar(it1.clParent,"No document found") }
                                dialog.dismiss()
                            }
                        }

                    }

                    "png" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url).placeholder(R.drawable.ic_image_placeholder).into(
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
                            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
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
                                table_name = getString(R.string.milk_union_visit_report_document).toRequestBody(
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
                context = this@AddMilkUnionVisit,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddMilkUnionVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.milk_union_visit_report_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
}