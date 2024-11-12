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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.DocumentData
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class NLSIAFeedFodderFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentNLSIAFeedFodderBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__feed_fodder

    private var mBinding: FragmentNLSIAFeedFodderBinding?=null
    val viewModel = ViewModel()
    private var savedAsDraft:Boolean=false
    private var DocumentName:String?=null
    var body: MultipartBody.Part? = null
    private var savedAsEdit:Boolean=false
    private var AddDocumentAdapter: SupportingDocumentAdapterWithDialog?=null
    private lateinit var DocumentList: MutableList<ImplementingAgencyDocument>
    private var savedAsDraftClick: OnBackSaveAsDraft? = null

    private var listener: OnNextButtonClickListener? = null
    private var DialogDocName:TextView?=null


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = mutableListOf()
        viewModel.init()
        if (viewEdit=="view")
        {
            mBinding?.etAssessmentOfGreen?.isEnabled=false
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
        AddDocumentAdapter=SupportingDocumentAdapterWithDialog(DocumentList)
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
                saveDataApi()
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
                saveDataApi()
            savedAsDraft=true
        }}
        fun addDocDialog(view: View){
            AddDocumentDialog(requireContext())
        }

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
                                DialogDocName?.text=DocumentName

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
                                role_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.role_id,
                                user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id,
                                table_name = getString(R.string.implementing_agency_document).toRequestBody(MultipartBody.FORM),
                                implementing_agency_id = Preferences.getPreference_int(requireContext(),AppConstants.FORM_FILLED_ID),
                                nlm_document = body
                            )
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
    private fun saveDataApi(){
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
                id = itemId,
                is_draft = 1,
            )
        )
    }
}