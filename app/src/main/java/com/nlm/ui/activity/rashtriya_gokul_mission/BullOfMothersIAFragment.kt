package com.nlm.ui.activity.rashtriya_gokul_mission

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
import com.nlm.callBack.CallBackBreedAvg
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentBullOfMothersIABinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemRspBreedWiseBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.FpFromNonForestFilledByNlmTeam
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.NlmEdpFormatForNlm
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspBreedList
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.FspNonPlantStorageNLMAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.RspBreedWiseAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
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


class BullOfMothersIAFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) : BaseFragment<FragmentBullOfMothersIABinding>(), CallBackDeleteFSPAtId, CallBackBreedAvg,
    CallBackDeleteAtId, CallBackItemUploadDocEdit {
    private var mBinding: FragmentBullOfMothersIABinding? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var nlmEdpFormatList: ArrayList<NlmEdpFormatForNlm>
    private var stateId: Int? = null // Store selected state
    private var listener: OnNextButtonClickListener? = null

    private lateinit var addBucksList: MutableList<RspBreedList>
    private var addBuckAdapter: RspBreedWiseAdapter? = null
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var addDocumentIAAdapter: RSPSupportingDocumentIAAdapter? = null
    private var districtIdIA: Int? = null // Store selected state
    private var districtIdNlm: Int? = null // Store selected state
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var TableName: String? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var Model: String? = null // Store selected state
    private var stateList = ArrayList<ResultGetDropDown>()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var isFrom: Int = 0
    private var img: Int = 0
    private var viewModel = ViewModel()
    private var districtId: Int? = null // Store selected state
    private var districtIdDropdown: Int? = null // Store selected state
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var uploadData: ImageView? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null
    private lateinit var plantStorageList: ArrayList<FpFromNonForestFilledByNlmTeam>
    private var plantStorageAdapter
            : FspNonPlantStorageNLMAdapter? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var latitude: Double? = null
    private var longitude: Double? = null
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
    private var remarkRadio: Int? = 0


    private val farm = listOf(
        ResultGetDropDown(-1, "State Govt"),
        ResultGetDropDown(-1, "Milk Fed"),
        ResultGetDropDown(-1, "University"),
        ResultGetDropDown(-1, "NDDB")
    )
    private val breed = listOf(
        ResultGetDropDown(-1, "Cattle Breed"),
        ResultGetDropDown(-1, "Buffalo Breed"),
    )
    private val status = listOf(
        ResultGetDropDown(-1, "Infra Completed"),
        ResultGetDropDown(-1, "Infra Yet To Be Completed"),
    )


    override val layoutId: Int
        get() = R.layout.fragment_bull_of_mothers_i_a

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = arrayListOf()
        viewModel.init()
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            requireContext(),
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        nlmAdapter()
        if (viewEdit == "view") {

            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.etNameOfTheFarm?.isEnabled = false
            mBinding?.etLocation?.isEnabled = false
            mBinding?.etPincode?.isEnabled = false
            mBinding?.etTelephone?.isEnabled = false
            mBinding?.etFarm?.isEnabled = false
            mBinding?.etBreed?.isEnabled = false
            mBinding?.etStatus?.isEnabled = false
            mBinding?.etAssistance?.isEnabled = false
            mBinding?.etFarmSanctioned?.isEnabled = false
            mBinding?.etFarmFilled?.isEnabled = false
            mBinding?.etVetSanctioned?.isEnabled = false
            mBinding?.etVetFilled?.isEnabled = false
            mBinding?.etParavetSanctioned?.isEnabled = false
            mBinding?.etParavetFilled?.isEnabled = false
            mBinding?.etCowSanctioned?.isEnabled = false
            mBinding?.etCowFilled?.isEnabled = false
            mBinding?.etStatusTagging?.isEnabled = false
            mBinding?.etArtificialInsemination?.isEnabled = false
            mBinding?.tvAddMore2?.isEnabled = false
            mBinding?.tvAddMore2?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
    }

    private fun nlmAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            context,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(context)
    }

    private fun viewEditApi() {
//        viewModel.getStateSemenAddBankApi2(
//            requireContext(), true,
//            StateSemenBankNLMRequest(
//                id = itemId,
//                state_code = getPreferenceOfScheme(
//                    requireContext(),
//                    AppConstants.SCHEME,
//                    Result::class.java
//                )?.state_code,
//                user_id = getPreferenceOfScheme(
//                    requireContext(),
//                    AppConstants.SCHEME,
//                    Result::class.java
//                )?.user_id.toString(),
//                district_code = dId,
//                role_id = getPreferenceOfScheme(
//                    requireContext(),
//                    AppConstants.SCHEME,
//                    Result::class.java
//                )?.role_id,
//                is_type = viewEdit
//            )
//        )
    }




    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(requireActivity()) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
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
        viewModel.getProfileUploadFileResult.observe(viewLifecycleOwner) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(requireContext())
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.main?.let { it1 ->
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
                    mBinding?.main?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
        viewModel.rspLabAddResult.observe(viewLifecycleOwner) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                } else {
                    TableName = userResponseModel.fileurl
                    if (savedAsDraft) {
                        savedAsDraftClick?.onSaveAsDraft()
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                listener?.onNextButtonClick()
                                return@observe
                            }
                            districtId = userResponseModel._result.district_code
//                            mBinding?.etLocation?.setText(userResponseModel._result.location)
//                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name
//                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
//                            mBinding?.etPincode?.setText(userResponseModel._result.pin_code.toString())
//                            mBinding?.etPhone?.setText(userResponseModel._result.phone_no.toString())
//                            mBinding?.etYear?.setText(userResponseModel._result.year_of_establishment)
//                            mBinding?.etAddress?.setText(userResponseModel._result.address)
//                            mBinding?.etCommentsNlm?.setText(userResponseModel._result.comments_infrastructure)
//                            mBinding?.etFund?.setText(userResponseModel._result.fund_properly_utilized)
//                            mBinding?.etSemen?.setText(userResponseModel._result.semen_straws_produced)
//                            mBinding?.etMsp?.setText(userResponseModel._result.processing_semen)
//                            mBinding?.etUnit?.setText(userResponseModel._result.equipments_per_msp)
//                            mBinding?.etPhysical?.setText(userResponseModel._result.suggestions_physical)
//                            mBinding?.etFinancial?.setText(userResponseModel._result.suggestions_financial)
//                            mBinding?.etAnyOther?.setText(userResponseModel._result.suggestions_any_other)
//                            addBucksList.clear()
//                            if (userResponseModel._result.rsp_laboratory_semen_station_quality_buck?.isEmpty() == true && viewEdit == "view") {
//                                // Add dummy data with default values
//                                val dummyData = RspBreedList(
//                                    id = 0, // Or null, depending on your use case
//                                    breed_maintained = "",
//                                    no_of_animals = null,
//                                    average_age = "",
//                                )
//
//                                addBucksList.add(dummyData)
//                            } else {
//                                userResponseModel._result.rsp_laboratory_semen_station_quality_buck?.let { it1 ->
//                                    addBucksList.addAll(
//                                        it1
//                                    )
//                                }
//                            }

                            addBuckAdapter?.notifyDataSetChanged()
                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()
                            if (userResponseModel._result.rsp_laboratory_semen_document.isEmpty() && viewEdit == "view") {

                            } else {
                                userResponseModel._result.rsp_laboratory_semen_document.forEach { document ->
                                    if (document.ia_document == null) {
                                        DocumentList.add(document)
                                    } else {
                                        viewDocumentList.add(document)

                                    }
                                }
                            }
                            addDocumentAdapter?.notifyDataSetChanged()


                        } else {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.main, userResponseModel.message)
                        }

                    }


                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("EXECUTION", "ON RESUME EXECUTED")
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API level 31
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            requireContext().registerReceiver(
                locationReceiver,
                intentFilter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(locationReceiver, intentFilter)
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("EXECUTION", "ON PAUSE EXECUTED")
        requireContext().unregisterReceiver(locationReceiver)
    }




    inner class ClickActions {
        fun saveAsDraft(view: View) {
            if (viewEdit == "view") {
                listener?.onNextButtonClick()
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

        fun save(view: View) {
            // Get the text from the input fields
            if (viewEdit == "view") {
                listener?.onNextButtonClick()
            }
            savedAsEdit = true

            if (itemId != 0) {
                saveDataApi(itemId, 0)
            } else {
                saveDataApi(null, 0)
            }
        }

        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun farm(view: View) {
            showBottomSheetDialog("Farm")
        }

        fun breed(view: View) {
            showBottomSheetDialog("breed")
        }

        fun status(view: View) {
            showBottomSheetDialog("status")
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext(), null, null)
        }

    }

    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(requireContext())
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
            "Farm" -> {
                selectedList = farm
                selectedTextView = mBinding!!.etFarm
            }
            "breed" -> {
                selectedList = breed
                selectedTextView = mBinding!!.etBreed
            }
            "status" -> {
                selectedList = status
                selectedTextView = mBinding!!.etStatus
            }


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
        stateAdapter = BottomSheetAdapter(requireContext(), selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            if (id != -1) {
                districtId = id
            }
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)


        // Rotate drawable
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
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
            requireContext(), loader, GetDropDownRequest(
                100,
                "Districts",
                currentPage,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
        if (hasLocationPermissions()) {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireContext().startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {
                    viewModel.getStateSemenAddBankApi2(
                        requireContext(), true,
                        StateSemenBankNLMRequest(
                            id = itemId,
                            role_id = getPreferenceOfScheme(
                                requireContext(),
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id,

                            state_code = getPreferenceOfScheme(
                                requireContext(),
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.state_code,
                            user_id = getPreferenceOfScheme(
                                requireContext(),
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            state_semen_bank_document = DocumentList,
                            is_draft = draft,
//                            lattitude=latitude,
//                            longitude = longitude
                        )
                    )
                } else {
                    showSnackbar(mBinding?.main!!, "Please wait for a sec and click again")
                }
            }
        } else {
            showLocationAlertDialog()
        }
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
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }

        if (selectedItem != null) {
            bindingDialog.ivPic.showView()
            if (selectedItem.is_edit == false) {
                bindingDialog.tvSubmit.hideView()
                bindingDialog.tvChooseFile.isEnabled = false
                bindingDialog.etDescription.isEnabled = false
            }
            if (getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24 || selectedItem.is_ia == true
            ) {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document

            } else {

                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
            }
            bindingDialog.etDescription.setText(selectedItem.description)

            val (isSupported, fileExtension) = getFileType(UploadedDocumentName.toString())
            Log.d("URLL", fileExtension.toString())
            if (isSupported) {
                when (fileExtension) {
                    "pdf" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(R.drawable.ic_pdf)
                                .placeholder(R.drawable.ic_pdf).into(
                                    it
                                )
                        }
                        val url = getPreferenceOfScheme(
                            requireContext(),
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                        val downloader = AndroidDownloader(context)
                        bindingDialog.etDoc.setOnClickListener {
                            if (!UploadedDocumentName.isNullOrEmpty()) {
                                downloader.downloadFile(url, UploadedDocumentName!!)
                                mBinding?.let { it1 ->
                                    showSnackbar(
                                        it1.main,
                                        "Download started"
                                    )
                                }
                                dialog.dismiss()
                            } else {
                                mBinding?.let { it1 ->
                                    showSnackbar(
                                        it1.main,
                                        "No document found"
                                    )
                                }
                                dialog.dismiss()
                            }
                        }
                    }

                    "png" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(
                                getPreferenceOfScheme(
                                    requireContext(),
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                            ).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(
                                getPreferenceOfScheme(
                                    requireContext(),
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                            ).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }
                }
            }
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                checkStoragePermission(requireContext())
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
                            requireContext(),
                            getPreferenceOfScheme(
                                requireContext(),
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
                        )
                    }
                }
            }
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
                                nlm_document = UploadedDocumentName,
                                state_semen_bank_id = selectedItem.rsp_laboratory_semen_id,
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
                            implementing_agency_id = null,
                            state_semen_bank_id = null
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
                    mBinding!!.main,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }

        dialog.show()
    }


    override fun onClickItem(selectedItem: RspBreedList, position: Int, isFrom: Int) {
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(requireContext(), selectedItem, position)
    }

    override fun showImage(bitmap: Bitmap) {
        // Override to display the image in  activity
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
                        val filePath = uriPathHelper.getPath(requireContext(), selectedImageUri)

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
                                    Toast.makeText(
                                        requireContext(),
                                        "File size exceeds 5 MB",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Format not supported",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )


                        val cursor = requireContext().contentResolver.query(
                            uri,
                            projection,
                            null,
                            null,
                            null
                        )
                        cursor?.use {
                            if (it.moveToFirst()) {
                                val documentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                val fileSizeInBytes =
                                    it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                                val fileSizeInMB =
                                    fileSizeInBytes / (1024 * 1024.0) // Convert to MB

                                // Validate file size (5 MB = 5 * 1024 * 1024 bytes)
                                if (fileSizeInMB <= 5) {
                                    uploadData?.showView()
                                    uploadData?.setImageResource(R.drawable.ic_pdf)
                                    DocumentName = documentName
                                    val requestBody = convertToRequestBody(requireContext(), uri)
                                    body = MultipartBody.Part.createFormData(
                                        "document_name",
                                        documentName,
                                        requestBody
                                    )
                                    viewModel.getProfileUploadFile(
                                        context = requireContext(),
                                        document_name = body,
                                        user_id = getPreferenceOfScheme(
                                            requireContext(),
                                            AppConstants.SCHEME,
                                            Result::class.java
                                        )?.user_id,
                                        table_name = getString(R.string.state_semen_bank_document).toRequestBody(
                                            MultipartBody.FORM
                                        ),
                                    )
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "File size exceeds 5 MB",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
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
                context = requireContext(),
                document_name = body,
                user_id = getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.state_semen_bank_document).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }

    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> addBuckAdapter?.onDeleteButtonClick(it1) }
    }


}