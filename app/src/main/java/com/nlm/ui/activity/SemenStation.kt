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
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.databinding.ActivityStateSemenBankBinding
import com.nlm.model.BackgroundData
import com.nlm.model.FacialAttributeData
import com.nlm.model.LocationData
import com.nlm.model.PhysicalAttributesData
import com.nlm.model.SightedChildData
import com.nlm.model.details_Semen_Station
import com.nlm.ui.fragment.Details_of_Semen_Station
import com.nlm.ui.fragment.SemenStaion_Manpower
import com.nlm.ui.fragment.Semen_production_and_semen_doses_distributed
import com.nlm.ui.fragment.Sex_Sorted_Semen
import com.nlm.ui.fragment.SightedBasicDetailsFragment
import com.nlm.ui.fragment.StateSemenManpower
import com.nlm.ui.fragment.StateSemen_BasicInformation
import com.nlm.ui.fragment.State_Semen_Infrastructure
import com.nlm.ui.fragment.State_Semen_Major_Clients
import com.nlm.utilities.BaseActivity

class SemenStation : BaseActivity<ActivitySemenStationBinding>(),Details_of_Semen_Station.OnNextButtonClickListener {
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
        loadFragment(Details_of_Semen_Station())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    private fun loadFragment(fragment: Fragment) {
        if (fragment is Details_of_Semen_Station) {
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
            addTab(newTab().setText("Semen production and semen doses distributed (breed wise)"))
            addTab(newTab().setText("Sex Sorted Semen: ( To be filled by NLM)"))




            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(Details_of_Semen_Station())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(SemenStaion_Manpower())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(Semen_production_and_semen_doses_distributed())
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
                            loadFragment(Sex_Sorted_Semen())
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
        if (currentFragment is Details_of_Semen_Station) {
            details_Semen_Station = currentFragment.getData()}
    }

    override fun onNextButtonClick() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is Details_of_Semen_Station) {
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