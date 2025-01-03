package com.nlm.ui.activity.national_livestock_mission

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
import com.nlm.callBack.CallBackItemFormat6Edit
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddNlmAssistanceForQfspactivityBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemImportExoticVerifiedNlmBinding
import com.nlm.databinding.ItemYearWiseFinancialProgressBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AssistanceForQfspCostAssistance
import com.nlm.model.AssistanceForQfspFinancialProgres
import com.nlm.model.Format6AssistanceForQspAddEdit
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetNlmDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.Format6YearWiseFinancialProgressAdapter
import com.nlm.ui.adapter.ImportExoticAdapterDetailOfImport
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
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
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddNlmAssistanceForQFSPActivity :
    BaseActivity<ActivityAddNlmAssistanceForQfspactivityBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackItemFormat6Edit {
    private var mBinding: ActivityAddNlmAssistanceForQfspactivityBinding? = null
    private var YearWiseFinancialProgressAdapter: Format6YearWiseFinancialProgressAdapter?=null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private lateinit var YearWiseFinancialProgressList: ArrayList<AssistanceForQfspFinancialProgres>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var layoutManager: LinearLayoutManager? = null
    private var savedAsDraft:Boolean=false
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtIdNlm: Int? = null // Store selected state
    private var QuantityOfFodderSeedID: Int? = null // Store selected state
    private var QuantityOfSeedID: Int? = null // Store selected state
    private var TargetAcheivementID: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var TargetAcheivementName: String? = null // Store selected state
    private var QuantityOfFodderSeedName: String? = null // Store selected state
    private var QuantityOfSeedName: String? = null // Store selected state
    private var uploadData : ImageView?=null

    private var DocumentId:Int?=null
    var body: MultipartBody.Part? = null
    private var DocumentName:String?=null
    private var DialogDocName: TextView?=null
    private var UploadedDocumentName:String?=null
    private var TableName: String? = null
    var selectedValue: Int = 1
    private var Model:String? = null // Store selected state
    private var stateList = ArrayList<ResultGetDropDown>()
    private var formId:Int?=null
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var loading = true
    private var currentPage = 1
    private var totalPage = 1
    private var latitude:Double?=null
    private var longitude:Double?=null
    val viewModel = ViewModel()
//    private val stateList = listOf(
//        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
//        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
//        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
//        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
//        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
//        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
//        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
//        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
//    )
    private val variety = listOf(
        "Class Wise", "Variety Wise"
    )
    override val layoutId: Int
        get() = R.layout.activity_add_nlm_assistance_for_qfspactivity

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
    inner class ClickActions {

        fun saveAsDraft(view: View){

                if(mBinding?.tvDistrict?.text.toString().isNotEmpty())
                {
            savedAsDraft=true
            saveDataApi(1)
                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
                }

        }

        fun saveAndNext(view: View){
            if(mBinding?.tvDistrict?.text.toString().isNotEmpty()) {
                savedAsDraft = true
                saveDataApi(0)
            }
            else{
                showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
            }

        }

        fun addDocDialog(view: View){
            AddDocumentDialog(this@AddNlmAssistanceForQFSPActivity,null,null)
        }
        fun addMore(view: View){
            AddFinancialYearProgressDialog(this@AddNlmAssistanceForQFSPActivity,null,null)
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            showBottomSheetDialog2("State",null,null)
        }

        fun district(view: View) {
            showBottomSheetDialog2("District",null,null)
        }

        fun districtName(view: View) {
            showBottomSheetDialog("DistrictName")
        }

        fun fodderClass(view: View) {
            showBottomSheetDialog2("quantity_of_fodder_seed_class",null,null)
        }

        fun targetClass(view: View) {
            showBottomSheetDialog2("target_achievement_class",null,null)
        }

        fun quantityClass(view: View) {
            showBottomSheetDialog2("quantity_of_seed_class",null,null)
        }
    }

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        YearWiseFinancialProgressList= arrayListOf()
        DocumentList= arrayListOf()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId",0)
        AddYearWiseFinancialProgressAdapter()
        AddDocumentAdapter()
        mBinding?.rbMentally?.setOnCheckedChangeListener { _, checkedId ->
            selectedValue = when (checkedId) {
                R.id.rbMentallyYes -> 1 // If "Yes" is selected, set variable to 1
                R.id.rbMentallyNo -> 0  // If "No" is selected, set variable to 0
                else -> selectedValue    // Keep the existing value if neither is selected (unlikely)
            }
        }
        if( getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.state_code!=null){
            stateId= getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.state_code
            mBinding?.tvState?.text= getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.state_name.toString()
            mBinding?.tvState?.isEnabled=false
        }
        if (viewEdit=="view")
        {
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etNameOfOrganization?.isEnabled=false
            mBinding?.etOrganogram?.isEnabled=false
            mBinding?.etTechnicalCompetance?.isEnabled=false
            mBinding?.etLocation?.isEnabled=false
            mBinding?.etAreaUnderProduction?.isEnabled=false
            mBinding?.etQuantityOfFodderSeedVariety?.isEnabled=false
            mBinding?.etQuantityOfSeedVariety?.isEnabled=false
            mBinding?.etTargetAchievementVariety?.isEnabled=false
            mBinding?.etSourceOfSeed?.isEnabled=false
            mBinding?.etCategoryProject?.isEnabled=false
            mBinding?.etSeedProduced?.isEnabled=false
            mBinding?.etCostAssistance?.isEnabled=false
            mBinding?.tvDistrict?.isEnabled=false
            mBinding?.tvState?.isEnabled=false
            mBinding?.tvQuantityOfFodder?.isEnabled=false
            mBinding?.tvQuantityClass?.isEnabled=false
            mBinding?.tvTargetClass?.isEnabled=false
            mBinding?.rbMentally?.isEnabled=false
            mBinding?.rbMentallyYes?.isEnabled=false
            mBinding?.rbMentallyNo?.isEnabled=false
            mBinding?.tvAddMore?.hideView()
            mBinding?.tvAddDocs?.hideView()
            ViewEditApi(viewEdit)
        }
        else if(viewEdit=="edit")
        {
            ViewEditApi(viewEdit)
        }

    }
    private fun showBottomSheetDialog2(type: String,isFrom: Int?,TextView:TextView?) {
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
            "State" -> {
                Model="State"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvState!!
            }

            "District" -> {
                Model="Districts"
                dropDownApiCall(paginate = false, loader = true)
                selectedTextView = if (isFrom==3 && TextView != null){

                    TextView
                } else{
                    mBinding?.tvDistrict!!
                }
                selectedList = stateList // Update the list to districtList for District

            }
            "quantity_of_fodder_seed_class" -> {
                Model="quantity_of_fodder_seed_class"
                getNlmDropDownApi(Model)
                selectedList = stateList
                selectedTextView = mBinding!!.tvQuantityOfFodder
            }

            "quantity_of_seed_class" -> {
                Model="quantity_of_seed_class"
                getNlmDropDownApi(Model)
                selectedList = stateList
                selectedTextView = mBinding!!.tvQuantityClass
            }

            "target_achievement_class" -> {
                Model="target_achievement_class"
                getNlmDropDownApi(Model)
                selectedList = stateList
                selectedTextView = mBinding!!.tvTargetClass
            }

            else -> return
        }

        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView.text = selectedItem


            if (Model=="Districts")
            {
                 if (isFrom==3 && TextView != null){

                districtIdNlm=id
            }
                else{
                     districtName=selectedItem
                     districtId=id
                 }

            }
            else if (Model=="quantity_of_fodder_seed_class")
            {
                QuantityOfFodderSeedName  =selectedItem
                QuantityOfFodderSeedID=id
            }
            else if (Model=="quantity_of_seed_class")
            {
                QuantityOfSeedName  =selectedItem
                QuantityOfSeedID=id
            }
            else if (Model=="target_achievement_class")
            {
                TargetAcheivementName  =selectedItem
                TargetAcheivementID=id
            }
            else{
                stateId = id
            }
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
            "State" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvState
            }

            "District" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvDistrict
            }
            "quantity_of_fodder_seed_class" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvQuantityOfFodder
            }

            "quantity_of_seed_class" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvQuantityClass
            }

            "target_achievement_class" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvTargetClass
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView.text = selectedItem

            // Store the appropriate ID based on the type
            if (type == "State") {
                stateId = id  // Save the selected state ID
            } else if (type == "District") {
                districtName = selectedItem
                districtId = id  // Save the selected district ID
            }

            if (Model=="Districts")
            {
                districtName=selectedItem
                districtId=id
            }
            else{
                stateId = id
            }

            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
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

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.foramt6AssistanceForQspAddEditResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }

                if (userResponseModel!=null) {
                    if (userResponseModel._resultflag == 0) {
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                    }
                    else{
                        TableName=userResponseModel.fileurl
                        if (viewEdit=="view" || viewEdit=="edit")
                        {
                            formId=userResponseModel._result.id
                            mBinding?.etNameOfOrganization?.setText(userResponseModel._result.name_of_organization)
                            mBinding?.etOrganogram?.setText(userResponseModel._result.organogram)
                            mBinding?.etTechnicalCompetance?.setText(userResponseModel._result.technical_competance)
                            mBinding?.etLocation?.setText(userResponseModel._result.location_address)
                            mBinding?.etAreaUnderProduction?.setText(userResponseModel._result.area_under_production?.toString()?:"")
                            mBinding?.etQuantityOfFodderSeedVariety?.setText(userResponseModel._result.quantity_of_fodder_seed_variety)
                            mBinding?.etQuantityOfSeedVariety?.setText(userResponseModel._result.quantity_of_seed_variety)
                            mBinding?.etTargetAchievementVariety?.setText(userResponseModel._result.target_achievement_variety)
                            mBinding?.etSourceOfSeed?.setText(userResponseModel._result.source_of_seed)
                            mBinding?.tvDistrict?.text=userResponseModel._result.district_name
                             Log.d("TESTT","${userResponseModel._result.effective_seed}")
                            when (userResponseModel._result.effective_seed) {
                                1.0 -> mBinding?.rbMentallyYes?.isChecked = true
                                0.0 -> mBinding?.rbMentallyNo?.isChecked = true
                            }

                            val assistanceList = userResponseModel._result.assistance_for_qfsp_cost_assistance

                            if (!assistanceList.isNullOrEmpty() && assistanceList.size > 0) {
                                mBinding?.etCategoryProject?.setText(
                                    assistanceList[0].name_of_fodder_seed ?: ""
                                )
                                mBinding?.etSeedProduced?.setText( assistanceList[0].seed_produced_first_year?:"")
                                mBinding?.etCostAssistance?.setText(  assistanceList[0].cost_assistance_first_year?:"")
                            } else {
                                // Optional: Handle the case where the list is empty or null
                                mBinding?.etCategoryProject?.setText("")
                                mBinding?.etSeedProduced?.setText("")
                                mBinding?.etCostAssistance?.setText("")
                            }

                            mBinding?.tvQuantityOfFodder?.text=userResponseModel._result.quantity_of_fodder_seed_class
                            mBinding?.tvQuantityClass?.text=userResponseModel._result.quantity_of_seed_class
                            mBinding?.tvTargetClass?.text=userResponseModel._result.target_achievement_class
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                            DocumentList.clear()
                            userResponseModel._result.assistance_for_qfsp_document?.let { it1 ->
                                DocumentList.addAll(
                                    it1
                                )
                            }
                            AddDocumentAdapter?.notifyDataSetChanged()
                            YearWiseFinancialProgressList.clear()
                            userResponseModel._result.assistance_for_qfsp_financial_progress?.let { it1 ->
                                YearWiseFinancialProgressList.addAll(
                                    it1
                                )
                            }
                            YearWiseFinancialProgressAdapter?.notifyDataSetChanged()
                        }

                            if (savedAsDraft)
                            {
                                onBackPressedDispatcher.onBackPressed()
                                showSnackbar(mBinding!!.clParent, userResponseModel.message)
                            }


                    }
                }
        }

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

