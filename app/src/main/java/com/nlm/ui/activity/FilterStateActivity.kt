package com.nlm.ui.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityFilterStateBinding
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataArtificialInsemination
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel

class FilterStateActivity : BaseActivity<ActivityFilterStateBinding>() {
    private var binding: ActivityFilterStateBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var stateList = ArrayList<ResultGetDropDown>()
    private var layoutManager: LinearLayoutManager? = null
    private var stateId: Int? = null // Store selected state
    private var districtId: Int? = null // Store selected state
    private var districtName: String? = null // Store selected state
    private var Model:String? = null // Store selected state


    //    private val stateList = listOf(
//        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
//        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
//        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
//        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
//        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
//        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
//        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
//        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
//    )
//    private val status = listOf(
//        "Active", "In Active"
//    )
//    private val reading = listOf(
//        "Yes", "No"
//    )
    override val layoutId: Int
        get() = R.layout.activity_filter_state

    override fun initView() {
        binding = viewDataBinding
        binding?.clickAction = ClickActions()
        viewModel.init()
        val isFrom = intent?.getIntExtra("isFrom", 0)
        val selectedStateId = intent.getIntExtra("selectedStateId", 0)
        val selectedDistrictId = intent.getIntExtra("districtId", 0)
        val selectedLocation = intent.getStringExtra("selectedLocation")
        val phoneNo = intent.getStringExtra("phoneNo")
        districtName = intent.getStringExtra("districtName")

       val LiquidNitrogen=intent.getStringExtra("LiquidNitrogen")
        val FrozenSemen=intent.getStringExtra("FrozenSemen")
        val Cryocans=intent.getStringExtra("Cryocans")
        val districtname=intent.getStringExtra("districtName")
        val NoOfFarmer=intent.getStringExtra("NumberOfFarmers")


        when (isFrom) {

            0 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()

                binding!!.tvLocationNDD.text = "Name/Location"
                binding!!.tvState.text = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_name.toString()
                if (getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_name?.isNotEmpty() == true
                ) {
                    binding!!.tvState.isEnabled = false
                    binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.black))

                    stateId = getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code
                }
                binding!!.tvLocationNDD.showView()
                if (selectedLocation != null) {
                    binding?.etLocationNDD?.setText(selectedLocation)
                }
                binding!!.etLocationNDD.showView()
            }

            1 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
            }

            2 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
            }

            3 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleBlock.showView()
                binding!!.etBlock.showView()

            }

            4 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleBlock.showView()
                binding!!.etBlock.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }

            5 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
            }

            6 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
            }

            7 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }

            8 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()

            }

            9 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()

            }

            11 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleLocAi.text = "Location"
                binding!!.tvTitleLocAi.showView()
                binding!!.etLocAi.showView()

            }

            12 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleAiName.showView()
                binding!!.etAiName.showView()
                binding!!.tvTitleLocAi.showView()
                binding!!.etLocAi.showView()
            }

            13 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNoa.showView()
                binding!!.etNoa.showView()
                binding!!.tvTitleOrganogram.showView()
                binding!!.etOrganogram.showView()
                binding!!.tvTitleTechnicalCompetance.showView()
                binding!!.etTechnicalCompetance.showView()
            }

            14 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNoa.showView()
                binding!!.etNoa.showView()
                binding!!.tvTitleLocationAddress.showView()
                binding!!.etLocationAddress.showView()
                binding!!.tvTitleCapacityofPlant.showView()
                binding!!.etCapacityofPlant.showView()
            }

            15 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNameofAgency.showView()
                binding!!.etNameofAgency.showView()
                binding!!.tvTitleLocationAddress.showView()
                binding!!.etLocationAddress.showView()
                binding!!.tvTitleAreaCovered.showView()
                binding!!.etAreaCovered.showView()
            }

            16 -> {
                binding!!.tvTitleReadingMaterial.showView()
                binding!!.tvReadingMaterial.showView()

            }

            17 -> {
                binding!!.tvTitleCommentProgress.showView()
                binding!!.etCommentProgress.showView()

            }

            25 -> {

                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvNameofMilkUnion.showView()
                binding!!.etNameofMilkUnion.showView()

            }

            26 -> {
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvfssaiLicNo.showView()
                binding!!.etfssaiLicNo.showView()


            }

            27 -> {
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvfssaiLicNo.showView()
                binding!!.etfssaiLicNo.showView()
                binding!!.tvnameOfDcs.showView()
                binding!!.etnameOfDcs.showView()
            }

            28 -> {
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvLocationNDD.showView()
                binding!!.etLocationNDD.showView()
            }

            29 -> {
                binding!!.tvTitleNDD.showView()
                binding!!.etTitleNDD.showView()
                binding!!.tvStatus.showView()

            }

            30 -> {
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.etNameofRetailShop.showView()
                binding!!.tvNameofRetailShop.showView()
                binding!!.tvNameOfMPCFederation.showView()
                binding!!.etNameOfMPCFederation.showView()
                binding!!.tvDateOfInspection.showView()
                binding!!.etDateOfInspection.showView()

            }

            31 -> {
                binding!!.tvTitleNDD.showView()
                binding!!.etTitleNDD.showView()
                binding!!.tvStatus.showView()

            }

            32 -> {
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()
            }

            33 -> {
                binding!!.tvNameOfBeneficiaery.showView()
                binding!!.etNameOfBeneficiary.showView()

            }

            34 -> {
                binding!!.tvState.showView()
                binding!!.tvTitleState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvPhoneNo.showView()
                binding!!.etPhoneno.showView()
                binding!!.tvTitleLoc.showView()
                binding!!.etLoc.showView()
                binding!!.tvYear.showView()
                binding!!.tvTitleYear.showView()

                if (selectedLocation != null) {
                    binding?.etLoc?.setText(selectedLocation)
                }
                if (phoneNo != null) {
                    binding?.etPhoneno?.setText(phoneNo)
                }
                if (districtName != null) {
                    binding?.tvDistrict?.text = districtName
                    binding!!.tvDistrict.setTextColor(ContextCompat.getColor(this, R.color.black))
                }


                binding!!.tvState.text = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_name.toString()
                if (getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_name?.isNotEmpty() == true
                ) {
                    binding!!.tvState.isEnabled = false
                    binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.black))

                    stateId = getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code
                }


            }

            35 -> {
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }
            36-> {
                binding!!.tvState.text = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_name.toString()
                if (getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_name?.isNotEmpty() == true
                ) {
                    binding!!.tvState.isEnabled = false
                    binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.black))

                    stateId = getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code
                }

                binding!!.tvState.showView()
                binding!!.tvTitleState.showView()
                binding!!.tvFrozenSemen.showView()
                binding!!.etFrozenSemen.showView()
                binding!!.etFrozenSemen.setText(FrozenSemen)

                binding!!.tvLiquidNitrogen.showView()
                binding!!.etLiquidNitrogen.showView()
                binding!!.etLiquidNitrogen.setText(LiquidNitrogen)
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvDistrict.text=districtname
                binding!!.tvCryocans.showView()
                binding!!.etCryocans.setText(Cryocans)


            }
            37 -> {
                binding!!.tvNoOfFarmer.showView()
                binding!!.etNoOfFarmer.showView()
                binding!!.etNoOfFarmer.setText(NoOfFarmer)
            }


            else -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleNodalName.showView()
                binding!!.etNodalName.showView()
                binding!!.tvTitleNodalEmail.showView()
                binding!!.etNodalOfficerEmail.showView()
            }
        }
        binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.grey))
        binding!!.tvDistrict.setTextColor(ContextCompat.getColor(this, R.color.grey))
        binding!!.tvState.setOnClickListener {
            showBottomSheetDialog("State")

        }
        binding!!.tvStateNDD.setOnClickListener { showBottomSheetDialog("StateNDD") }
        binding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
        binding!!.tvStatus.setOnClickListener { showBottomSheetDialog("Status") }
        binding!!.tvReadingMaterial.setOnClickListener { showBottomSheetDialog("Reading") }

    }

    private fun dropDownApiCallDistrict(paginate: Boolean, loader: Boolean) {
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

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {

        if (paginate) {
            currentPage++
        }
        if (Model=="Districts") {
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    20,
                    Model,
                    currentPage,
                    state_code = stateId,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
        else{
            viewModel.getDropDownApi(
                this, loader, GetDropDownRequest(
                    20,
                    Model,
                    currentPage,
                    null,
                    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                )
            )
        }
        }



    private fun showBottomSheetDialog(type: String) {
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
            "State" -> {
                Model="State"
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = binding!!.tvState
            }

            "StateNDD" -> {
                selectedList = stateList
                selectedTextView = binding!!.tvStateNDD
            }

            "District" -> {
                dropDownApiCallDistrict(paginate = false, loader = true)
                selectedList = stateList // Update the list to districtList for District
                Model="Districts"
                selectedTextView = binding!!.tvDistrict
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle item click
            selectedTextView.text = selectedItem

            // Store the appropriate ID based on the type
            if (type == "State") {
                stateId = id  // Save the selected state ID
            } else if (type == "District") {
                districtName = selectedItem
                districtId = id  // Save the selected district ID
            }

            if (Model=="Districts")
            {
                districtName=selectedItem
                districtId=id
            }
            else{
                stateId = id
            }
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateList.clear()

                        val remainingCount = userResponseModel.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 10
                            count
                        } else {
                            val count = userResponseModel.total_count / 10
                            count + 1
                        }
                    }
                    stateList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()

//                    mBinding?.tvNoDataFound?.hideView()
//                    mBinding?.rvArtificialInsemination?.showView()
                } else {
//                    mBinding?.tvNoDataFound?.showView()
//                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }

    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun clear(view: View) {
            val isFrom = intent?.getIntExtra("isFrom", 0)

            if (isFrom == 0 && stateId != null) {
                binding!!.etLocationNDD.setText("")
            }
            if (isFrom == 36 && stateId != null) {
                binding!!.etFrozenSemen.setText("")
                binding!!.etLiquidNitrogen.setText("")
                binding!!.etCryocans.setText("")
                binding!!.tvDistrict.text=""
            }
            if (isFrom == 34 && stateId != null) {
                // Prepare intent to send the result back
                binding!!.etPhoneno.setText("")
                binding!!.etLoc.setText("")
                binding!!.tvDistrict.text = "Please Select"
                districtId = null
            }
        }


        fun submit(view: View) {
            val isFrom = intent?.getIntExtra("isFrom", 0)

            if (isFrom == 0 && stateId != null) {
                // Prepare intent to send the result back
                val resultIntent = Intent()
                resultIntent.putExtra("nameLocation", binding!!.etLocationNDD.text.toString())
                resultIntent.putExtra("stateId", stateId) // Add selected data to intent
                setResult(RESULT_OK, resultIntent) // Send result
                finish()
            }
            if (isFrom == 36 && stateId != null) {
                // Prepare intent to send the result back
                val resultIntent = Intent()
                resultIntent.putExtra("FrozenSemen", binding!!.etFrozenSemen.text.toString())
                resultIntent.putExtra("LiquidNitrogen", binding!!.etLiquidNitrogen.text.toString())
                resultIntent.putExtra("Cryocans", binding!!.etCryocans.text.toString())
                resultIntent.putExtra("DistrictId", districtId)
                resultIntent.putExtra("stateId", stateId) // Add selected data to intent
                resultIntent.putExtra("districtName", districtName) // Add selected data to intent
                setResult(RESULT_OK, resultIntent) // Send result
                finish()
            }
            if (isFrom == 34 && stateId != null) {
                // Prepare intent to send the result back
                val resultIntent = Intent()
                resultIntent.putExtra("nameLocation", binding!!.etLoc.text.toString())
                resultIntent.putExtra("etPhoneno", binding!!.etPhoneno.text.toString())
                resultIntent.putExtra("stateId", stateId) // Add selected data to intent
                resultIntent.putExtra("districtId", districtId) // Add selected data to intent
                resultIntent.putExtra("districtName", districtName) // Add selected data to intent
                resultIntent.putExtra("year", stateId) // Add selected data to intent
                setResult(RESULT_OK, resultIntent) // Send result
                toast(stateId.toString())
                finish()
            }
            if (isFrom == 37 ) {
                // Prepare intent to send the result back
                val resultIntent = Intent()
                resultIntent.putExtra("NumberOfFarmers", binding!!.etNoOfFarmer.text.toString())
                       // Add selected data to intent
                setResult(RESULT_OK, resultIntent) // Send result
                finish()
            }


            // Close the activity

        }
    }
}
