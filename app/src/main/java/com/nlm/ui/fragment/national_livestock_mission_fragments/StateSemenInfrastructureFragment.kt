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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
import com.nlm.model.DocumentData
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.StateSemenInfraGoat
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody


class StateSemenInfrastructureFragment : BaseFragment<FragmentStateSemenInfrastructureBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_state__semen__infrastructure
    private var mBinding: FragmentStateSemenInfrastructureBinding? = null
    private lateinit var stateSemenInfraGoatList: MutableList<StateSemenInfraGoat>
    private var stateSemenInfraGoatAdapter: StateSemenInfrastructureAdapter? = null
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft: Boolean = false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var addDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private lateinit var DocumentList: MutableList<ImplementingAgencyDocument>
    private var DialogDocName: TextView?=null
    private var DocumentName:String?=null
    var body: MultipartBody.Part? = null
    val viewModel = ViewModel()

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = mutableListOf()
        viewModel.init()
        stateSemenInfraGoatAdapter()
        addDocumentAdapter=SupportingDocumentAdapterWithDialog(DocumentList)
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun stateSemenInfraGoatAdapter() {
        stateSemenInfraGoatList = mutableListOf()
        stateSemenInfraGoatAdapter =
            StateSemenInfrastructureAdapter(stateSemenInfraGoatList)
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

    }

    private fun compositionOfGoverningNlmIaDialog(context: Context, isFrom: Int) {
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

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etListOfEquipment.text.toString().isNotEmpty() || bindingDialog.etYearOfProcurement.text.toString().isNotEmpty()) {
                stateSemenInfraGoatList.add(
                    StateSemenInfraGoat(
                        bindingDialog.etListOfEquipment.text.toString(),
                        bindingDialog.etYearOfProcurement.text.toString(),
                        id = null,
                    )
                )
                stateSemenInfraGoatList.size.minus(1).let {
                    stateSemenInfraGoatAdapter?.notifyItemInserted(it)
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
        fun saveAsDraft(view: View) {}
        fun save(view: View) {
            listener?.onNextButtonClick()
        }
        fun addDocDialog(view: View){
            addDocumentDialog(requireContext())
        }
        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1)
        }
    }

    private fun addDocumentDialog(context: Context) {
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
                    addDocumentAdapter?.notifyItemInserted(it)
                    dialog.dismiss()
//
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
                        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName=
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                DialogDocName?.text=DocumentName

                                val requestBody = convertToRequestBody(requireActivity(), uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
//                            viewModel.getProfileUploadFile(
//                                context = requireActivity(),
//                                role_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id,
//                                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id,
//                                table_name = getString(R.string.implementing_agency_document).toRequestBody(
//                                    MultipartBody.FORM),
//                                implementing_agency_id = Preferences.getPreference_int(requireContext(),
//                                    AppConstants.FORM_FILLED_ID),
//                                nlm_document = body
//                            )
                        }
                    }
                }
            }}
    }




}