package com.nlm.ui.activity

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddMilkProcessingBinding
import com.nlm.databinding.ActivityAddMilkProductMarketingBinding
import com.nlm.databinding.ActivityAddProductivityEnhancemmentServicesBinding
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.toast

class AddProductivityEnhancementServices : BaseActivity<ActivityAddProductivityEnhancemmentServicesBinding>() {
    private var mBinding: ActivityAddProductivityEnhancemmentServicesBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private val stateList = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
    )

    override val layoutId: Int
        get() = R.layout.activity_add_productivity_enhancemment_services

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        mBinding!!.etState.setOnClickListener { showBottomSheetDialog("State") }
//        mBinding!!.tvDesignation.setOnClickListener { showBottomSheetDialog("Designation") }

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


    override fun setVariables() {
    }

    override fun setObservers() {
    }


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun submit(view: View) {
            toast("Milk Union Visit Report Added Successfully")
//            val intent =
//                Intent(this@AddImplementingAgency, ImplementingAgencyMasterActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}