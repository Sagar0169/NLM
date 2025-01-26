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
import com.nlm.model.Result

import com.nlm.model.details_Semen_Station
import com.nlm.ui.fragment.DetailsOfSemenStationFragment
import com.nlm.ui.fragment.SemenStationManpowerFragment
import com.nlm.utilities.AppConstants

import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme

class SemenStation : BaseActivity<ActivitySemenStationBinding>() {
    private var mBinding: ActivitySemenStationBinding? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    override val layoutId: Int
        get() = R.layout.activity_semen_station
    var details_Semen_Station = details_Semen_Station()
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        setupTabLayout()
        loadFragment(DetailsOfSemenStationFragment(viewEdit, itemId,dId))
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
        }

    }

    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("To Be Filled By IA"))
            if (getPreferenceOfScheme(
                    this@SemenStation,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                addTab(newTab().setText("To Be Filled By NLM"))

            }


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(DetailsOfSemenStationFragment(viewEdit, itemId,dId))
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(SemenStationManpowerFragment(viewEdit, itemId,dId))

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
    }


}