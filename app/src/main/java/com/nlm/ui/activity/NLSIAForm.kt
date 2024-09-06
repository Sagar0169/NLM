package com.nlm.ui.activity

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
import com.nlm.ui.fragment.NLSIAFormIA
import com.nlm.ui.fragment.NLSIA_Agencies_involved_in_genetic_improvement_goat_sheep
import com.nlm.ui.fragment.NLSIA_Constraints_faced_by_IA
import com.nlm.ui.fragment.NLSIA_Feed_fodder
import com.nlm.ui.fragment.NLSIA_Fodder_Seed
import com.nlm.ui.fragment.NLSIA_GoverningBody_Board_Of_Directors
import com.nlm.ui.fragment.NLSIA_Infrastructure_Sheep_goat
import com.nlm.ui.fragment.NLSIA_Reporting_System
import com.nlm.utilities.BaseActivity

class NLSIAForm() : BaseActivity<ActivityNlsiaFormBinding>() {
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
        loadFragment(NLSIAFormIA())


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
                            loadFragment(NLSIAFormIA())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(NLSIA_Infrastructure_Sheep_goat())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(NLSIA_GoverningBody_Board_Of_Directors())
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
                            loadFragment(NLSIA_Reporting_System())
                        }

                        4 -> {
                            onTabClicks()
                            loadFragment(NLSIA_Agencies_involved_in_genetic_improvement_goat_sheep())
                        }

                        5 -> {
                            onTabClicks()
                            loadFragment(NLSIA_Constraints_faced_by_IA())
                        }

                        6 -> {
                            onTabClicks()
                            loadFragment(NLSIA_Feed_fodder())
                        }

                        7 -> {
                            onTabClicks()
                            loadFragment(NLSIA_Fodder_Seed())
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