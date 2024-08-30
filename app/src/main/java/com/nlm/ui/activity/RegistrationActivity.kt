package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivityRegistrationBinding

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {
    private var mBinding: ActivityRegistrationBinding? = null
    private var isSelected: Boolean? = false
    private var isSelectedDistrict: Boolean? = false
    private lateinit var stateAdapter: StateAdapter

    //    private lateinit var districtAdapter: DistrictAdapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override val layoutId: Int
        get() = R.layout.activity_registration

    private val stateList = listOf(
        "Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia"
    )
    private val districtList = listOf(
        // Andhra Pradesh
        "Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna",
        "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam",
        "Vizianagaram", "West Godavari", "YSR Kadapa",
    )

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        callAdapter()
//        callAdapterDistrict()


        mBinding!!.rlDistrict.setOnClickListener {
            if (isSelectedDistrict == false) {
                isSelectedDistrict = true
                mBinding!!.ivArrowDownDistrict.isVisible = true
                mBinding?.ivArrowUpDistrict?.isVisible = false
                showBottomSheetDialog("district") // Specify type as district
            } else {
                isSelectedDistrict = false
                mBinding?.ivArrowDownDistrict?.isVisible = false
                mBinding?.ivArrowUpDistrict?.isVisible = true
            }
        }
        mBinding!!.rlSelectCategory.setOnClickListener {
            if (isSelected == false) {
                isSelected = true
                mBinding!!.ivArrowDown.isVisible = true
                mBinding?.ivArrowUp?.isVisible = false
                showBottomSheetDialog("state") // Specify type as state
            } else {
                isSelected = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
            }
        }


        mBinding!!.ivArrowUp.setOnClickListener {
            isSelected = true
            mBinding?.ivArrowDown?.isVisible = true
            mBinding?.ivArrowUp?.isVisible = false
            showBottomSheetDialog("state")
//            mBinding?.llCategory?.isVisible = true
        }



        mBinding!!.ivArrowUpDistrict.setOnClickListener {
            isSelected = true
            mBinding?.ivArrowDownDistrict?.isVisible = true
            mBinding?.ivArrowUpDistrict?.isVisible = false
            showBottomSheetDialog("district") // Specify type as district
//            mBinding?.llDistrict?.isVisible = true
        }
        mBinding!!.ivArrowDownDistrict.setOnClickListener {
            isSelectedDistrict = false
            mBinding?.ivArrowDownDistrict?.isVisible = false
            mBinding?.ivArrowUpDistrict?.isVisible = true
//            mBinding?.llDistrict?.isVisible = false
        }

    }

    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT // Set height to 80% of the screen
        )

        // Properly find the RecyclerView from the inflated view
        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener{
            bottomSheetDialog.dismiss()
        }

        // Set up the adapter based on the type
        if (type == "state") {
            stateAdapter = StateAdapter(stateList, this) { selectedItem ->
                // Handle state item click
                mBinding!!.tvSelectCategory.text = selectedItem
                bottomSheetDialog.dismiss()
            }
            rvBottomSheet.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvBottomSheet.adapter = stateAdapter
            bottomSheetDialog.setContentView(view)

            // Set a dismiss listener to reset the view visibility
            bottomSheetDialog.setOnDismissListener {
                isSelected = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
            }
        } else if (type == "district") {
            stateAdapter = StateAdapter(districtList, this) { selectedItem ->
                // Handle district item click
                mBinding!!.tvSelectDistrict.text = selectedItem
                bottomSheetDialog.dismiss()
            }
            rvBottomSheet.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvBottomSheet.adapter = stateAdapter
            bottomSheetDialog.setContentView(view)

            // Set a dismiss listener to reset the view visibility
            bottomSheetDialog.setOnDismissListener {
                isSelected = false
                mBinding?.ivArrowDownDistrict?.isVisible = false
                mBinding?.ivArrowUpDistrict?.isVisible = true
            }
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
    }


//    private fun bottomSheetAdapter() {
//        val rvBottomSheet = findViewById<RecyclerView>(R.id.rvBottomSheet)
//        stateAdapter = StateAdapter(stateList, this)
//        rvBottomSheet?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rvBottomSheet?.adapter = stateAdapter
//    }


    fun onClickItem(selectedItem: String) {
        mBinding!!.tvSelectCategory.text = selectedItem
        mBinding?.llCategory?.isVisible = false
        mBinding?.ivArrowDown?.isVisible = false
        mBinding?.ivArrowUp?.isVisible = true
    }

    fun onClickItemDistrict(selectedItem: String) {
        mBinding!!.tvSelectDistrict.text = selectedItem
        mBinding?.llDistrict?.isVisible = false
        mBinding?.ivArrowDownDistrict?.isVisible = false
        mBinding?.ivArrowUpDistrict?.isVisible = true
    }

//    private fun callAdapter() {
//        stateAdapter = StateAdapter(stateList, this)
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        mBinding!!.rvCategory.layoutManager = layoutManager
//        mBinding!!.rvCategory.adapter = stateAdapter
//    }

//    private fun callAdapterDistrict() {
//        districtAdapter = DistrictAdapter(stateList, this, this)
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        mBinding!!.rvDistrict.layoutManager = layoutManager
//        mBinding!!.rvDistrict.adapter = districtAdapter
//    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun login(view: View) {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        fun otp(view: View) {
            val intent = Intent(this@RegistrationActivity, OtpActivity::class.java)
            startActivity(intent)
        }
    }

}