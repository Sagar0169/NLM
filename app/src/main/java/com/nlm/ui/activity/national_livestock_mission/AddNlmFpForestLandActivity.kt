package com.nlm.ui.activity.national_livestock_mission

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
import com.nlm.callBack.CallBackItemFormat9Edit
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddNlmFpForestLandBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemForestlandNlmBinding
import com.nlm.databinding.ItemYearWiseFinancialProgressBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.AssistanceForQfspCostAssistance
import com.nlm.model.AssistanceForQfspFinancialProgres
import com.nlm.model.Format6AssistanceForQspAddEdit
import com.nlm.model.FpFromForestLandAddEditFormat9Request
import com.nlm.model.FpFromForestLandFilledByNlm
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetNlmDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.NlmEdp
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.EdpIAAdapter
import com.nlm.ui.adapter.EdpNlmAdapter
import com.nlm.ui.adapter.ForestLandNLMAdapter
import com.nlm.ui.adapter.NlmEdpAdapter
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

class AddNlmFpForestLandActivity : BaseActivity<ActivityAddNlmFpForestLandBinding>(),
    CallBackItemFormat9Edit, CallBackItemUploadDocEdit,
    CallBackDeleteAtId {
    private var mBinding: ActivityAddNlmFpForestLandBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private var TableName: String? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var ViewDocumentAdapter: SupportingDocumentAdapterViewOnly?=null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private lateinit var TotalDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: MutableList<ImplementingAgencyDocument>
    private var DocumentId:Int?=null
    private var formId:Int?=null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var adapter: ForestLandNLMAdapter
    private var savedAsDraft:Boolean=false
    var body: MultipartBody.Part? = null
    private var currentPage = 1
    private var totalPage = 1
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var AgencyInvolvedName: String? = null
    private var AgencyInvolvedId: Int? = null
    private var TypeOfLandName: String? = null
    private var TypeOfLandId: Int? = null
    private var TypeOfAgencyName: String? = null
    private var TypeOfAgencyId: Int? = null
    private var districtIdIA: Int? = null // Store selected state
    private var districtIdNlm: Int? = null // Store selected state
    private var districtNameIA: String? = null // Store selected state
    private var loading = true
    val viewModel = ViewModel()
    private var Model:String? = null // Store selected state
    private var stateList = ArrayList<ResultGetDropDown>()
    private lateinit var programmeList: ArrayList<FpFromForestLandFilledByNlm>
    private var DialogDocName: TextView?=null
    private var UploadedDocumentName:String?=null
    private var isFrom: Int = 0
    private var DocumentName:String?=null
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var latitude:Double?=null
    private var longitude:Double?=null
    private var uploadData : ImageView?=null
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
        get() = R.layout.activity_add_nlm_fp_forest_land


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun addMore(view: View) {
            AddForestLandNLMDialog(this@AddNlmFpForestLandActivity,null,null)
        }
        fun addDocDialog(view: View) {
            AddDocumentDialog(this@AddNlmFpForestLandActivity,null,null)
        }
        fun saveAsDraft(view: View) {
            if (getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==24)
            {
                if(mBinding?.tvDistrictIA?.text.toString().isNotEmpty() && mBinding?.etImplementingAgencyIA?.text.toString().isNotEmpty() && mBinding?.etLocationIA?.text.toString().isNotEmpty())
                {
                    TotalDocumentList.clear()
                    TotalDocumentList.addAll(DocumentList)
                    TotalDocumentList.addAll(viewDocumentList)
                    saveDataApi(1)
                    savedAsDraft=true
                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
                }
            }
            else{
                if(mBinding?.tvDistrict?.text.toString().isNotEmpty() && mBinding?.etImplementingAgency?.text.toString().isNotEmpty() && mBinding?.etLocation?.text.toString().isNotEmpty())
                {
                    TotalDocumentList.clear()
                    TotalDocumentList.addAll(DocumentList)
                    TotalDocumentList.addAll(viewDocumentList)
                    saveDataApi(1)
                    savedAsDraft=true
                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
                }
            }

        }
        fun saveAndNext(view: View) {
            if (getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==24)
            {
                if(mBinding?.tvDistrictIA?.text.toString().isNotEmpty() && mBinding?.etImplementingAgencyIA?.text.toString().isNotEmpty() && mBinding?.etLocationIA?.text.toString().isNotEmpty())
                {
                    TotalDocumentList.clear()
                    TotalDocumentList.addAll(DocumentList)
                    TotalDocumentList.addAll(viewDocumentList)
                    saveDataApi(0)
                    savedAsDraft=true
                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
                }
            }
            else{
                if(mBinding?.tvDistrict?.text.toString().isNotEmpty() && mBinding?.etImplementingAgency?.text.toString().isNotEmpty() && mBinding?.etLocation?.text.toString().isNotEmpty())
                {
                    TotalDocumentList.clear()
                    TotalDocumentList.addAll(DocumentList)
                    TotalDocumentList.addAll(viewDocumentList)
                    saveDataApi(0)
                    savedAsDraft=true
                }
                else{
                    showSnackbar(mBinding?.clParent!!,"Please Fill the mandatory field")
                }
            }

        }

    }

    private fun ViewDocumentAdapter(){
        ViewDocumentAdapter= SupportingDocumentAdapterViewOnly(this,viewDocumentList,this)
        mBinding?.ShowDocumentRv?.adapter = ViewDocumentAdapter
        mBinding?.ShowDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.getIntExtra("isFrom", 0)!!
        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog2("State",0,null) }
        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog2("District",1,null) }
        mBinding!!.tvDistrictIA.setOnClickListener { showBottomSheetDialog2("District",2,null) }
        mBinding!!.tvLand.setOnClickListener { showBottomSheetDialog2("type_of_land",0,null) }
        mBinding!!.tvAgency.setOnClickListener { showBottomSheetDialog2("type_of_agency",0,null) }
        TotalDocumentList = arrayListOf()
        viewDocumentList = arrayListOf()
        ViewDocumentAdapter()
        if( getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_code!=null){
            stateId= getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_code
            mBinding?.tvState?.text= getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_name.toString()
            mBinding?.tvState?.isEnabled=false
            mBinding?.tvStateIa?.isEnabled=false
            mBinding?.tvStateIa?.text= getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_name.toString()
        }
        if( getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==24){
            mBinding?.llNLM?.hideView()
            mBinding?.llSDRv?.hideView()
            mBinding?.tvSupportingDocumentView?.hideView()
        }
        else if( getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==8){
            mBinding?.tvStateIa?.isEnabled=false
            mBinding?.tvDistrictIA?.isEnabled=false
            mBinding?.etImplementingAgencyIA?.isEnabled=false
            mBinding?.etLocationIA?.isEnabled=false
            mBinding?.etAreaCovered?.isEnabled=false
            mBinding?.tvLand?.isEnabled=false
            mBinding?.tvAgency?.isEnabled=false
            mBinding?.etVariteryFodder?.isEnabled=false
            mBinding?.etSchemeGuideline?.isEnabled=false
            mBinding?.etGrantReceived?.isEnabled=false
            mBinding?.etTarget?.isEnabled=false
        }
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId",0)
        Log.d("VIEWEDIT",viewEdit.toString())
        if(viewEdit=="view")
        {

            mBinding?.tvStateIa?.isEnabled=false
            mBinding?.tvDistrictIA?.isEnabled=false
            mBinding?.etImplementingAgencyIA?.isEnabled=false
            mBinding?.etLocationIA?.isEnabled=false
            mBinding?.tvState?.isEnabled=false
            mBinding?.tvDistrict?.isEnabled=false
            mBinding?.etImplementingAgency?.isEnabled=false
            mBinding?.etLocation?.isEnabled=false
            mBinding?.etAreaCovered?.isEnabled=false
            mBinding?.tvLand?.isEnabled=false
            mBinding?.tvAgency?.isEnabled=false
            mBinding?.etVariteryFodder?.isEnabled=false
            mBinding?.etSchemeGuideline?.isEnabled=false
            mBinding?.etGrantReceived?.isEnabled=false
            mBinding?.etTarget?.isEnabled=false
            mBinding?.tvAddMore?.hideView()
            mBinding?.tvAddDocs?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            ViewEditApi(viewEdit)
        }
        else if(viewEdit=="edit"){
            ViewEditApi(viewEdit)
        }




        programmeList = arrayListOf()
        DocumentList = arrayListOf()
        ForestLandNLMAdapter()
        AddDocumentAdapter()


        when (isFrom) {
            1 -> {
                mBinding!!.tvHeading.text = "Add New Fpfrom Non Forest"
                mBinding!!.tvMainHeading.text = "MONITORING OF NLM BY NATIONAL LEVEL MONITORS\n" +
                        "Format for Fodder production from Non. forest/rangeland/non. arableland"
            }
        }


    }
    private fun AddDocumentAdapter(){
        AddDocumentAdapter= SupportingDocumentAdapterWithDialog(this,DocumentList,viewEdit,this,this)
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun ForestLandNLMAdapter() {
        adapter = ForestLandNLMAdapter(programmeList,this,viewEdit,this,this)
        mBinding?.recyclerView1?.adapter = adapter
        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(this)
    }
    private fun showBottomSheetDialog2(type: String,isFrom: Int,TextView:TextView?) {
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
                Model="State"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvState!!
            }

            "District" -> {
                Model="Districts"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                if (isFrom==2)
                {
                selectedTextView = mBinding?.tvDistrictIA!!}
                else if (isFrom==3 && TextView != null){

                        selectedTextView = TextView
                }
                else{
                    selectedTextView = mBinding?.tvDistrict!!
                }
            }
            "agency_involved" -> {
                Model="agency_involved"
                getNlmDropDownApi(Model)
                selectedList = stateList
               selectedTextView=TextView
            }

            "type_of_land" -> {
                Model="type_of_land"
                getNlmDropDownApi(Model)
                selectedList = stateList
                selectedTextView = mBinding!!.tvLand
            }

            "type_of_agency" -> {
                Model="type_of_agency"
                getNlmDropDownApi(Model)
                selectedList = stateList
                selectedTextView = mBinding!!.tvAgency
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView?.text = selectedItem


            if (Model=="Districts")
            {
                if (isFrom==2)
                {
                    districtNameIA=selectedItem
                    districtIdIA=id
                }
                else if (isFrom==3 && TextView != null){

                    districtIdNlm=id
                }
                else{
                districtName=selectedItem
                districtId=id}
            }
            else if (Model=="agency_involved")
            {
                AgencyInvolvedName  =selectedItem
                AgencyInvolvedId=id
            }
            else if (Model=="type_of_land")
            {
                TypeOfLandName  =selectedItem
                TypeOfLandId=id
            }
            else if (Model=="type_of_agency")
            {
                TypeOfAgencyName  =selectedItem
                TypeOfAgencyId=id
            }
            else{
                stateId = id
            }
            selectedTextView?.setTextColor(ContextCompat.getColor(this, R.color.black))
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
        viewModel.fpFromForestLandAddEditResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }

            if (userResponseModel!=null) {
                if (userResponseModel._resultflag == 0) {
                    userResponseModel.message?.let { it1 -> showSnackbar(mBinding!!.clParent, it1) }
                }
                else{
                    TableName=userResponseModel.fileurl
                    if (viewEdit=="view" || viewEdit=="edit")
                    {

                        formId=userResponseModel._result.id
                        mBinding?.tvDistrictIA?.text = userResponseModel._result.district_name
                        mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                        mBinding?.etLocation?.setText(userResponseModel._result.location_address)
                        mBinding?.etLocationIA?.setText(userResponseModel._result.location_address)
                        mBinding?.etTarget?.setText(userResponseModel._result.target_achievement)
                        mBinding?.etVariteryFodder?.setText(userResponseModel._result.variety_of_fodder)
                        mBinding?.etSchemeGuideline?.setText(userResponseModel._result.scheme_guidelines)
                        mBinding?.etImplementingAgency?.setText(userResponseModel._result.name_implementing_agency)
                        mBinding?.etImplementingAgencyIA?.setText(userResponseModel._result.name_implementing_agency)
                        mBinding?.etGrantReceived?.setText(userResponseModel._result.grant_received)
                        mBinding?.etAreaCovered?.setText(userResponseModel._result.area_covered)
                        mBinding?.tvAgency?.text = userResponseModel._result.type_of_agency
                        mBinding?.tvLand?.text = userResponseModel._result.type_of_land
                        programmeList.clear()
                        userResponseModel._result.fp_from_forest_land_filled_by_nlm?.let { it1 ->
                            programmeList.addAll(
                                it1
                            )
                        }
                        adapter.notifyDataSetChanged()


                        userResponseModel.message?.let { it1 ->
                            showSnackbar(mBinding!!.clParent,
                                it1
                            )
                        }
                        DocumentList.clear()
                        viewDocumentList.clear()
                        userResponseModel._result.fp_from_forest_land_document?.forEach { document ->
                            if (document.nlm_document == null) {
                                if(getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==24)
                                {

                                    DocumentList.add(document)
                                }
                                else{
                                    viewDocumentList.add(document)}
                            } else  {
                                if(getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==8)
                                {

                                    DocumentList.add(document)
                                }
                            } }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()


                    }

                        if (savedAsDraft)
                        {
                            onBackPressedDispatcher.onBackPressed()
                            userResponseModel.message?.let { it1 ->
                                showSnackbar(mBinding!!.clParent,
                                    it1
                                )
                            }
                        }

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
                    TableName=userResponseModel._result.document_name
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
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
    private fun getNlmDropDownApi(Model:String?)
    {
        viewModel.getNlmDropDown(
            this, true, GetNlmDropDownRequest(
                column = Model,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
    }
    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {

        if (paginate) {
            currentPage++
        }
        if (Model=="Districts") {
            Log.d("EXECUTE","yes")
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    100,
                    Model,
                    currentPage,
                    state_code = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_code,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
        else if (Model=="agency_involved")
        {getNlmDropDownApi(Model)}
        else if (Model=="type_of_land")
        {getNlmDropDownApi(Model)}
        else if (Model=="type_of_agency")
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
    private fun AddForestLandNLMDialog(context: Context, selectedItem: FpFromForestLandFilledByNlm?, position: Int?) {
        val bindingDialog: ItemForestlandNlmBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_forestland_nlm,
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
        bindingDialog.tvDistrictNlm.setOnClickListener { showBottomSheetDialog2("District",3,bindingDialog.tvDistrictNlm) }
        bindingDialog.tvAgencyInvolved.setOnClickListener { showBottomSheetDialog2("agency_involved",0,bindingDialog.tvAgencyInvolved) }
        if(selectedItem!=null )
        {
            bindingDialog.tvAgencyInvolved.text=selectedItem.agency_involved
            bindingDialog.etBlock.setText(selectedItem.block_name)
            bindingDialog.etAreaCovered.setText(selectedItem.area_covered)
            bindingDialog.etConsumerFodder.setText(selectedItem.consumer_fodder)
            bindingDialog.etFodderProduced.setText(selectedItem.estimated_quantity)
            bindingDialog.etVillageName.setText(selectedItem.village_name)
            bindingDialog.tvDistrictNlm.text= selectedItem.district?.name ?:""
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (
                bindingDialog.tvDistrictNlm.text.toString().isNotEmpty()||bindingDialog.etBlock.text.toString().isNotEmpty()||bindingDialog.etAreaCovered.text.toString().isNotEmpty()||bindingDialog.etFodderProduced.text.toString().isNotEmpty()||bindingDialog.etConsumerFodder.text.toString().isNotEmpty()||bindingDialog.tvAgencyInvolved.text.toString().isNotEmpty()
                )
            {
                if(selectedItem!=null)
                {
                    if (position != null) {
                        if (districtIdNlm==null)
                        {
                            districtIdNlm=selectedItem.district_code
                        }
                        programmeList[position] = FpFromForestLandFilledByNlm(
                            id = selectedItem.id,
                            agency_involved = bindingDialog.tvAgencyInvolved.text.toString(),
                            area_covered = bindingDialog.etAreaCovered.text.toString(),
                            block_name = bindingDialog.etBlock.text.toString(),
                            consumer_fodder = bindingDialog.etConsumerFodder.text.toString(),
                            estimated_quantity = bindingDialog.etFodderProduced.text.toString(),
                            fp_from_forest_land_id=selectedItem.fp_from_forest_land_id,
                            village_name = bindingDialog.etVillageName.text.toString(),
                            district_code = districtIdNlm,
                            district_name = bindingDialog.tvDistrictNlm.text.toString(),
                            district = null
                        )
                        adapter.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                }
                else{
                    programmeList.add(FpFromForestLandFilledByNlm(
                        id = null,
                        agency_involved = bindingDialog.tvAgencyInvolved.text.toString(),
                        area_covered = bindingDialog.etAreaCovered.text.toString(),
                        block_name = bindingDialog.etBlock.text.toString(),
                        consumer_fodder = bindingDialog.etConsumerFodder.text.toString(),
                        estimated_quantity = bindingDialog.etFodderProduced.text.toString(),
                        fp_from_forest_land_id=null,
                        village_name = bindingDialog.etVillageName.text.toString(),
                        district_code = districtIdNlm,
                        district_name = bindingDialog.tvDistrictNlm.text.toString(),
                        district = null
                    ))


                    programmeList?.size?.minus(1).let {
                        if (it != null) {
                            adapter?.notifyItemInserted(it)
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

    override fun onClickItem(
        selectedItem: FpFromForestLandFilledByNlm,
        position: Int,
        isFrom: Int
    ) {
           AddForestLandNLMDialog(this,selectedItem,position)
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
//                checkStoragePermission(this@AddNlmFpForestLandActivity)
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
//                        DocumentList.add(
//                            ImplementingAgencyDocument(
//                            description = bindingDialog.etDescription.text.toString(),
//                            ia_document = UploadedDocumentName,
//                            nlm_document = null,
//
//
//                            )
//                        )
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

            checkStoragePermission(this@AddNlmFpForestLandActivity)
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

    bindingDialog.tvSubmit.setOnClickListener {
        if (bindingDialog.etDescription.text.toString().isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty())
        {
            if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==24) {
                if(selectedItem!=null)
                {
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

                } else{

                    DocumentList.add(
                        ImplementingAgencyDocument(
                            description = bindingDialog.etDescription.text.toString(),
                            ia_document = UploadedDocumentName,
                            nlm_document = null,


                            )
                    )

                    DocumentList.size.minus(1).let {
                        AddDocumentAdapter?.notifyItemInserted(it)
                        Log.d("DOCUMENTLIST",DocumentList.toString())
                        dialog.dismiss()
//
                    }

                }                    }
            else{
                if (getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id==8) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            DocumentList?.set(position, ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = null,
                                nlm_document = UploadedDocumentName,
                                assistance_for_qfsp_id = selectedItem.assistance_for_qfsp_id,
                                id = selectedItem.id,
                            )
                            )
                            AddDocumentAdapter?.notifyItemChanged(position)
                            dialog.dismiss()
                        }

                    } else{
                        DocumentList?.add(
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


        }


        else {
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
    private fun saveDataApi(isDraft:Int?){
        if (getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id==24)
        {
            if (hasLocationPermissions()) {
                val intent = Intent(this@AddNlmFpForestLandActivity, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    delay(1000) // Delay for 2 seconds
                    if(latitude!=null&&longitude!=null)
                    {
                        viewModel.getFpFromForestLandAddEdit(this@AddNlmFpForestLandActivity,true, FpFromForestLandAddEditFormat9Request(
                            state_code = getPreferenceOfScheme(
                                this@AddNlmFpForestLandActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddNlmFpForestLandActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id,
                            role_id = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id,
                            id = formId,
                            fp_from_forest_land_document = TotalDocumentList,
                            fp_from_forest_land_filled_by_nlm = programmeList,
                            area_covered = mBinding?.etAreaCovered?.text.toString().toIntOrNull(),
                            is_draft = isDraft,
                            is_deleted = null,
                            location_address = mBinding?.etLocationIA?.text.toString(),
                            name_implementing_agency = mBinding?.etImplementingAgencyIA?.text.toString(),
                            scheme_guidelines = mBinding?.etSchemeGuideline?.text.toString(),
                            target_achievement = mBinding?.etTarget?.text.toString(),
                            type_of_land = mBinding?.tvLand?.text.toString(),
                            type_of_agency = mBinding?.tvAgency?.text.toString(),
                            variety_of_fodder = mBinding?.etVariteryFodder?.text.toString(),
                            district_code = districtIdIA,
                            lattitude_ia = latitude,
                            longitude_ia = longitude
                        )
                        )
                    }
                    else{
                        showSnackbar(mBinding?.clParent!!,"No location fetched")
                    }
                }}
            else {
                showLocationAlertDialog()
            }
        }
        else{
            if (hasLocationPermissions()) {
                val intent = Intent(this@AddNlmFpForestLandActivity, LocationService::class.java)
                startService(intent)
                lifecycleScope.launch {
                    delay(1000) // Delay for 2 seconds
                    if(latitude!=null&&longitude!=null)
                    {
                        viewModel.getFpFromForestLandAddEdit(this@AddNlmFpForestLandActivity,true, FpFromForestLandAddEditFormat9Request(
                            state_code = getPreferenceOfScheme(
                                this@AddNlmFpForestLandActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                this@AddNlmFpForestLandActivity,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id,
                            role_id = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id,
                            id = formId,
                            fp_from_forest_land_document = DocumentList,
                            fp_from_forest_land_filled_by_nlm = programmeList,
                            area_covered = mBinding?.etAreaCovered?.text.toString().toIntOrNull(),
                            is_draft = isDraft,
                            is_deleted = null,
                            location_address = mBinding?.etLocation?.text.toString(),
                            name_implementing_agency = mBinding?.etImplementingAgency?.text.toString(),
                            scheme_guidelines = mBinding?.etSchemeGuideline?.text.toString(),
                            target_achievement = mBinding?.etTarget?.text.toString(),
                            type_of_land = mBinding?.tvLand?.text.toString(),
                            type_of_agency = mBinding?.tvAgency?.text.toString(),
                            variety_of_fodder = mBinding?.etVariteryFodder?.text.toString(),
                            district_code = districtId,
                            grant_received = mBinding?.etGrantReceived?.text.toString() ,
                            lattitude_nlm = latitude,
                            longitude_nlm = longitude
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
                                        table_name = getString(R.string.fp_from_forest_land_document).toRequestBody(MultipartBody.FORM),
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
    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
        }
        else if (isFrom==2){
            position.let { it1 -> adapter?.onDeleteButtonClick(it1) }
        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(this,selectedItem,position)
    }
    private fun ViewEditApi(viewEdit:String?){
        viewModel.getFpFromForestLandAddEdit(this@AddNlmFpForestLandActivity,true,
            FpFromForestLandAddEditFormat9Request(
                state_code = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this@AddNlmFpForestLandActivity,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                role_id = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = viewEdit,
                id = itemId,

            )
        )
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
                context = this@AddNlmFpForestLandActivity,
                document_name = body,
                user_id = getPreferenceOfScheme(this@AddNlmFpForestLandActivity, AppConstants.SCHEME, Result::class.java)?.user_id,
                table_name = getString(R.string.fp_from_forest_land_document).toRequestBody(MultipartBody.FORM),
            )
        }
    }
}