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
import com.nlm.databinding.ActivityArtificialInseminationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemAiObservationBinding
import com.nlm.model.ArtificialInseminationAddRequest
import com.nlm.model.ArtificialInseminationObservationByNlm
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.ObservationAIAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterViewOnly
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

class ArtificialInseminationForms : BaseActivity<ActivityArtificialInseminationBinding>()
    , CallBackDeleteAtId , CallBackItemUploadDocEdit {
    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination
    private var mBinding: ActivityArtificialInseminationBinding? = null
    private var ViewDocumentAdapter: SupportingDocumentAdapterViewOnly?=null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var mObservationAIAdapter: ObservationAIAdapter
    private var DocumentName:String?=null
    private var currentPage = 1
    private var totalPage = 1
    private var uploadData : ImageView?=null
    private var formId:Int?=null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var districtList = ArrayList<ResultGetDropDown>()
    val viewModel = ViewModel()
    var body: MultipartBody.Part? = null
    private lateinit var DocumentList:ArrayList<ImplementingAgencyDocument>
    private lateinit var TotalDocumentList: ArrayList<ImplementingAgencyDocument>
    private  var ObservationBynlmList: MutableList<ArtificialInseminationObservationByNlm>?=null
    private var DialogDocName:TextView?=null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private var TableName: String? = null
    private var districtId: Int? = null // Store selected state
    private var TotalNoOfGoat: String? = null // Store selected state
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var isSubmitted: Boolean = false
    private var DocumentId:Int?=null
    private var UploadedDocumentName:String?=null
    private lateinit var viewDocumentList: MutableList<ImplementingAgencyDocument>
    private var latitude:Double?=null
    private var longitude:Double?=null
    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

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
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId",0)
        ObservationBynlmList = mutableListOf()
        ObservationAIAdapter()
        DocumentList= arrayListOf()
        TotalDocumentList= arrayListOf()
        viewDocumentList= arrayListOf()
        ViewDocumentAdapter()
        AddDocumentAdapter()



        if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
        {
            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etStateIa?.isEnabled=false
            mBinding?.tvSupportingDocumentView?.hideView()
            mBinding?.llSDRv?.hideView()
            mBinding?.llObservationByNlm?.hideView()
        }
        else{
            mBinding?.etState?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etState?.isEnabled=false
            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etStateIa?.isEnabled=false
            mBinding?.etDistrictIa?.isEnabled=false
            mBinding?.etTotalNoOfSheepIa?.isEnabled=false
            mBinding?.etLiquidNitrogen?.isEnabled=false
            mBinding?.etFrozenSemenStraws?.isEnabled=false
            mBinding?.etCryocans?.isEnabled=false

        }

        if(viewEdit=="view"){
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
            mBinding?.etStateIa?.isEnabled=false
            mBinding?.etDistrictIa?.isEnabled=false
            mBinding?.etDistrict?.isEnabled=false
            mBinding?.etTotalNoOfSheep?.isEnabled=false
            mBinding?.etTotalNoOfSheepIa?.isEnabled=false
            mBinding?.etLiquidNitrogen?.isEnabled=false
            mBinding?.etFrozenSemenStraws?.isEnabled=false
            mBinding?.etCryocans?.isEnabled=false
            mBinding?.tvAddMore?.hideView()
            mBinding?.tvAddDocs?.hideView()
            mBinding?.etImportExotics?.isEnabled=false

            ViewEditApi()
        }
        if(viewEdit=="edit"){
            ViewEditApi()
        }
    }

    override fun setVariables() {

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
    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
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

        viewModel.artificialInseminationAddResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }
                else{
                    TableName=userResponseModel.fileurl
                    if (viewEdit=="view"||viewEdit=="edit")
                    {
                        if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==8) {
                            ObservationBynlmList?.addAll(userResponseModel._result.artificial_insemination_observation_by_nlm)
                            mObservationAIAdapter.notifyDataSetChanged()
                        }
                        formId=userResponseModel._result.id
                        mBinding?.etDistrictIa?.text= userResponseModel._result.district_name
                        mBinding?.etTotalNoOfSheep?.setText(userResponseModel._result.total_sheep_goat_labs?.toString()?:"")
                        mBinding?.etTotalNoOfSheepIa?.setText(userResponseModel._result.total_sheep_goat_labs?.toString()?:"")
                        mBinding?.etLiquidNitrogen?.setText(userResponseModel._result.liquid_nitrogen)
                        mBinding?.etFrozenSemenStraws?.setText(userResponseModel._result.frozen_semen_straws)
                        mBinding?.etCryocans?.setText(userResponseModel._result.cryocans)
                        mBinding?.etImportExotics?.setText(userResponseModel._result.exotic_sheep_goat)
                        mBinding?.etStateIa?.text= userResponseModel._result.state_name
                        mBinding?.etState?.text= userResponseModel._result.state_name
                        mBinding?.etDistrict?.text= userResponseModel._result.district_name
                        userResponseModel._result.artificial_insemination_document.forEach { document ->
                            if (document.nlm_document == null) {
                                if(getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                                {
                                    DocumentList.add(document)
                                }
                                else{
                                    viewDocumentList.add(document)}

                            }
                            else if (getPreferenceOfScheme(
                                this@ArtificialInseminationForms,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id == 8
                        ) {

                            DocumentList.add(document)
                        }

                        }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()
                    }
                    if(isSubmitted) {
                        onBackPressedDispatcher.onBackPressed()
                    }
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }

//                else{
//                    if (savedAsDraft)
//                    {
//                        savedAsDraftClick?.onSaveAsDraft()
//                    }
//                    else{
//                        Preferences.setPreference_int(requireContext(),AppConstants.FORM_FILLED_ID,userResponseModel._result.id)
//                        listener?.onNextButtonClick()
//                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
//                    }
//
//                }

            }

        }
        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.main?.let { it1 ->
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
                    mBinding?.main?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
    }
    private fun ViewDocumentAdapter(){
        ViewDocumentAdapter= SupportingDocumentAdapterViewOnly(this,viewDocumentList,this)
        mBinding?.ShowDocumentRv?.adapter = ViewDocumentAdapter
        mBinding?.ShowDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    inner class ClickActions {
        fun state(view: View){showBottomSheetDialog("state",2)}
        fun district(view: View){showBottomSheetDialog("district",0)}
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun submit(view: View){

        }
        fun addDocDialog(view: View){
            AddDocumentDialog(this@ArtificialInseminationForms,null,null)
        }
        fun addMore(view:View){
            mObservationbynlmDialog(this@ArtificialInseminationForms,1)
        }

        fun saveAsDraft(view: View){
            if (hasLocationPermissions()) {
                val intent = Intent(this@ArtificialInseminationForms, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    delay(1000) // Delay for 2 seconds
                    if(latitude!=null&&longitude!=null)
                    {
                        if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                        {
                            if(mBinding?.etDistrictIa?.text.toString().isNotEmpty())
                            {
                                TotalDocumentList.clear()
                                TotalDocumentList.addAll(DocumentList)
                                TotalDocumentList.addAll(viewDocumentList)
                                isSubmitted=true
                                viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                                    ArtificialInseminationAddRequest(
                                        state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                                        district_code = districtId,
                                        user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                                        artificial_insemination_observation_by_nlm = ObservationBynlmList,
                                        artificial_insemination_document = TotalDocumentList,
                                        is_deleted = 0,
                                        is_draft = 1,
                                        total_sheep_goat_labs =mBinding?.etTotalNoOfSheepIa?.text.toString().toIntOrNull(),
                                        liquid_nitrogen = mBinding?.etLiquidNitrogen?.text.toString(),
                                        frozen_semen_straws = mBinding?.etFrozenSemenStraws?.text.toString(),
                                        cryocans = mBinding?.etCryocans?.text.toString(),
                                        exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                                        role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                                        is_type = null,
                                        id = formId,
                                        lattitude_ia=latitude,
                                        longitude_ia = longitude
                                    )
                                )
                            }
                            else{
                                showSnackbar(mBinding?.main!!,"Please Fill the mandatory field")
                            }
                        }

                        else{
                            if(mBinding?.etDistrict?.text.toString().isNotEmpty())
                            {
                                TotalDocumentList.clear()
                                TotalDocumentList.addAll(DocumentList)
                                TotalDocumentList.addAll(viewDocumentList)
                                isSubmitted=true
                                viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                                    ArtificialInseminationAddRequest(
                                        state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                                        district_code = districtId,
                                        user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                                        artificial_insemination_observation_by_nlm = ObservationBynlmList,
                                        artificial_insemination_document = TotalDocumentList,
                                        is_deleted = 0,
                                        is_draft = 1,
                                        total_sheep_goat_labs =mBinding?.etTotalNoOfSheep?.text.toString().toIntOrNull(),
                                        liquid_nitrogen = null,
                                        frozen_semen_straws = null,
                                        cryocans = null,
                                        exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                                        role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                                        is_type = null,
                                        id = formId,
                                        lattitude_nlm=latitude,
                                        longitude_nlm = longitude
                                    )
                                )}
                            else{
                                showSnackbar(mBinding?.main!!,"Please Fill the mandatory field")
                            }
                        }
                    }
                    else {
                        showSnackbar(mBinding?.main!!, "Please wait for a sec and click again")
                    }
                }}
            else {
                showLocationAlertDialog()
            }
        }
        fun saveAndNext(view: View){
            if (hasLocationPermissions()) {
                val intent = Intent(this@ArtificialInseminationForms, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    delay(1000) // Delay for 2 seconds
                    if(latitude!=null&&longitude!=null)
                    {
                            if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                            {
                            if(mBinding?.etDistrictIa?.text.toString().isNotEmpty())
                            {
                                TotalDocumentList.clear()
                                TotalDocumentList.addAll(DocumentList)
                                TotalDocumentList.addAll(viewDocumentList)
                                isSubmitted=true
                                viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                                    ArtificialInseminationAddRequest(
                                        state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                                        district_code = districtId,
                                        user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                                        artificial_insemination_observation_by_nlm = ObservationBynlmList,
                                        artificial_insemination_document = TotalDocumentList,
                                        is_deleted = 0,
                                        is_draft = 0,
                                        total_sheep_goat_labs =mBinding?.etTotalNoOfSheepIa?.text.toString().toIntOrNull(),
                                        liquid_nitrogen = mBinding?.etLiquidNitrogen?.text.toString(),
                                        frozen_semen_straws = mBinding?.etFrozenSemenStraws?.text.toString(),
                                        cryocans = mBinding?.etCryocans?.text.toString(),
                                        exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                                        role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                                        is_type = null,
                                        id = formId,
                                        lattitude_ia=latitude,
                                        longitude_ia = longitude
                                    )
                                )
                            }
                            else{
                                showSnackbar(mBinding?.main!!,"Please wait for a sec and click again")
                            }
                        }

                        else{
                            if(mBinding?.etDistrict?.text.toString().isNotEmpty())
                            {
                                TotalDocumentList.clear()
                                TotalDocumentList.addAll(DocumentList)
                                TotalDocumentList.addAll(viewDocumentList)
                                isSubmitted=true
                                viewModel.getArtificialInseminationAdd(this@ArtificialInseminationForms,true,
                                    ArtificialInseminationAddRequest(
                                        state_code = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                                        district_code = districtId,
                                        user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                                        artificial_insemination_observation_by_nlm = ObservationBynlmList,
                                        artificial_insemination_document = TotalDocumentList,
                                        is_deleted = 0,
                                        is_draft = 0,
                                        total_sheep_goat_labs =mBinding?.etTotalNoOfSheep?.text.toString().toIntOrNull(),
                                        liquid_nitrogen = null,
                                        frozen_semen_straws = null,
                                        cryocans = null,
                                        exotic_sheep_goat = mBinding?.etImportExotics?.text.toString(),
                                        role_id =   getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                                        is_type = null,
                                        id = formId,
                                        lattitude_nlm=latitude,
                                        longitude_nlm = longitude
                                    )
                                )}
                            else{
                                showSnackbar(mBinding?.main!!,"No location fetched")
                            }
                        }
                    }
                    else {
                        showSnackbar(mBinding?.main!!, "Please wait for a sec and click again")
                    }
                }
            }
            else {
                showLocationAlertDialog()
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
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
    }
    private fun dropDownApiCallState(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                20,
                "States",
                currentPage,
                null,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
    }
