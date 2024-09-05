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
import com.nlm.databinding.ActivityRgmStateImplementingAgencyBinding
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.databinding.FragmentImplementationOfNAIPRGMBinding
import com.nlm.model.RGM_IA
import com.nlm.ui.fragment.Details_of_Semen_Station
import com.nlm.ui.fragment.Fragment_Assets_not_being_used
import com.nlm.ui.fragment.Funds_Received_RGM
import com.nlm.ui.fragment.Implementation_of_NAIP_RGM
import com.nlm.ui.fragment.RGM_Composition_Of_governing
import com.nlm.ui.fragment.RGM_Details_of_committee_meetings
import com.nlm.ui.fragment.RGM_IA_Other_Staff
import com.nlm.ui.fragment.SemenStaion_Manpower
import com.nlm.ui.fragment.Semen_production_and_semen_doses_distributed
import com.nlm.ui.fragment.Sex_Sorted_Semen
import com.nlm.utilities.BaseActivity

class RGM_State_Implementing_Agency : BaseActivity<ActivityRgmStateImplementingAgencyBinding>() {
    private var mBinding: ActivityRgmStateImplementingAgencyBinding? = null
    override val layoutId: Int
        get() = R.layout.activity_rgm_state_implementing_agency

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(RGM_IA_Other_Staff())
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
            addTab(newTab().setText("Other Staff"))
            addTab(newTab().setText("Composition of Governing Body / Board of DIrectors of IA"))
            addTab(newTab().setText("Details of committee meetings"))
            addTab(newTab().setText("Funds Received:- (In Lakhs)"))
            addTab(newTab().setText("Implementation of NAIP"))
            addTab(newTab().setText("Any of the assets created under RGM and not being used. Specify the reasons"))




            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(RGM_IA_Other_Staff())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(RGM_Composition_Of_governing())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(RGM_Details_of_committee_meetings())
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
                            loadFragment(Funds_Received_RGM())
                        }
                        4 -> {
                            onTabClicks()
                            loadFragment(Implementation_of_NAIP_RGM())
                        }
                        5 -> {
                            onTabClicks()
                            loadFragment(Fragment_Assets_not_being_used())
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