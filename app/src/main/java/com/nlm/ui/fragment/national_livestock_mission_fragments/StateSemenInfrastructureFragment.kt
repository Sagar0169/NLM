package com.nlm.ui.fragment.national_livestock_mission_fragments

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
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemGoatSemen
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.model.StateSemenInfraGoat
import com.nlm.services.LocationService
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
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


class StateSemenInfrastructureFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) :
    BaseFragment<FragmentStateSemenInfrastructureBinding>(), CallBackItemGoatSemen,
    CallBackDeleteAtId, CallBackItemUploadDocEdit {

    override val layoutId: Int
        get() = R.layout.fragment_state__semen__infrastructure
    private var mBinding: FragmentStateSemenInfrastructureBinding? = null
    private lateinit var stateSemenInfraGoatList: MutableList<StateSemenInfraGoat>
    private var stateSemenInfraGoatAdapter: StateSemenInfrastructureAdapter? = null
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft: Boolean = false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var addDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    var body: MultipartBody.Part? = null
    val viewModel = ViewModel()
    private var TableName: String? = null
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var savedAsEdit: Boolean = false
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
    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = arrayListOf()
        viewModel.init()


        stateSemenInfraGoatAdapter()
        addDocumentAdapter = SupportingDocumentAdapterWithDialog(
            requireContext(),
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())
        if (viewEdit == "view") {

            mBinding?.tvAddMore1?.isEnabled = false
            mBinding?.tvAddMore1?.hideView()
            mBinding?.tvAddMore2?.isEnabled = false
            mBinding?.tvAddMore2?.hideView()
            mBinding?.etStorageCapacity?.isEnabled = false
            mBinding?.etCoopOne?.isEnabled = false
            mBinding?.etCoopTwo?.isEnabled = false
            mBinding?.etCoopThree?.isEnabled = false
            mBinding?.etNgoOne?.isEnabled = false
            mBinding?.etNgoTwo?.isEnabled = false
            mBinding?.etNgoThree?.isEnabled = false
            mBinding?.etPrivateOne?.isEnabled = false
            mBinding?.etPrivateTwo?.isEnabled = false
            mBinding?.etPrivateThree?.isEnabled = false
            mBinding?.etOtherStateOne?.isEnabled = false
            mBinding?.etOtherStateTwo?.isEnabled = false
            mBinding?.etOtherStateThree?.isEnabled = false
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
    }

    private fun viewEditApi() {

        viewModel.getStateSemenAddBankApi2(
            requireContext(), true,
            StateSemenBankNLMRequest(
                id = itemId,
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
                district_code = dId,
                role_id = getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit
            )
        )
    }

    private fun stateSemenInfraGoatAdapter() {
        stateSemenInfraGoatList = mutableListOf()
        stateSemenInfraGoatAdapter =
            StateSemenInfrastructureAdapter(stateSemenInfraGoatList, viewEdit, this)
        mBinding?.recyclerView1?.adapter = stateSemenInfraGoatAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.getProfileUploadFileResult.observe(viewLifecycleOwner) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(requireContext())
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
        viewModel.stateSemenBankAddResult.observe(viewLifecycleOwner) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {
                    TableName=userResponseModel.fileurl
                    if (savedAsDraft) {
                        savedAsDraftClick?.onSaveAsDraft()
                    }
                    if (savedAsEdit) {
                        savedAsDraftClick?.onSaveAsDraft()
                    }
                    if (viewEdit == "view" || viewEdit == "edit") {
                        mBinding?.etStorageCapacity?.setText(userResponseModel._result.storage_capacity)
                        mBinding?.etCoopOne?.setText(userResponseModel._result.major_clients_coop_fin_year_one)
                        mBinding?.etCoopTwo?.setText(userResponseModel._result.major_clients_coop_fin_year_two)
                        mBinding?.etCoopThree?.setText(userResponseModel._result.major_clients_coop_fin_year_three)
                        mBinding?.etNgoOne?.setText(userResponseModel._result.major_clients_ngo_fin_year_one)
                        mBinding?.etNgoTwo?.setText(userResponseModel._result.major_clients_ngo_fin_year_two)
                        mBinding?.etNgoThree?.setText(userResponseModel._result.major_clients_ngo_fin_year_three)
                        mBinding?.etPrivateOne?.setText(userResponseModel._result.major_clients_private_fin_year_one)
                        mBinding?.etPrivateTwo?.setText(userResponseModel._result.major_clients_private_fin_year_two)
                        mBinding?.etPrivateThree?.setText(userResponseModel._result.major_clients_private_fin_year_three)
                        mBinding?.etOtherStateOne?.setText(userResponseModel._result.major_clients_other_states_fin_year_one)
                        mBinding?.etOtherStateTwo?.setText(userResponseModel._result.major_clients_other_states_fin_year_two)
                        mBinding?.etOtherStateThree?.setText(userResponseModel._result.major_clients_other_states_fin_year_three)
                        stateSemenInfraGoatList.clear()
                        if (userResponseModel._result.state_semen_bank_infrastructure.isEmpty() && viewEdit == "view") {
                            // Add dummy data with default values
                            val dummyData = StateSemenInfraGoat(
                                "",
                                "",
                                null,
                                null
                            )

                            stateSemenInfraGoatList.add(dummyData)
                        } else {
                            userResponseModel._result.state_semen_bank_infrastructure.let { it1 ->
                                stateSemenInfraGoatList.addAll(
                                    it1
                                )
                            }
                        }
                        stateSemenInfraGoatAdapter?.notifyDataSetChanged()
                        DocumentList.clear()
                        if (userResponseModel._result.state_semen_bank_document.isEmpty() && viewEdit == "view") {
//                            // Add dummy data with default values
//                            val dummyData = ImplementingAgencyDocument(
//                                id = 0, // Or null, depending on your use case
//                                description = "",
//                                ia_document = "",
//                                nlm_document = "",
//                                fsp_plant_storage_id = 0 // Or null, depending on your use case
//                            )
//                            DocumentList.add(dummyData)
                        } else {
                            userResponseModel._result.state_semen_bank_document.let { it1 ->
                                DocumentList.addAll(
                                    it1
                                )
                            }
                        }
                        addDocumentAdapter?.notifyDataSetChanged()

                    }
                    else {
                        savedAsDraftClick?.onSaveAsDraft()

                    }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        Log.d("EXECUTION","ON RESUME EXECUTED")
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.S) { // API level 31
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            requireContext().registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationReceiver, intentFilter)
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("EXECUTION","ON PAUSE EXECUTED")
        requireContext().unregisterReceiver(locationReceiver)
    }
    private fun compositionOfGoverningNlmIaDialog(
        context: Context,
        isFrom: Int,
        selectedItem: StateSemenInfraGoat?,
        position: Int?
    ) {
        val bindingDialog: ItemStateSemenInfragoatBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_state_semen_infragoat,
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
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etListOfEquipment.setText(selectedItem.infrastructure_list_of_equipment)
            bindingDialog.etYearOfProcurement.setText(selectedItem.infrastructure_year_of_procurement)

        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etListOfEquipment.text.toString()
                    .isNotEmpty() || bindingDialog.etYearOfProcurement.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        stateSemenInfraGoatList[position] =
                            StateSemenInfraGoat(
                                bindingDialog.etListOfEquipment.text.toString(),
                                bindingDialog.etYearOfProcurement.text.toString(),
                                selectedItem.id,
                                selectedItem.infra_goat_id
                            )
                        stateSemenInfraGoatAdapter?.notifyItemChanged(position)
                    }

                } else {
                    stateSemenInfraGoatList.add(
                        StateSemenInfraGoat(
                            bindingDialog.etListOfEquipment.text.toString(),
                            bindingDialog.etYearOfProcurement.text.toString(),
                            id = null,
                            null
                        )
                    )
                    stateSemenInfraGoatList.size.minus(1).let {
                        stateSemenInfraGoatAdapter?.notifyItemInserted(it)
                    }
                }
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

        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext(), null, null)
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1, null, null)
        }
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
        if(itemId==0){
            mBinding?.clParent?.let { showSnackbar(it,"Please Fill the mandatory fields of Basic Information Tab") }
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireContext().startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if(latitude!=null&&longitude!=null) {
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
                            storage_capacity = mBinding?.etStorageCapacity?.text.toString(),
                            state_semen_bank_infrastructure = stateSemenInfraGoatList,
                            major_clients_coop_fin_year_one = mBinding?.etCoopOne?.text.toString(),
                            major_clients_coop_fin_year_two = mBinding?.etCoopTwo?.text.toString(),
                            major_clients_coop_fin_year_three = mBinding?.etCoopThree?.text.toString(),
                            major_clients_ngo_fin_year_one = mBinding?.etNgoOne?.text.toString(),
                            major_clients_ngo_fin_year_two = mBinding?.etNgoTwo?.text.toString(),
                            major_clients_ngo_fin_year_three = mBinding?.etNgoThree?.text.toString(),
                            major_clients_private_fin_year_one = mBinding?.etPrivateOne?.text.toString(),
                            major_clients_private_fin_year_two = mBinding?.etPrivateTwo?.text.toString(),
                            major_clients_private_fin_year_three = mBinding?.etPrivateThree?.text.toString(),
                            major_clients_other_states_fin_year_one = mBinding?.etOtherStateOne?.text.toString(),
                            major_clients_other_states_fin_year_two = mBinding?.etOtherStateTwo?.text.toString(),
                            major_clients_other_states_fin_year_three = mBinding?.etOtherStateThree?.text.toString(),
                            state_semen_bank_document = DocumentList,
                            is_draft = draft,
//                            lattitude=latitude,
//                            longitude = longitude
                        )
                    )
                }
                else {
                    showSnackbar(mBinding?.clParent!!, "Please wait for a sec and click again")
                }
            }
        }
        else {
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
            if (selectedItem.is_edit==false)
            {
                bindingDialog.tvSubmit.hideView()
                bindingDialog.tvChooseFile.isEnabled=false
                bindingDialog.etDescription.isEnabled=false
            }
            if (getPreferenceOfScheme(
                    requireContext(),
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
                        val url=getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
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
                            Glide.with(context).load(getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)).placeholder(R.drawable.ic_image_placeholder).into(
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
                            requireContext(),
                            getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
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
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }

        dialog.show()
    }




    override fun onClickItem(selectedItem: StateSemenInfraGoat, position: Int, isFrom: Int) {

        compositionOfGoverningNlmIaDialog(requireContext(), isFrom, selectedItem, position)
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(requireContext(), selectedItem, position)
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
                                     Toast.makeText(requireContext(), "File size exceeds 5 MB", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Format not supported", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )


                        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
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
                                    Toast.makeText(requireContext(), "File size exceeds 5 MB", Toast.LENGTH_LONG).show()
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
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id,
                table_name = getString(R.string.state_semen_bank_document).toRequestBody(MultipartBody.FORM),
            )
        }
    }
}