package com.nlm.ui.activity.rashtriya_gokul_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.ActivityRgmStateImplementingAgencyBinding
import com.nlm.ui.fragment.RGMStateImplementingAgencyNLMFragment
import com.nlm.ui.fragment.FundsReceivedRGMFragment
import com.nlm.ui.fragment.ImplementationOfNAIPRGMFragment
import com.nlm.ui.fragment.RGMCompositionOfGoverningFragment
import com.nlm.ui.fragment.RGMDetailsOfCommitteeMeetings
import com.nlm.ui.fragment.RGMIAOtherStaffFragment
import com.nlm.utilities.BaseActivity

class RGMStateImplementingAgency : BaseActivity<ActivityRgmStateImplementingAgencyBinding>(),
    OnNextButtonClickListener, OnBackSaveAsDraft {
    private var mBinding: ActivityRgmStateImplementingAgencyBinding? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    override val layoutId: Int
        get() = R.layout.activity_rgm_state_implementing_agency

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        setupTabLayout()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.extras?.getInt("itemId")
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
            addTab(newTab().setText("To be filled by NLM"))




            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(RGMIAOtherStaffFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(RGMCompositionOfGoverningFragment(viewEdit,itemId))

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
                            loadFragment(FundsReceivedRGMFragment(viewEdit,itemId))
                        }
                        4 -> {
                            onTabClicks()
                            loadFragment(ImplementationOfNAIPRGMFragment(viewEdit,itemId))
                        }
                        5 -> {
                            onTabClicks()
                            loadFragment(RGMStateImplementingAgencyNLMFragment(viewEdit,itemId))
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

    }
    private fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

    override fun onNextButtonClick() {
        moveToNextTab()
    }

    override fun onNavigateToFirstFragment() {
        // Load the first fragment (Implementing Agency)
        loadFragment(RGMIAOtherStaffFragment())

        // Select the first tab
        mBinding?.tabLayout?.getTabAt(0)?.select()
    }

    override fun onSaveAsDraft() {
        onBackPressedDispatcher.onBackPressed()
    }

}