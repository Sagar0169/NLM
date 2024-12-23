package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.CallBackItemManPower
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenBasicInformationBinding
import com.nlm.databinding.ItemStateSemenManpowerBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.RspAddAverage
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.services.LocationService
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm
import com.nlm.ui.activity.national_livestock_mission.StateSemenBankForms
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RspManPowerAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StateSemenBasicInformationFragment(
    private val viewEdit: String?,
    private val itemId: Int?,
    private val dId: Int?
) :
    BaseFragment<FragmentStateSemenBasicInformationBinding>(), CallBackItemManPower {
    override val layoutId: Int
        get() = R.layout.fragment_state_semen__basic_information
    private var viewModel = ViewModel()
    private var mBinding: FragmentStateSemenBasicInformationBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var savedAsDraft: Boolean = false
    private lateinit var tabLayout: TabLayout
    protected lateinit var mActivityMain: StateSemenBankForms
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var districtId: Int? = null // Store selected state
    private var typeOfSemen: String? = null // Store selected state
    private var currentPage = 1
    private var totalPage = 1
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var stateSemenManPowerList: MutableList<StateSemenBankOtherAddManpower>
    private var stateSemenManPowerAdapter: RspManPowerAdapter? = null
    private var savedAsEdit: Boolean = false
    private var latitude:Double?=null
    private var longitude:Double?=null
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
    override fun onResume() {
        super.onResume()
        Log.d("EXECUTION","ON RESUME EXECUTED")
        val intentFilter = IntentFilter("LOCATION_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API level 26
            Log.d("Receiver", "Registering receiver with RECEIVER_NOT_EXPORTED")
            requireContext().registerReceiver(locationReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            Log.d("Receiver", "Registering receiver without RECEIVER_NOT_EXPORTED")
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationReceiver, intentFilter)
        }
    }
    override fun onPause() {
        super.onPause()
        Log.d("EXECUTION","ON PAUSE EXECUTED")
        requireContext().unregisterReceiver(locationReceiver)
    }


    private val typeSemen = listOf(
        ResultGetDropDown(id = 1, name = "Individual"),
        ResultGetDropDown(id = 2, name = "Integrated")
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        mBinding?.etState?.text = getPreferenceOfScheme(
            requireContext(),
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.etState?.isEnabled = false
        mActivityMain = activity as StateSemenBankForms
        if (viewEdit == "view") {
            mBinding?.tvAddMore1?.hideView()
            mBinding?.etState?.isEnabled = false
            mBinding?.tvDistrict?.isEnabled = false
            mBinding?.etLocation?.isEnabled = false
            mBinding?.etPincode?.isEnabled = false
            mBinding?.etPhone?.isEnabled = false
            mBinding?.etyear?.isEnabled = false
            mBinding?.etQuality?.isEnabled = false
            mBinding?.tvSemenStation?.isEnabled = false
            mBinding?.etAddress?.isEnabled = false
            mBinding?.etAreaUnderBuild?.isEnabled = false
            mBinding?.etAreaForFodder?.isEnabled = false
            mBinding?.etManPower?.isEnabled = false
            mBinding?.etOfficerInCharge?.isEnabled = false
            mBinding?.tvAddMore1?.isEnabled = false
            mBinding?.tvSaveDraft?.hideView()
            mBinding?.tvSendOtp?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()

        }
        mBinding?.tvSemenStation?.setOnClickListener {
            showBottomSheetDialog2("typeSemen")
        }
        stateSemenManPowerAdapter()

    }

    private fun stateSemenManPowerAdapter() {
        stateSemenManPowerList = mutableListOf()
        stateSemenManPowerAdapter =
            RspManPowerAdapter(stateSemenManPowerList, viewEdit, this)
        mBinding?.recyclerView1?.adapter = stateSemenManPowerAdapter
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
                            mBinding?.tvSemenStation?.text =
                                userResponseModel._result.type_of_semen_station
                            mBinding?.tvSemenStation?.setTextColor(Color.parseColor("#000000"))
                            if (userResponseModel._result.pin_code == 0) {
                                mBinding?.etPincode?.setText("")

                            } else {
                                mBinding?.etPincode?.setText(userResponseModel._result.manpower_no_of_people.toString())
                            }
                            if (userResponseModel._result.phone_no.toString()=="0") {
                                mBinding?.etPhone?.setText("")
                            } else {
                                mBinding?.etPhone?.setText(userResponseModel._result.phone_no.toString())
                            }
                            mBinding?.etPhone?.setText(userResponseModel._result.phone_no.toString())
                            mBinding?.etyear?.setText(userResponseModel._result.year_of_establishment)
                            mBinding?.etQuality?.setText(userResponseModel._result.quality_status)
                            mBinding?.etAddress?.setText(userResponseModel._result.address)
                            mBinding?.etAreaForFodder?.setText(userResponseModel._result.area_fodder_cultivation)
                            mBinding?.etAreaUnderBuild?.setText(userResponseModel._result.area_under_buildings)
                            if (userResponseModel._result.manpower_no_of_people == 0) {
                                mBinding?.etManPower?.setText("")

                            } else {
                                mBinding?.etManPower?.setText(userResponseModel._result.manpower_no_of_people.toString())

                            }
                            mBinding?.etOfficerInCharge?.setText(userResponseModel._result.officer_in_charge_name)
                            stateSemenManPowerList.clear()
                            if (userResponseModel._result.state_semen_bank_other_manpower.isEmpty() && viewEdit == "view") {
                                // Add dummy data with default values
                                val dummyData = StateSemenBankOtherAddManpower(
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                )

                                stateSemenManPowerList.add(dummyData)
                            } else {
                                userResponseModel._result.state_semen_bank_other_manpower.let { it1 ->
                                    stateSemenManPowerList.addAll(
                                        it1
                                    )
                                }
                            }

                            stateSemenManPowerAdapter?.notifyDataSetChanged()


                        } else {

                            Preferences.setPreference_int(
                                requireContext(),
                                AppConstants.FORM_FILLED_ID,
                                userResponseModel._result.id
                            )
                            mActivityMain.itemId = userResponseModel._result.id
                            listener?.onNextButtonClick()
                            showSnackbar(mBinding!!.clParent, userResponseModel.message)
                        }
                    }


                }
            }
        }

    }

    private fun compositionOfGoverningNlmIaDialog(
        context: Context,
        isFrom: Int,
        selectedItem: StateSemenBankOtherAddManpower?,
        position: Int?
    ) {
        val bindingDialog: ItemStateSemenManpowerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_state_semen_manpower,
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
            bindingDialog.etDesignation.setText(selectedItem.designation)
            bindingDialog.etQualification.setText(selectedItem.qualification)
            bindingDialog.etExperience.setText(selectedItem.experience)
            bindingDialog.etTrainingStatus.setText(selectedItem.training_status)
        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDesignation.text.toString()
                    .isNotEmpty() || bindingDialog.etQualification.text.toString()
                    .isNotEmpty() || bindingDialog.etExperience.text.toString()
                    .isNotEmpty() || bindingDialog.etTrainingStatus.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        stateSemenManPowerList[position] =
                            StateSemenBankOtherAddManpower(
                                bindingDialog.etDesignation.text.toString(),
                                bindingDialog.etQualification.text.toString(),
                                bindingDialog.etExperience.text.toString(),
                                bindingDialog.etTrainingStatus.text.toString(),
                                null, selectedItem.id,
                                selectedItem.state_semen_bank_id
                            )
                        stateSemenManPowerAdapter?.notifyItemChanged(position)
                    }

                } else {
                    stateSemenManPowerList.add(
                        StateSemenBankOtherAddManpower(
                            bindingDialog.etDesignation.text.toString(),
                            bindingDialog.etQualification.text.toString(),
                            bindingDialog.etExperience.text.toString(),
                            bindingDialog.etTrainingStatus.text.toString(),
                        )
                    )
                    stateSemenManPowerList.size.minus(1).let {
                        stateSemenManPowerAdapter?.notifyItemInserted(it)
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
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun saveAsDraft(view: View) {
            if (viewEdit == "view") {
                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId)
            } else {
                saveDataApi(null)
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
                saveDataApi(itemId)
            } else {
                saveDataApi(null)
            }

            // If all required fields are filled, proceed with the API call

        }

        private fun saveDataApi(itemId: Int?) {
            val areaForFodder = mBinding?.etAreaForFodder?.text.toString()
            val areaUnderBuilding = mBinding?.etAreaUnderBuild?.text.toString()
            val qualityStatus = mBinding?.etQuality?.text.toString()
            val yearOfEstablishment = mBinding?.etyear?.text.toString()
            val manpower = mBinding!!.etManPower.text.toString()
            val officerInCharge = mBinding!!.etOfficerInCharge.text.toString()
            val address = mBinding?.etAddress?.text.toString()
            val location = mBinding?.etLocation?.text.toString()
            val phoneNumber = mBinding?.etPhone?.text.toString()
            val pincode = mBinding?.etPincode?.text.toString()
            val state = mBinding?.tvState?.text.toString()
            val district = mBinding?.tvDistrict?.text.toString()


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

                        viewModel.getStateSemenAddBankApi(
                            requireContext(), true,
                            StateSemenBankNLMRequest(
                                id = itemId,
                                address = address,
                                area_fodder_cultivation = areaForFodder,
                                location = location,
                                area_under_buildings = areaUnderBuilding,
                                district_code = districtId,
                                phone_no = phoneNumber.toLongOrNull(),
                                pin_code = pincode.toIntOrNull(),
                                quality_status = qualityStatus,
                                type_of_semen_station = typeOfSemen,
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
                                year_of_establishment = yearOfEstablishment,
                                manpower_no_of_people = manpower.toIntOrNull(),
                                officer_in_charge_name = officerInCharge,
                                state_semen_bank_other_manpower = stateSemenManPowerList,
                                is_draft = 1,
                                lattitude = latitude,
                                longitude = longitude
                            )
                        )
                    }
                    else{
                        showSnackbar(mBinding?.clParent!!,"Please wait for a sec and click again")
                    }
                }
            }

        }


        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1, null, null)
        }

    }

    private fun viewEditApi() {

        viewModel.getStateSemenAddBankApi(
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

    private fun showBottomSheetDialog2(type: String) {
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
            "typeSemen" -> {
                selectedList = typeSemen
                selectedTextView = mBinding!!.tvSemenStation
            }
//
//            "StateNDD" -> {
//                selectedList = stateList
//                selectedTextView = binding!!.tvStateNDD
//            }

//            "District" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = districtList
//                selectedTextView = mBinding!!.tvDistrict
//            }

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
            typeOfSemen = selectedItem
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

//    private fun swipeForRefreshImplementingAgency() {
//        mBinding?.srlImplementingAgency?.setOnRefreshListener {
//            implementingAgencyAPICall(paginate = false, loader = true)
//            mBinding?.srlImplementingAgency?.isRefreshing = false
//        }
//    }

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

    override fun onClickItem(
        selectedItem: StateSemenBankOtherAddManpower,
        position: Int,
        isFrom: Int
    ) {
        compositionOfGoverningNlmIaDialog(requireContext(), isFrom, selectedItem, position)
    }


}