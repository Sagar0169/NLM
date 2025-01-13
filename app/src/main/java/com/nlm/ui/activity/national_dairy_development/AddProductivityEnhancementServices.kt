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
import com.nlm.databinding.ActivityAddProductivityEnhancemmentServicesBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AddProductivityEnchancementRequest
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

class AddProductivityEnhancementServices : BaseActivity<ActivityAddProductivityEnhancemmentServicesBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddProductivityEnhancemmentServicesBinding? = null
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
    private var rgPregnancy: Int? = null
    private var rgDeworming: Int? = null
    private var rgVaccination: Int? = null
    private var rgCalves: Int? = null
    private var rgRecord: Int? = null
    private var rgTransition: Int? = null
    private var rgMineral: Int? = null
    private var rgCattles: Int? = null
    private var rgKeeping: Int? = null
    private var rgDistribution: Int? = null
    private var rgPlantation: Int? = null
    private var rgMineralMix: Int? = null
    private var rgLow: Int? = null
    private var rgAssets: Int? = null
    private var rgSelection: Int? = null


    private var tvDateOfInspection: String? = null
    override val layoutId: Int
        get() = R.layout.activity_add_productivity_enhancemment_services

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

        mBinding?.rgPregnancy?.setOnCheckedChangeListener { group, checkedId ->
            rgPregnancy = when (checkedId) {
                R.id.rbPregnancyYes -> 1
                R.id.rbPregnancyNo -> 0
                else -> null
            }
        }

        mBinding?.rgDeworming?.setOnCheckedChangeListener { group, checkedId ->
            rgDeworming = when (checkedId) {
                R.id.rbDewormingYes -> 1
                R.id.rbDewormingNo -> 0
                else -> null
            }
        }

        mBinding?.rgVaccination?.setOnCheckedChangeListener { group, checkedId ->
            rgVaccination = when (checkedId) {
                R.id.rbVaccinationYes -> 1
                R.id.rbVaccinationNo -> 0
                else -> null
            }
        }

        mBinding?.rgCalves?.setOnCheckedChangeListener { group, checkedId ->
            rgCalves = when (checkedId) {
                R.id.rbCalvesYes -> 1
                R.id.rbCalvesNo -> 0
                else -> null
            }
        }

        mBinding?.rgRecord?.setOnCheckedChangeListener { group, checkedId ->
            rgRecord = when (checkedId) {
                R.id.rbRecordYes -> 1
                R.id.rbRecordNo -> 0
                else -> null
            }
        }

        mBinding?.rgTransition?.setOnCheckedChangeListener { group, checkedId ->
            rgTransition = when (checkedId) {
                R.id.rgTransitionYes -> 1
                R.id.rgTransitionNo -> 0
                else -> null
            }
        }

        mBinding?.rgMineral?.setOnCheckedChangeListener { group, checkedId ->
            rgMineral = when (checkedId) {
                R.id.rgMineralYes -> 1
                R.id.rgMineralNo -> 0
                else -> null
            }
        }

        mBinding?.rgCattles?.setOnCheckedChangeListener { group, checkedId ->
            rgCattles = when (checkedId) {
                R.id.rgCattlesYes -> 1
                R.id.rgCattlesNo -> 0
                else -> null
            }
        }

        mBinding?.rgKeeping?.setOnCheckedChangeListener { group, checkedId ->
            rgKeeping = when (checkedId) {
                R.id.rgKeepingYes -> 1
                R.id.rgKeepingNo -> 0
                else -> null
            }
        }

        mBinding?.rgDistribution?.setOnCheckedChangeListener { group, checkedId ->
            rgDistribution = when (checkedId) {
                R.id.rgDistributionYes -> 1
                R.id.rgDistributionNo -> 0
                else -> null
            }
        }


        mBinding?.rgPlantation?.setOnCheckedChangeListener { group, checkedId ->
            rgPlantation = when (checkedId) {
                R.id.rgPlantationYes -> 1
                R.id.rgPlantationNo -> 0
                else -> null
            }
        }

        mBinding?.rgMineralMix?.setOnCheckedChangeListener { group, checkedId ->
            rgMineralMix = when (checkedId) {
                R.id.rgMineralMixYes -> 1
                R.id.rgMineralMixNo -> 0
                else -> null
            }
        }


        mBinding?.rgLow?.setOnCheckedChangeListener { group, checkedId ->
            rgLow = when (checkedId) {
                R.id.rgLowYes -> 1
                R.id.rgLowNo -> 0
                else -> null
            }
        }

        mBinding?.rgAssets?.setOnCheckedChangeListener { group, checkedId ->
            rgAssets = when (checkedId) {
                R.id.rgAssetsYes -> 1
                R.id.rgAssetsNo -> 0
                else -> null
            }
        }
        mBinding?.rgSelection?.setOnCheckedChangeListener { group, checkedId ->
            rgSelection = when (checkedId) {
                R.id.rgSelectionYes -> 1
                R.id.rgSelectionNo -> 0
                else -> null
            }
        }


        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))

