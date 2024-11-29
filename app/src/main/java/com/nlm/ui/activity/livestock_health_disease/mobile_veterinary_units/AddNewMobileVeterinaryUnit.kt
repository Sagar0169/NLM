package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitBinding
import com.nlm.model.Indicator
import com.nlm.ui.adapter.AddNewMobileVeterinaryUnitAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.showView

class AddNewMobileVeterinaryUnit : BaseActivity<ActivityAddNewMobileVeterinaryUnitBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitBinding? = null
    private lateinit var addNewMobileUnit: AddNewMobileVeterinaryUnitAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null



    override val layoutId: Int
        get() = R.layout.activity_add_new_mobile_veterinary_unit


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)

        when (isFrom) {
            getString(R.string.district) -> {
                mBinding!!.tvHeading.text = "Add New District Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
            }

            getString(R.string.block_level) -> {
                mBinding!!.tvHeading.text = "Add New Block Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
                mBinding!!.llBlock.showView()

            }

            getString(R.string.farmer_level) -> {
                mBinding!!.tvHeading.text = "Add New Farmer Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
                mBinding!!.llBlock.showView()
                mBinding!!.llFarmer.showView()

            }
        }
//        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
//        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }

    }


    override fun setVariables() {
    }

    override fun setObservers() {
    }
}