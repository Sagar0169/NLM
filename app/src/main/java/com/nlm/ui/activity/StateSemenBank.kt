package com.nlm.ui.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityStateSemenBankBinding
import com.nlm.ui.fragment.StateSemenManpowerFragment
import com.nlm.ui.fragment.StateSemenBasicInformationFragment
import com.nlm.ui.fragment.StateSemenInfrastructureFragment
import com.nlm.ui.fragment.StateSemenMajorClientsFragment
import com.nlm.utilities.BaseActivity

class StateSemenBank : BaseActivity<ActivityStateSemenBankBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank
    private var mBinding: ActivityStateSemenBankBinding? = null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(StateSemenBasicInformationFragment())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

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
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }}
    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Basic Information"))
            addTab(newTab().setText("Manpower"))
            addTab(newTab().setText("Infrastructure for goat semen bank"))
//            addTab(newTab().setText("Composition of Advisory committee (if any)"))
//            addTab(newTab().setText("Project Monitoring Committee (PMC)"))
            addTab(newTab().setText("Major clients of semen& Quantity supplied"))




            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(StateSemenBasicInformationFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(StateSemenManpowerFragment())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(StateSemenInfrastructureFragment())
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
                            loadFragment(StateSemenMajorClientsFragment())
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