//    private fun AddDocumentDialog(context: Context,selectedItem: ImplementingAgencyDocument?,position: Int?) {
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
//                checkStoragePermission(this@ArtificialInseminationForms)
//            }
//            else{
//
//                mBinding?.main?.let { showSnackbar(it,"please enter description") }
//            }
//        }
//
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
//                                artificial_insemination_id = selectedItem.artificial_insemination_id,
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
//                                    artificial_insemination_id = selectedItem.artificial_insemination_id,
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
//                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
//            }
//        }
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
            Log.d("URLL",getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName))
            if (isSupported) {
                when (fileExtension) {
                    "pdf" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(R.drawable.ic_pdf).placeholder(R.drawable.ic_pdf).into(
                                it
                            )
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

                checkStoragePermission(this)
            } else {

                mBinding?.main?.let { showSnackbar(it, "please enter description") }
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
                                artificial_insemination_id = selectedItem.artificial_insemination_id,
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
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    artificial_insemination_id = selectedItem.artificial_insemination_id,
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
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }

        dialog.show()
    }
    private fun showBottomSheetDialog(type: String, isFrom: Int) {
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


            "state" -> {
                selectedList = districtList
                if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                {
                    selectedTextView = mBinding!!.etStateIa
                }
                else{
                    selectedTextView = mBinding!!.etState
                }
                dropDownApiCallState(paginate = false, loader = true)
            }

            "district" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                if (getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                {
                    selectedTextView = mBinding!!.etDistrictIa
                }
                else{
                    selectedTextView = mBinding!!.etDistrict
                }

            }



            else -> return
        }
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
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
                    Log.d("DOCUMENT",imageBitmap.toString())
                    uploadData?.showView()
                    uploadData?.setImageBitmap(imageBitmap)
