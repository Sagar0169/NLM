package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityRspLabSemenBinding
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPAvailabilityOfEquipmentFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPAverageSemenDoseFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPBasicInformationFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPSuggestionsForImprovement
import com.nlm.utilities.BaseActivity

class RspLabSemenForms() : BaseActivity<ActivityRspLabSemenBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_rsp_lab_semen
    private var mBinding: ActivityRspLabSemenBinding? = null
    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(RSPBasicInformationFragment())
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
                            loadFragment(RSPBasicInformationFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(RSPAvailabilityOfEquipmentFragment())
                        }
                        2 -> {
                            onTabClicks()
                            loadFragment(RSPAverageSemenDoseFragment())
                        }

                        3 -> {
                            onTabClicks()
                            loadFragment(RSPSuggestionsForImprovement())
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