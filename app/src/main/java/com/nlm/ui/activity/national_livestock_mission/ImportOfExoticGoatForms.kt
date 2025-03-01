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
import com.nlm.callBack.CallBackItemImportExoticAchivementEdit
import com.nlm.callBack.CallBackItemImportExoticDetailtEdit
import com.nlm.callBack.CallBackItemImportExoticVerifiedByNlm
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityImportOfExoticGoatBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.databinding.ItemImportExoticVerifiedNlmBinding
import com.nlm.databinding.ItemImportOfExoticGoatAchievementBinding
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.GetDropDownRequest
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.ImportExoticAchivementAdapter
import com.nlm.ui.adapter.ImportExoticAdapterDetailOfImport
import com.nlm.ui.adapter.ImportOfExoticGoatVerifiedNlmAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterViewOnly
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.URIPathHelper
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.formatFileSize
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

class ImportOfExoticGoatForms : BaseActivity<ActivityImportOfExoticGoatBinding>(),
    CallBackItemImportExoticAchivementEdit,
    CallBackItemImportExoticDetailtEdit, CallBackDeleteAtId, CallBackItemUploadDocEdit,
    CallBackItemImportExoticVerifiedByNlm {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat
    private var mBinding: ActivityImportOfExoticGoatBinding? = null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private var ViewDocumentAdapter: SupportingDocumentAdapterViewOnly? = null
    private var DetailOfImportAdapter: ImportExoticAdapterDetailOfImport? = null
    private var AchievementAdapter: ImportExoticAchivementAdapter? = null
    private var VerifiedNlmAdapter: ImportOfExoticGoatVerifiedNlmAdapter? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var TotalDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: MutableList<ImplementingAgencyDocument>
    private var DetailOfImportList: MutableList<ImportOfExoticGoatDetailImport>? = null
    private var AchievementList: MutableList<ImportOfExoticGoatAchievement>? = null
    private var VerifiedNlmList: MutableList<ImportOfExoticGoatVerifiedNlm>? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var DocumentName: String? = null
    private var TableName: String? = null
    private var savedAsDraft: Boolean = false
    private var formId: Int? = null
    private var currentPage = 1
    private var totalPage = 1
    private var layoutManager: LinearLayoutManager? = null
    private var stateList = ArrayList<ResultGetDropDown>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var DocumentId: Int? = null
    private var DialogDocName: TextView? = null
    private var UploadedDocumentName: String? = null
    var body: MultipartBody.Part? = null
    val viewModel = ViewModel()
    var selectedValue: Int = 1
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var Model: String? = null // Store selected state
    private var loading = true
    private var uploadData: ImageView? = null
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

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        DocumentList = arrayListOf()
        viewDocumentList = mutableListOf()
        DetailOfImportList = mutableListOf()
        AchievementList = mutableListOf()
        VerifiedNlmList = mutableListOf()
        TotalDocumentList = arrayListOf()
        ViewDocumentAdapter()
        AddDocumentAdapter()
        AddImportDetailAdapter()
        AddAcheivementAdapter()
        AddVerifiedAdapter()

        mBinding?.etStateIA?.text =
            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.etStateIA?.isEnabled = false
        mBinding?.etStateNlm?.text =
            getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.etStateNlm?.isEnabled = false
        mBinding?.rbMentally?.setOnCheckedChangeListener { _, checkedId ->
            selectedValue = when (checkedId) {
                R.id.rbMentallyYes -> 1 // If "Yes" is selected, set variable to 1
                R.id.rbMentallyNo -> 0  // If "No" is selected, set variable to 0
                else -> selectedValue    // Keep the existing value if neither is selected (unlikely)
            }
        }
        if (getPreferenceOfScheme(
                this@ImportOfExoticGoatForms,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {
            mBinding?.llNLM?.hideView()
            mBinding?.llSDRv?.hideView()
            mBinding?.tvSupportingDocumentView?.hideView()

        } else {
            mBinding?.tvAddDetail?.hideView()
            mBinding?.tvAddDetail?.hideView()
            mBinding?.tvAddAcheivement?.hideView()
            mBinding?.tvStateIA?.hideView()
            mBinding?.etStateIA?.hideView()
//            ViewEditApi("view")
        }
        if (viewEdit == "view") {
            mBinding?.rlWelcome?.hideView()
            mBinding?.etNoOfFarmer?.isEnabled = false
            mBinding?.tvStateIA?.hideView()
            mBinding?.etStateIA?.hideView()
            mBinding?.tvStateNLm?.hideView()
            mBinding?.etStateNlm?.hideView()
            mBinding?.rbMentally?.isEnabled = false
            mBinding?.rbMentallyYes?.isEnabled = false
            mBinding?.rbMentallyNo?.isEnabled = false
            mBinding?.tvAddDetail?.hideView()
            mBinding?.tvAddVerified?.hideView()
            mBinding?.tvAddAcheivement?.hideView()
            mBinding?.tvAddDocs?.hideView()
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            ViewEditApi(viewEdit)
        }
        if (viewEdit == "edit") {

            ViewEditApi(viewEdit)
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.importExoticGoatAddEditResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                } else {
                    TableName=userResponseModel.fileurl
                    if (viewEdit == "view") {
                        userResponseModel._result.number_of_farmers_benefited?.let { it1 ->
                            mBinding?.etNoOfFarmer?.setText(it1.toString())
                        } ?: mBinding?.etNoOfFarmer?.setText("")
                        formId = userResponseModel._result.id
                        viewDocumentList.clear()
                        DocumentList.clear()

                        userResponseModel._result.import_of_exotic_goat_document?.forEach { document ->

                            if (document.nlm_document == null) {
                                if (getPreferenceOfScheme(
                                        this@ImportOfExoticGoatForms,
                                        AppConstants.SCHEME,
                                        Result::class.java
                                    )?.role_id == 24
                                ) {

                                    DocumentList.add(document)
                                } else {
                                    viewDocumentList.add(document)
                                }
                            } else  if (getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id == 8
                            ) {

                                DocumentList.add(document)
                            }
                        }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()
                        AchievementList?.clear()
                        VerifiedNlmList?.clear()
                        DetailOfImportList?.clear()
                        if (userResponseModel._result.import_of_exotic_goat_achievement.isNullOrEmpty()) {
                            // Add an item with empty fields if data is null or empty
                            AchievementList?.add(
                                ImportOfExoticGoatAchievement(
                                    number_of_animals = null,
                                    f1_generation_produced = "",
                                    f2_generation_produced = "",
                                    no_of_animals_f1 = null,
                                    no_of_animals_f2 = null,
                                    balance = "",
                                    performance_animals_doorstep = "",
                                    import_of_exotic_goat_id = null,
                                    id = null,
                                )
                            )
                        } else {
                            AchievementList?.addAll(userResponseModel._result.import_of_exotic_goat_achievement)
                        }

// Add data to VerifiedNlmList if it's null or empty
                        if (userResponseModel._result.import_of_exotic_goat_verified_nlm.isNullOrEmpty()) {
                            // Add an item with empty fields if data is null or empty
                            VerifiedNlmList?.add(
                                ImportOfExoticGoatVerifiedNlm(
                                    number_of_animals = null,
                                    f1_generation_produced = "",
                                    f2_generation_produced = "",
                                    import_of_exotic_goat_id = null,
                                    id = null,
                                    species_breed = "",
                                    year = null,
                                    f2_generation_distributed = "",
                                )
                            )
                        } else {
                            VerifiedNlmList?.addAll(userResponseModel._result.import_of_exotic_goat_verified_nlm)
                        }

                        if (userResponseModel._result.import_of_exotic_goat_detail_import.isNullOrEmpty()) {
                            // Add an item with empty fields if data is null or empty
                            DetailOfImportList?.add(
                                ImportOfExoticGoatDetailImport(
                                    import_of_exotic_goat_id = null,
                                    id = null,
                                    species_breed = "",
                                    year = null,
                                    procurement_cost = null,
                                    place_of_procurement = "",
                                    place_of_induction = "",
                                    unit = ""
                                )
                            )
                        } else {
                            DetailOfImportList?.addAll(userResponseModel._result.import_of_exotic_goat_detail_import)
                        }
                        AchievementAdapter?.notifyDataSetChanged()
                        DetailOfImportAdapter?.notifyDataSetChanged()
                        VerifiedNlmAdapter?.notifyDataSetChanged()


                        if (userResponseModel._result.comment_by_nlm_whether == 1) {
                            mBinding?.rbMentallyYes?.isChecked = true
                        } else {
                            mBinding?.rbMentallyNo?.isChecked = true
                        }
                    } else if (viewEdit == "edit") {
                        viewDocumentList.clear()
                        DocumentList.clear()
                        userResponseModel._result.import_of_exotic_goat_document?.forEach { document ->
                            if (document.nlm_document == null) {
                                if (getPreferenceOfScheme(
                                        this@ImportOfExoticGoatForms,
                                        AppConstants.SCHEME,
                                        Result::class.java
                                    )?.role_id == 24
                                ) {
                                    DocumentList.add(document)
                                } else {
                                    viewDocumentList.add(document)
                                }
                            } else {
                                if (getPreferenceOfScheme(
                                        this@ImportOfExoticGoatForms,
                                        AppConstants.SCHEME,
                                        Result::class.java
                                    )?.role_id == 8
                                ) {

                                    DocumentList.add(document)
                                }
                            }
                        }
                        AddDocumentAdapter?.notifyDataSetChanged()
                        ViewDocumentAdapter?.notifyDataSetChanged()
                        formId = userResponseModel._result.id
                        AchievementList?.clear()
                        VerifiedNlmList?.clear()
                        DetailOfImportList?.clear()
                        userResponseModel._result.import_of_exotic_goat_achievement?.let { it1 ->
                            AchievementList?.addAll(
                                it1
                            )
                        }
                        userResponseModel._result.import_of_exotic_goat_verified_nlm?.let { it1 ->
                            VerifiedNlmList?.addAll(
                                it1
                            )
                        }
                        userResponseModel._result.import_of_exotic_goat_detail_import?.let { it1 ->
                            DetailOfImportList?.addAll(
                                it1
                            )
                        }
                        AchievementAdapter?.notifyDataSetChanged()
                        DetailOfImportAdapter?.notifyDataSetChanged()
                        VerifiedNlmAdapter?.notifyDataSetChanged()
                        if (userResponseModel._result.comment_by_nlm_whether == 1) {
                            mBinding?.rbMentallyYes?.isChecked = true
                        } else {
                            mBinding?.rbMentallyNo?.isChecked = true
                        }
                        mBinding?.etNoOfFarmer?.setText(userResponseModel._result.number_of_farmers_benefited)
                    } else {
                        showSnackbar(mBinding!!.main, userResponseModel.message)
                    }
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                        showSnackbar(mBinding!!.main, userResponseModel.message)
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

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun saveAndNext(view: View) {
            TotalDocumentList.clear()
            TotalDocumentList.addAll(DocumentList)
            TotalDocumentList.addAll(viewDocumentList)
        Log.d("DOCUMENT",TotalDocumentList.toString())
            Log.d("DOCUMENT2",viewDocumentList.toString())
            Log.d("DOCUMENT3",DocumentList.toString())

            if (viewEdit == "view") {
                onBackPressedDispatcher.onBackPressed()
            } else {

                saveDataApi(0)
            }
            savedAsDraft = true
        }

        fun saveAsDraft(view: View) {
            TotalDocumentList.clear()
            TotalDocumentList.addAll(DocumentList)
            TotalDocumentList.addAll(viewDocumentList)
            Log.d("DOCUMENT",TotalDocumentList.toString())
            Log.d("DOCUMENT2",viewDocumentList.toString())
            Log.d("DOCUMENT3",DocumentList.toString())
            if (viewEdit == "view") {
                onBackPressedDispatcher.onBackPressed()
            } else {
                saveDataApi(1)
            }
            savedAsDraft = true
        }

        fun addDocument(view: View) {
            AddDocumentDialog(this@ImportOfExoticGoatForms, null, null)
        }

        fun addDetailOfImport(view: View) {
            AddImportDetailDialog(this@ImportOfExoticGoatForms, null, null)
        }

        fun addAchievement(view: View) {
            AddAchievementDialog(this@ImportOfExoticGoatForms, null, null)
        }

        fun addVerifiedNlm(view: View) {
            if (VerifiedNlmList?.size!! >= 5) {
                mBinding?.main?.let { it1 -> showSnackbar(it1, "Only 5 items can be added") }
            } else {
                AddVerifiedNlmDialog(this@ImportOfExoticGoatForms, null, null)
            }
        }
    }

    private fun AddDocumentAdapter() {
        AddDocumentAdapter =
            SupportingDocumentAdapterWithDialog(this, DocumentList, viewEdit, this, this)
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun ViewDocumentAdapter() {
        ViewDocumentAdapter = SupportingDocumentAdapterViewOnly(this,viewDocumentList, this)
        mBinding?.ShowDocumentRv?.adapter = ViewDocumentAdapter
        mBinding?.ShowDocumentRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun AddImportDetailAdapter() {
        DetailOfImportAdapter = DetailOfImportList?.let {
            ImportExoticAdapterDetailOfImport(
                this,
                it,
                viewEdit,
                this,
                this
            )
        }
        mBinding?.DetailImportRv?.adapter = DetailOfImportAdapter
        mBinding?.DetailImportRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun AddAcheivementAdapter() {
        AchievementAdapter =
            AchievementList?.let { ImportExoticAchivementAdapter(this, it, viewEdit, this, this) }
        mBinding?.AchivementRv?.adapter = AchievementAdapter
        mBinding?.AchivementRv?.layoutManager = LinearLayoutManager(this)
    }

    private fun AddVerifiedAdapter() {
        VerifiedNlmAdapter = VerifiedNlmList?.let {
            ImportOfExoticGoatVerifiedNlmAdapter(
                this,
                it,
                this,
                viewEdit,
                this
            )
        }
        mBinding?.VerifiedNlmRv?.adapter = VerifiedNlmAdapter
        mBinding?.VerifiedNlmRv?.layoutManager = LinearLayoutManager(this)
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
                                mBinding?.let { it1 -> showSnackbar(it1.main,"Download started") }
                                dialog.dismiss()
                            }
                            else{
                                mBinding?.let { it1 -> showSnackbar(it1.main,"No document found") }
                                dialog.dismiss()
                            }
                        }

                    }

                    "png" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }

                    "jpg" -> {
                        bindingDialog.ivPic.let {
                            Glide.with(context).load(url).placeholder(R.drawable.ic_image_placeholder).into(
                                it
                            )
                        }
                    }
                }
            }
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                checkStoragePermission(this@ImportOfExoticGoatForms)
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
                                import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
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
                                DocumentList.set(
                                    position, ImplementingAgencyDocument(
                                        description = bindingDialog.etDescription.text.toString(),
                                        ia_document = null,
                                        nlm_document = UploadedDocumentName,
                                        import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
                                        id = selectedItem.id,
                                    )
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

    private fun AddImportDetailDialog(
        context: Context,
        selectedItem: ImportOfExoticGoatDetailImport?,
        position: Int?
    ) {
        val bindingDialog: ItemImportExoticGermplasmBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_import_exotic_germplasm,
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
        if (selectedItem != null) {
            bindingDialog.etSpeciesBreed.setText(selectedItem.species_breed)
            bindingDialog.etUnit.setText(selectedItem.unit)
            bindingDialog.etYear.setText(selectedItem.year.toString())
            bindingDialog.etProcurementCost.setText(selectedItem.procurement_cost.toString())
            bindingDialog.etPlaceOfProcurement.setText(selectedItem.place_of_procurement)
            bindingDialog.etPlaceOfInduction.setText(selectedItem.place_of_induction)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etSpeciesBreed.text.toString()
                    .isNotEmpty() || bindingDialog.etUnit.text.toString()
                    .isNotEmpty() || bindingDialog.etYear.text.toString()
                    .isNotEmpty() || bindingDialog.etProcurementCost.text.toString()
                    .isNotEmpty() || bindingDialog.etPlaceOfProcurement.text.toString()
                    .isNotEmpty() || bindingDialog.etPlaceOfInduction.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        DetailOfImportList?.set(
                            position, ImportOfExoticGoatDetailImport(
                                species_breed = bindingDialog.etSpeciesBreed.text.toString(),
                                unit = bindingDialog.etUnit.text.toString(),
                                year = bindingDialog.etYear.text.toString(),
                                procurement_cost = bindingDialog.etProcurementCost.text.toString()
                                    .toIntOrNull(),
                                place_of_procurement = bindingDialog.etPlaceOfProcurement.text.toString(),
                                place_of_induction = bindingDialog.etPlaceOfInduction.text.toString(),
                                import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
                                id = selectedItem.id,
                            )
                        )
                        DetailOfImportAdapter?.notifyItemChanged(position)
                    }
                } else {
                    DetailOfImportList?.add(
                        ImportOfExoticGoatDetailImport(
                            species_breed = bindingDialog.etSpeciesBreed.text.toString(),
                            unit = bindingDialog.etUnit.text.toString(),
                            year = bindingDialog.etYear.text.toString(),
                            procurement_cost = bindingDialog.etProcurementCost.text.toString()
                                .toIntOrNull(),
                            place_of_procurement = bindingDialog.etPlaceOfProcurement.text.toString(),
                            place_of_induction = bindingDialog.etPlaceOfInduction.text.toString(),
                            import_of_exotic_goat_id = null,
                            id = null,
                        )
                    )
                    DetailOfImportList?.size?.minus(1).let {
                        if (it != null) {
                            DetailOfImportAdapter?.notifyItemInserted(it)
                        }
                    }

                }
                dialog.dismiss()
            } else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }

    private fun AddAchievementDialog(
        context: Context,
        selectedItem: ImportOfExoticGoatAchievement?,
        position: Int?
    ) {
        val bindingDialog: ItemImportOfExoticGoatAchievementBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_import_of_exotic_goat_achievement,
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
        if (selectedItem != null) {
            selectedItem.number_of_animals?.let { bindingDialog.etNoOfAnimals.setText(it.toString()) }
                ?: bindingDialog.etNoOfAnimals.setText("")
            bindingDialog.etF1Generation.setText(selectedItem.f1_generation_produced)
            bindingDialog.etF2Generation.setText(selectedItem.f2_generation_produced)
            selectedItem.no_of_animals_f1?.let { bindingDialog.etNoOfAnimalsDistributedF1.setText(it.toString()) }
                ?: bindingDialog.etNoOfAnimalsDistributedF1.setText("")
            selectedItem.no_of_animals_f2?.let { bindingDialog.etNoOfAnimalsDistributedF2.setText(it.toString()) }
                ?: bindingDialog.etNoOfAnimalsDistributedF2.setText("")
            bindingDialog.etBalance.setText(selectedItem.balance)
            bindingDialog.etPerformanceOfTheAnimals.setText(selectedItem.performance_animals_doorstep)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNoOfAnimals.text.toString()
                    .isNotEmpty() || bindingDialog.etF1Generation.text.toString()
                    .isNotEmpty() || bindingDialog.etF2Generation.text.toString()
                    .isNotEmpty() || bindingDialog.etNoOfAnimalsDistributedF1.text.toString()
                    .isNotEmpty() || bindingDialog.etNoOfAnimalsDistributedF2.text.toString()
                    .isNotEmpty() || bindingDialog.etPerformanceOfTheAnimals.text.toString()
                    .isNotEmpty() || bindingDialog.etBalance.text.toString().isNotEmpty()
            ) {
                    if (selectedItem != null) {
                        if (position != null) {

                        AchievementList?.set(
                            position, ImportOfExoticGoatAchievement(
                                number_of_animals = bindingDialog.etNoOfAnimals.text.toString()
                                    .toIntOrNull(),
                                f1_generation_produced = bindingDialog.etF1Generation.text.toString(),
                                f2_generation_produced = bindingDialog.etF2Generation.text.toString(),
                                no_of_animals_f1 = bindingDialog.etNoOfAnimalsDistributedF1.text.toString()
                                    .toIntOrNull(),
                                no_of_animals_f2 = bindingDialog.etNoOfAnimalsDistributedF2.text.toString()
                                    .toIntOrNull(),
                                balance = bindingDialog.etBalance.text.toString(),
                                performance_animals_doorstep = bindingDialog.etPerformanceOfTheAnimals.text.toString(),
                                import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
                                id = selectedItem.id,
                            )
                        )
                        AchievementAdapter?.notifyItemChanged(position)
                    }
                } else {
                    AchievementList?.add(
                        ImportOfExoticGoatAchievement(
                            number_of_animals = bindingDialog.etNoOfAnimals.text.toString()
                                .toIntOrNull(),
                            f1_generation_produced = bindingDialog.etF1Generation.text.toString(),
                            f2_generation_produced = bindingDialog.etF2Generation.text.toString(),
                            no_of_animals_f1 = bindingDialog.etNoOfAnimalsDistributedF1.text.toString()
                                .toIntOrNull(),
                            no_of_animals_f2 = bindingDialog.etNoOfAnimalsDistributedF2.text.toString()
                                .toIntOrNull(),
                            balance = bindingDialog.etBalance.text.toString(),
                            performance_animals_doorstep = bindingDialog.etPerformanceOfTheAnimals.text.toString(),
                            import_of_exotic_goat_id = null,
                            id = null,
                        )
                    )
                    AchievementList?.size?.minus(1).let {
                        if (it != null) {
                            AchievementAdapter?.notifyItemInserted(it)
                        }
                    }
                }
                dialog.dismiss()
            } else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }

    private fun AddVerifiedNlmDialog(
        context: Context,
        selectedItem: ImportOfExoticGoatVerifiedNlm?,
        position: Int?
    ) {
        val bindingDialog: ItemImportExoticVerifiedNlmBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_import_exotic_verified_nlm,
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
        if (selectedItem != null) {
            selectedItem.number_of_animals?.let { bindingDialog.etF2GenerationDistributed.setText(it.toString()) }
                ?: bindingDialog.etF2GenerationDistributed.setText("")
            bindingDialog.etF1GenerationProduced.setText(selectedItem.f1_generation_produced)
            bindingDialog.etF2GenerationProduced.setText(selectedItem.f2_generation_produced)
            selectedItem.f2_generation_distributed?.let {
                bindingDialog.etF1GenerationDistributed.setText(
                    it.toString()
                )
            } ?: bindingDialog.etF1GenerationDistributed.setText("")
            bindingDialog.etYear.setText(selectedItem.year)
            bindingDialog.etSpeciesBreed.setText(selectedItem.species_breed)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etSpeciesBreed.text.toString()
                    .isNotEmpty() || bindingDialog.etYear.text.toString()
                    .isNotEmpty() || bindingDialog.etF1GenerationProduced.text.toString()
                    .isNotEmpty() || bindingDialog.etF2GenerationProduced.text.toString()
                    .isNotEmpty() || bindingDialog.etF2GenerationDistributed.text.toString()
                    .isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        VerifiedNlmList?.set(
                            position, ImportOfExoticGoatVerifiedNlm(
                                number_of_animals = bindingDialog.etF2GenerationDistributed.text.toString()
                                    .toIntOrNull(),
                                f1_generation_produced = bindingDialog.etF1GenerationProduced.text.toString(),
                                f2_generation_produced = bindingDialog.etF2GenerationProduced.text.toString(),
                                f2_generation_distributed = bindingDialog.etF1GenerationDistributed.text.toString(),
                                year = bindingDialog.etYear.text.toString(),
                                import_of_exotic_goat_id = selectedItem.import_of_exotic_goat_id,
                                id = selectedItem.id,
                                species_breed = bindingDialog.etSpeciesBreed.text.toString(),
                            )
                        )

                        VerifiedNlmAdapter?.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                } else {

                    VerifiedNlmList?.add(
                        ImportOfExoticGoatVerifiedNlm(
                            number_of_animals = bindingDialog.etF2GenerationDistributed.text.toString()
                                .toIntOrNull(),
                            f1_generation_produced = bindingDialog.etF1GenerationProduced.text.toString(),
                            f2_generation_produced = bindingDialog.etF2GenerationProduced.text.toString(),
                            f2_generation_distributed = bindingDialog.etF1GenerationDistributed.text.toString(),
                            import_of_exotic_goat_id = null,
                            year = bindingDialog.etYear.text.toString(),
                            id = null,
                            species_breed = bindingDialog.etSpeciesBreed.text.toString(),
                        )
                    )
                    VerifiedNlmList?.size?.minus(1).let {
                        if (it != null) {
                            VerifiedNlmAdapter?.notifyItemInserted(it)
                        }

                        dialog.dismiss()

                    }
                }
            } else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
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
                                        table_name = getString(R.string.import_of_exotic_goat).toRequestBody(
                                            MultipartBody.FORM
                                        ),
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

    private fun ViewEditApi(viewEdit: String?) {
        viewModel.getImportExoticGoatAdd(
            this@ImportOfExoticGoatForms, true,
            ImportExoticGoatAddEditRequest(
                comment_by_nlm_whether = null,
                import_of_exotic_goat_detail_import = null,
                import_of_exotic_goat_achievement = null,
                import_of_exotic_goat_verified_nlm = null,
                state_code = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit,
                id = itemId,
                is_draft = null,
                import_of_exotic_goat_document = null,
                is_deleted = null,
                number_of_farmers_benefited = null
            )
        )
    }

    private fun saveDataApi(isDraft: Int?) {
        if (hasLocationPermissions()) {
            val intent = Intent(this@ImportOfExoticGoatForms, LocationService::class.java)
            startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {
                    if (getPreferenceOfScheme(
                            this@ImportOfExoticGoatForms,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 24
                    ) {
                        viewModel.getImportExoticGoatAdd(
                            this@ImportOfExoticGoatForms, true,
                            ImportExoticGoatAddEditRequest(
                                comment_by_nlm_whether = selectedValue,
                                import_of_exotic_goat_detail_import = DetailOfImportList,
                                import_of_exotic_goat_achievement = AchievementList,
                                import_of_exotic_goat_verified_nlm = VerifiedNlmList,
                                state_code = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.state_code,
                                user_id = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id.toString(),
                                role_id = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                is_type = null,
                                id = formId,
                                is_draft = isDraft,
                                import_of_exotic_goat_document = TotalDocumentList,
                                is_deleted = null,
                                number_of_farmers_benefited = mBinding?.etNoOfFarmer?.text.toString()
                                    .toIntOrNull(),
                                lattitude_ia = latitude,
                                longitude_ia = longitude
                            )
                        )
                    } else {
                        viewModel.getImportExoticGoatAdd(
                            this@ImportOfExoticGoatForms, true,
                            ImportExoticGoatAddEditRequest(
                                comment_by_nlm_whether = selectedValue,
                                import_of_exotic_goat_detail_import = DetailOfImportList,
                                import_of_exotic_goat_achievement = AchievementList,
                                import_of_exotic_goat_verified_nlm = VerifiedNlmList,
                                state_code = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.state_code,
                                user_id = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id.toString(),
                                role_id = getPreferenceOfScheme(
                                    this@ImportOfExoticGoatForms,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.role_id,
                                is_type = null,
                                id = formId,
                                is_draft = isDraft,
                                import_of_exotic_goat_document = TotalDocumentList,
                                is_deleted = null,
                                number_of_farmers_benefited = mBinding?.etNoOfFarmer?.text.toString()
                                    .toIntOrNull(),
                                lattitude_nlm = latitude,
                                longitude_nlm = longitude
                            )
                        )
                    }
                }
                else {
                    showSnackbar(mBinding?.main!!, "Please wait for a sec and click again")
                }
            }
        } else {
            showLocationAlertDialog()
        }
    }

    override fun onClickItem(
        selectedItem: ImportOfExoticGoatAchievement,
        position: Int,
        isFrom: Int
    ) {
        AddAchievementDialog(this, selectedItem, position)
    }

    private fun dropDownApiCallDistrict(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                100,
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

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {

        if (paginate) {
            currentPage++
        }
        if (Model == "Districts") {
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    20,
                    Model,
                    currentPage,
                    state_code = stateId,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        } else {
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
                Model = "State"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.etStateIA!!
            }

            "StateNDD" -> {
                selectedList = stateList
                selectedTextView = mBinding?.etStateIA!!
            }

            "District" -> {
                dropDownApiCallDistrict(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                Model = "Districts"
                selectedTextView = mBinding?.etStateIA!!
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

            if (Model == "Districts") {
                districtName = selectedItem
                districtId = id
            } else {
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

    override fun onClickItemDetail(
        selectedItem: ImportOfExoticGoatDetailImport,
        position: Int,
        isFrom: Int
    ) {
        AddImportDetailDialog(this, selectedItem, position)
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom == 1) {
            position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
        } else if (isFrom == 2) {
            position.let { it1 -> DetailOfImportAdapter?.onDeleteButtonClick(it1) }
        } else if (isFrom == 3) {
            position.let { it1 -> AchievementAdapter?.onDeleteButtonClick(it1) }
        } else if (isFrom == 4) {
            position.let { it1 -> VerifiedNlmAdapter?.onDeleteButtonClick(it1) }
        }

    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(this, selectedItem, position)
    }

    override fun onClickItem(
        selectedItem: ImportOfExoticGoatVerifiedNlm,
        position: Int,
        isFrom: Int
    ) {
        AddVerifiedNlmDialog(this, selectedItem, position)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) { // API level 26
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
                context = this@ImportOfExoticGoatForms,
                document_name = body,
                user_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                table_name = getString(R.string.import_of_exotic_goat).toRequestBody(
                    MultipartBody.FORM
                ),
            )
        }
    }
}