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
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemRspBucksBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspBasicInfoEquipment
import com.nlm.model.StateSemenInfraGoat
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.ui.adapter.rgm.AvailabilityOfEquipmentAdapter
import com.nlm.ui.adapter.rgm.AverageSemenDoseAdapter
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import okhttp3.MultipartBody


class RSPBasicInformationFragment : BaseFragment<FragmentRSPBasicInformationBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__basic_information
    private var mBinding: FragmentRSPBasicInformationBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var programmeList: MutableList<Array<String>>
    private var DialogDocName: TextView? = null
    private var DialogDocNameNLM: TextView? = null
    private var DocumentName: String? = null
    private var DocumentNameNLM: String? = null
    private var addDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private lateinit var DocumentList: MutableList<ImplementingAgencyDocument>
    private lateinit var DocumentListNLM: MutableList<ImplementingAgencyDocument>
    var body: MultipartBody.Part? = null
    private lateinit var rspEquipList: MutableList<RspBasicInfoEquipment>
    private lateinit var addBucksList: MutableList<RspAddBucksList>
    private var rspEquipAdapter: AvailabilityOfEquipmentAdapter? = null
    private var addBuckAdapter: AverageSemenDoseAdapter? = null
    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        DocumentList = mutableListOf()
        DocumentListNLM = mutableListOf()
        addDocumentAdapter = SupportingDocumentAdapterWithDialog(DocumentList)
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())

//        addDocumentAdapter = SupportingDocumentAdapterWithDialog(DocumentListNLM)
//        mBinding?.rvNlmDoc?.adapter = addDocumentAdapter
//        mBinding?.rvNlmDoc?.layoutManager = LinearLayoutManager(requireContext())
        rspEquipAdapter()
        rspBuckAdapter()
    }

    private fun rspEquipAdapter() {
        rspEquipList = mutableListOf()
        rspEquipAdapter =
            AvailabilityOfEquipmentAdapter(rspEquipList)
        mBinding?.recyclerView4?.adapter = rspEquipAdapter
        mBinding?.recyclerView4?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    private fun rspBuckAdapter() {
        addBucksList = mutableListOf()
        addBuckAdapter =
            AverageSemenDoseAdapter(addBucksList)
        mBinding?.rvBuckNlm?.adapter = addBuckAdapter
        mBinding?.rvBuckNlm?.layoutManager =
            LinearLayoutManager(requireContext())
    }


    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("district")
        }

        fun save(view: View) {}
        fun saveAsDraft(view: View) {}
        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext())
        }
        fun addDocDialogNLM(view: View) {
            addDocumentDialogNLM(requireContext())
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1)
        }
        fun addBuck(view: View) {
            addBucks(requireContext(), 1)
        }
    }

    private fun addBucks(context: Context, isFrom: Int) {
        val bindingDialog: ItemRspBucksBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_rsp_bucks,
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
            if (bindingDialog.etBreedMaintained.text.toString()
                    .isNotEmpty() || bindingDialog.etAnimal.text.toString().isNotEmpty()
                || bindingDialog.etAvgAge.text.toString().isNotEmpty()
            ) {
                addBucksList.add(
                    RspAddBucksList(
                        bindingDialog.etBreedMaintained.text.toString(),
                        bindingDialog.etAnimal.text.toString(),
                        bindingDialog.etAvgAge.text.toString(),
                        id = null,
                    )
                )
                addBucksList.size.minus(1).let {
                    addBuckAdapter?.notifyItemInserted(it)
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
        bindingDialog.tvMake.showView()
        bindingDialog.etMake.showView()

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etListOfEquipment.text.toString()
                    .isNotEmpty() || bindingDialog.etYearOfProcurement.text.toString().isNotEmpty()
                    || bindingDialog.etMake.text.toString().isNotEmpty()
            ) {
                rspEquipList.add(
                    RspBasicInfoEquipment(
                        bindingDialog.etListOfEquipment.text.toString(),
                        bindingDialog.etMake.text.toString(),
                        bindingDialog.etYearOfProcurement.text.toString(),
                        id = null,
                    )
                )
                rspEquipList.size.minus(1).let {
                    rspEquipAdapter?.notifyItemInserted(it)
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

    private fun addDocumentDialogNLM(context: Context) {
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
        DialogDocName = bindingDialog.etDoc
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                DocumentListNLM.add(
                    ImplementingAgencyDocument(
                        bindingDialog.etDescription.text.toString(),
                        ia_document = DocumentName,
                        id = null,
                        implementing_agency_id = null,
                        null
                    )
                )

                DocumentListNLM.size.minus(1).let {
                    addDocumentAdapter?.notifyItemInserted(it)
                    dialog.dismiss()
//
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
        DialogDocName = bindingDialog.etDoc
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString().isNotEmpty()) {

                DocumentList.add(
                    ImplementingAgencyDocument(
                        bindingDialog.etDescription.text.toString(),
                        ia_document = DocumentName,
                        id = null,
                        implementing_agency_id = null,
                        null
                    )
                )

                DocumentList.size.minus(1).let {
                    addDocumentAdapter?.notifyItemInserted(it)
                    dialog.dismiss()
//
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
            }
        }
    }


    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(requireContext())
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
        val selectedList: List<String>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {


            "state" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }

            "district" -> {
                selectedList = district
                selectedTextView = mBinding!!.etDistrict
            }


            else -> return
        }
        bottomSheetAdapter = StateAdapter(selectedList, requireContext()) { selectedItem ->
            selectedTextView.text = selectedItem
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = bottomSheetAdapter
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }
}