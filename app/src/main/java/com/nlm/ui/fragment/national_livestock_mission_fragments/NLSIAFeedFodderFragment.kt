package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Activity
import android.app.AlertDialog
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
import androidx.core.content.ContextCompat.registerReceiver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.services.LocationService
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
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


class NLSIAFeedFodderFragment(private val viewEdit: String?, private val itemId: Int?) :
    BaseFragment<FragmentNLSIAFeedFodderBinding>(), CallBackDeleteAtId, CallBackItemUploadDocEdit {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__feed_fodder

    private var mBinding: FragmentNLSIAFeedFodderBinding? = null
    val viewModel = ViewModel()
    private var savedAsDraft: Boolean = false
    private var DocumentName: String? = null
    private var uploadData : ImageView?=null
    var body: MultipartBody.Part? = null
    private var savedAsEdit: Boolean = false
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var UploadedDocumentName: String? = null
    private var listener: OnNextButtonClickListener? = null
    private var DialogDocName: TextView? = null
    private var DocumentId: Int? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var itemPosition: Int? = null
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


    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = arrayListOf()
        viewModel.init()
        if (viewEdit == "view") {
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etAssessmentOfGreen?.isEnabled = false
            mBinding?.tvAddMore2?.hideView()
            mBinding?.etAvailabilityOfGreen?.isEnabled = false
            mBinding?.etAvailibilityOfDry?.isEnabled = false
            mBinding?.AvailabilityOfConcentrate?.isEnabled = false
            mBinding?.etAvailabilityCommon?.isEnabled = false
            mBinding?.etEffortsOfState?.isEnabled = false
            mBinding?.etNameOfAgency?.isEnabled = false
            mBinding?.etQuantityOfFodder?.isEnabled = false
            mBinding?.etDistributionChannel?.isEnabled = false
            mBinding?.etNumberOfFodder?.isEnabled = false

            ViewEditApi()
        } else if (viewEdit == "edit") {
            ViewEditApi()

        }
        AddDocumentAdapter = SupportingDocumentAdapterWithDialog(
            requireContext(),
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView1?.adapter = AddDocumentAdapter
        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner) {
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
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                savedAsDraftClick?.onSaveAsDraft()
                            } else {
                                mBinding?.etAssessmentOfGreen?.setText(userResponseModel._result.assessments_of_green)
                                mBinding?.etAvailabilityOfGreen?.setText(userResponseModel._result.availability_of_green_area)
                                mBinding?.etAvailibilityOfDry?.setText(userResponseModel._result.availability_of_dry)
                                mBinding?.AvailabilityOfConcentrate?.setText(userResponseModel._result.availability_of_concentrate)
                                mBinding?.etAvailabilityCommon?.setText(userResponseModel._result.availability_of_common)
                                mBinding?.etEffortsOfState?.setText(userResponseModel._result.efforts_of_state)
                                mBinding?.etNameOfAgency?.setText(userResponseModel._result.name_of_the_agency)
                                mBinding?.etQuantityOfFodder?.setText(userResponseModel._result.quantity_of_fodder)
                                mBinding?.etDistributionChannel?.setText(userResponseModel._result.distribution_channel)
                                mBinding?.etNumberOfFodder?.setText(userResponseModel._result.number_of_fodder)
                                userResponseModel._result.implementing_agency_document?.let { it1 ->
                                    DocumentList.addAll(
                                        it1
                                    )
                                }
                                AddDocumentAdapter?.notifyDataSetChanged()

                            }
                        } else {
                            savedAsDraftClick?.onSaveAsDraft()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }
                    }
                }
            }
        }
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
    }

    inner class ClickActions {

        fun saveAndNext(view: View) {
            if (viewEdit == "view") {
                listener?.onNextButtonClick()
            }
            if (itemId == 0) {
                activity?.supportFragmentManager?.popBackStackImmediate(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                showSnackbar(
                    mBinding!!.clParent,
                    "Please fill the mandatory field and save the data"
                )
                listener?.onNavigateToFirstFragment()

            } else {
                if (viewEdit == "edit") {
                    savedAsEdit = true
                }
                saveDataApi(0)
            }
        }

        fun saveAsDraft(view: View) {
            if (itemId == 0) {
                activity?.supportFragmentManager?.popBackStackImmediate(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                showSnackbar(
                    mBinding!!.clParent,
                    "Please fill the mandatory field and save the data"
                )
                listener?.onNavigateToFirstFragment()

            } else {
                if (viewEdit == "edit") {
                    savedAsEdit = true
                }
                saveDataApi(1)
                savedAsDraft = true
            }
        }

        fun addDocDialog(view: View) {
            AddDocumentDialog(requireContext(), null, null)
        }

    }
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
                if (getPreferenceOfScheme(
                        requireContext(),
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
                            requireContext(),
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
//    private fun AddDocumentDialog(
//        context: Context,
//        selectedItem: ImplementingAgencyDocument?,
//        position: Int?,
//    ) {
//        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
//            layoutInflater,
//            R.layout.item_add_document_dialog,
//            null,
//            false
//        )
//        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(false)
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
//        DialogDocName = bindingDialog.etDoc
//        uploadData=bindingDialog.ivPic
//        if (selectedItem != null) {
//            if (getPreferenceOfScheme(
//                    requireContext(),
//                    AppConstants.SCHEME,
//                    Result::class.java
//                )?.role_id == 24
//            ) {
//                UploadedDocumentName = selectedItem.ia_document
//                bindingDialog.etDoc.text = selectedItem.ia_document
//            } else {
//                if (getPreferenceOfScheme(
//                        requireContext(),
//                        AppConstants.SCHEME,
//                        Result::class.java
//                    )?.role_id == 8
//                ) {
//                    UploadedDocumentName = selectedItem.nlm_document
//                    bindingDialog.etDoc.text = selectedItem.nlm_document
//                }
//            }
//            bindingDialog.etDescription.setText(selectedItem.description)
//        }
//
//        bindingDialog.btnDelete.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        bindingDialog.tvChooseFile.setOnClickListener {
//            if (bindingDialog.etDescription.text.toString().isNotEmpty())
//            {
//
//                checkStoragePermission(requireContext())
//            }
//            else{
//
//                mBinding?.clParent?.let { showSnackbar(it,"please enter description") }
//            }
//        }
//
//
//        bindingDialog.tvSubmit.setOnClickListener {
//            if (bindingDialog.etDescription.text.toString()
//                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
//            ) {
//                if (getPreferenceOfScheme(
//                        requireContext(),
//                        AppConstants.SCHEME,
//                        Result::class.java
//                    )?.role_id == 24
//                ) {
//                    if (selectedItem != null) {
//                        if (position != null) {
//                            DocumentList[position] =
//                                ImplementingAgencyDocument(
//                                    description = bindingDialog.etDescription.text.toString(),
//                                    ia_document = UploadedDocumentName,
//                                    nlm_document = null,
//                                    implementing_agency_id = selectedItem.implementing_agency_id,
//                                    id = selectedItem.id,
//                                )
//                            AddDocumentAdapter?.notifyItemChanged(position)
//                            dialog.dismiss()
//                        }
//
//                    } else {
//                        DocumentList.add(
//                            ImplementingAgencyDocument(
//                                description = bindingDialog.etDescription.text.toString(),
//                                ia_document = UploadedDocumentName,
//                                nlm_document = null,
////                            implementing_agency_id = itemId,
////                            id = DocumentId,
//                            )
//                        )
//                        DocumentList.size.minus(1).let {
//                            AddDocumentAdapter?.notifyItemInserted(it)
//                            dialog.dismiss()
//                        }
//                    }
//
//                } else {
//                    if (getPreferenceOfScheme(
//                            requireContext(),
//                            AppConstants.SCHEME,
//                            Result::class.java
//                        )?.role_id == 8
//                    ) {
//                        if (selectedItem != null) {
//                            if (position != null) {
//                                DocumentList[position] =
//                                    ImplementingAgencyDocument(
//                                        description = bindingDialog.etDescription.text.toString(),
//                                        ia_document = null,
//                                        nlm_document = UploadedDocumentName,
//                                        implementing_agency_id = selectedItem.implementing_agency_id,
//                                        id = selectedItem.id,
//                                    )
//                                AddDocumentAdapter?.notifyItemChanged(position)
//                                dialog.dismiss()
//                            }
//
//                        } else {
//                            DocumentList.add(
//                                ImplementingAgencyDocument(
//                                    description = bindingDialog.etDescription.text.toString(),
//                                    ia_document = null,
//                                    nlm_document = UploadedDocumentName,
////                            implementing_agency_id = itemId,
////                            id = DocumentId,
//                                )
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
//            } else {
//                showSnackbar(
//                    mBinding!!.clParent,
//                    getString(R.string.please_enter_atleast_one_field)
//                )
//            }
//        }
//        dialog.show()
//    }

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

                    uploadData?.showView()
                    uploadData?.setImageURI(selectedImageUri)
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
                                    mBinding?.let { showSnackbar(it.clParent,"File size exceeds 5 MB") }
                                }
                            }
                        } else {
                            mBinding?.let { showSnackbar(it.clParent,"Format not supported") }
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
                                        table_name = getString(R.string.implementing_agency_document).toRequestBody(
                                            MultipartBody.FORM
                                        ),
                                    )
                                } else {
                                    mBinding?.let { showSnackbar(it.clParent,"File size exceeds 5 MB") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ViewEditApi() {
        viewModel.getImplementingAgencyAddApi(
            requireContext(), true,
            ImplementingAgencyAddRequest(
                part = "part7",
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
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            requireContext().registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationReceiver, intentFilter)
        }
    }


    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(locationReceiver)
    }

    private fun saveDataApi(isDraft: Int?) {
        if (hasLocationPermissions()) {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireContext().startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {

                    viewModel.getImplementingAgencyAddApi(
                        requireContext(), true,
                        ImplementingAgencyAddRequest(
                            user_id = getPreferenceOfScheme(
                                requireContext(),
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.user_id.toString(),
                            part = "part7",
                            assessments_of_green = mBinding?.etAssessmentOfGreen?.text.toString(),
                            availability_of_green_area = mBinding?.etAvailabilityOfGreen?.text.toString(),
                            availability_of_dry = mBinding?.etAvailibilityOfDry?.text.toString(),
                            availability_of_concentrate = mBinding?.AvailabilityOfConcentrate?.text.toString(),
                            availability_of_common = mBinding?.etAvailabilityCommon?.text.toString(),
                            efforts_of_state = mBinding?.etEffortsOfState?.text.toString(),
                            name_of_the_agency = mBinding?.etNameOfAgency?.text.toString(),
                            quantity_of_fodder = mBinding?.etQuantityOfFodder?.text.toString(),
                            distribution_channel = mBinding?.etDistributionChannel?.text.toString(),
                            number_of_fodder = mBinding?.etNumberOfFodder?.text.toString(),
                            implementing_agency_document = DocumentList,
                            id = itemId,
                            is_draft = isDraft,
                            lattitude = latitude,
                            longitude = longitude
                        )
                    )
                } else {
                    showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
                }
            }
        }
        else {
            showLocationAlertDialog()
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(requireContext(), selectedItem, position)
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
                table_name = getString(R.string.implementing_agency_document).toRequestBody(MultipartBody.FORM),
            )
        }
    }


}