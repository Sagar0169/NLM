package com.nlm.ui.activity.national_livestock_mission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackAnimalFund
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackDeleteFormatAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.CallBackNlmAnimalMonitor
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.databinding.ActivityAddAnimalHusbandryBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemNlmAnimalBinding
import com.nlm.databinding.ItemNlmIaAnimalFundBinding
import com.nlm.model.AddAnimalRequest
import com.nlm.model.AhidfFormatForNlm
import com.nlm.model.AhidfMonitoring
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.NlmAnimalFundAdapter
import com.nlm.ui.adapter.NlmAnimalMonitoringAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddAnimalHusbandryActivity(
) : BaseActivity<ActivityAddAnimalHusbandryBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit, CallBackNlmAnimalMonitor, CallBackDeleteFSPAtId, CallBackAnimalFund,
    CallBackDeleteFormatAtId {
    private var mBinding: ActivityAddAnimalHusbandryBinding? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var addDocumentIAAdapter: RSPSupportingDocumentIAAdapter? = null

    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var isFrom: Int = 0
    private var img: Int = 0
    private var viewModel = ViewModel()
    private var districtId: Int? = null // Store selected state
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null
    private lateinit var nlmEdpTrainingList: ArrayList<AhidfMonitoring>
    private lateinit var nlmEdpFormatList: ArrayList<AhidfFormatForNlm>
    private var nlmEDPMonitoringAdapter: NlmAnimalMonitoringAdapter? = null
    private var nlmEDPFormatAdapter: NlmAnimalFundAdapter? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false


    private val variety = listOf(
        ResultGetDropDown(0, "YES"),
        ResultGetDropDown(0, "NO")
    )

    private val projectFinance = listOf(
        ResultGetDropDown(0, "Subsidy Loan"),
        ResultGetDropDown(0, "Self Finance")
    )

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
//            showBottomSheetDialog("state")
        }


        fun chooseFile(view: View) {
            openOnlyPdfAccordingToPosition()
            toast("Hi")
            img = 1
        }

        fun district(view: View) {
//            showBottomSheetDialog("District")
        }

        fun districtNLM(view: View) {
//            showBottomSheetDialog("DistrictNLM")
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddAnimalHusbandryActivity, null, null)
            img = 0
        }

        fun trainingInstitute(view: View) {
            trainingInstitute(this@AddAnimalHusbandryActivity, 1, null, null)
        }

        fun formatNlmEdp(view: View) {
            formatNlmEdp(this@AddAnimalHusbandryActivity, 1, null, null)
        }

        fun save(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            isSubmitted = true
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 0)
            } else {
                saveDataApi(null, 0)
            }


        }


        fun saveAsDraft(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
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

    }

    override val layoutId: Int
        get() = R.layout.activity_add_animal_husbandry

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        DocumentList = arrayListOf()
        nlmEdpFormatList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        nlmEdpTrainingList = arrayListOf()





        isFrom = intent?.getIntExtra("isFrom", 0)!!
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvStateNLM?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        mBinding?.tvStateNLM?.isEnabled = false

        mBinding?.tvState?.setTextColor(Color.parseColor("#000000"))
        mBinding?.tvStateNLM?.setTextColor(Color.parseColor("#000000"))
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            mBinding?.tvIADoc?.hideView()
            nlmAdapter()
        }
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {

            mBinding?.tvNLMDoc?.hideView()
            mBinding?.llNLM?.hideView()
            iaAdapter()
        }




        if (viewEdit == "view" || (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
                    )
            ||
            (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
                    )
        ) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                mBinding?.tvState?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
                mBinding?.tvIANLMEDP?.hideView()
            }
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 24
            ) {
                mBinding?.tvStateNLM?.isEnabled = false
                mBinding?.etNLMComment?.isEnabled = false

                mBinding?.tvNLMComment?.isEnabled = false
                mBinding?.tvNLMComment?.hideView()
                mBinding?.tvNLMDoc?.hideView()
            }
            if (viewEdit == "view") {
                viewEditApi()
                mBinding?.tvState?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
                mBinding?.tvStateNLM?.isEnabled = false
                mBinding?.etNLMComment?.isEnabled = false
                mBinding?.tvState?.isEnabled = false
                mBinding?.tvIADoc?.hideView()
                mBinding?.tvIANLMEDP?.hideView()
                mBinding?.tvNLMComment?.isEnabled = false
                mBinding?.tvNLMComment?.hideView()
                mBinding?.tvNLMDoc?.hideView()
                mBinding?.tvSaveDraft?.hideView()
                mBinding?.tvSendOtp?.hideView()
            }
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
        nlmEDPMonitoringAdapter()
        nlmEDPFormatAdapter()
    }

    private fun viewEditApi() {

        viewModel.getNlmAhidfADD(
            this, true,
            AddAnimalRequest(
                id = itemId,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                is_type = viewEdit
            )
        )
    }

    private fun nlmEDPMonitoringAdapter() {
        nlmEDPMonitoringAdapter =
            NlmAnimalMonitoringAdapter(
                this@AddAnimalHusbandryActivity,
                nlmEdpTrainingList,
                viewEdit,
                this@AddAnimalHusbandryActivity,
                this
            )
        mBinding?.recyclerView1?.adapter = nlmEDPMonitoringAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(this@AddAnimalHusbandryActivity)
    }

    private fun nlmEDPFormatAdapter() {
        nlmEDPFormatAdapter =
            NlmAnimalFundAdapter(
                this@AddAnimalHusbandryActivity,
                nlmEdpFormatList,
                viewEdit,
                this@AddAnimalHusbandryActivity,
                this
            )
        mBinding?.rvIANLMEDP?.adapter = nlmEDPFormatAdapter
        mBinding?.rvIANLMEDP?.layoutManager =
            LinearLayoutManager(this@AddAnimalHusbandryActivity)
    }

    private fun nlmAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(this)
    }

    private fun iaAdapter() {
        addDocumentIAAdapter = RSPSupportingDocumentIAAdapter(
            this,
            viewDocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView?.adapter = addDocumentIAAdapter
        mBinding?.recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

        viewModel.getNlmAhidfADD(
            this@AddAnimalHusbandryActivity, true,
            AddAnimalRequest(
                id = itemId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                is_draft = draft,
                remarks_by_nlm = mBinding?.etNLMComment?.text.toString(),
                ahidf_document = totalListDocument,
                ahidf_monitoring = nlmEdpTrainingList,
                ahidf_format_for_nlm = nlmEdpFormatList,
            )
        )
    }


    private fun trainingInstitute(
        context: Context,
        isFrom: Int,
        selectedItem: AhidfMonitoring?,
        position: Int?
    ) {
        val bindingDialog: ItemNlmAnimalBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_nlm_animal,
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
        bindingDialog.btnEdit.hideView()
        bindingDialog.tvSubmit.showView()
        bindingDialog.tvFull.setOnClickListener {
            showBottomSheetDialog("whetherFull", bindingDialog.tvFull)
        }
        bindingDialog.etProcessingCapacity.setOnClickListener {
            showBottomSheetDialog("whetherFull", bindingDialog.etProcessingCapacity)
        }
        bindingDialog.tvProjectFinancing.setOnClickListener {
            showBottomSheetDialog("projectFinance", bindingDialog.tvProjectFinancing)
        }
        var processing = ""

        if (selectedItem != null && isFrom == 2) {
            processing = if (selectedItem.processing_capacity_nlm.toString().toInt() == 0) {
                "No"
            } else {
                "Yes"
            }
            bindingDialog.etNameOfBeneficiary.setText(selectedItem.name_of_beneficiary)
            bindingDialog.etCategory.setText(selectedItem.category_of_project)
            bindingDialog.tvProjectFinancing.text = selectedItem.project_financing
            bindingDialog.etTypeofFarming.setText(selectedItem.type_of_farming)
            bindingDialog.etCapacity.setText(selectedItem.capacity)
            bindingDialog.tvFull.text = selectedItem.whether_full
            bindingDialog.etProcessingCapacity.text =
                processing
            bindingDialog.etNumberOfFarmer.setText(selectedItem.number_of_farmers.toString())
            bindingDialog.etFinacial.setText(selectedItem.financial_status.toString())
            bindingDialog.etNoJob.setText(selectedItem.number_of_job.toString())
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNameOfBeneficiary.text.toString().isNotEmpty()
                || bindingDialog.etCategory.text.toString().isNotEmpty()
                || bindingDialog.tvProjectFinancing.text.toString().isNotEmpty()
                || bindingDialog.etTypeofFarming.text.toString().isNotEmpty()
                || bindingDialog.etCapacity.text.toString().isNotEmpty()
                || bindingDialog.tvFull.text.toString().isNotEmpty()
                || bindingDialog.etFinacial.text.toString().isNotEmpty()
                || bindingDialog.etProcessingCapacity.text.toString().isNotEmpty()
                || bindingDialog.etNumberOfFarmer.text.toString().isNotEmpty()
                || bindingDialog.etNoJob.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        nlmEdpTrainingList[position] =
                            AhidfMonitoring(
                                selectedItem.id,
                                bindingDialog.etNameOfBeneficiary.text.toString(),
                                bindingDialog.etCategory.text.toString(),
                                bindingDialog.tvProjectFinancing.text.toString(),
                                bindingDialog.etTypeofFarming.text.toString(),
                                bindingDialog.etCapacity.text.toString(),
                                bindingDialog.tvFull.text.toString(),
                                bindingDialog.etFinacial.text.toString().toIntOrNull(),
                                if (bindingDialog.etProcessingCapacity.text.toString()
                                        .equals("Yes", ignoreCase = true)
                                ) 1 else 0, // Check for Yes/No
                                bindingDialog.etNumberOfFarmer.text.toString().toIntOrNull(),
                                bindingDialog.etNoJob.text.toString().toIntOrNull(),
                                selectedItem.ahidf_id,
                            )
                        nlmEDPMonitoringAdapter
                            ?.notifyItemChanged(position)
                    }

                } else {
                    nlmEdpTrainingList.add(
                        AhidfMonitoring(
                            null,
                            bindingDialog.etNameOfBeneficiary.text.toString(),
                            bindingDialog.etCategory.text.toString(),
                            bindingDialog.tvProjectFinancing.text.toString(),
                            bindingDialog.etTypeofFarming.text.toString(),
                            bindingDialog.etCapacity.text.toString(),
                            bindingDialog.tvFull.text.toString(),
                            bindingDialog.etFinacial.text.toString().toIntOrNull(),
                            if (bindingDialog.etProcessingCapacity.text.toString()
                                    .equals("Yes", ignoreCase = true)
                            ) 1 else 0, // Check for Yes/No
                            bindingDialog.etNumberOfFarmer.text.toString().toIntOrNull(),
                            bindingDialog.etNoJob.text.toString().toIntOrNull(),
                            null,
                        )
                    )

                    nlmEdpTrainingList.size.minus(1).let {
                        nlmEDPMonitoringAdapter
                            ?.notifyItemInserted(it)
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

    private fun formatNlmEdp(
        context: Context,
        isFrom: Int,
        selectedItem: AhidfFormatForNlm?,
        position: Int?
    ) {
        val bindingDialog: ItemNlmIaAnimalFundBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_nlm_ia_animal_fund,
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
        bindingDialog.btnEdit.hideView()
        bindingDialog.tvSubmit.showView()
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etCategory.setText(selectedItem.category_of_project)
            bindingDialog.etNoProject.setText(selectedItem.no_of_project.toString())
            bindingDialog.etCostOfProject.setText(selectedItem.cost_of_project.toString())
            bindingDialog.etTermLoan.setText(selectedItem.term_loan.toString())
            bindingDialog.etTotalNoEmp.setText(selectedItem.total_employment_generated.toString())
            bindingDialog.etProcessing.setText(selectedItem.processing_capacity.toString())
            bindingDialog.etBirth.setText(selectedItem.birth_percentage.toString())
            bindingDialog.etAvg.setText(selectedItem.average_revenue_earned.toString())
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etCategory.text.toString().isNotEmpty()
                || bindingDialog.etNoProject.text.toString().isNotEmpty()
                || bindingDialog.etCostOfProject.text.toString().isNotEmpty()
                || bindingDialog.etTermLoan.text.toString().isNotEmpty()
                || bindingDialog.etTotalNoEmp.text.toString().isNotEmpty()
                || bindingDialog.etProcessing.text.toString().isNotEmpty()
                || bindingDialog.etBirth.text.toString().isNotEmpty()
                || bindingDialog.etAvg.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        nlmEdpFormatList[position] =
                            AhidfFormatForNlm(
                                selectedItem.id,
                                bindingDialog.etCategory.text.toString(),
                                bindingDialog.etNoProject.text.toString().toIntOrNull(),
                                bindingDialog.etCostOfProject.text.toString().toDoubleOrNull(),
                                bindingDialog.etTermLoan.text.toString().toIntOrNull(),
                                bindingDialog.etTotalNoEmp.text.toString().toIntOrNull(),
                                bindingDialog.etProcessing.text.toString().toDoubleOrNull(),
                                bindingDialog.etBirth.text.toString().toIntOrNull(),
                                bindingDialog.etAvg.text.toString().toDoubleOrNull(),
                                selectedItem.ahidf_id,
                            )
                        nlmEDPFormatAdapter
                            ?.notifyItemChanged(position)
                    }

                } else {
                    nlmEdpFormatList.add(
                        AhidfFormatForNlm(
                            null,
                            bindingDialog.etCategory.text.toString(),
                            bindingDialog.etNoProject.text.toString().toIntOrNull(),
                            bindingDialog.etCostOfProject.text.toString().toDoubleOrNull(),
                            bindingDialog.etTermLoan.text.toString().toIntOrNull(),
                            bindingDialog.etTotalNoEmp.text.toString().toIntOrNull(),
                            bindingDialog.etProcessing.text.toString().toDoubleOrNull(),
                            bindingDialog.etBirth.text.toString().toIntOrNull(),
                            bindingDialog.etAvg.text.toString().toDoubleOrNull(),
                            null,
                        )
                    )

                    nlmEdpFormatList.size.minus(1).let {
                        nlmEDPFormatAdapter
                            ?.notifyItemInserted(it)
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

        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
                bindingDialog.etDescription.setText(selectedItem.description)
            } else {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document
                bindingDialog.etDescription.setText(selectedItem.description)
            }

        }
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }


        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString()
                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        if (getPreferenceOfScheme(
                                this,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id == 8
                        ) {
                            DocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    ahidf_id = selectedItem.ahidf_id,
                                    id = selectedItem.id,
                                )
                            addDocumentAdapter?.notifyItemChanged(position)

                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    ahidf_id = selectedItem.ahidf_id,
                                    id = selectedItem.id,
                                )
                            addDocumentIAAdapter?.notifyItemChanged(position)
                        }

                        dialog.dismiss()
                    }

                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                nlm_document = UploadedDocumentName,
                                id = null,
                                ahidf_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                id = null,
                                ahidf_id = null,
                                nlm_document = null
                            )
                        )
                        Log.d("Debug", "viewDocumentList: ${viewDocumentList.size}")

                    }

                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.size.minus(1).let {
                            addDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
//
                        }
                    } else {

                        viewDocumentList.size.minus(1).let {
                            addDocumentIAAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }
                        toast("else")
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
                        val cursor = this.contentResolver.query(
                            uri,
                            projection,
                            null,
                            null,
                            null
                        )
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text = DocumentName


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
                                table_name = getString(R.string.ahidf_document).toRequestBody(
                                    MultipartBody.FORM
                                ),
                                document_name = body,
                                user_id = getPreferenceOfScheme(
                                    this,
                                    AppConstants.SCHEME,
                                    Result::class.java
                                )?.user_id,
                            )
                        }
                    }
                }
            }
        }
    }


    private fun showBottomSheetDialog(type: String, full: TextView) {
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
//            "typeSemen" -> {
//                selectedList = typeSemen
//                selectedTextView = mBinding!!.tvSemenStation
//            }
//
//            "StateNDD" -> {
//                selectedList = stateList
//                selectedTextView = binding!!.tvStateNDD
//            }
//
//            "District" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = districtList
//                selectedTextView = mBinding!!.tvDistrict
//            }

//            "DistrictNLM" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = districtList
//                selectedTextView = mBinding!!.tvDistrictNlm
//            }

            "whetherFull" -> {
                img = 2
                selectedList = variety
                selectedTextView = full
            }

            "projectFinance" -> {
                img = 2
                selectedList = projectFinance
                selectedTextView = full
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
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                20,
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

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddAnimalHusbandryActivity)
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
                    DocumentId = userResponseModel._result.id
                    UploadedDocumentName = userResponseModel._result.document_name
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }

        viewModel.nlmAhidfADDResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            toast(viewEdit.toString())

                            mBinding?.etNLMComment?.setText(userResponseModel._result.remarks_by_nlm)
                            nlmEdpTrainingList.clear()
                            val comments =
                                userResponseModel._result.ahidf_monitoring
                                    ?: emptyList()

                            if (comments.isEmpty() && viewEdit == "view") {
                                val dummyData = AhidfMonitoring(
                                    id = 0,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                )
                                nlmEdpTrainingList.add(dummyData)
                            } else {
                                nlmEdpTrainingList.addAll(comments)
                            }


                            nlmEDPMonitoringAdapter?.notifyDataSetChanged()

                            nlmEdpFormatList.clear()
                            val commentss =
                                userResponseModel._result.ahidf_format_for_nlm
                                    ?: emptyList()

                            if (commentss.isEmpty() && viewEdit == "view") {
                                val dummyData = AhidfFormatForNlm(
                                    id = 0,
                                    "",
                                    0,
                                    0.0,
                                    0,
                                    0,
                                    0.0,
                                    0,
                                    0.0,
                                    null
                                )
                                nlmEdpFormatList.add(dummyData)
                            } else {
                                nlmEdpFormatList.addAll(commentss)
                            }


                            nlmEDPFormatAdapter?.notifyDataSetChanged()
                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()
                            val dummyData = ImplementingAgencyDocument(
                                id = 0, // Or null, depending on your use case
                                description = "",
                                ia_document = "",
                                nlm_document = "",
                                assistance_for_ea_id = 0 // Or null, depending on your use case
                            )
                            if (userResponseModel._result?.ahidf_document?.isEmpty() == true && viewEdit == "view") {
                                // Add dummy data with default values

//
//                                DocumentList.add(dummyData)
//                                viewDocumentList.add(dummyData)

                            } else {
                                userResponseModel._result?.ahidf_document?.forEach { document ->
                                    if (document.ia_document == null) {
                                        DocumentList.add(document)//nlm
                                    } else {
                                        viewDocumentList.add(document)//ia

                                    }
                                }
                                // Check if viewDocumentList is empty after the loop
                                if (viewDocumentList.isEmpty() && viewEdit == "view") {
//                                    viewDocumentList.add(dummyData)
                                }
                                if (DocumentList.isEmpty() && viewEdit == "view") {
//                                    DocumentList.add(dummyData)
                                }
                            }

                            nlmAdapter()
                            iaAdapter()

                            addDocumentAdapter?.notifyDataSetChanged()
                            addDocumentIAAdapter?.notifyDataSetChanged()

                        } else {
                            onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }

                    }


                }
            }
        }

    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom == 10) {
            position.let { it1 -> addDocumentIAAdapter?.onDeleteButtonClick(it1) }

        } else {
            position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }

        }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(this@AddAnimalHusbandryActivity, selectedItem, position)
    }


    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> nlmEDPMonitoringAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItem(
        selectedItem: AhidfMonitoring,
        position: Int,
        isFrom: Int
    ) {
        trainingInstitute(this@AddAnimalHusbandryActivity, isFrom, selectedItem, position)
    }

    override fun onClickItem(selectedItem: AhidfFormatForNlm, position: Int, isFrom: Int) {
        formatNlmEdp(this@AddAnimalHusbandryActivity, isFrom, selectedItem, position)
    }

    override fun onClickItemFormatDelete(ID: Int?, position: Int) {
        position.let { it1 -> nlmEDPFormatAdapter?.onDeleteButtonClick(it1) }
    }


}