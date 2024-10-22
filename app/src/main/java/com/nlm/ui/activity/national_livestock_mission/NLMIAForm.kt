package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.SwitchFragmentCallBack
import com.nlm.databinding.ActivityNlsiaFormBinding
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFormIAFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLMDistrictWiseNoOfAiCenter
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAConstraintsFacedByIAFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFeedFodderFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAGoverningBodyBoardOfDirectorsFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAInfrastructureSheepGoat
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAReportingSystem

import com.nlm.utilities.BaseActivity

class NLMIAForm() : BaseActivity<ActivityNlsiaFormBinding>(),SwitchFragmentCallBack {
    override val layoutId: Int
        get() = R.layout.activity_nlsia_form
    private var mBinding: ActivityNlsiaFormBinding? = null
    private var IAFragment: NLSIAFormIAFragment? = null

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        IAFragment=NLSIAFormIAFragment(this)
        loadFragment(IAFragment)
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


    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Implementing Agency"))
            addTab(newTab().setText("Available infrastructure for Sheep / Goat Genetic Improvement"))
            addTab(newTab().setText("Composition of Advisory committee (if any)/Project Monitoring Committee (PMC)"))
            addTab(newTab().setText("Reporting System"))
            addTab(newTab().setText("District wise no. of AI centres for Goat and Sheep in the State "))
            addTab(newTab().setText("Constraints faced by IA in implementation of the project (elaborate)"))
            addTab(newTab().setText("Feed Fodder Situation in the State"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(IAFragment)
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(NLSIAInfrastructureSheepGoat(0))

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(NLSIAGoverningBodyBoardOfDirectorsFragment())
                        }

                        3 -> {
                            onTabClicks()
                            loadFragment(NLSIAReportingSystem())
                        }

                        4 -> {
                            onTabClicks()
                            loadFragment(NLMDistrictWiseNoOfAiCenter())
                        }


                        5-> {
                            onTabClicks()
                            loadFragment(NLSIAConstraintsFacedByIAFragment())
                        }

                        6 -> {
                            onTabClicks()
                            loadFragment(NLSIAFeedFodderFragment())
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

    private fun loadFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.replace(R.id.frameLayout, fragment)
        }
        transaction.commit()
    }

    override fun onClickItem(fragment: Fragment, tabId: Int) {
        loadFragment(fragment)
        mBinding?.tabLayout?.getTabAt(tabId)?.select()
    }
}