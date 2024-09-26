package com.nlm.ui.activity.rashtriya_gokul_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.model.BackgroundData
import com.nlm.model.FacialAttributeData
import com.nlm.model.LocationData
import com.nlm.model.PhysicalAttributesData

import com.nlm.model.details_Semen_Station
import com.nlm.ui.fragment.DetailsOfSemenStationFragment
import com.nlm.ui.fragment.SemenStationManpowerFragment

import com.nlm.utilities.BaseActivity

class SemenStation : BaseActivity<ActivitySemenStationBinding>(),DetailsOfSemenStationFragment.OnNextButtonClickListener {
    private var mBinding: ActivitySemenStationBinding? = null
    override val layoutId: Int
        get() = R.layout.activity_semen_station
    var details_Semen_Station = details_Semen_Station()
    var facialAttributeData = FacialAttributeData()
    var physicalAttributesData = PhysicalAttributesData()
    var backgroundData = BackgroundData()
    var locationData = LocationData()
    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        loadFragment(DetailsOfSemenStationFragment())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    private fun loadFragment(fragment: Fragment) {
        if (fragment is DetailsOfSemenStationFragment) {
            fragment.setData(details_Semen_Station)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Details of Semen Station"))
            addTab(newTab().setText("Manpower"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(DetailsOfSemenStationFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(SemenStationManpowerFragment())

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
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is DetailsOfSemenStationFragment) {
            details_Semen_Station = currentFragment.getData()}
    }

    override fun onNextButtonClick() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is DetailsOfSemenStationFragment) {
            details_Semen_Station = currentFragment.getData()}
        moveToNextTab()
    }
    private fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

}