package com.nlm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityNlsiaFormBinding
import com.nlm.databinding.ActivityRspLabSemenBinding
import com.nlm.ui.fragment.NLSIAFormIA
import com.nlm.ui.fragment.NLSIA_Agencies_involved_in_genetic_improvement_goat_sheep
import com.nlm.ui.fragment.NLSIA_Constraints_faced_by_IA
import com.nlm.ui.fragment.NLSIA_Feed_fodder
import com.nlm.ui.fragment.NLSIA_Fodder_Seed
import com.nlm.ui.fragment.NLSIA_GoverningBody_Board_Of_Directors
import com.nlm.ui.fragment.NLSIA_Infrastructure_Sheep_goat
import com.nlm.ui.fragment.NLSIA_Reporting_System
import com.nlm.ui.fragment.RSPManpower
import com.nlm.ui.fragment.RSP_Availability_of_equipment
import com.nlm.ui.fragment.RSP_Average_Semen_Dose
import com.nlm.ui.fragment.RSP_BasicInformation
import com.nlm.ui.fragment.RSP_Suggestions_for_improvement
import com.nlm.utilities.BaseActivity

class RspLabSemen() : BaseActivity<ActivityRspLabSemenBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_rsp_lab_semen
    private var mBinding: ActivityRspLabSemenBinding? = null
    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(RSP_BasicInformation())
    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }


        fun group(view: View){

        }

    }
    override fun setVariables() {

    }

    override fun setObservers() {

    }
    fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }
    private fun loadFragment(fragment: Fragment) {
//        if (fragment is SightedBasicDetailsFragment) {
//            fragment.setData(sightedChildData)
//        } else if (fragment is SightedFacialAttributesFragment) {
//            fragment.setData(facialAttributeData)
//        } else if (fragment is SightedPhysicalAttributeFragment) {
//            fragment.setData(physicalAttributesData)
//        } else if (fragment is SightedBackgroundFragment) {
//            fragment.setData(backgroundData)
//        }else if (fragment is SightedLocationDetailsFragment) {
//            fragment.setData(locationData)
//        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Basic Information"))
            addTab(newTab().setText("Manpower"))
            addTab(newTab().setText("Availability of equipment"))
//            addTab(newTab().setText("Composition of Advisory committee (if any)"))
//            addTab(newTab().setText("Project Monitoring Committee (PMC)"))
            addTab(newTab().setText("Average semen dose production of goat/sheep during last four years (breed wise)"))
            addTab(newTab().setText("Suggestions for improvement if any elaborate"))



            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(RSP_BasicInformation())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(RSPManpower())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(RSP_Availability_of_equipment())
                        }

//                        3 -> {
//                            onTabClicks()
//                            loadFragment(NLSIA_Composition_of_Advisory_committee())
//                        }
//
//                        4 -> {
//                            onTabClicks()
//                            loadFragment(NLSIA_PMC())
//                        }

                        3 -> {
                            onTabClicks()
                            loadFragment(RSP_Average_Semen_Dose())
                        }

                        4 -> {
                            onTabClicks()
                            loadFragment(RSP_Suggestions_for_improvement())
                        }

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
    fun onTabClicks() {
        // Collect data from current fragment
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
//        if (currentFragment is SightedBasicDetailsFragment) {
//            sightedChildData = currentFragment.getData()
//        } else if (currentFragment is SightedFacialAttributesFragment) {
//            facialAttributeData = currentFragment.getData()
//        } else if (currentFragment is SightedPhysicalAttributeFragment) {
//            physicalAttributesData = currentFragment.getData()
//        } else if (currentFragment is SightedBackgroundFragment) {
//            backgroundData = currentFragment.getData()
//        }else if (currentFragment is SightedLocationDetailsFragment) {
//            locationData = currentFragment.getData()
//        }
    }

}