package com.nlm.ui.activity.national_dairy_development

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityNlmCompnentBdairyDevelopmentBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.NDDComponentBAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.NlmIAFundsRecievedAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertDate
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NlmComponentBDairyDevelopment : BaseActivity<ActivityNlmCompnentBdairyDevelopmentBinding>(),
    CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityNlmCompnentBdairyDevelopmentBinding? = null
    override val layoutId: Int
        get() = R.layout.activity_nlm_compnent_bdairy_development
    private lateinit var bottomSheetDialog: BottomSheetDialog
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
    private var formattedDate: String? = null
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var isSubmitted: Boolean = false
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
    private var goOne: String? = null
    private var goTwo: String? = null
    private var goThree: String? = null
    private var goFive: String? = null
    private var goSix: String? = null
    private var whatOne: String? = null
    private var whatTwo: String? = null
    private var whatThree: String? = null
    private var whatFour: String? = null


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
        mBinding?.rbGoOne?.setOnCheckedChangeListener { group, checkedId ->
            goOne = when (checkedId) {
                R.id.rbAssetYes -> "Yes"
                R.id.rbAssetNo -> "No"
                else -> null
            }
        }
        mBinding?.rbGoTwo?.setOnCheckedChangeListener { group, checkedId ->
            goTwo = when (checkedId) {
                R.id.rbOverallYes -> "Good"
                R.id.rbOverallNo -> "Bad"
                else -> null
            }
        }
        mBinding?.rbGoThree?.setOnCheckedChangeListener { group, checkedId ->
            goThree = when (checkedId) {
                R.id.rbHygieneYes -> "Good"
                R.id.rbHygieneNo -> "Bad"
                else -> null
            }
        }
        mBinding?.rbGoFive?.setOnCheckedChangeListener { group, checkedId ->
            goFive = when (checkedId) {
                R.id.rbMembersYes -> "Yes"
                R.id.rbMembersNo -> "No"
                else -> null
            }
        }
        mBinding?.rbGoSix?.setOnCheckedChangeListener { group, checkedId ->
            goSix = when (checkedId) {
                R.id.rbPositiveYes -> "Yes"
                R.id.rbPositiveNo -> "No"
                else -> null
            }
        }
        mBinding?.rbWhatOne?.setOnCheckedChangeListener { group, checkedId ->
            whatOne = when (checkedId) {
                R.id.rbBetterYes -> "Yes"
                R.id.rbBetterNo -> "No"
                else -> null
            }
        }
        mBinding?.rbWhatTwo?.setOnCheckedChangeListener { group, checkedId ->
            whatTwo = when (checkedId) {
                R.id.rbTransparencyYes -> "Yes"
                R.id.rbTransparencyNo -> "No"
                else -> null
            }
        }
        mBinding?.rbWhatThree?.setOnCheckedChangeListener { group, checkedId ->
            whatThree = when (checkedId) {
                R.id.rbTimelyYes -> "Yes"
                R.id.rbTimelyNo -> "No"
                else -> null
            }
        }
        mBinding?.rbWhatFour?.setOnCheckedChangeListener { group, checkedId ->
            whatFour = when (checkedId) {
                R.id.rbAssuredYes -> "Yes"
                R.id.rbAssuredNo -> "No"
                else -> null
            }
        }
        if (viewEdit == "view"){
            mBinding?.tvState?.isEnabled=false
            mBinding?.tvDistrict?.isEnabled=false
            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
            mBinding?.etDCS?.isEnabled=false
            mBinding?.etNameOfRevenueVillage?.isEnabled=false
            mBinding?.etNameOfTehsil?.isEnabled=false
            mBinding?.etLat?.isEnabled=false
            mBinding?.etLong?.isEnabled=false
            mBinding?.etDate?.isEnabled=false
            mBinding?.etAssetRemark?.isEnabled=false
            mBinding?.etOverallRemark?.isEnabled=false
            mBinding?.etHygieneRemark?.isEnabled=false
            mBinding?.etSelectionRemark?.isEnabled=false
            mBinding?.etMembersRemark?.isEnabled=false
            mBinding?.etPositiveRemark?.isEnabled=false
            mBinding?.etBetterRemark?.isEnabled=false
            mBinding?.etTransparencyRemark?.isEnabled=false
            mBinding?.etTimelyRemark?.isEnabled=false
            mBinding?.etAssuredRemark?.isEnabled=false
            mBinding?.etAnyOtherRemark?.isEnabled=false
            mBinding?.rbAssetYes?.isEnabled=false
            mBinding?.rbAssetNo?.isEnabled=false
            mBinding?.rbOverallYes?.isEnabled=false
            mBinding?.rbOverallNo?.isEnabled=false
            mBinding?.rbHygieneYes?.isEnabled=false
            mBinding?.rbHygieneNo?.isEnabled=false
            mBinding?.rbMembersYes?.isEnabled=false
            mBinding?.rbMembersNo?.isEnabled=false
            mBinding?.rbPositiveYes?.isEnabled=false
            mBinding?.rbPositiveNo?.isEnabled=false
            mBinding?.rbBetterYes?.isEnabled=false
            mBinding?.rbBetterNo?.isEnabled=false
            mBinding?.rbTransparencyYes?.isEnabled=false
            mBinding?.rbTransparencyNo?.isEnabled=false
            mBinding?.rbTimelyYes?.isEnabled=false
            mBinding?.rbTimelyNo?.isEnabled=false
            mBinding?.rbAssuredYes?.isEnabled=false
            mBinding?.rbAssuredNo?.isEnabled=false
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
    private fun viewEditApi() {

        viewModel.getComponentBAdd(
            this, true,
            NDDComponentBAddRequest(
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
                Utility.logout(this@NlmComponentBDairyDevelopment)
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


        viewModel.componentBAddResult.observe(this) {
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
                            districtId = userResponseModel._result.district_id
                            mBinding?.tvState?.text =
                                userResponseModel._result.state_name
                            mBinding?.tvDistrict?.text =
                                userResponseModel._result.district_name
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etDCS?.setText(userResponseModel._result.name_of_dcs_mpp)
                            mBinding?.etNameOfRevenueVillage?.setText(userResponseModel._result.name_of_revenue_village)
                            mBinding?.etNameOfTehsil?.setText(userResponseModel._result.name_of_tehsil)
                            mBinding?.etLat?.setText(userResponseModel._result.latitude)
                            mBinding?.etLong?.setText(userResponseModel._result.longitude)
                            mBinding?.etDate?.text = convertDate(userResponseModel._result.date_of_inspection)
                            mBinding?.etAssetRemark?.setText(userResponseModel._result.asset_earmarked_remarks)
                            mBinding?.etOverallRemark?.setText(userResponseModel._result.overall_upkeep_remarks)
                            mBinding?.etHygieneRemark?.setText(userResponseModel._result.overall_hygiene_remarks)
                            mBinding?.etSelectionRemark?.setText(userResponseModel._result.standard_operating_procedures)
                            mBinding?.etMembersRemark?.setText(userResponseModel._result.overall_interventions_remarks)
                            mBinding?.etPositiveRemark?.setText(userResponseModel._result.positive_impact_remarks)
                            mBinding?.etBetterRemark?.setText(userResponseModel._result.better_price_realisation_remarks)
                            mBinding?.etTransparencyRemark?.setText(userResponseModel._result.transparency_milk_pricing_remarks)
                            mBinding?.etTimelyRemark?.setText(userResponseModel._result.timely_milk_payment_remarks)
                            mBinding?.etAssuredRemark?.setText(userResponseModel._result.assured_marked_surplus_remarks)
                            mBinding?.etAnyOtherRemark?.setText(userResponseModel._result.any_other)

                            if (userResponseModel._result.asset_earmarked == "Yes") {
                                mBinding?.rbAssetYes?.isChecked = true
                            } else {
                                mBinding?.rbAssetNo?.isChecked = true
                            }

                            if (userResponseModel._result.overall_upkeep == "Good") {
                                mBinding?.rbOverallYes?.isChecked = true
                            }else{
                                mBinding?.rbOverallNo?.isChecked = true
                            }

                            if (userResponseModel._result.overall_hygiene == "Good") {
                                mBinding?.rbHygieneYes?.isChecked = true
                            }else{
                                mBinding?.rbHygieneNo?.isChecked = true
                            }

                            if (userResponseModel._result.overall_interventions == "Yes") {
                                mBinding?.rbMembersYes?.isChecked = true
                            }else{
                                mBinding?.rbMembersNo?.isChecked = true
                            }

                            if (userResponseModel._result.positive_impact == "Yes") {
                                mBinding?.rbPositiveYes?.isChecked = true
                            }else{
                                mBinding?.rbPositiveNo?.isChecked = true
                            }

                            if (userResponseModel._result.better_price_realisation == "Yes") {
                                mBinding?.rbBetterYes?.isChecked = true
                            }else{
                                mBinding?.rbBetterNo?.isChecked = true
                            }

                            if (userResponseModel._result.transparency_milk_pricing == "Yes") {
                                mBinding?.rbTransparencyYes?.isChecked = true
                            }else{
                                mBinding?.rbTransparencyNo?.isChecked = true
                            }

                            if (userResponseModel._result.timely_milk_payment == "Yes") {
                                mBinding?.rbTimelyYes?.isChecked = true
                            }else{
                                mBinding?.rbTimelyNo?.isChecked = true
                            }

                            if (userResponseModel._result.assured_marked_surplus == "Yes") {
                                mBinding?.rbAssuredYes?.isChecked = true
                            }else{
                                mBinding?.rbAssuredNo?.isChecked = true
                            }



                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()

                            userResponseModel._result.nlm_b_components_document.forEach { document ->
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
            addDocumentDialog(this@NlmComponentBDairyDevelopment, null, null)
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

        @SuppressLint("DefaultLocale")
        fun openCalendar(view: View) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this@NlmComponentBDairyDevelopment,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val calendarInstance = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }
                    // Convert to desired format: yyyy-MM-dd'T'HH:mm:ssXXX
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                    formattedDate = sdf.format(calendarInstance.time)

                    mBinding?.etDate?.text = convertDate(formattedDate)  // Assuming you have an EditText for the date
                    mBinding?.etDate?.setTextColor(
                        ContextCompat.getColor(
                            this@NlmComponentBDairyDevelopment,
                            R.color.black
                        )
                    )
                },
                year, month, day
            )
            datePickerDialog.show()
        }

    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
//        if (hasLocationPermissions()) {
//            val intent = Intent(this@NlmComponentBDairyDevelopment, LocationService::class.java)
//            startService(intent)
//            lifecycleScope.launch {
//                Log.d("Scope", "out")
//                delay(1000) // Delay for 2 seconds
//                Log.d("Scope", "In")
//                Log.d("Scope", latitude.toString())
//                Log.d("Scope", longitude.toString())
//
//                if (latitude != null && longitude != null) {
//                    toast("hi")
                    viewModel.getComponentBAdd(
                        this@NlmComponentBDairyDevelopment, true,
                        NDDComponentBAddRequest(
                            id = itemId,
                            district_id = districtId,
                            role_id = getPreferenceOfScheme(
                                this@NlmComponentBDairyDevelopment,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
//                            state_code = getPreferenceOfScheme(
//                                this@NlmComponentBDairyDevelopment,
//                                AppConstants.SCHEME,
//                                Result::class.java
//                            )?.state_code,
                            state_id = getPreferenceOfScheme(
                                this@NlmComponentBDairyDevelopment,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@NlmComponentBDairyDevelopment,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            status = 1,
                            is_draft = draft,
                            latitude = mBinding?.etLat?.text.toString().toDoubleOrNull(),
                            longitude = mBinding?.etLong?.text.toString().toDoubleOrNull(),
                            date_of_inspection = formattedDate,
                            name_of_dcs_mpp = mBinding?.etDCS?.text.toString(),
                            name_of_tehsil = mBinding?.etNameOfTehsil?.text.toString(),
                            name_of_revenue_village = mBinding?.etNameOfRevenueVillage?.text.toString(),
                            asset_earmarked = goOne,
                            asset_earmarked_remarks = mBinding?.etAssetRemark?.text.toString(),
                            overall_upkeep = goTwo,
                            overall_upkeep_remarks = mBinding?.etOverallRemark?.text.toString(),
                            overall_hygiene = goThree,
                            overall_hygiene_remarks = mBinding?.etHygieneRemark?.text.toString(),
                            standard_operating_procedures = mBinding?.etSelectionRemark?.text.toString(),
                            overall_interventions = goFive,
                            overall_interventions_remarks = mBinding?.etMembersRemark?.text.toString(),
                            positive_impact = goSix,
                            positive_impact_remarks = mBinding?.etPositiveRemark?.text.toString(),
                            better_price_realisation = whatOne,
                            better_price_realisation_remarks = mBinding?.etBetterRemark?.text.toString(),
                            transparency_milk_pricing = whatTwo,
                            transparency_milk_pricing_remarks = mBinding?.etTransparencyRemark?.text.toString(),
                            timely_milk_payment = whatThree,
                            timely_milk_payment_remarks = mBinding?.etTimelyRemark?.text.toString(),
                            assured_marked_surplus = whatFour,
                            assured_marked_surplus_remarks = mBinding?.etAssuredRemark?.text.toString(),
                            any_other = mBinding?.etAnyOtherRemark?.text.toString(),
                            nlm_b_components_document = totalListDocument,
                        )
                    )
//                } else {
//                    showSnackbar(mBinding?.clParent!!, "Please wait for a sec and click again")
//                }
//            }
//        }
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

        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
                bindingDialog.etDescription.setText(selectedItem.description)
            } else {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document
                bindingDialog.etDescription.setText(selectedItem.description)
            }

        }
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
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
                        if (getPreferenceOfScheme(
                                this,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id == 8
                        ) {
                            DocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    nlm_b_component_id = selectedItem.nlm_b_component_id,
                                    id = selectedItem.id,
                                )
                            addDocumentAdapter?.notifyItemChanged(position)

                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    nlm_b_component_id = selectedItem.nlm_b_component_id,
                                    id = selectedItem.id,
                                )
                            addDocumentIAAdapter?.notifyItemChanged(position)
                        }

                        dialog.dismiss()
                    }

                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                nlm_document = UploadedDocumentName,
                                id = null,
                                nlm_b_component_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                id = null,
                                nlm_b_component_id = null,
                                nlm_document = null
                            )
                        )
                        Log.d("Debug", "viewDocumentList: ${viewDocumentList.size}")

                    }

                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.size.minus(1).let {
                            addDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
//
                        }
                    } else {

                        viewDocumentList.size.minus(1).let {
                            addDocumentIAAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }
                        toast("else")
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

    @SuppressLint("Range")
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
                                DocumentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text = DocumentName


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
                                table_name = getString(R.string.nlm_b_components_document).toRequestBody(
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
                    }
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {

    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33
            registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(locationReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
    }
}