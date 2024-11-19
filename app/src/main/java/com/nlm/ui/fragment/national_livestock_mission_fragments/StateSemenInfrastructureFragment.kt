package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemGoatSemen
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenInfraGoat
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class StateSemenInfrastructureFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) :
    BaseFragment<FragmentStateSemenInfrastructureBinding>(), CallBackItemGoatSemen ,
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
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var savedAsEdit: Boolean = false


    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = arrayListOf()
        viewModel.init()
        stateSemenInfraGoatAdapter()
        addDocumentAdapter = SupportingDocumentAdapterWithDialog(requireContext(),DocumentList, viewEdit,this,this)
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())
        if (viewEdit == "view") {
            showToast(dId.toString())
            mBinding?.tvAddMore1?.isEnabled = false
            mBinding?.tvAddMore2?.isEnabled = false
            mBinding?.etStorageCapacity?.isEnabled = false
            mBinding?.etCoopOne?.isEnabled = false
            mBinding?.etCoopTwo?.isEnabled = false
            mBinding?.etCoopThree?.isEnabled = false
            mBinding?.etNgoOne?.isEnabled = false
            mBinding?.etNgoTwo?.isEnabled = false
            mBinding?.etNgoTwo?.isEnabled = false
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
            showToast(itemId.toString())
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
                    if (savedAsDraft) {
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
                        stateSemenInfraGoatList.addAll(userResponseModel._result.state_semen_bank_infrastructure)
                        stateSemenInfraGoatAdapter?.notifyDataSetChanged()
                        DocumentList.clear()
                        DocumentList.addAll(userResponseModel._result.state_semen_bank_document)
                        addDocumentAdapter?.notifyDataSetChanged()

                    } else {

                        Preferences.setPreference_int(
                            requireContext(),
                            AppConstants.FORM_FILLED_ID,
                            userResponseModel._result.id
                        )
                        listener?.onNextButtonClick()
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                    }

                }
            }
        }
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

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 0)
            } else {
                saveDataApi(null, 0)
            }
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext(),null,null)
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1, null, null)
        }
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

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
                major_clients_ngo_fin_year_two = mBinding?.etNgoThree?.text.toString(),
                major_clients_ngo_fin_year_three = mBinding?.etNgoOne?.text.toString(),
                major_clients_other_states_fin_year_one = mBinding?.etOtherStateOne?.text.toString(),
                major_clients_other_states_fin_year_two = mBinding?.etOtherStateTwo?.text.toString(),
                major_clients_other_states_fin_year_three = mBinding?.etOtherStateThree?.text.toString(),
                state_semen_bank_document = DocumentList,
                is_draft = draft,
            )
        )
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
            UploadedDocumentName = selectedItem.nlm_document
            bindingDialog.etDoc.text = selectedItem.nlm_document
            bindingDialog.etDescription.setText(selectedItem.description)
        }
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }

        bindingDialog.btnDelete.setOnClickListener{
            dialog.dismiss()
        }

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {
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
                            state_semen_bank_id=null
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
                        val cursor = requireActivity().contentResolver.query(
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

                                val requestBody = convertToRequestBody(requireActivity(), uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
                            viewModel.getProfileUploadFile(
                                context = requireActivity(),
                                table_name = getString(R.string.state_semen_bank_document).toRequestBody(
                                    MultipartBody.FORM
                                ),
                                document_name = body,
                                user_id = getPreferenceOfScheme(
                                    requireContext(),
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

    override fun onClickItem(selectedItem: StateSemenInfraGoat, position: Int, isFrom: Int) {

        compositionOfGoverningNlmIaDialog(requireContext(), isFrom, selectedItem, position)
    }
    override fun onClickItem(ID: Int?, position: Int) {
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(requireContext(), selectedItem, position)
    }

}