//            mBinding?.etNameOfRetail?.isEnabled = false
//            mBinding?.etNameOfMilkUnion?.isEnabled = false
//            mBinding?.etNameOfTehsil?.isEnabled = false
//            mBinding?.tvDateOfInspection?.isEnabled = false
//            mBinding?.etLat?.isEnabled = false
//            mBinding?.etLong?.isEnabled = false
//            mBinding?.etRemarkAsset?.isEnabled = false
//            mBinding?.etOverallRemark?.isEnabled = false
//            mBinding?.etHygieneRemark?.isEnabled = false
//            mBinding?.etNewRemark?.isEnabled = false
//            mBinding?.etWhetherRemark?.isEnabled = false
//            mBinding?.etAnyOther?.isEnabled = false
//            mBinding?.rbAssetYes?.isEnabled = false
//            mBinding?.rbAssetNo?.isEnabled = false
//            mBinding?.rbOverallYes?.isEnabled = false
//            mBinding?.rbOverallNo?.isEnabled = false
//            mBinding?.rbVaccinationYes?.isEnabled = false
//            mBinding?.rbVaccinationNo?.isEnabled = false
//            mBinding?.rbCalvesYes?.isEnabled = false
//            mBinding?.rbCalvesNo?.isEnabled = false
//            mBinding?.rbWhetherYes?.isEnabled = false
//            mBinding?.rbWhetherNo?.isEnabled = false
//            mBinding?.rbIncreasedYes?.isEnabled = false
//            mBinding?.rbIncreasedNo?.isEnabled = false
//            mBinding?.rbProductsYes?.isEnabled = false
//            mBinding?.rbProductsNo?.isEnabled = false
//            mBinding?.rbAdditionYes?.isEnabled = false
//            mBinding?.rbAdditionNo?.isEnabled = false
//            mBinding?.rbIncreaseYes?.isEnabled = false
//            mBinding?.rbIncreaseNo?.isEnabled = false
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

        viewModel.getProductivityEnhancementServicesAdd(
            this@AddProductivityEnhancementServices, true,
            AddProductivityEnchancementRequest(
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
                district_id = dId,
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
            this@AddProductivityEnhancementServices,
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
                Utility.logout(this@AddProductivityEnhancementServices)
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
        viewModel.productivityEnchancementServicesAddResult.observe(this) {
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
                            districtId = userResponseModel._result.district_id
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.district_name
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etNameDCS?.setText(userResponseModel._result.name_dcs_mpp)
                            mBinding?.etNameOfRevenueVillage?.setText(userResponseModel._result.name_revenue_village)
                            mBinding?.etNameOfTehsil?.setText(userResponseModel._result.name_tehsil)
                            mBinding?.etLat?.setText(userResponseModel._result.lat_dcs)
                            mBinding?.etLong?.setText(userResponseModel._result.long_dcs)
                            mBinding?.etHasAssitanceRemark?.setText(userResponseModel._result.farmer_under_calf_rearing_program_remarks)
                            mBinding?.etCalvesRemark?.setText(userResponseModel._result.are_calves_born_remarks)
                            mBinding?.etRecordRemark?.setText(userResponseModel._result.record_keeping_remarks)
                            mBinding?.etMineralRemark?.setText(userResponseModel._result.under_anas_program_remarks)
                            mBinding?.etCattlesRemark?.setText(userResponseModel._result.under_anas_tagged_remarks)
                            mBinding?.etKeepingRemark?.setText(userResponseModel._result.is_record_keeping_remarks)
                            mBinding?.etLowRemark?.setText(userResponseModel._result.assistance_been_provided_remarks)
                            mBinding?.etAssetsRemark?.setText(userResponseModel._result.assets_distributed_remarks)
                            mBinding?.etSelectionRemark?.setText(userResponseModel._result.selection_village_remarks)
                            mBinding?.etVisibleImpact?.setText(userResponseModel._result.visible_impacts)
                            mBinding?.tvDateOfInspection?.text =
                                convertDate(userResponseModel._result.date_of_inspection)

                            if (userResponseModel._result.pregnancy_feed==null ) {
                                mBinding?.rbPregnancyYes?.isChecked = false
                                mBinding?.rbPregnancyNo?.isChecked = false

                            } else if (userResponseModel._result.pregnancy_feed == 1) {
                                mBinding?.rbPregnancyYes?.isChecked = true
                            } else {
                                mBinding?.rbPregnancyNo?.isChecked = true
                            }



                            if (userResponseModel._result.deworming_of_female_calves==null) {
                                mBinding?.rbDewormingYes?.isChecked = false
                                mBinding?.rbDewormingNo?.isChecked = false

                            } else if (userResponseModel._result.deworming_of_female_calves == 1) {
                                mBinding?.rbDewormingYes?.isChecked = true
                            } else {
                                mBinding?.rbDewormingNo?.isChecked = true
                            }

                            if (userResponseModel._result.vaccination_female_calves==null ) {
                                mBinding?.rbVaccinationYes?.isChecked = false
                                mBinding?.rbVaccinationNo?.isChecked = false

                            } else if (userResponseModel._result.vaccination_female_calves == "1") {
                                mBinding?.rbVaccinationYes?.isChecked = true
                            } else {
                                mBinding?.rbVaccinationNo?.isChecked = true
                            }

                            if (userResponseModel._result.are_calves_born==null ) {
                                mBinding?.rbCalvesYes?.isChecked = false
                                mBinding?.rbCalvesNo?.isChecked = false

                            } else if (userResponseModel._result.are_calves_born == 1) {
                                mBinding?.rbCalvesYes?.isChecked = true
                            } else {
                                mBinding?.rbCalvesNo?.isChecked = true
                            }

                            if (userResponseModel._result.record_keeping==null ) {
                                mBinding?.rbRecordYes?.isChecked = false
                                mBinding?.rbRecordNo?.isChecked = false

                            } else if (userResponseModel._result.record_keeping == 1) {
                                mBinding?.rbRecordYes?.isChecked = true
                            } else {
                                mBinding?.rbRecordNo?.isChecked = true
                            }

                            if (userResponseModel._result.transition_feed_early==null ) {
                                mBinding?.rgTransitionYes?.isChecked = false
                                mBinding?.rgTransitionNo?.isChecked = false

                            } else if (userResponseModel._result.transition_feed_early == 1) {
                                mBinding?.rgTransitionYes?.isChecked = true
                            } else {
                                mBinding?.rgTransitionNo?.isChecked = true
                            }

                            if (userResponseModel._result.animal_mineral_mixture==null ) {
                                mBinding?.rgMineralYes?.isChecked = false
                                mBinding?.rgMineralNo?.isChecked = false

                            } else if (userResponseModel._result.animal_mineral_mixture == 1) {
                                mBinding?.rgMineralYes?.isChecked = true
                            } else {
                                mBinding?.rgMineralNo?.isChecked = true
                            }

                            if (userResponseModel._result.under_anas_tagged==null ) {
                                mBinding?.rgCattlesYes?.isChecked = false
                                mBinding?.rgCattlesNo?.isChecked = false

                            } else if (userResponseModel._result.under_anas_tagged == 1) {
                                mBinding?.rgCattlesYes?.isChecked = true
                            } else {
                                mBinding?.rgCattlesNo?.isChecked = true
                            }

                            if (userResponseModel._result.is_record_keeping==null ) {
                                mBinding?.rgKeepingYes?.isChecked = false
                                mBinding?.rgKeepingNo?.isChecked = false

                            } else if (userResponseModel._result.is_record_keeping == 1) {
                                mBinding?.rgKeepingYes?.isChecked = true
                            } else {
                                mBinding?.rgKeepingNo?.isChecked = true
                            }


                            if (userResponseModel._result.distribution_fodder_seeds==null ) {
                                mBinding?.rgDistributionYes?.isChecked = false
                                mBinding?.rgDistributionNo?.isChecked = false

                            } else if (userResponseModel._result.distribution_fodder_seeds == 1) {
                                mBinding?.rgDistributionYes?.isChecked = true
                            } else {
                                mBinding?.rgDistributionNo?.isChecked = true
                            }

                            if (userResponseModel._result.plantation_root_stems==null ) {
                                mBinding?.rgPlantationYes?.isChecked = false
                                mBinding?.rgPlantationNo?.isChecked = false

                            } else if (userResponseModel._result.plantation_root_stems == 1) {
                                mBinding?.rgPlantationYes?.isChecked = true
                            } else {
                                mBinding?.rgPlantationNo?.isChecked = true
                            }

                            if (userResponseModel._result.fodder_mineral_mixture==null ) {
                                mBinding?.rgMineralMixYes?.isChecked = false
                                mBinding?.rgMineralMixNo?.isChecked = false

                            } else if (userResponseModel._result.fodder_mineral_mixture == 1) {
                                mBinding?.rgMineralMixYes?.isChecked = true
                            } else {
                                mBinding?.rgMineralMixNo?.isChecked = true
                            }

                            if (userResponseModel._result.low_cost_silage_making==null ) {
                                mBinding?.rgLowYes?.isChecked = false
                                mBinding?.rgLowNo?.isChecked = false

                            } else if (userResponseModel._result.low_cost_silage_making == 1) {
                                mBinding?.rgLowYes?.isChecked = true
                            } else {
                                mBinding?.rgLowNo?.isChecked = true
                            }

                            if (userResponseModel._result.assets_distributed==null ) {
                                mBinding?.rgAssetsYes?.isChecked = false
                                mBinding?.rgAssetsNo?.isChecked = false

                            } else if (userResponseModel._result.assets_distributed == 1) {
                                mBinding?.rgAssetsYes?.isChecked = true
                            } else {
                                mBinding?.rgAssetsNo?.isChecked = true
                            }

                            if (userResponseModel._result.selection_village==null ) {
                                mBinding?.rgSelectionYes?.isChecked = false
                                mBinding?.rgSelectionNo?.isChecked = false

                            } else if (userResponseModel._result.selection_village == 1) {
                                mBinding?.rgSelectionYes?.isChecked = true
                            } else {
                                mBinding?.rgSelectionNo?.isChecked = true
                            }

                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.productivity_enhancement_services_document?.forEach { document ->
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
            addDocumentDialog(this@AddProductivityEnhancementServices, null, null)
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
            viewModel.getProductivityEnhancementServicesAdd(
                this@AddProductivityEnhancementServices, true,
                AddProductivityEnchancementRequest(
                    id = itemId,
                    district_id = districtId,
                    role_id = getPreferenceOfScheme(
                        this@AddProductivityEnhancementServices,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        this@AddProductivityEnhancementServices,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        this@AddProductivityEnhancementServices,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    status = 1,
                    is_draft = draft,
                    name_dcs_mpp = mBinding?.etNameDCS?.text.toString(),
                    name_revenue_village = mBinding?.etNameOfRevenueVillage?.text.toString(),
                    name_tehsil = mBinding?.etNameOfTehsil?.text.toString(),
                    lat_dcs = mBinding?.etLat?.text.toString(),
                    long_dcs = mBinding?.etLong?.text.toString(),
                    date_of_inspection = tvDateOfInspection,
                    farmer_under_calf_rearing_program_remarks = mBinding?.etHasAssitanceRemark?.text.toString(),
                    are_calves_born_remarks = mBinding?.etCalvesRemark?.text.toString(),
                    record_keeping_remarks = mBinding?.etRecordRemark?.text.toString(),
                    under_anas_program_remarks = mBinding?.etMineralRemark?.text.toString(),
                    under_anas_tagged_remarks = mBinding?.etCattlesRemark?.text.toString(),
                    is_record_keeping_remarks = mBinding?.etKeepingRemark?.text.toString(),
                    assistance_been_provided_remarks = mBinding?.etLowRemark?.text.toString(),
                    assets_distributed_remarks = mBinding?.etAssetsRemark?.text.toString(),
                    selection_village_remarks = mBinding?.etSelectionRemark?.text.toString(),
                    visible_impacts = mBinding?.etVisibleImpact?.text.toString(),
                    pregnancy_feed = rgPregnancy,
                    deworming_of_female_calves = rgDeworming,
                    vaccination_female_calves = rgVaccination,
                    are_calves_born = rgCalves,
                    record_keeping = rgRecord,
                    transition_feed_early = rgTransition,
                    animal_mineral_mixture = rgMineral,
                    under_anas_tagged = rgCattles,
                    is_record_keeping = rgKeeping,
                    distribution_fodder_seeds = rgDistribution,
                    plantation_root_stems = rgPlantation,
                    fodder_mineral_mixture = rgMineralMix,
                    low_cost_silage_making = rgLow,
                    assets_distributed = rgAssets,
                    selection_village = rgSelection,
                    productivity_enhancement_services_document = totalListDocument

                )
            )
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddProductivityEnhancementServices, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                Log.d("Scope", "out")
                delay(1000) // Delay for 2 seconds
                Log.d("Scope", "In")
                Log.d("Scope", latitude.toString())
                Log.d("Scope", longitude.toString())

                if (latitude != null && longitude != null) {
                    toast("hi")
                    viewModel.getProductivityEnhancementServicesAdd(
                        this@AddProductivityEnhancementServices, true,
                        AddProductivityEnchancementRequest(
                            id = itemId,
                            district_id = districtId,
                            role_id = getPreferenceOfScheme(
                                this@AddProductivityEnhancementServices,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
                            state_code = getPreferenceOfScheme(
                                this@AddProductivityEnhancementServices,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddProductivityEnhancementServices,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            name_dcs_mpp = mBinding?.etNameDCS?.text.toString(),
                            name_revenue_village = mBinding?.etNameOfRevenueVillage?.text.toString(),
                            name_tehsil = mBinding?.etNameOfTehsil?.text.toString(),
                            lat_dcs = mBinding?.etLat?.text.toString(),
                            long_dcs = mBinding?.etLong?.text.toString(),
                            date_of_inspection = tvDateOfInspection,
                            farmer_under_calf_rearing_program_remarks = mBinding?.etHasAssitanceRemark?.text.toString(),
                            are_calves_born_remarks = mBinding?.etCalvesRemark?.text.toString(),
                            record_keeping_remarks = mBinding?.etRecordRemark?.text.toString(),
                            under_anas_program_remarks = mBinding?.etMineralRemark?.text.toString(),
                            under_anas_tagged_remarks = mBinding?.etCattlesRemark?.text.toString(),
                            is_record_keeping_remarks = mBinding?.etKeepingRemark?.text.toString(),
                            assistance_been_provided_remarks = mBinding?.etLowRemark?.text.toString(),
                            assets_distributed_remarks = mBinding?.etAssetsRemark?.text.toString(),
                            selection_village_remarks = mBinding?.etSelectionRemark?.text.toString(),
                            visible_impacts = mBinding?.etVisibleImpact?.text.toString(),
                            pregnancy_feed = rgPregnancy,
                            deworming_of_female_calves = rgDeworming,
                            vaccination_female_calves = rgVaccination,
                            are_calves_born = rgCalves,
                            record_keeping = rgRecord,
                            transition_feed_early = rgTransition,
                            animal_mineral_mixture = rgMineral,
                            under_anas_tagged = rgCattles,
                            is_record_keeping = rgKeeping,
                            distribution_fodder_seeds = rgDistribution,
                            plantation_root_stems = rgPlantation,
                            fodder_mineral_mixture = rgMineralMix,
                            low_cost_silage_making = rgLow,
                            assets_distributed = rgAssets,
                            selection_village = rgSelection,
                            productivity_enhancement_services_document = totalListDocument,
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
                                milk_product_marketing_id = selectedItem.milk_product_marketing_id,
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
                            milk_product_marketing_id = null,
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
                                table_name = getString(R.string.productivity_enhancement_services_document).toRequestBody(
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
                this@AddProductivityEnhancementServices, true,
                SubTableDeleteRequest(
                    ID,
                    getString(R.string.productivity_enhancement_services_document)
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
                context = this@AddProductivityEnhancementServices,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@AddProductivityEnhancementServices,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.productivity_enhancement_services_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
}