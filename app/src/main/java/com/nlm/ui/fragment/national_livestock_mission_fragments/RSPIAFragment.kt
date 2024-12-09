package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackDeleteFormatAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.callBack.CallBackSemenDose
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.databinding.ItemRspSemendoseBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
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
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.services.LocationService
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.SemenDoseAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterWithDialog
import com.nlm.ui.adapter.rgm.AvailabilityOfEquipmentAdapter
import com.nlm.ui.adapter.rgm.AverageSemenDoseAdapter
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread


class RSPIAFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) : BaseFragment<FragmentRSPBasicInformationBinding>(), CallBackAvilabilityEquipment,
    CallBackDeleteAtId, CallBackItemUploadDocEdit, CallBackDeleteFormatAtId,
    CallBackSemenDose, CallBackDeleteFSPAtId {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__basic_information
    private var mBinding: FragmentRSPBasicInformationBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var programmeList: MutableList<Array<String>>
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false
    private var viewModel = ViewModel()
    private var addDocumentAdapter: RSPSupportingDocumentIAAdapter? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: MutableList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private lateinit var DocumentListNLM: MutableList<ImplementingAgencyDocument>
    var body: MultipartBody.Part? = null
    private var rspEquipList = ArrayList<RspAddEquipment>()
    private var semenDoseList = ArrayList<RspAddAverage>()
    private lateinit var addBucksList: MutableList<RspLaboratorySemenAverage>
    private var rspEquipAdapter: AvailabilityOfEquipmentAdapter? = null
    private var semenAdapter: SemenDoseAdapter? = null
    private var currentPage = 1
    private var totalPage = 1
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var districtId: Int? = null // Store selected state
    private var listener: OnNextButtonClickListener? = null
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var latitude:Double?=null
    private var longitude:Double?=null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        }
    }

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            requireContext(),
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        addDocumentAdapter = RSPSupportingDocumentIAAdapter(
            requireContext(),
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                requireContext(),
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 8
        ) {
            mBinding?.tvState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.etLocation?.isEnabled = false
            mBinding?.etPincode?.isEnabled = false
            mBinding?.etPhone?.isEnabled = false
            mBinding?.etYear?.isEnabled = false
            mBinding?.etAddress?.isEnabled = false

            mBinding?.etAreaBuildings?.isEnabled = false
            mBinding?.etAreaFodder?.isEnabled = false
            mBinding?.etManpower?.isEnabled = false
            mBinding?.etNofcomp?.isEnabled = false
            mBinding?.etTypeOfRecords?.isEnabled = false
            mBinding?.etSIA1?.isEnabled = false
            mBinding?.etSIA2?.isEnabled = false
            mBinding?.etSIA3?.isEnabled = false
            mBinding?.etCOOP3?.isEnabled = false
            mBinding?.etCOOP2?.isEnabled = false
            mBinding?.etCOOP1?.isEnabled = false
            mBinding?.etNGO3?.isEnabled = false
            mBinding?.etNGO2?.isEnabled = false
            mBinding?.etNGO1?.isEnabled = false
            mBinding?.etPrivate1?.isEnabled = false
            mBinding?.etPrivate2?.isEnabled = false
            mBinding?.etPrivate3?.isEnabled = false
            mBinding?.etOtherState2?.isEnabled = false
            mBinding?.etOtherState3?.isEnabled = false
            mBinding?.etOtherState1?.isEnabled = false
            mBinding?.etInfra?.isEnabled = false

            mBinding?.tvAddMore1?.isEnabled = false
            mBinding?.tvAddMore1?.hideView()
            mBinding?.tvAddMore2?.isEnabled = false
            mBinding?.tvAddMore2?.hideView()
            mBinding?.tvAddSemenDose?.isEnabled = false
            mBinding?.tvAddSemenDose?.hideView()

            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            if (viewEdit == "view") {
                viewEditApi()
            }
        }
        if (viewEdit == "edit") {
            viewEditApi()


        }
        rspEquipAdapter()
        semenDoseAdapter()
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

    private fun semenDoseAdapter() {
        semenAdapter =
            SemenDoseAdapter(requireContext(), semenDoseList, viewEdit, this, this)
        mBinding?.rvSemenDose?.adapter = semenAdapter
        mBinding?.rvSemenDose?.layoutManager =
            LinearLayoutManager(requireContext())
    }


    private fun rspEquipAdapter() {
        rspEquipAdapter =
            AvailabilityOfEquipmentAdapter(requireContext(), rspEquipList, viewEdit, this, this)
        mBinding?.recyclerView4?.adapter = rspEquipAdapter
        mBinding?.recyclerView4?.layoutManager =
            LinearLayoutManager(requireContext())
    }


    override fun setVariables() {

    }
    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(
            locationReceiver,
            IntentFilter("LOCATION_UPDATED")
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(locationReceiver)
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
                            mBinding?.tvDistrict?.setTextColor(Color.parseColor("#000000"))
                            mBinding?.etPincode?.setText(userResponseModel._result.pin_code.toString())
                            mBinding?.etPhone?.setText(userResponseModel._result.phone_no.toString())
                            mBinding?.etYear?.setText(userResponseModel._result.year_of_establishment)
                            mBinding?.etAddress?.setText(userResponseModel._result.address)
                            mBinding?.etAreaBuildings?.setText(userResponseModel._result.area_under_buildings.toString())
                            mBinding?.etAreaFodder?.setText(userResponseModel._result.area_for_fodder_cultivation)
                            mBinding?.etManpower?.setText(userResponseModel._result.manpower)
                            mBinding?.etNofcomp?.setText(userResponseModel._result.availability_no_of_computers.toString())
                            mBinding?.etTypeOfRecords?.setText(userResponseModel._result.availability_type_of_records)
                            mBinding?.etSIA1?.setText(userResponseModel._result.major_clients_sia_one_year)
                            mBinding?.etSIA2?.setText(userResponseModel._result.major_clients_sia_two_year)
                            mBinding?.etSIA3?.setText(userResponseModel._result.major_clients_sia_three_year)
                            mBinding?.etCOOP1?.setText(userResponseModel._result.major_clients_coop_one_year)
                            mBinding?.etCOOP2?.setText(userResponseModel._result.major_clients_coop_two_year)
                            mBinding?.etCOOP3?.setText(userResponseModel._result.major_clients_coop_three_year)
                            mBinding?.etNGO1?.setText(userResponseModel._result.major_clients_ngo_one_year)
                            mBinding?.etNGO2?.setText(userResponseModel._result.major_clients_ngo_two_year)
                            mBinding?.etNGO3?.setText(userResponseModel._result.major_clients_ngo_three_year)
                            mBinding?.etPrivate1?.setText(userResponseModel._result.major_clients_private_one_year)
                            mBinding?.etPrivate2?.setText(userResponseModel._result.major_clients_private_two_year)
                            mBinding?.etPrivate3?.setText(userResponseModel._result.major_clients_private_three_year)
                            mBinding?.etOtherState1?.setText(userResponseModel._result.major_clients_other_states_one_year)
                            mBinding?.etOtherState2?.setText(userResponseModel._result.major_clients_other_states_two_year)
                            mBinding?.etOtherState3?.setText(userResponseModel._result.major_clients_other_states_three_year)
                            mBinding?.etInfra?.setText(userResponseModel._result.Infrastructure_faced_Institute)
                            rspEquipList.clear()
                            if (userResponseModel._result.rsp_laboratory_semen_availability_equipment?.isEmpty() == true && viewEdit == "view") {
                                // Add dummy data with default values
                                val dummyData = RspAddEquipment(
                                    id = 0, // Or null, depending on your use case
                                    list_of_equipment = "",
                                    make = "",
                                    year_of_procurement = "",

                                    )

                                rspEquipList.add(dummyData)
                            } else {
                                userResponseModel._result.rsp_laboratory_semen_availability_equipment?.let { it1 ->
                                    rspEquipList.addAll(
                                        it1
                                    )
                                }
                            }

                            DocumentList.clear()
                            totalListDocument.clear()
                            viewDocumentList.clear()
                            if (userResponseModel._result.rsp_laboratory_semen_document.isEmpty() && viewEdit == "view") {
                            } else {
                                userResponseModel._result.rsp_laboratory_semen_document.forEach { document ->
                                    if (document.nlm_document == null) {
                                        DocumentList.add(document)
                                    } else {
                                        viewDocumentList.add(document)

                                    }

                                }
                            }

                            addDocumentAdapter?.notifyDataSetChanged()


                            semenDoseList.clear()
                            if (userResponseModel._result.rsp_laboratory_semen_average.isEmpty() && viewEdit == "view") {
                                // Add dummy data with default values
                                val dummyData = RspAddAverage(
                                    id = 0, // Or null, depending on your use case
                                    name_of_breed = "",
                                    twentyOne_twentyTwo = "",
                                    twentyTwo_twentyThree = "",
                                    twentyThree_twentyFour = "",
                                )

                                semenDoseList.add(dummyData)
                            } else {
                                userResponseModel._result.rsp_laboratory_semen_average.let { it1 ->
                                    semenDoseList.addAll(
                                        it1
                                    )
                                }
                            }

                            semenAdapter?.notifyDataSetChanged()
                            rspEquipAdapter?.notifyDataSetChanged()


                        } else {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }
                    }


                }
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

    }

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun save(view: View) {
            totalListDocument.clear()
            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            Log.d("Data check", totalListDocument.toString())
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
            savedAsDraft = true

        }


        fun saveAsDraft(view: View) {
            totalListDocument.clear()
            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
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

        fun addDocDialog(view: View) {
            addDocumentDialog(requireContext(), null, null)
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1, null, null)
        }

        fun semenDose(view: View) {
            semenDoseDialog(requireContext(), 1, null, null)
        }
    }