//                    mBinding?.tvNoDataFound?.hideView()
//                    mBinding?.rvArtificialInsemination?.showView()
                } else {
//                    mBinding?.tvNoDataFound?.showView()
//                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }
        viewModel.getNlmDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    stateList.clear()
                    stateList.addAll(userResponseModel._result)
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
                    DocumentId=userResponseModel._result.id
                    UploadedDocumentName=userResponseModel._result.document_name
                    DialogDocName?.text=userResponseModel._result.document_name
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
    }
//    private fun AddDocumentDialog(context: Context, selectedItem: ImplementingAgencyDocument?, position: Int?) {
//        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
//            layoutInflater,
//            R.layout.item_add_document_dialog,
//            null,
//            false
//        )
//        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(true)
//        dialog.setContentView(bindingDialog.root)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window!!.setLayout(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window!!.setGravity(Gravity.CENTER)
//        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
//        lp.dimAmount = 0.5f
//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        DialogDocName=bindingDialog.etDoc
//        uploadData=bindingDialog.ivPic
//        bindingDialog.btnDelete.setOnClickListener{
//            dialog.dismiss()
//        }
//        if(selectedItem!=null){
//            if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==24)
//            {
//                UploadedDocumentName=selectedItem.ia_document
//                bindingDialog.etDoc.text=selectedItem.ia_document
//            }
//            else{
//                UploadedDocumentName=selectedItem.nlm_document
//                bindingDialog.etDoc.text=selectedItem.nlm_document
//            }
//            bindingDialog.etDescription.setText(selectedItem.description)
//
//        }
//        bindingDialog.tvChooseFile.setOnClickListener {
//            if (bindingDialog.etDescription.text.toString().isNotEmpty())
//            {
//
//                checkStoragePermission(this@AddNlmAssistanceForQFSPActivity)
//            }
//            else{
//
//                mBinding?.clParent?.let { showSnackbar(it,"please enter description") }
//            }
//        }
//        bindingDialog.tvSubmit.setOnClickListener {
//            if (bindingDialog.etDescription.text.toString().isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty())
//            {
//                if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==24) {
//                    if(selectedItem!=null)
//                    {
//                        if (position != null) {
//                            DocumentList[position] = ImplementingAgencyDocument(
//                                description = bindingDialog.etDescription.text.toString(),
//                                ia_document = UploadedDocumentName,
//                                nlm_document = null,
//                                assistance_for_qfsp_id = selectedItem.assistance_for_qfsp_id,
//                                id = selectedItem.id,
//                            )
//                            AddDocumentAdapter?.notifyItemChanged(position)
//                            dialog.dismiss()
//                        }
//
//                    } else{
//
//                        DocumentList.add(ImplementingAgencyDocument(
//                            description = bindingDialog.etDescription.text.toString(),
//                            ia_document = UploadedDocumentName,
//                            nlm_document = null,
//
//
//                            ))
//
//                        DocumentList.size.minus(1).let {
//                            AddDocumentAdapter?.notifyItemInserted(it)
//                            Log.d("DOCUMENTLIST",DocumentList.toString())
//                            dialog.dismiss()
////
//                        }
//
//                    }                    }
//                else{
//                    if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==8) {
//                        if(selectedItem!=null)
//                        {
//                            if (position != null) {
//                                DocumentList?.set(position, ImplementingAgencyDocument(
//                                    description = bindingDialog.etDescription.text.toString(),
//                                    ia_document = null,
//                                    nlm_document = UploadedDocumentName,
//                                    assistance_for_qfsp_id = selectedItem.assistance_for_qfsp_id,
//                                    id = selectedItem.id,
//                                )
//                                )
//                                AddDocumentAdapter?.notifyItemChanged(position)
//                                dialog.dismiss()
//                            }
//
//                        } else{
//                            DocumentList?.add(
//                                ImplementingAgencyDocument(
//                                    description = bindingDialog.etDescription.text.toString(),
//                                    ia_document = null,
//                                    nlm_document = UploadedDocumentName,
//
//                                    )
//                            )
//                            DocumentList.size.minus(1).let {
//                                AddDocumentAdapter?.notifyItemInserted(it)
//                                dialog.dismiss()
//                            }
//                        }
//                    }
//                }
//
//
//            }
//
//
//            else {
//                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
//            }
//        }
//
//        dialog.show()
//    }
    private fun AddDocumentDialog(
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
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }

        if (selectedItem != null) {
            bindingDialog.ivPic.showView()
            if (selectedItem.is_edit==false)
            {
                bindingDialog.tvSubmit.hideView()
                bindingDialog.tvChooseFile.isEnabled=false
                bindingDialog.etDescription.isEnabled=false
            }
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24 ||selectedItem.is_ia == true
            ) {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document

            }
            else{

                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
            }
            bindingDialog.etDescription.setText(selectedItem.description)

            val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
            Log.d("URLL",fileExtension.toString())
            if (isSupported) {
                when (fileExtension) {
                    "pdf" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(R.drawable.ic_pdf).placeholder(R.drawable.ic_pdf).into(
                                it
                            )
                        }
                        val url=getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                        val downloader = AndroidDownloader(context)
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
                            Glide.with(context).load(getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }
                }
            }
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                checkStoragePermission(this@AddNlmAssistanceForQFSPActivity)
            } else {

                mBinding?.clParent?.let { showSnackbar(it, "please enter description") }
            }
        }
        val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
        if (isSupported) {
            val url=getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
            when (fileExtension) {
                "pdf" -> {
                    val downloader = AndroidDownloader(context)
                    bindingDialog.ivPic.let {
                        Glide.with(context).load(R.drawable.ic_pdf).into(
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

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString()
                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
            ) {
                if (getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id == 24
                ) {
                    if (selectedItem != null) {
                        if (position != null) {
                            DocumentList[position] = ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                nlm_document = null,
                                assistance_for_qfsp_id = selectedItem.assistance_for_qfsp_id,
                                id = selectedItem.id,
                            )
                            AddDocumentAdapter?.notifyItemChanged(position)
                            dialog.dismiss()
                        }

                    } else {

                        DocumentList.add(
                            ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                nlm_document = null,


                                )
                        )

                        DocumentList.size.minus(1).let {
                            AddDocumentAdapter?.notifyItemInserted(it)
                            Log.d("DOCUMENTLIST", DocumentList.toString())
                            dialog.dismiss()
//
                        }

                    }
                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        if (selectedItem != null) {
                            if (position != null) {
                                DocumentList[position] = ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    assistance_for_qfsp_id = selectedItem.assistance_for_qfsp_id,
                                    id = selectedItem.id,
                                )
                                AddDocumentAdapter?.notifyItemChanged(position)
                                dialog.dismiss()
                            }

                        } else {
                            DocumentList.add(
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,

                                    )
                            )
                            DocumentList.size.minus(1).let {
                                AddDocumentAdapter?.notifyItemInserted(it)
                                dialog.dismiss()
                            }
                        }
                    }
                }


            } else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAPTURE_IMAGE_REQUEST -> {

                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    uploadData?.showView()
                    uploadData?.setImageBitmap(imageBitmap)
//                    data.data?.let { startCrop(it) }
//                    fetchLocation()
                }

                PICK_IMAGE -> {
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        val uriPathHelper = URIPathHelper()
                        val filePath = uriPathHelper.getPath(this, selectedImageUri)

                        val fileExtension =
                            filePath?.substringAfterLast('.', "").orEmpty().lowercase()
                        // Validate file extension
                        if (fileExtension in listOf("png", "jpg", "jpeg")) {
                            val file = filePath?.let { File(it) }

                            // Check file size (5 MB = 5 * 1024 * 1024 bytes)
                            file?.let {
                                val fileSizeInMB = it.length() / (1024 * 1024.0) // Convert to MB
                                if (fileSizeInMB <= 5) {
                                    uploadData?.showView()
                                    uploadData?.setImageURI(selectedImageUri)
                                    uploadImage(it) // Proceed to upload
                                } else {
                                    Toast.makeText(this, "File size exceeds 5 MB", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Format not supported", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )
                        val cursor = contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                val documentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                val fileSizeInBytes =
                                    it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                                val fileSizeInMB = fileSizeInBytes / (1024 * 1024.0) // Convert to MB

                                // Validate file size (5 MB = 5 * 1024 * 1024 bytes)
                                if (fileSizeInMB <= 5) {
                                    uploadData?.showView()
                                    uploadData?.setImageResource(R.drawable.ic_pdf)
                                    DocumentName = documentName
                                    val requestBody = convertToRequestBody(this, uri)
                                    body = MultipartBody.Part.createFormData(
                                        "document_name",
                                        documentName,
                                        requestBody
                                    )
                                    viewModel.getProfileUploadFile(
                                        context = this,
                                        document_name = body,
                                        user_id = getPreferenceOfScheme(
                                            this,
                                            AppConstants.SCHEME,
                                            Result::class.java
                                        )?.user_id,
                                        table_name = getString(R.string.assistance_for_qfsp_document).toRequestBody(MultipartBody.FORM),
                                    )
                                } else {
                                    Toast.makeText(this, "File size exceeds 5 MB", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun AddYearWiseFinancialProgressAdapter(){
        YearWiseFinancialProgressAdapter= YearWiseFinancialProgressList?.let { Format6YearWiseFinancialProgressAdapter(this,it,viewEdit,this,this) }
        mBinding?.rvYearWiseFinancialProgress?.adapter = YearWiseFinancialProgressAdapter
        mBinding?.rvYearWiseFinancialProgress?.layoutManager = LinearLayoutManager(this)
    }
    private fun AddDocumentAdapter(){
        AddDocumentAdapter= SupportingDocumentAdapterWithDialog(this,DocumentList,viewEdit,this,this)
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        if (isFrom==1){
            position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
        }
        else if (isFrom==2){
            position.let { it1 -> YearWiseFinancialProgressAdapter?.onDeleteButtonClick(it1) }
        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(this,selectedItem,position)
    }
    private fun saveDataApi(isDraft: Int?) {
        Log.d("SELECTEDVALUE",selectedValue.toString())
        if (hasLocationPermissions()) {
            val intent = Intent(this@AddNlmAssistanceForQFSPActivity, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if(latitude!=null&&longitude!=null)
                {
                    viewModel.getAssistanceForQfspAddEdit(
                        this@AddNlmAssistanceForQFSPActivity, true, Format6AssistanceForQspAddEdit(
                            state_code = getPreferenceOfScheme(
                                this@AddNlmAssistanceForQFSPActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddNlmAssistanceForQFSPActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id,
                            role_id = getPreferenceOfScheme(
                                this@AddNlmAssistanceForQFSPActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,
                            id = formId,
                            is_draft = isDraft,
                            assistance_for_qfsp_document = DocumentList,
                            assistance_for_qfsp_financial_progress = YearWiseFinancialProgressList,
                            name_of_organization = mBinding?.etNameOfOrganization?.text.toString(),
                            organogram = mBinding?.etOrganogram?.text.toString(),
                            technical_competance = mBinding?.etTechnicalCompetance?.text.toString(),
                            district_code = districtId,
                            location_address = mBinding?.etLocation?.text.toString(),
                            area_under_production = mBinding?.etAreaUnderProduction?.text?.toString()
                                ?.toDoubleOrNull(),
                            quantity_of_fodder_seed_variety = mBinding?.etQuantityOfFodderSeedVariety?.text?.toString()
                                ?.toIntOrNull(),
                            quantity_of_fodder_seed_class = QuantityOfFodderSeedID,
                            quantity_of_seed_class = QuantityOfSeedID,
                            target_achievement_class = TargetAcheivementID,
                            target_achievement_variety = mBinding?.etTargetAchievementVariety?.text.toString(),
                            quantity_of_seed_variety = mBinding?.etQuantityOfSeedVariety?.text.toString()
                                .toIntOrNull(),
                            source_of_seed = mBinding?.etSourceOfSeed?.text.toString(),
                            assistance_for_qfsp_cost_assistance = arrayListOf(
                                AssistanceForQfspCostAssistance(
                                    name_of_fodder_seed = mBinding?.etCategoryProject?.text.toString(),
                                    seed_produced_first_year = mBinding?.etSeedProduced?.text?.toString(),
                                    cost_assistance_first_year = mBinding?.etCostAssistance?.text.toString()
                                )
                            ),
                            effective_seed = selectedValue,
                            lattitude=latitude,
                            longitude= longitude
                        )
                    )

                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
                }
            }}
        else {
            showLocationAlertDialog()
        }
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
        if (Model=="Districts") {
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    100,
                    Model,
                    currentPage,
                    state_code = stateId,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
        else if (Model=="quantity_of_fodder_seed_class")
        {getNlmDropDownApi(Model)}
        else if (Model=="quantity_of_seed_class")
        {getNlmDropDownApi(Model)}
        else if (Model=="target_achievement_class")
        {getNlmDropDownApi(Model)}
        else{
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    20,
                    Model,
                    currentPage,
                    null,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
    }
    private fun getNlmDropDownApi(Model:String?)
    {
        viewModel.getNlmDropDown(
            this, true, GetNlmDropDownRequest(
                column = Model,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
    }
    private fun ViewEditApi(viewEdit:String?){
        viewModel.getAssistanceForQfspAddEdit(this@AddNlmAssistanceForQFSPActivity,true,
            Format6AssistanceForQspAddEdit(

                state_code = getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this@AddNlmAssistanceForQFSPActivity,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                role_id = getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = viewEdit,
                id = itemId,
                is_draft = null,
            )
        )
    }

    override fun onClickItem(
        selectedItem: AssistanceForQfspFinancialProgres,
        position: Int,
        isFrom: Int
    ) {
        AddFinancialYearProgressDialog(this,selectedItem,position)
    }
    private fun AddFinancialYearProgressDialog(context: Context, selectedItem: AssistanceForQfspFinancialProgres?, position: Int?) {
        val bindingDialog: ItemYearWiseFinancialProgressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_year_wise_financial_progress,
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
        bindingDialog.tvSubmit.showView()
        bindingDialog.btnDelete.hideView()
        if(selectedItem!=null )
        {
            selectedItem.farmers_impacted_first?.let {
                bindingDialog.etFrarmersImpacted.setText(
                    it.toString()
                )
            } ?: run {
                bindingDialog.etFrarmersImpacted.setText("")}
            selectedItem.area_covered_first?.let {
                bindingDialog.etAreaCovered.setText(
                    it.toString()
                )
            } ?: run {
                bindingDialog.etAreaCovered.setText("")}

            selectedItem.amount_utilized_state_first?.let {
                bindingDialog.etAmountUtilizedByState.setText(
                    it.toString()
                )
            } ?: run {
                bindingDialog.etAmountUtilizedByState.setText("")}

            bindingDialog.etAssistancedByDAHD.setText(selectedItem.assistance_provided_first)
            bindingDialog.tvDistrictName.setText(selectedItem.name_of_district)
        }
        bindingDialog.tvDistrictName.setOnClickListener { showBottomSheetDialog2("District",3,bindingDialog.tvDistrictName) }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etAssistancedByDAHD.text.toString().isNotEmpty()||bindingDialog.etAmountUtilizedByState.text.toString().isNotEmpty()||bindingDialog.etAreaCovered.text.toString().isNotEmpty()||bindingDialog.etFrarmersImpacted.text.toString().isNotEmpty())
            {
                if(selectedItem!=null)
                {
                    if (position != null) {
                        if (districtIdNlm==null)
                        {
//                            districtIdNlm=selectedItem.district_code
                        }
                        YearWiseFinancialProgressList[position] = AssistanceForQfspFinancialProgres(
                            district_code = districtIdNlm,
                            assistance_for_qfsp_id=selectedItem.assistance_for_qfsp_id,
                            name_of_district = bindingDialog.tvDistrictName.text.toString(),
                            assistance_provided_first=bindingDialog.etAssistancedByDAHD.text.toString(),
                            amount_utilized_state_first=bindingDialog.etAmountUtilizedByState.text.toString().toIntOrNull(),
                            area_covered_first=bindingDialog.etAreaCovered.text.toString().toIntOrNull(),
                            farmers_impacted_first=bindingDialog.etFrarmersImpacted.text.toString(),
                            id=selectedItem.id,

                        )

                        YearWiseFinancialProgressAdapter?.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                }
                else{
                    YearWiseFinancialProgressList.add( AssistanceForQfspFinancialProgres(

                        assistance_for_qfsp_id=null,
                        name_of_district = bindingDialog.tvDistrictName.text.toString(),
                        assistance_provided_first=bindingDialog.etAssistancedByDAHD.text.toString(),
                        amount_utilized_state_first=bindingDialog.etAmountUtilizedByState.text.toString().toIntOrNull(),
                        area_covered_first=bindingDialog.etAreaCovered.text.toString().toIntOrNull(),
                        farmers_impacted_first=bindingDialog.etFrarmersImpacted.text.toString(),
                        id=null,
                    )
                    )
                    YearWiseFinancialProgressList?.size?.minus(1).let {
                        if (it != null) {
                            YearWiseFinancialProgressAdapter?.notifyItemInserted(it)
                        }

                        dialog.dismiss()
                    }}
            }


            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API level 26
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
                context = this@AddNlmAssistanceForQFSPActivity,
                document_name = body,
                user_id = getPreferenceOfScheme(this@AddNlmAssistanceForQFSPActivity, AppConstants.SCHEME, Result::class.java)?.user_id,
                table_name = getString(R.string.assistance_for_qfsp_document).toRequestBody(MultipartBody.FORM),
            )
        }
    }
}
