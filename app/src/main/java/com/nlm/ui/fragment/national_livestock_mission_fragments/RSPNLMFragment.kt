package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackSemenDoseAvg
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemRspBucksBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspAddAverage
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspAddEquipment
import com.nlm.model.RspBasicInfoEquipment
import com.nlm.model.RspLaboratorySemenAverage
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.SemenDoseAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.ui.adapter.rgm.AvailabilityOfEquipmentAdapter
import com.nlm.ui.adapter.rgm.AverageSemenDoseAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Preferences.getPreference_int
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody


class RSPNLMFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) : BaseFragment<FragmentRSPManpowerBinding>(

), CallBackSemenDoseAvg {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p_manpower
    private var viewModel = ViewModel()
    private var mBinding: FragmentRSPManpowerBinding? = null
    private lateinit var addBucksList: MutableList<RspAddBucksList>
    private var addBuckAdapter: AverageSemenDoseAdapter? = null
    private var addDocumentAdapter: SupportingDocumentAdapterWithDialog? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var districtId: Int? = null // Store selected state
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var programmeList: MutableList<Array<String>>
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var currentPage = 1
    private var totalPage = 1
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var listener: OnNextButtonClickListener? = null

    private lateinit var DocumentList: MutableList<ImplementingAgencyDocument>
    var body: MultipartBody.Part? = null
    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        DocumentList = mutableListOf()
        addDocumentAdapter = SupportingDocumentAdapterWithDialog(DocumentList, "view")
        mBinding?.rvNlmDoc?.adapter = addDocumentAdapter
        mBinding?.rvNlmDoc?.layoutManager = LinearLayoutManager(requireContext())
        mBinding?.tvState?.text = getPreferenceOfScheme(
            requireContext(),
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                requireContext(),
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.etLocation?.isEnabled = false
            mBinding?.etPincode?.isEnabled = false
            mBinding?.etPhone?.isEnabled = false
            mBinding?.etYear?.isEnabled = false
            mBinding?.etAddress?.isEnabled = false

            mBinding?.etCommentsNlm?.isEnabled = false
            mBinding?.etFund?.isEnabled = false
            mBinding?.etSemen?.isEnabled = false
            mBinding?.etMsp?.isEnabled = false
            mBinding?.etUnit?.isEnabled = false
            mBinding?.tvAddMore3?.isEnabled = false
            mBinding?.tvAddMore4?.isEnabled = false
            mBinding?.etPhysical?.isEnabled = false
            mBinding?.etFinancial?.isEnabled = false
            mBinding?.etAnyOther?.isEnabled = false


            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
            showToast(itemId.toString())

        }
        rspBuckAdapter()
    }

    private fun viewEditApi() {

        viewModel.getRspLabAddApi(
            requireContext(), true,
            RSPAddRequest(
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

    private fun viewEditApii() {

        viewModel.getRspLabAddApi(
            requireContext(), true,
            RSPAddRequest(
                id = getPreference_int(requireContext(), AppConstants.FORM_FILLED_ID),
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

    private fun rspBuckAdapter() {
        addBucksList = mutableListOf()
        addBuckAdapter =
            AverageSemenDoseAdapter(requireContext(),addBucksList, viewEdit, this)
        mBinding?.rvBuckNlm?.adapter = addBuckAdapter
        mBinding?.rvBuckNlm?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
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

        viewModel.rspLabAddResult.observe(viewLifecycleOwner) {
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
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                listener?.onNextButtonClick()
                                return@observe
                            }
                            districtId = userResponseModel._result.district_code
                            mBinding?.etLocation?.setText(userResponseModel._result.location)
                            mBinding?.tvDistrict?.text = userResponseModel._result.district_name
                            mBinding?.etPincode?.setText(userResponseModel._result.pin_code.toString())
                            mBinding?.etPhone?.setText(userResponseModel._result.phone_no.toString())
                            mBinding?.etYear?.setText(userResponseModel._result.year_of_establishment)
                            mBinding?.etAddress?.setText(userResponseModel._result.address)
                            mBinding?.etCommentsNlm?.setText(userResponseModel._result.comments_infrastructure)
                            mBinding?.etFund?.setText(userResponseModel._result.fund_properly_utilized)
                            mBinding?.etSemen?.setText(userResponseModel._result.semen_straws_produced)
                            mBinding?.etMsp?.setText(userResponseModel._result.processing_semen)
                            mBinding?.etUnit?.setText(userResponseModel._result.equipments_per_msp)
                            mBinding?.etPhysical?.setText(userResponseModel._result.suggestions_physical)
                            mBinding?.etFinancial?.setText(userResponseModel._result.suggestions_financial)
                            mBinding?.etAnyOther?.setText(userResponseModel._result.suggestions_any_other)
                            addBucksList.clear()
                            userResponseModel._result.rsp_laboratory_semen_station_quality_buck?.let { it1 ->
                                addBucksList.addAll(
                                    it1
                                )
                            }
                            addBuckAdapter?.notifyDataSetChanged()


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

    }

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
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
                saveDataApi(getPreference_int(requireContext(), AppConstants.FORM_FILLED_ID), 1)
            }
            savedAsDraft = true
        }

        fun addBuck(view: View) {
            addBucks(requireContext(), 1, null, null)
        }

        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext())
        }
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
        val address = mBinding?.etAddress?.text.toString()
        val location = mBinding?.etLocation?.text.toString()
        val phoneNumber = mBinding?.etPhone?.text.toString()
        val pincode = mBinding?.etPincode?.text.toString()
        val yearOfEstablishment = mBinding?.etYear?.text.toString()

        // Validation checks
        if (address.isEmpty()) {
            showError("Address is required")
            return
        }

        if (location.isEmpty()) {
            showError("Location is required")
            return
        }

        if (districtId == null) {
            showError("District is required")
            return
        }

        if (pincode.isEmpty()) {
            showError("Pincode is required")
            return
        }

        if (phoneNumber.isEmpty() || phoneNumber.length < 10) {
            showError("Valid phone number is required")
            return
        }

        if (yearOfEstablishment.isEmpty()) {
            showError("Year of establishment is required")
            return
        }
        viewModel.getRspLabAddApi(
            requireContext(), true,
            RSPAddRequest(
                id = itemId,
                address = address,
                location = location,
                district_code = districtId,
                phone_no = phoneNumber.toLongOrNull(),
                pin_code = pincode.toIntOrNull(),
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
                year_of_establishment = yearOfEstablishment.toIntOrNull(),
                comments_infrastructure = mBinding?.etCommentsNlm?.text.toString(),
                fund_properly_utilized = mBinding?.etFund?.text.toString(),
                semen_straws_produced = mBinding?.etSemen?.text.toString(),
                equipments_per_msp = mBinding?.etUnit?.text.toString(),
                processing_semen = mBinding?.etMsp?.text.toString(),
                suggestions_financial = mBinding?.etFinancial?.text.toString(),
                suggestions_physical = mBinding?.etPhysical?.text.toString(),
                suggestions_any_other = mBinding?.etAnyOther?.text.toString(),
                rsp_laboratory_semen_station_quality_buck = addBucksList,
                is_draft = 1,
            )
        )
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    private fun addBucks(
        context: Context,
        isFrom: Int,
        selectedItem: RspAddBucksList?,
        position: Int?
    ) {
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
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etBreedMaintained.setText(selectedItem.breed_maintained)
            bindingDialog.etAnimal.setText(selectedItem.no_of_animals.toString())
            bindingDialog.etAvgAge.setText(selectedItem.average_age)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etBreedMaintained.text.toString()
                    .isNotEmpty() || bindingDialog.etAnimal.text.toString().isNotEmpty()
                || bindingDialog.etAvgAge.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        addBucksList[position] =
                            RspAddBucksList(
                                bindingDialog.etBreedMaintained.text.toString(),
                                bindingDialog.etAnimal.text.toString().toInt(),
                                bindingDialog.etAvgAge.text.toString(),
                                id = selectedItem.id,
                                selectedItem.rsp_laboratory_semen_id
                            )
                        addBuckAdapter?.notifyItemChanged(position)
                    }

                } else {
                    addBucksList.add(
                        RspAddBucksList(
                            bindingDialog.etBreedMaintained.text.toString(),
                            bindingDialog.etAnimal.text.toString().toIntOrNull(),
                            bindingDialog.etAvgAge.text.toString(),
                            id = null,
                        )
                    )
                    addBucksList.size.minus(1).let {
                        addBuckAdapter?.notifyItemInserted(it)
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

            "District" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrict
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
        stateAdapter = BottomSheetAdapter(requireContext(), selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)


        // Rotate drawable
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
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
            requireContext(), loader, GetDropDownRequest(
                20,
                "Districts",
                currentPage,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    requireContext(),
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
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

    override fun onClickItem(selectedItem: RspAddBucksList, position: Int, isFrom: Int) {
        addBucks(requireContext(), isFrom, selectedItem, position)
    }
}