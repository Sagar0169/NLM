package com.nlm.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseAi
import com.nlm.callBack.CallBackItemTypeRGMStateIAAnyOfTheAssetEdit
import com.nlm.callBack.CallBackItemTypeRGMStateIANumberOfFertilityCampsEdit
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.AnyOfTheAssetsCreatedBinding
import com.nlm.databinding.FragmentRgmStateImplementingAgencyNlmBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.NumberOfFertilityCampsItemsBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyAgencyWiseCalfBorn
import com.nlm.model.RgmImplementingAgencyAnyOfTheAsset
import com.nlm.model.RgmImplementingAgencyNumberOfFertility
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.ui.adapter.rgm.AgencyWiseAIAdapterRGM
import com.nlm.ui.adapter.rgm.AgencyWiseAdapter
import com.nlm.ui.adapter.rgm.AgencyWiseCalfAdapterRGM
import com.nlm.ui.adapter.rgm.RGMAnyOfTheAssetAdapter
import com.nlm.ui.adapter.rgm.RGMNumberOfFertilityCampsAdapter
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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RGMStateImplementingAgencyNLMFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentRgmStateImplementingAgencyNlmBinding>(),
    CallBackDeleteAtId, CallBackItemUploadDocEdit, CallBackItemTypeRGMStateIAAnyOfTheAssetEdit,
    CallBackItemTypeRGMStateIANumberOfFertilityCampsEdit {
    private var mBinding: FragmentRgmStateImplementingAgencyNlmBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment__rgm_state_implementing_agency_nlm
    private var DocumentName: String? = null
    var body: MultipartBody.Part? = null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var DialogDocName: TextView? = null
    private var DocumentId: Int? = null
    private var uploadData : ImageView?=null
    private var UploadedDocumentName: String? = null
    private var TableName: String? = null
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private lateinit var NumberOfFertilityCamps:RGMNumberOfFertilityCampsAdapter
    private lateinit var AnyOfTheAssetAdapter: RGMAnyOfTheAssetAdapter
    private lateinit var AnyOfTheAssetList: MutableList<RgmImplementingAgencyAnyOfTheAsset>
    private lateinit var NumberOfFertilityList: MutableList<RgmImplementingAgencyNumberOfFertility>
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList= arrayListOf()
        AddDocumentAdapter = SupportingDocumentAdapterWithDialog(
            requireContext(),
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView1?.adapter = AddDocumentAdapter
        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())
        AnyOfTheAssetAdapter()
        NumberOfFertilityCamps()
    }
    private fun AnyOfTheAssetAdapter() {
        AnyOfTheAssetList = mutableListOf()
        AnyOfTheAssetAdapter =
            RGMAnyOfTheAssetAdapter(requireActivity(),AnyOfTheAssetList,viewEdit,this,this)
        mBinding?.rvAnyOfAssets?.adapter = AnyOfTheAssetAdapter
        mBinding?.rvAnyOfAssets?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    private fun NumberOfFertilityCamps() {
        NumberOfFertilityList = mutableListOf()
        NumberOfFertilityCamps =
            RGMNumberOfFertilityCampsAdapter(requireActivity(),NumberOfFertilityList,viewEdit,this,this)
        mBinding?.rvNumberOfFertitlity?.adapter = NumberOfFertilityCamps
        mBinding?.rvNumberOfFertitlity?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    override fun setVariables() {

    }

    override fun setObservers() {

    }
    fun AgencyAnyOfTheAssetDialog(context: Context, isFrom:Int, selectedItem: RgmImplementingAgencyAnyOfTheAsset?, position: Int?) {
        val bindingDialog: AnyOfTheAssetsCreatedBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.any_of_the_assets_created,
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
        Log.d("ISFROM",isFrom.toString())
        if(selectedItem!=null
//            && isFrom == 1
        )
        {

            bindingDialog.etAssets.setText(selectedItem.assets)
            bindingDialog.etReasons.setText(selectedItem.reasons)

        }
//        if(selectedItem!=null && isFrom == 2)
//        { bindingDialog.etAgency.setText(selectedItem.agency_name)
//            bindingDialog.et2022.setText(selectedItem.first_year)
//            bindingDialog.et2023.setText(selectedItem.secound_year)
//            bindingDialog.et2024.setText(selectedItem.third_year)
//        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etAssets.text.toString().isNotEmpty()||bindingDialog.etReasons.text.toString().isNotEmpty())
            {
                if(isFrom == 1) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            AnyOfTheAssetList[position] =
                                RgmImplementingAgencyAnyOfTheAsset(
                                    assets = bindingDialog.etAssets.text.toString(),
                                    reasons =  bindingDialog.etReasons.text.toString(),
                                    rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                                    id= selectedItem.id
                                )
                            AnyOfTheAssetAdapter.notifyItemChanged(position)
                        }

                    } else{
                        AnyOfTheAssetList.add(
                            RgmImplementingAgencyAnyOfTheAsset(
                                assets = bindingDialog.etAssets.text.toString(),
                                reasons =  bindingDialog.etReasons.text.toString(),
                                id=null,
                                rgm_implementing_agency_id =  null,
                            )
                        )
                        AnyOfTheAssetList.size.minus(1).let {
                            AnyOfTheAssetAdapter.notifyItemInserted(it)
                        }}
                    dialog.dismiss()
                }

            }

            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    fun NumberOfFertilityCampsDialog(context: Context, isFrom:Int, selectedItem: RgmImplementingAgencyNumberOfFertility?, position: Int?) {
        val bindingDialog: NumberOfFertilityCampsItemsBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.number_of_fertility_camps_items,
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
        Log.d("ISFROM",isFrom.toString())
        if(selectedItem!=null
//            && isFrom == 1
        )
        {

            selectedItem.no_of_fertility_camps.toString().let { bindingDialog.etNumberOfFertility.setText(it) }
            selectedItem.no_of_animals_treated.toString().let { bindingDialog.etNoOfAnimals.setText(it) }

        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNumberOfFertility.text.toString().isNotEmpty()||bindingDialog.etNoOfAnimals.text.toString().isNotEmpty())
            {
                if(isFrom == 1) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            NumberOfFertilityList[position] =
                                RgmImplementingAgencyNumberOfFertility(
                                    no_of_fertility_camps = bindingDialog.etNumberOfFertility.text.toString().toIntOrNull(),
                                    no_of_animals_treated =  bindingDialog.etNoOfAnimals.text.toString().toIntOrNull(),
                                    rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                                    id= selectedItem.id
                                )
                            NumberOfFertilityCamps.notifyItemChanged(position)
                        }

                    } else{
                        NumberOfFertilityList.add(
                            RgmImplementingAgencyNumberOfFertility(
                                no_of_fertility_camps = bindingDialog.etNumberOfFertility.text.toString().toIntOrNull(),
                                no_of_animals_treated =  bindingDialog.etNoOfAnimals.text.toString().toIntOrNull(),
                                id=null,
                                rgm_implementing_agency_id =  null,
                            )
                        )
                        NumberOfFertilityList.size.minus(1).let {
                            NumberOfFertilityCamps.notifyItemInserted(it)
                        }}
                    dialog.dismiss()
                }

            }

            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
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
                        val url=
                            getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.siteurl.plus(TableName).plus("/").plus(UploadedDocumentName)
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
    inner class ClickActions {
        fun addDocDialog(view: View) {
            AddDocumentDialog(requireContext(), null, null)
        }
        fun AgencyAnyOfTheAsset(view: View) {

            AgencyAnyOfTheAssetDialog(requireContext(),1,null,null)
        }
        fun NumberOfFertilityCamps(view: View) {

            NumberOfFertilityCampsDialog(requireContext(),1,null,null)
        }
        fun SaveAndNext(view: View) {
            listener?.onNextButtonClick()

        }
        fun SaveAsDraft(view: View) {
            savedAsDraftClick?.onSaveAsDraft()
        }
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
    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if(isFrom==1)
        {
        position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }}
        if(isFrom==2)
        {
        position.let { it1 -> AnyOfTheAssetAdapter.onDeleteButtonClick(it1) }}
        if(isFrom==3)
        {
        position.let { it1 -> NumberOfFertilityCamps.onDeleteButtonClick(it1) }}
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(requireContext(), selectedItem, position)
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
//                                    uploadImage(it) // Proceed to upload
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
                                    Toast.makeText(requireContext(), "File size exceeds 5 MB", Toast.LENGTH_LONG).show()
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

    override fun onClickItem(
        selectedItem: RgmImplementingAgencyAnyOfTheAsset,
        position: Int,
        isFrom: Int
    ) {
        AgencyAnyOfTheAssetDialog(requireContext(),isFrom,selectedItem,position)
    }

    override fun onClickItem(
        selectedItem: RgmImplementingAgencyNumberOfFertility,
        position: Int,
        isFrom: Int
    ) {
        NumberOfFertilityCampsDialog(requireContext(),isFrom,selectedItem,position)
    }
}