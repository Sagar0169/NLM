package com.nlm.ui.activity.national_dairy_development

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityAddDairyPlantVisitBinding
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.fragment.ndd.dpv.FirstDPVFragment
import com.nlm.ui.fragment.ndd.dpv.SecondDPVFragment
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.toast

class AddDairyPlantVisit : BaseActivity<ActivityAddDairyPlantVisitBinding>() {
    private var mBinding: ActivityAddDairyPlantVisitBinding? = null
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
        get() = R.layout.activity_add_dairy_plant_visit

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        mBinding!!.etState.setOnClickListener { showBottomSheetDialog("State") }
//        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("State") }
//        mBinding!!.tvDesignation.setOnClickListener { showBottomSheetDialog("Designation") }
        setupTabLayout()
        // Set default fragment

        loadFragment(FirstDPVFragment())

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
    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Part 1"))
            addTab(newTab().setText("Part 2"))

            // If you have only two tabs and want to ensure they occupy the entire width:
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(FirstDPVFragment())
                        1 -> loadFragment(SecondDPVFragment())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Optional: handle tab unselected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Optional: handle tab reselected
                }
            })
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}