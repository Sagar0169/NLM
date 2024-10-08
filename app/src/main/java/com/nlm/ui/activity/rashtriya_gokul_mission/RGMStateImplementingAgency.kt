package com.nlm.ui.activity.rashtriya_gokul_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityRgmStateImplementingAgencyBinding
import com.nlm.ui.fragment.FragmentAssetsNotBeingUsed
import com.nlm.ui.fragment.FundsReceivedRGMFragment
import com.nlm.ui.fragment.ImplementationOfNAIPRGMFragment
import com.nlm.ui.fragment.RGMCompositionOfGoverningFragment
import com.nlm.ui.fragment.RGMDetailsOfCommitteeMeetings
import com.nlm.ui.fragment.RGMIAOtherStaffFragment
import com.nlm.utilities.BaseActivity

class RGMStateImplementingAgency : BaseActivity<ActivityRgmStateImplementingAgencyBinding>() {
    private var mBinding: ActivityRgmStateImplementingAgencyBinding? = null
    override val layoutId: Int
        get() = R.layout.activity_rgm_state_implementing_agency

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(RGMIAOtherStaffFragment())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    private fun loadFragment(fragment: Fragment) {

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
            addTab(newTab().setText("Other Staff"))
            addTab(newTab().setText("Composition of Governing Body / Board of DIrectors of IA"))
            addTab(newTab().setText("Details of committee meetings"))
            addTab(newTab().setText("Funds Received:- (In Lakhs)"))
            addTab(newTab().setText("Implementation of NAIP"))
//            addTab(newTab().setText("Any of the assets created under RGM and not being used. Specify the reasons"))




            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(RGMIAOtherStaffFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(RGMCompositionOfGoverningFragment())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(RGMDetailsOfCommitteeMeetings())
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
                            loadFragment(FundsReceivedRGMFragment())
                        }
                        4 -> {
                            onTabClicks()
                            loadFragment(ImplementationOfNAIPRGMFragment())
                        }
                        5 -> {
                            onTabClicks()
                            loadFragment(FragmentAssetsNotBeingUsed())
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