//    private fun showError(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {

        val etAreaFodder = mBinding!!.etAreaFodder.text.toString()
        val etManpower = mBinding!!.etManpower.text.toString()
        val address = mBinding?.etAddress?.text.toString()
        val location = mBinding?.etLocation?.text.toString()
        val phoneNumber = mBinding?.etPhone?.text.toString()
        val pincode = mBinding?.etPincode?.text.toString()
        val state = mBinding?.tvState?.text.toString()
        val district = mBinding?.tvDistrict?.text.toString()
        val yearOfEstablishment = mBinding?.etYear?.text.toString()


        if (state == "Select") {
            mBinding?.clParent?.let { showSnackbar(it, "State Name is required") }
            return
        }
        if (district == "Select") {
            mBinding?.clParent?.let { showSnackbar(it, "District Name is required") }
            return
        }



        if (location.isEmpty()) {
            mBinding?.clParent?.let { showSnackbar(it, "Location is required") }
            return
        }

        if (address.isEmpty()) {
            mBinding?.clParent?.let { showSnackbar(it, "Address is required") }
            return
        }

        if (districtId == null) {
            mBinding?.clParent?.let { showSnackbar(it, "District is required") }
            return
        }

        if (pincode.isEmpty()) {
            mBinding?.clParent?.let { showSnackbar(it, "Pin code is required") }
            return
        }
        if (hasLocationPermissions()) {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireContext().startService(intent)
            lifecycleScope.launch {
                delay(1000) // Delay for 2 seconds
                if (latitude != null && longitude != null) {
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
                            area_under_buildings = mBinding?.etAreaBuildings?.text.toString().toDoubleOrNull(),
                            area_for_fodder_cultivation = etAreaFodder,
                            manpower = etManpower,
                            availability_no_of_computers = mBinding?.etNofcomp?.text.toString()
                                .toIntOrNull(),
                            availability_type_of_records = mBinding?.etTypeOfRecords?.text.toString(),
                            major_clients_sia_one_year = mBinding?.etSIA1?.text.toString(),
                            major_clients_sia_two_year = mBinding?.etSIA2?.text.toString(),
                            major_clients_sia_three_year = mBinding?.etSIA3?.text.toString(),
                            major_clients_coop_one_year = mBinding?.etCOOP1?.text.toString(),
                            major_clients_coop_two_year = mBinding?.etCOOP2?.text.toString(),
                            major_clients_coop_three_year = mBinding?.etCOOP3?.text.toString(),
                            major_clients_ngo_one_year = mBinding?.etNGO1?.text.toString(),
                            major_clients_ngo_two_year = mBinding?.etNGO2?.text.toString(),
                            major_clients_ngo_three_year = mBinding?.etNGO3?.text.toString(),
                            major_clients_private_one_year = mBinding?.etPrivate1?.text.toString(),
                            major_clients_private_two_year = mBinding?.etPrivate2?.text.toString(),
                            major_clients_private_three_year = mBinding?.etPrivate3?.text.toString(),
                            major_clients_other_states_one_year = mBinding?.etOtherState1?.text.toString(),
                            major_clients_other_states_two_year = mBinding?.etOtherState2?.text.toString(),
                            major_clients_other_states_three_year = mBinding?.etOtherState3?.text.toString(),
                            Infrastructure_faced_Institute = mBinding?.etInfra?.text.toString(),
                            rsp_laboratory_semen_availability_equipment = rspEquipList,
                            rsp_laboratory_semen_average = semenDoseList,
                            is_draft = draft,
                            rsp_laboratory_semen_document = totalListDocument,
                            lattitude_ia =latitude,
                            longitude_ia = longitude
                        )
                    )
                }
            else{
                    showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
            }}}

    }

    private fun compositionOfGoverningNlmIaDialog(
        context: Context,
        isFrom: Int,
        selectedItem: RspAddEquipment?,
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
        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
        lp.dimAmount = 0.5f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.showView()
        bindingDialog.tvMake.showView()
        bindingDialog.etMake.showView()
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etListOfEquipment.setText(selectedItem.list_of_equipment)
            bindingDialog.etYearOfProcurement.setText(selectedItem.year_of_procurement)
            bindingDialog.etMake.setText(selectedItem.make)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etListOfEquipment.text.toString()
                    .isNotEmpty() || bindingDialog.etYearOfProcurement.text.toString().isNotEmpty()
                || bindingDialog.etMake.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        rspEquipList[position] =
                            RspAddEquipment(
                                selectedItem.id,
                                bindingDialog.etListOfEquipment.text.toString(),
                                bindingDialog.etMake.text.toString(),
                                bindingDialog.etYearOfProcurement.text.toString(),
                                selectedItem.rsp_laboratory_semen_id
                            )
                        rspEquipAdapter?.notifyItemChanged(position)
                    }

                } else {
                    rspEquipList.add(
                        RspAddEquipment(
                            null,
                            bindingDialog.etListOfEquipment.text.toString(),
                            bindingDialog.etMake.text.toString(),
                            bindingDialog.etYearOfProcurement.text.toString(),
                        )
                    )
                    rspEquipList.size.minus(1).let {
                        rspEquipAdapter?.notifyItemInserted(it)
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

    private fun semenDoseDialog(
        context: Context,
        isFrom: Int,
        selectedItem: RspAddAverage?,
        position: Int?
    ) {
        val bindingDialog: ItemRspSemendoseBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_rsp_semendose,
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
        if (selectedItem != null && isFrom == 2) {
            bindingDialog.etNameOfBreed.setText(selectedItem.name_of_breed)
            bindingDialog.etTwentyTwo.setText(selectedItem.twentyOne_twentyTwo)
            bindingDialog.etTwentyThree.setText(selectedItem.twentyTwo_twentyThree)
            bindingDialog.etTwentyFour.setText(selectedItem.twentyThree_twentyFour)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etNameOfBreed.text.toString().isNotEmpty()
                || bindingDialog.etTwentyTwo.text.toString().isNotEmpty()
                || bindingDialog.etTwentyThree.text.toString().isNotEmpty()
                || bindingDialog.etTwentyFour.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        semenDoseList[position] =
                            RspAddAverage(
                                selectedItem.id,
                                bindingDialog.etNameOfBreed.text.toString(),
                                bindingDialog.etTwentyTwo.text.toString(),
                                bindingDialog.etTwentyThree.text.toString(),
                                bindingDialog.etTwentyFour.text.toString(),
                                selectedItem.rsp_laboratory_semen_id
                            )
                        semenAdapter?.notifyItemChanged(position)
                    }

                } else {
                    semenDoseList.add(
                        RspAddAverage(
                            null,
                            bindingDialog.etNameOfBreed.text.toString(),
                            bindingDialog.etTwentyTwo.text.toString(),
                            bindingDialog.etTwentyThree.text.toString(),
                            bindingDialog.etTwentyFour.text.toString(),
                            null
                        )
                    )
                    semenDoseList.size.minus(1).let {
                        semenAdapter?.notifyItemInserted(it)
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
            bindingDialog.etDoc.text = selectedItem.ia_document
            bindingDialog.etDescription.setText(selectedItem.description)
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
                        DocumentList[position] =
                            ImplementingAgencyDocument(
                                description = bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                nlm_document = null,
                                rsp_laboratory_semen_id = selectedItem.rsp_laboratory_semen_id,
                                id = selectedItem.id,
                            )
                        addDocumentAdapter?.notifyItemChanged(position)
                        dialog.dismiss()
                    }

                } else {
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
                                table_name = getString(R.string.rsp_laboratory_semen_document).toRequestBody(
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

    override fun onClickItem(selectedItem: RspAddEquipment, position: Int, isFrom: Int) {
        compositionOfGoverningNlmIaDialog(requireContext(), isFrom, selectedItem, position)

    }

    override fun onClickItem(selectedItem: RspAddAverage, position: Int, isFrom: Int) {
        semenDoseDialog(requireContext(), isFrom, selectedItem, position)

    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        position.let { it1 -> addDocumentAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        addDocumentDialog(requireContext(), selectedItem, position)
    }

    override fun onClickItemDelete(ID: Int?, position: Int) {
        position.let { it1 -> semenAdapter?.onDeleteButtonClick(it1) }
    }

    override fun onClickItemFormatDelete(ID: Int?, position: Int) {
        position.let { it1 -> rspEquipAdapter?.onDeleteButtonClick(it1) }
    }
}