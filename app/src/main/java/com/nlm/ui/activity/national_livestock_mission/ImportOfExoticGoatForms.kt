package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImportOfExoticGoatBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemImportExoticGermplasmBinding
import com.nlm.databinding.ItemImportExoticVerifiedNlmBinding
import com.nlm.databinding.ItemImportOfExoticGoatAchievementBinding
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.model.Result
import com.nlm.ui.adapter.ImportExoticAchivementAdapter
import com.nlm.ui.adapter.ImportExoticAdapterDetailOfImport
import com.nlm.ui.adapter.ImportOfExoticGoatVerifiedNlmAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody

class ImportOfExoticGoatForms : BaseActivity<ActivityImportOfExoticGoatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat
    private var mBinding: ActivityImportOfExoticGoatBinding? = null
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private var DetailOfImportAdapter: ImportExoticAdapterDetailOfImport?=null
    private var AchievementAdapter: ImportExoticAchivementAdapter?=null
    private var VerifiedNlmAdapter: ImportOfExoticGoatVerifiedNlmAdapter?=null
    private lateinit var DocumentList: MutableList<ImplementingAgencyDocument>
    private  var DetailOfImportList: MutableList<ImportOfExoticGoatDetailImport>? =null
    private  var AchievementList: MutableList<ImportOfExoticGoatAchievement>?=null
    private  var VerifiedNlmList: MutableList<ImportOfExoticGoatVerifiedNlm>?=null
    private var DialogDocName: TextView?=null
    private var DocumentName:String?=null
    private var savedAsDraft:Boolean=false
    var body: MultipartBody.Part? = null
    val viewModel = ViewModel()
    var selectedValue: Int = 1
    private var viewEdit: String? = null
    private var itemId: Int? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId",0)
        DocumentList = mutableListOf()
        DetailOfImportList = mutableListOf()
        AchievementList = mutableListOf()
        VerifiedNlmList = mutableListOf()
        AddDocumentAdapter()
        AddImportDetailAdapter()
        AddAcheivementAdapter()
        AddVerifiedAdapter()
        mBinding?.rbMentally?.setOnCheckedChangeListener { _, checkedId ->
            selectedValue = when (checkedId) {
                R.id.rbMentallyYes -> 1 // If "Yes" is selected, set variable to 1
                R.id.rbMentallyNo -> 0  // If "No" is selected, set variable to 0
                else -> selectedValue    // Keep the existing value if neither is selected (unlikely)
            }
        }
        if(viewEdit=="view"){

//            mBinding?.etStateIa?.text= getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_name
//            mBinding?.etStateIa?.isEnabled=false
            mBinding?.rbMentally?.isEnabled=false
            mBinding?.rbMentallyYes?.isEnabled=false
            mBinding?.rbMentallyNo?.isEnabled=false
            ViewEditApi()
        }
        if(viewEdit=="edit"){
            ViewEditApi()
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.importExoticGoatAddEditResult.observe(this){
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
                    if (viewEdit=="view"||viewEdit=="edit")
                    {
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
                        if (userResponseModel._result.comment_by_nlm_whether==1)
                        {
                            mBinding?.rbMentallyYes?.isChecked=true
                        }
                        else{
                            mBinding?.rbMentallyNo?.isChecked=true
                        }
                    }
                    if (savedAsDraft)
                    {
                        onBackPressedDispatcher.onBackPressed()
                    }
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }




            }
        }
    }
    inner class ClickActions {

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun saveAndNext(view: View){
            saveDataApi()
        }
        fun saveAsDraft(view: View){
            saveDataApi()
            savedAsDraft=true
        }
        fun addDocument(view: View){
            AddDocumentDialog(this@ImportOfExoticGoatForms)
        }
        fun addDetailOfImport(view: View){
            AddImportDetailDialog(this@ImportOfExoticGoatForms)
        }
        fun addAchievement(view: View){
            AddAchievementDialog(this@ImportOfExoticGoatForms)
        }
        fun addVerifiedNlm(view: View){
            AddVerifiedNlmDialog(this@ImportOfExoticGoatForms)
        }
    }
    private fun AddDocumentAdapter(){
        AddDocumentAdapter= SupportingDocumentAdapterWithDialog(DocumentList,"viewEdit")
        mBinding?.AddDocumentRv?.adapter = AddDocumentAdapter
        mBinding?.AddDocumentRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun AddImportDetailAdapter(){
        DetailOfImportAdapter= DetailOfImportList?.let { ImportExoticAdapterDetailOfImport(it,viewEdit) }
        mBinding?.DetailImportRv?.adapter = DetailOfImportAdapter
        mBinding?.DetailImportRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun AddAcheivementAdapter(){
        AchievementAdapter= AchievementList?.let { ImportExoticAchivementAdapter(it,viewEdit) }
        mBinding?.AchivementRv?.adapter = AchievementAdapter
        mBinding?.AchivementRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun AddVerifiedAdapter(){
        VerifiedNlmAdapter= VerifiedNlmList?.let { ImportOfExoticGoatVerifiedNlmAdapter(it,viewEdit) }
        mBinding?.VerifiedNlmRv?.adapter = VerifiedNlmAdapter
        mBinding?.VerifiedNlmRv?.layoutManager = LinearLayoutManager(this)
    }
    private fun AddDocumentDialog(context: Context) {
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
        DialogDocName=bindingDialog.etDoc
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty())
            {

                DocumentList.add(ImplementingAgencyDocument(
                    description = bindingDialog.etDescription.text.toString(),
                    ia_document = DocumentName,
                    nlm_document = null,
                    implementing_agency_id = null,
                    id = null,
                ))

                DocumentList.size.minus(1).let {
                    AddDocumentAdapter?.notifyItemInserted(it)
                    dialog.dismiss()
//
                }
            }


            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    private fun AddImportDetailDialog(context: Context) {
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
        bindingDialog.tvSubmit.showView()
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etSpeciesBreed.text.toString().isNotEmpty()||bindingDialog.etUnit.text.toString().isNotEmpty()||bindingDialog.etYear.text.toString().isNotEmpty()||bindingDialog.etProcurementCost.text.toString().isNotEmpty()||bindingDialog.etPlaceOfProcurement.text.toString().isNotEmpty()||bindingDialog.etPlaceOfInduction.text.toString().isNotEmpty())
            {
                DetailOfImportList?.add(ImportOfExoticGoatDetailImport(
                    species_breed =bindingDialog.etSpeciesBreed.text.toString(),
                    unit = bindingDialog.etUnit.text.toString(),
                    year = bindingDialog.etYear.text.toString(),
                    procurement_cost = bindingDialog.etProcurementCost.text.toString().toInt(),
                    place_of_procurement = bindingDialog.etPlaceOfProcurement.text.toString(),
                    place_of_induction = bindingDialog.etPlaceOfInduction.text.toString(),
                    import_of_exotic_goat_id = null,
                    id = null,
                ))
                DetailOfImportList?.size?.minus(1).let {
                    if (it != null) {
                        DetailOfImportAdapter?.notifyItemInserted(it)
                    }
                    dialog.dismiss()
                }
            }


            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    private fun AddAchievementDialog(context: Context) {
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
        bindingDialog.tvSubmit.showView()
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNoOfAnimals.text.toString().isNotEmpty()||bindingDialog.etF1Generation.text.toString().isNotEmpty()||bindingDialog.etF2Generation.text.toString().isNotEmpty()||bindingDialog.etNoOfAnimalsDistributedF1.text.toString().isNotEmpty()||bindingDialog.etNoOfAnimalsDistributedF2.text.toString().isNotEmpty()||bindingDialog.etPerformanceOfTheAnimals.text.toString().isNotEmpty()||bindingDialog.etBalance.text.toString().isNotEmpty())
            {
                AchievementList?.add(ImportOfExoticGoatAchievement(
                 number_of_animals = bindingDialog.etNoOfAnimals.text.toString().toInt(),
                    f1_generation_produced = bindingDialog.etF1Generation.text.toString(),
                    f2_generation_produced = bindingDialog.etF2Generation.text.toString(),
                    no_of_animals_f1 = bindingDialog.etNoOfAnimalsDistributedF1.text.toString().toInt(),
                    no_of_animals_f2 = bindingDialog.etNoOfAnimalsDistributedF2.text.toString().toInt(),
                    balance = bindingDialog.etBalance.text.toString(),
                    performance_animals_doorstep = bindingDialog.etPerformanceOfTheAnimals.text.toString(),
                    import_of_exotic_goat_id = null,
                    id = null,
                ))
                AchievementList?.size?.minus(1).let {
                    if (it != null) {
                        AchievementAdapter?.notifyItemInserted(it)
                    }
                    dialog.dismiss()
                }
            }


            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }
    private fun AddVerifiedNlmDialog(context: Context) {
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
        bindingDialog.tvSubmit.showView()
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etSpeciesBreed.text.toString().isNotEmpty()||bindingDialog.etYear.text.toString().isNotEmpty()||bindingDialog.etF1GenerationProduced.text.toString().isNotEmpty()||bindingDialog.etF2GenerationProduced.text.toString().isNotEmpty()||bindingDialog.etF2GenerationDistributed.text.toString().isNotEmpty())
            {
                VerifiedNlmList?.add(ImportOfExoticGoatVerifiedNlm(
                    number_of_animals = bindingDialog.etF2GenerationDistributed.text.toString().toInt(),
                    f1_generation_produced = bindingDialog.etF1GenerationProduced.text.toString(),
                    f2_generation_produced = bindingDialog.etF2GenerationProduced.text.toString(),
                    f2_generation_distributed =bindingDialog.etF1GenerationDistributed.text.toString() ,
                    import_of_exotic_goat_id = null,
                    year = bindingDialog.etYear.text.toString(),
                    id = null,
                    species_breed = bindingDialog.etSpeciesBreed.text.toString(),
                ))
                VerifiedNlmList?.size?.minus(1).let {
                    if (it != null) {
                        VerifiedNlmAdapter?.notifyItemInserted(it)
                    }
                    dialog.dismiss()
                }
            }


            else {
                showSnackbar(mBinding!!.main, getString(R.string.please_enter_atleast_one_field))
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
                        val cursor = contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName=
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text=DocumentName

                                val requestBody = convertToRequestBody(this, uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
//                            viewModel.getProfileUploadFile(
//                                context = this,
//                                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
//                                table_name = getString(R.string.implementing_agency_document).toRequestBody(
//                                    MultipartBody.FORM),
//                                null,
//                                ia_document = body
//                            )
                        }
                    }
                }
            }}
    }
    private fun ViewEditApi(){
        viewModel.getImportExoticGoatAdd(this@ImportOfExoticGoatForms,true,
            ImportExoticGoatAddEditRequest(
                comment_by_nlm_whether = null,
                import_of_exotic_goat_detail_import = null,
                import_of_exotic_goat_achievement = null,
                import_of_exotic_goat_verified_nlm = null,
                state_code = getPreferenceOfScheme(this@ImportOfExoticGoatForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(this@ImportOfExoticGoatForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = viewEdit,
                id = itemId,
                is_draft = null
            )
        )
    }
    private fun saveDataApi(){
        viewModel.getImportExoticGoatAdd(this@ImportOfExoticGoatForms,true,
            ImportExoticGoatAddEditRequest(
                comment_by_nlm_whether = selectedValue,
                import_of_exotic_goat_detail_import = DetailOfImportList,
                import_of_exotic_goat_achievement = AchievementList,
                import_of_exotic_goat_verified_nlm = VerifiedNlmList,
                state_code = getPreferenceOfScheme(this@ImportOfExoticGoatForms, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatForms,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(this@ImportOfExoticGoatForms, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = null,
                id = null,
                is_draft=1
            )
        )
    }

}