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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.CallBackNddScheme
import com.nlm.databinding.ActivityAddDairyPlantVisitBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemNddSchemeBinding
import com.nlm.databinding.ItemRspBucksBinding
import com.nlm.model.AddDairyPlantRequest
import com.nlm.model.DairyPlantVisitReportNpddScheme
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspAddBucksList
import com.nlm.model.SubTableDeleteRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.ui.adapter.ndd.NddSchemeAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.URIPathHelper
import com.nlm.utilities.Utility
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

class AddDairyPlantVisit : BaseActivity<ActivityAddDairyPlantVisitBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackNddScheme, CallBackDeleteFSPAtId {
    private var mBinding: ActivityAddDairyPlantVisitBinding? = null
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
    private var photographSite: String? = null
    private var haccpRadio: Int? = null
    private var qualityRadio: String? = null
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var uploadData: ImageView? = null
    private lateinit var addBucksList: MutableList<DairyPlantVisitReportNpddScheme>
    private var addBuckAdapter: NddSchemeAdapter? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var longitude: Double? = null
    private var latitude: Double? = null

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
        get() = R.layout.activity_add_dairy_plant_visit

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

        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))

            mBinding?.etLocationDairyPlant?.isEnabled = false
            mBinding?.etFSSAILicenseNo?.isEnabled = false
            mBinding?.etCapacityDairyPlant?.isEnabled = false
            mBinding?.etYearEstablishment?.isEnabled = false
            mBinding?.etTotalDcsCovered?.isEnabled = false
            mBinding?.etTotalBMCNoCapacity?.isEnabled = false
            mBinding?.etBMCNoOfMilkRoutes?.isEnabled = false
            mBinding?.etNonBMCNoOfMilkRoutes?.isEnabled = false
            mBinding?.etTotalRoutesNoOfMilkRoutes?.isEnabled = false
            mBinding?.etLiquidMilkDailyAverage?.isEnabled = false
            mBinding?.etGheeDailyAverage?.isEnabled = false
            mBinding?.etCurdDailyAverage?.isEnabled = false
            mBinding?.etLassiDailyAverage?.isEnabled = false
            mBinding?.etTestsPerformedChemicalMicrobiological?.isEnabled = false
            mBinding?.etFrequencyChemicalMicrobiological?.isEnabled = false
            mBinding?.etTestsPerformed2ChemicalMicrobiological?.isEnabled = false
            mBinding?.etFrequency2ChemicalMicrobiological?.isEnabled = false
            mBinding?.etETP?.isEnabled = false
            mBinding?.etCapacity?.isEnabled = false
            mBinding?.etRemarksNlM?.isEnabled = false
            mBinding?.rbIsTheDairyYes?.isEnabled = false
            mBinding?.rbIsTheDairyNo?.isEnabled = false
            mBinding?.rbQualityYes?.isEnabled = false
            mBinding?.rbQualityNo?.isEnabled = false
            mBinding?.tvChooseFile?.isEnabled = false
            mBinding?.tvNLMDoc?.hideView()
            mBinding?.tvScheme?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()


            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }

        mBinding?.rgIsTheDairy?.setOnCheckedChangeListener { group, checkedId ->
            haccpRadio = when (checkedId) {
                R.id.rbIsTheDairyYes -> 1
                R.id.rbIsTheDairyNo -> 0
                else -> null
            }
        }

        mBinding?.rgQuality?.setOnCheckedChangeListener { group, checkedId ->
            qualityRadio = when (checkedId) {
                R.id.rbQualityYes -> "Yes"
                R.id.rbQualityNo -> "No"
                else -> null
            }
        }

        nlmAdapter()
        rspBuckAdapter()
    }

    private fun rspBuckAdapter() {
        addBucksList = mutableListOf()
        addBuckAdapter =
            NddSchemeAdapter(this, addBucksList, viewEdit, this, this)
        mBinding?.rvScheme?.adapter = addBuckAdapter
        mBinding?.rvScheme?.layoutManager =
            LinearLayoutManager(this)
    }

    private fun addBucks(
        context: Context,
        isFrom: Int,
        selectedItem: DairyPlantVisitReportNpddScheme?,
        position: Int?
    ) {
        val bindingDialog: ItemNddSchemeBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_ndd_scheme,
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
        bindingDialog.btnDelete.hideView()
        bindingDialog.btnEdit.hideView()
        bindingDialog.tvSubmit.showView()
        var goOne: Int? = null
        var goTwo: Int? = null
        if (selectedItem != null) {
            bindingDialog.etNddScheme.setText(selectedItem.npdd_scheme_proj_no.toString())
            bindingDialog.etMajor.setText(selectedItem.major_equipments_milk_products)
            bindingDialog.etRemark.setText(selectedItem.npdd_scheme_remarks)

            if (selectedItem.npdd_scheme_available.toString() == "null") {
                bindingDialog.rbAvailable.isChecked = false
                bindingDialog.rbNotAvailable.isChecked = false

            } else if (selectedItem.npdd_scheme_available == 1) {
                bindingDialog.rbAvailable.isChecked = true
                goOne=1
            } else {
                bindingDialog.rbNotAvailable.isChecked = true
                goOne=0
            }

            if (selectedItem.npdd_scheme_present_status.toString() == "null") {
                bindingDialog.rbPresentOperational.isChecked = false
                bindingDialog.rbPresentNonOperational.isChecked = false

            } else if (selectedItem.npdd_scheme_present_status == 1) {
                bindingDialog.rbPresentOperational.isChecked = true
                goTwo=1
            } else {
                bindingDialog.rbPresentNonOperational.isChecked = true
                goTwo=0
            }
        }


        bindingDialog.rgAvailable.setOnCheckedChangeListener { group, checkedId ->
            goOne = when (checkedId) {
                R.id.rbAvailable -> 1
                R.id.rbNotAvailable -> 0
                else -> null
            }
        }

        bindingDialog.rgPresent.setOnCheckedChangeListener { group, checkedId ->
            goTwo = when (checkedId) {
                R.id.rbPresentOperational -> 1
                R.id.rbPresentNonOperational -> 0
                else -> null
            }
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNddScheme.text.toString()
                    .isNotEmpty() || bindingDialog.etMajor.text.toString().isNotEmpty()
                || bindingDialog.etRemark.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        addBucksList[position] =
                            DairyPlantVisitReportNpddScheme(
                                npdd_scheme_proj_no = bindingDialog.etNddScheme.text.toString()
                                    .toIntOrNull(),
                                major_equipments_milk_products = bindingDialog.etMajor.text.toString(),
                                npdd_scheme_remarks = bindingDialog.etRemark.text.toString(),
                                id = selectedItem.id,
                                dairy_plant_visit_report_id = selectedItem.dairy_plant_visit_report_id,
                                npdd_scheme_available = goOne,
                                npdd_scheme_present_status = goTwo
                            )
                        addBuckAdapter?.notifyItemChanged(position)
                    }

                } else {
                    addBucksList.add(
                        DairyPlantVisitReportNpddScheme(
                            npdd_scheme_proj_no = bindingDialog.etNddScheme.text.toString()
                                .toIntOrNull(),
                            major_equipments_milk_products = bindingDialog.etMajor.text.toString(),
                            npdd_scheme_remarks = bindingDialog.etRemark.text.toString(),
                            id = null,
                            dairy_plant_visit_report_id = null,
                            npdd_scheme_available = goOne,
                            npdd_scheme_present_status = goTwo
                        )
                    )
                    addBucksList.size.minus(1).let {
                        addBuckAdapter?.notifyItemInserted(it)
                    }
                }
                Log.d("Listing", addBucksList.toString())
                dialog.dismiss()
            } else {
                showSnackbar(
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }
        dialog.show()
    }

    private fun viewEditApi() {

        viewModel.getDairyPlantAdd(
            this, true,
            AddDairyPlantRequest(
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


    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddDairyPlantVisit)
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



        viewModel.dairyPlantAddResult.observe(this) {
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
                            mBinding?.etLocationDairyPlant?.setText(userResponseModel._result.location_of_dairy_plant)
                            mBinding?.etFSSAILicenseNo?.setText(userResponseModel._result.fssai_license_no)
                            mBinding?.etCapacityDairyPlant?.setText(userResponseModel._result.capacity_of_dairy_plant)
                            mBinding?.etYearEstablishment?.setText(userResponseModel._result.year_of_establishment)
                            mBinding?.etTotalDcsCovered?.setText(userResponseModel._result.total_dcs_covered)

                            mBinding?.etTotalBMCNoCapacity?.setText(userResponseModel._result.total_bmc_capacity)
                            mBinding?.etBMCNoOfMilkRoutes?.setText(userResponseModel._result.number_of_bmc_route_milk)
                            mBinding?.etNonBMCNoOfMilkRoutes?.setText(userResponseModel._result.number_of_non_bmc_route_milk)
                            mBinding?.etTotalRoutesNoOfMilkRoutes?.setText(userResponseModel._result.number_of_total_route_milk)
                            mBinding?.etLiquidMilkDailyAverage?.setText(userResponseModel._result.daily_avg_last_year_liquid_milk)
                            mBinding?.etGheeDailyAverage?.setText(userResponseModel._result.daily_avg_last_year_value_added_ghee)
                            mBinding?.etCurdDailyAverage?.setText(userResponseModel._result.daily_avg_last_year_value_added_curd)
                            mBinding?.etLassiDailyAverage?.setText(userResponseModel._result.daily_avg_last_year_value_added_lassi)
                            mBinding?.etTestsPerformedChemicalMicrobiological?.setText(
                                userResponseModel._result.chemical_tests_performed
                            )
                            mBinding?.etFrequencyChemicalMicrobiological?.setText(userResponseModel._result.chemical_frequency_tests_performed)
                            mBinding?.etTestsPerformed2ChemicalMicrobiological?.setText(
                                userResponseModel._result.chemical_snf_frequency
                            )
                            mBinding?.etFrequency2ChemicalMicrobiological?.setText(userResponseModel._result.chemical_snf_result)
                            mBinding?.etETP?.setText(userResponseModel._result.other_details_etp_functioning)
                            mBinding?.etCapacity?.setText(userResponseModel._result.other_details_capacity)
                            mBinding?.etRemarksNlM?.setText(userResponseModel._result.remark_by_nlm)

                            if (userResponseModel._result.plant_haccp_complied.isNullOrEmpty()) {
                                mBinding?.rbIsTheDairyYes?.isChecked = false
                                mBinding?.rbIsTheDairyNo?.isChecked = false

                            } else if (userResponseModel._result.plant_haccp_complied == "1") {
                                mBinding?.rbIsTheDairyYes?.isChecked = true
                            } else {
                                mBinding?.rbIsTheDairyNo?.isChecked = true
                            }

                            if (userResponseModel._result.haccp_quality_management_system_complied.isNullOrEmpty()) {
                                mBinding?.rbQualityYes?.isChecked = false
                                mBinding?.rbQualityNo?.isChecked = false

                            } else if (userResponseModel._result.haccp_quality_management_system_complied == "Yes") {
                                mBinding?.rbQualityYes?.isChecked = true
                            } else {
                                mBinding?.rbQualityNo?.isChecked = true
                            }
                            addBucksList.clear()
                            if (userResponseModel._result.dairy_plant_visit_report_npdd_scheme?.isEmpty() == true && viewEdit == "view") {
                                // Add dummy data with default values
                                val dummyData = DairyPlantVisitReportNpddScheme(
                                    dairy_plant_visit_report_id = null,
                                    id = null,
                                    major_equipments_milk_products = null,
                                    npdd_scheme_available = null,
                                    npdd_scheme_present_status = null,
                                    npdd_scheme_proj_no = null,
                                    npdd_scheme_remarks = null,
                                    npdd_scheme_major_lab_available = null,
                                    npdd_scheme_major_lab_no = null,
                                    npdd_scheme_major_lab_present_status = null,
                                    npdd_scheme_major_lab_remarks = null,
                                    npdd_scheme_major_lab_strengthening = null,
                                )

                                addBucksList.add(dummyData)
                            } else {
                                userResponseModel._result.dairy_plant_visit_report_npdd_scheme?.let { it1 ->
                                    addBucksList.addAll(
                                        it1
                                    )
                                }
                            }

                            addBuckAdapter?.notifyDataSetChanged()



                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.dairy_plant_visit_report_document.forEach { document ->
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
            addDocumentDialog(this@AddDairyPlantVisit, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
        }

        fun addBuck(view: View) {
            addBucks(this@AddDairyPlantVisit, 1, null, null)
        }

        fun photoSite(view: View) {
//            checkStoragePermission(this@AddDairyPlantVisit)
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
            viewModel.getDairyPlantAdd(
                this@AddDairyPlantVisit, true,
                AddDairyPlantRequest(
                    id = itemId,
                    districts = districtId,
                    role_id = getPreferenceOfScheme(
                        this@AddDairyPlantVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
//                            state_code = getPreferenceOfScheme(
//                                this@AddDairyPlantVisit,
//                                AppConstants.SCHEME,
//                                Result::class.java
//                            )?.state_code,
                    state_code = getPreferenceOfScheme(
                        this@AddDairyPlantVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddDairyPlantVisit,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    status = 1,
                    is_draft = draft,
                    location_of_dairy_plant = mBinding?.etLocationDairyPlant?.text.toString(),
                    fssai_license_no = mBinding?.etFSSAILicenseNo?.text.toString(),
                    capacity_of_dairy_plant = mBinding?.etCapacityDairyPlant?.text.toString(),
                    year_of_establishment = mBinding?.etYearEstablishment?.text.toString(),
                    total_dcs_covered = mBinding?.etTotalDcsCovered?.text.toString(),
                    total_bmc_capacity = mBinding?.etTotalBMCNoCapacity?.text.toString(),
                    number_of_bmc_route_milk = mBinding?.etBMCNoOfMilkRoutes?.text.toString(),
                    number_of_non_bmc_route_milk = mBinding?.etNonBMCNoOfMilkRoutes?.text.toString(),
                    number_of_total_route_milk = mBinding?.etTotalRoutesNoOfMilkRoutes?.text.toString(),
                    daily_avg_last_year_liquid_milk = mBinding?.etLiquidMilkDailyAverage?.text.toString(),
                    daily_avg_last_year_value_added_ghee = mBinding?.etGheeDailyAverage?.text.toString(),
                    daily_avg_last_year_value_added_curd = mBinding?.etCurdDailyAverage?.text.toString(),
                    daily_avg_last_year_value_added_lassi = mBinding?.etLassiDailyAverage?.text.toString(),
                    chemical_tests_performed = mBinding?.etTestsPerformedChemicalMicrobiological?.text.toString(),
                    chemical_frequency_tests_performed = mBinding?.etFrequencyChemicalMicrobiological?.text.toString(),
                    chemical_snf_frequency = mBinding?.etTestsPerformed2ChemicalMicrobiological?.text.toString(),
                    chemical_snf_result = mBinding?.etFrequency2ChemicalMicrobiological?.text.toString(),
                    other_details_etp_functioning = mBinding?.etETP?.text.toString(),
                    other_details_capacity = mBinding?.etCapacity?.text.toString(),
                    plant_haccp_complied = haccpRadio,
                    haccp_quality_management_system_complied = qualityRadio,
                    remark_by_nlm = mBinding?.etRemarksNlM?.text.toString(),
                    photographs_of_site = photographSite,
                    dairy_plant_visit_report_document = totalListDocument,
                    dairy_plant_visit_report_npdd_scheme = addBucksList,
                )
            )
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddDairyPlantVisit, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getDairyPlantAdd(
                        this@AddDairyPlantVisit, true,
                        AddDairyPlantRequest(
                            id = itemId,
                            districts = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddDairyPlantVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
//                            state_code = getPreferenceOfScheme(
//                                this@AddDairyPlantVisit,
//                                AppConstants.SCHEME,
//                                Result::class.java
//                            )?.state_code,
                            state_code = getPreferenceOfScheme(
                                this@AddDairyPlantVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddDairyPlantVisit,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            location_of_dairy_plant = mBinding?.etLocationDairyPlant?.text.toString(),
                            fssai_license_no = mBinding?.etFSSAILicenseNo?.text.toString(),
                            capacity_of_dairy_plant = mBinding?.etCapacityDairyPlant?.text.toString(),
                            year_of_establishment = mBinding?.etYearEstablishment?.text.toString(),
                            total_dcs_covered = mBinding?.etTotalDcsCovered?.text.toString(),
                            total_bmc_capacity = mBinding?.etTotalBMCNoCapacity?.text.toString(),
                            number_of_bmc_route_milk = mBinding?.etBMCNoOfMilkRoutes?.text.toString(),
                            number_of_non_bmc_route_milk = mBinding?.etNonBMCNoOfMilkRoutes?.text.toString(),
                            number_of_total_route_milk = mBinding?.etTotalRoutesNoOfMilkRoutes?.text.toString(),
                            daily_avg_last_year_liquid_milk = mBinding?.etLiquidMilkDailyAverage?.text.toString(),
                            daily_avg_last_year_value_added_ghee = mBinding?.etGheeDailyAverage?.text.toString(),
                            daily_avg_last_year_value_added_curd = mBinding?.etCurdDailyAverage?.text.toString(),
                            daily_avg_last_year_value_added_lassi = mBinding?.etLassiDailyAverage?.text.toString(),
                            chemical_tests_performed = mBinding?.etTestsPerformedChemicalMicrobiological?.text.toString(),
                            chemical_frequency_tests_performed = mBinding?.etFrequencyChemicalMicrobiological?.text.toString(),
                            chemical_snf_frequency = mBinding?.etTestsPerformed2ChemicalMicrobiological?.text.toString(),
                            chemical_snf_result = mBinding?.etFrequency2ChemicalMicrobiological?.text.toString(),
                            other_details_etp_functioning = mBinding?.etETP?.text.toString(),
                            other_details_capacity = mBinding?.etCapacity?.text.toString(),
                            plant_haccp_complied = haccpRadio,
                            haccp_quality_management_system_complied = qualityRadio,
                            remark_by_nlm = mBinding?.etRemarksNlM?.text.toString(),
                            photographs_of_site = photographSite,
                            dairy_plant_visit_report_document = totalListDocument,
                            dairy_plant_visit_report_npdd_scheme = addBucksList,
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
                                table_name = getString(R.string.dairy_plant_visit_report_document).toRequestBody(
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
                context = this@AddDairyPlantVisit,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddDairyPlantVisit,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.dairy_plant_visit_report_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }

    override fun onClickItem(
        selectedItem: DairyPlantVisitReportNpddScheme,
        position: Int,
        isFrom: Int
    ) {
        addBucks(this, isFrom, selectedItem, position)
    }

    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> addBuckAdapter?.onDeleteButtonClick(it1) }
//        Log.d("ID",ID.toString())
        if (ID.toString().isNotEmpty() && viewEdit == "edit") {
            viewModel.getDeleteSubTable(
                this@AddDairyPlantVisit, true,
                SubTableDeleteRequest(
                    ID,
                    "DairyPlantVisitReportNpddScheme"
                )
            )
        }


    }
}