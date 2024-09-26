package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityNlsiaFormBinding
import com.nlm.model.BackgroundData
import com.nlm.model.FacialAttributeData
import com.nlm.model.LocationData
import com.nlm.model.PhysicalAttributesData
import com.nlm.model.SightedChildData
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFormIAFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAAgenciesInvolvedInGeneticImprovementGoatSheep
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAConstraintsFacedByIAFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFeedFodderFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFodderSeedFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAGoverningBodyBoardOfDirectorsFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAInfrastructureSheepGoat
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAReportingSystem
import com.nlm.utilities.BaseActivity

class NLMIAForm() : BaseActivity<ActivityNlsiaFormBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_nlsia_form
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var mBinding: ActivityNlsiaFormBinding? = null
    var sightedChildData = SightedChildData()
    var facialAttributeData = FacialAttributeData()
    var physicalAttributesData = PhysicalAttributesData()
    var backgroundData = BackgroundData()
    var locationData = LocationData()

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        loadFragment(NLSIAFormIAFragment())


    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }


        fun group(view: View) {

        }

    }

    fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

//    override fun onNextButtonClick() {
//        // Collect data from current fragment
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
////        if (currentFragment is SightedBasicDetailsFragment) {
////            sightedChildData = currentFragment.getData()
////        } else if (currentFragment is SightedFacialAttributesFragment) {
////            facialAttributeData = currentFragment.getData()
////        } else if (currentFragment is SightedPhysicalAttributeFragment) {
////            physicalAttributesData = currentFragment.getData()
////        } else if (currentFragment is SightedBackgroundFragment) {
////            backgroundData = currentFragment.getData()
////        }else if (currentFragment is SightedLocationDetailsFragment) {
////            locationData = currentFragment.getData()
////        }
//        moveToNextTab()
//    }

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


    override fun onBackPressed() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        if (currentTab > 0) {
            // Move to the previous tab if not on the first tab
            mBinding?.tabLayout?.getTabAt(currentTab - 1)?.select()
        } else {
            // If on the first tab, you can either call super to finish the activity or handle it differently
            super.onBackPressed()
        }
    }


    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Implementing Agency"))
            addTab(newTab().setText("Available infrastructure for Sheep / Goat Genetic Improvement"))
            addTab(newTab().setText("Composition of Governing Body / Board of Directors of Regional semen bank"))
//            addTab(newTab().setText("Composition of Advisory committee (if any)"))
//            addTab(newTab().setText("Project Monitoring Committee (PMC)"))
            addTab(newTab().setText("Reporting System"))
            addTab(newTab().setText("Funds Received"))
            addTab(newTab().setText("Constraints faced by IA in implementation of the project (elaborate)"))
            addTab(newTab().setText("Feed Fodder Situation in the State"))
            addTab(newTab().setText("Fodder Seed procurement and Distribution"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(NLSIAFormIAFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(NLSIAInfrastructureSheepGoat())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(NLSIAGoverningBodyBoardOfDirectorsFragment())
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
                            loadFragment(NLSIAReportingSystem())
                        }

                        4 -> {
                            onTabClicks()
                            loadFragment(NLSIAAgenciesInvolvedInGeneticImprovementGoatSheep())
                        }

                        5 -> {
                            onTabClicks()
                            loadFragment(NLSIAConstraintsFacedByIAFragment())
                        }

                        6 -> {
                            onTabClicks()
                            loadFragment(NLSIAFeedFodderFragment())
                        }

                        7 -> {
                            onTabClicks()
                            loadFragment(NLSIAFodderSeedFragment())
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
}