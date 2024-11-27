package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.DocumentData
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class NLSIAFeedFodderFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAFeedFodderBinding>()
, CallBackDeleteAtId,CallBackItemUploadDocEdit {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__feed_fodder

    private var mBinding: FragmentNLSIAFeedFodderBinding?=null
    val viewModel = ViewModel()
    private var savedAsDraft:Boolean=false
    private var DocumentName:String?=null
    var body: MultipartBody.Part? = null
    private var savedAsEdit:Boolean=false
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var UploadedDocumentName:String?=null
    private var listener: OnNextButtonClickListener? = null
    private var DialogDocName:TextView?=null
    private var DocumentId:Int?=null
    private var itemPosition : Int ?= null


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = arrayListOf()
        viewModel.init()
        if (viewEdit=="view")
        {mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            mBinding?.etAssessmentOfGreen?.isEnabled=false
            mBinding?.tvAddMore2?.hideView()
            mBinding?.etAvailabilityOfGreen?.isEnabled=false
            mBinding?.etAvailibilityOfDry?.isEnabled=false
            mBinding?.AvailabilityOfConcentrate?.isEnabled=false
            mBinding?.etAvailabilityCommon?.isEnabled=false
            mBinding?.etEffortsOfState?.isEnabled=false
            mBinding?.etNameOfAgency?.isEnabled=false
            mBinding?.etQuantityOfFodder?.isEnabled=false
            mBinding?.etDistributionChannel?.isEnabled=false
            mBinding?.etNumberOfFodder?.isEnabled=false

            ViewEditApi()
        }
        else if (viewEdit=="edit"){
            ViewEditApi()

        }
        AddDocumentAdapter=SupportingDocumentAdapterWithDialog(requireContext(),DocumentList,viewEdit,this,this)
        mBinding?.recyclerView1?.adapter = AddDocumentAdapter
        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                }
                else{
                    if (savedAsDraft)
                    {
                        savedAsDraftClick?.onSaveAsDraft()
                    }else
                    {
                        if (viewEdit=="view"||viewEdit=="edit")
                        {
                            if (savedAsEdit)
                            {
                                savedAsDraftClick?.onSaveAsDraft()
                            }
                            else {
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
                        }
                        else{
                            savedAsDraftClick?.onSaveAsDraft()
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }
                    }}
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
                    DocumentId=userResponseModel._result.id
                    UploadedDocumentName=userResponseModel._result.document_name
                    DialogDocName?.text=userResponseModel._result.document_name
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

        fun saveAndNext(view: View){
            if (viewEdit=="view")
            {
                listener?.onNextButtonClick()
            }
            if (itemId==0)
            {
                activity?.supportFragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                showSnackbar(mBinding!!.clParent, "Please fill the mandatory field and save the data")
                listener?.onNavigateToFirstFragment()

            }
            else {
                if (viewEdit=="edit")
                {
                    savedAsEdit=true
                }
                saveDataApi(0)
        }}
        fun saveAsDraft(view: View){
            if (itemId==0)
            {
                activity?.supportFragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                showSnackbar(mBinding!!.clParent, "Please fill the mandatory field and save the data")
                listener?.onNavigateToFirstFragment()

            }
            else {
                if (viewEdit=="edit")
                {
                    savedAsEdit=true
                }
                saveDataApi(1)
            savedAsDraft=true
        }}
        fun addDocDialog(view: View){
            AddDocumentDialog(requireContext(),null,null)
        }

    }
    private fun AddDocumentDialog(context: Context,selectedItem: ImplementingAgencyDocument?,position: Int?) {
        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_add_document_dialog,
            null,
            false
        )
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
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
        DialogDocName=bindingDialog.etDoc
        if(selectedItem!=null){
            if (getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id==24)
            {
            UploadedDocumentName=selectedItem.ia_document
                bindingDialog.etDoc.text=selectedItem.ia_document}
            else{
                if (getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id==8){
                UploadedDocumentName=selectedItem.nlm_document
                bindingDialog.etDoc.text=selectedItem.nlm_document
            }}
            bindingDialog.etDescription.setText(selectedItem.description)
        }

        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog.tvChooseFile.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty())
            {

            openOnlyPdfAccordingToPosition()
        }
            else{

                mBinding?.clParent?.let { showSnackbar(it,"please enter description") }
            }
        }


        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty())
            {
                if (getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id==24) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            DocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                 implementing_agency_id = selectedItem.implementing_agency_id,
                                    id = selectedItem.id,
                                )
                            AddDocumentAdapter?.notifyItemChanged(position)
                            dialog.dismiss()
                        }

                    } else{  DocumentList.add(
                        ImplementingAgencyDocument(
                            description = bindingDialog.etDescription.text.toString(),
                            ia_document = UploadedDocumentName,
                            nlm_document = null,
//                            implementing_agency_id = itemId,
//                            id = DocumentId,
                        )
                    )
                        DocumentList.size.minus(1).let {
                            AddDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }}

                }
                else{
                    if (getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id==8) {
                        if(selectedItem!=null)
                        {
                            if (position != null) {
                                DocumentList[position] =
                                    ImplementingAgencyDocument(
                                        description = bindingDialog.etDescription.text.toString(),
                                        ia_document = null,
                                        nlm_document = UploadedDocumentName,
                                        implementing_agency_id = selectedItem.implementing_agency_id,
                                        id = selectedItem.id,
                                    )
                                AddDocumentAdapter?.notifyItemChanged(position)
                                dialog.dismiss()
                            }

                        } else{  DocumentList.add(
                            ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = null,
                                nlm_document = UploadedDocumentName,
//                            implementing_agency_id = itemId,
//                            id = DocumentId,
                            )
                        )
                            DocumentList.size.minus(1).let {
                                AddDocumentAdapter?.notifyItemInserted(it)
                                dialog.dismiss()
                            }}
                    }
                }


            }


            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
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
//                                DialogDocName?.text=DocumentName

                                val requestBody = convertToRequestBody(requireActivity(), uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
                                viewModel.getProfileUploadFile(
                                    context = requireActivity(),
                                    table_name = getString(R.string.implementing_agency_document).toRequestBody(MultipartBody.FORM),
                                    document_name = body,
                                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id,
                                    )
//                                use this code to add new view with image name and uri
                        }

                        }
                    }
                }
            }}
    }
    private fun ViewEditApi(){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                part = "part7",
                id = itemId,
                state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                is_deleted = 0,
                is_type = viewEdit
            )
        )
    }
    private fun saveDataApi(isDraft:Int?){
        viewModel.getImplementingAgencyAddApi(requireContext(),true,
            ImplementingAgencyAddRequest(
                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                part = "part7",
                assessments_of_green = mBinding?.etAssessmentOfGreen?.text.toString(),
                    availability_of_green_area = mBinding?.etAvailabilityOfGreen?.text.toString(),
                availability_of_dry = mBinding?.etAvailibilityOfDry?.text.toString(),
                availability_of_concentrate = mBinding?.AvailabilityOfConcentrate?.text.toString(),
                    availability_of_common = mBinding?.etAvailabilityCommon?.text.toString(),
                efforts_of_state = mBinding?.etEffortsOfState?.text.toString(),
                name_of_the_agency = mBinding?.etNameOfAgency?.text.toString(),
                quantity_of_fodder = mBinding?.etQuantityOfFodder?.text.toString(),
                distribution_channel =mBinding?.etDistributionChannel?.text.toString() ,
                number_of_fodder = mBinding?.etNumberOfFodder?.text.toString(),
                implementing_agency_document = DocumentList,
                id = itemId,
                is_draft = isDraft,
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        position.let { it1 -> AddDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        AddDocumentDialog(requireContext(),selectedItem,position)
    }
}