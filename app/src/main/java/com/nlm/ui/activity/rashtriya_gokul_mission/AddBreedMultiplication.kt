package com.nlm.ui.activity.rashtriya_gokul_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.ActivityAddBreedMultiplicationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.ui.fragment.BreedMultiplicationNDDBFragment
import com.nlm.ui.fragment.BreedMultiplicationNLMFragment
import com.nlm.ui.fragment.FundsReceivedRGMFragment
import com.nlm.ui.fragment.ImplementationOfNAIPRGMFragment
import com.nlm.ui.fragment.RGMCompositionOfGoverningFragment
import com.nlm.ui.fragment.RGMDetailsOfCommitteeMeetings
import com.nlm.ui.fragment.RGMIAOtherStaffFragment
import com.nlm.ui.fragment.RGMStateImplementingAgencyNLMFragment
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
import okhttp3.MultipartBody
import java.io.File

class AddBreedMultiplication : BaseActivity<ActivityAddBreedMultiplicationBinding>(),
    CallBackDeleteAtId,
    CallBackItemUploadDocEdit
{
    private var mBinding: ActivityAddBreedMultiplicationBinding? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var DialogDocName: TextView? = null
    private var DocumentId: Int? = null
    private var uploadData : ImageView?=null
    private var UploadedDocumentName: String? = null
    private var TableName: String? = null
    private var DocumentName: String? = null
    var body: MultipartBody.Part? = null

    override val layoutId: Int
        get() = R.layout.activity_add_breed_multiplication

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.extras?.getInt("itemId")
        DocumentList= arrayListOf()
        AddDocumentAdapter = SupportingDocumentAdapterWithDialog(
           this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView1?.adapter = AddDocumentAdapter
        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(this)

    }
    override fun setVariables() {
    }

    override fun setObservers() {
    }


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun addDocDialog(view: View) {
            AddDocumentDialog(this@AddBreedMultiplication, null, null)
        }
        fun SaveAndNext(view: View) {


        }
        fun SaveAsDraft(view: View) {

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
                        val url=
                            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
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


    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }}



    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(this, selectedItem, position)
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
//                                    uploadImage(it) // Proceed to upload
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
                        uploadData?.showView()
                        uploadData?.setImageResource(R.drawable.ic_pdf)

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
                                    DocumentName = documentName
                                    val requestBody = convertToRequestBody(this, uri)
                                    body = MultipartBody.Part.createFormData(
                                        "document_name",
                                        documentName,
                                        requestBody
                                    )
//                                    viewModel.getProfileUploadFile(
//                                        context = requireContext(),
//                                        document_name = body,
//                                        user_id = getPreferenceOfScheme(
//                                            requireContext(),
//                                            AppConstants.SCHEME,
//                                            Result::class.java
//                                        )?.user_id,
//                                        table_name = getString(R.string.implementing_agency_document).toRequestBody(
//                                            MultipartBody.FORM
//                                        ),
//                                    )
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
    override fun showImage(bitmap: Bitmap) {
        // Override to display the image in this activity
        uploadData?.showView()
        uploadData?.setImageBitmap(bitmap)
        val imageFile = saveImageToFile(bitmap)
        photoFile = imageFile
//        photoFile?.let { uploadImage(it) }
    }




}