//                    data.data?.let { startCrop(it) }
//                    fetchLocation()
                }

                PICK_IMAGE -> {
                    val selectedImageUri = data?.data
                    Log.d("DOCUMENT",selectedImageUri.toString())
                    uploadData?.showView()
                    uploadData?.setImageURI(selectedImageUri)
                    if (selectedImageUri != null) {
                        val uriPathHelper = URIPathHelper()
                        val filePath = uriPathHelper.getPath(this, selectedImageUri)
                        val fileExtension = filePath?.substringAfterLast('.', "").orEmpty().lowercase()

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
                        val cursor = contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName=
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
                                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                                table_name = getString(R.string.artificial_insemination).toRequestBody(MultipartBody.FORM),
                            )
                        }
                    }
                }
            }}
    }

    private fun ObservationAIAdapter() {
        ObservationBynlmList = mutableListOf()
        mObservationAIAdapter = ObservationAIAdapter(ObservationBynlmList!!,viewEdit)
        mBinding?.rvObservationByNlm?.adapter = mObservationAIAdapter
        mBinding?.rvObservationByNlm?.layoutManager =
            LinearLayoutManager(this)
    }

    private fun AddDocumentAdapter(){
        AddDocumentAdapter= DocumentList?.let { SupportingDocumentAdapterWithDialog(this,it,viewEdit,this,this) }
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun mObservationbynlmDialog(context: Context,isFrom:Int) {
        val bindingDialog: ItemAiObservationBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_ai_observation,
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
        bindingDialog.tvSubmit.showView()

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNameOfTheCenter.text.toString().isNotEmpty() && bindingDialog.etNumberOFAiPerformed.text.toString().isNotEmpty() && bindingDialog.etWhetherManPowerTrainedForGoatAI.text.toString().isNotEmpty() && bindingDialog.etEquipmentAvailable.text.toString().isNotEmpty())
            {
                bindingDialog.etNumberOFAiPerformed.text.toString().toIntOrNull()?.let { it1 ->
                    ArtificialInseminationObservationByNlm(
                        bindingDialog.etNameOfTheCenter.text.toString(),
                        it1,
                        bindingDialog.etWhetherManPowerTrainedForGoatAI.text.toString(),
                        bindingDialog.etEquipmentAvailable.text.toString()
                    )
                }?.let { it2 ->
                    ObservationBynlmList?.add(
                        it2
                    )
                }

                ObservationBynlmList?.size?.minus(1).let {
                    if (it != null) {
                        mObservationAIAdapter.notifyItemInserted(it)
                    }
                }
                dialog.dismiss()
            }

            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    private fun ViewEditApi(){
        viewModel.getArtificialInseminationAdd(this,true,
            ArtificialInseminationAddRequest(
                id = itemId,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                is_type = viewEdit,
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                artificial_insemination_observation_by_nlm = null,
                artificial_insemination_document = null,
                is_deleted = null,
                is_draft = null,
                total_sheep_goat_labs = null,
                liquid_nitrogen = null,
                frozen_semen_straws = null,
                cryocans = null,
                exotic_sheep_goat = null,
                district_code = null,
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(this,selectedItem,position)
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
                context = this@ArtificialInseminationForms,
                document_name = body,
                user_id = getPreferenceOfScheme(this@ArtificialInseminationForms, AppConstants.SCHEME, Result::class.java)?.user_id,
                table_name = getString(R.string.artificial_insemination).toRequestBody(MultipartBody.FORM),
            )
        }
    }
}