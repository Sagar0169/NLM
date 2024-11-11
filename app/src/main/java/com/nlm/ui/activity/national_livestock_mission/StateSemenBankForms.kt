package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.ActivityStateSemenBankBinding
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAFormIAFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenManpowerFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenBasicInformationFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenInfrastructureFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenMajorClientsFragment
import com.nlm.utilities.BaseActivity

class StateSemenBankForms : BaseActivity<ActivityStateSemenBankBinding>(),
    OnNextButtonClickListener, OnBackSaveAsDraft {
    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank
    private var mBinding: ActivityStateSemenBankBinding? = null
    private var mDoubleBackToExitPressedOnce = false
    private var viewEdit: String? = null
    private var itemId: Int? = null
    private var dId: Int? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        loadFragment(StateSemenBasicInformationFragment(viewEdit, itemId,dId))
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    fun loadFragment(fragment: Fragment) {
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
            addTab(newTab().setText("Basic Information"))
            addTab(newTab().setText("Infrastructure for goat semen bank"))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            loadFragment(StateSemenBasicInformationFragment(viewEdit, itemId,dId))
                        }

                        1 -> {
                            loadFragment(StateSemenInfrastructureFragment(viewEdit, itemId,dId))
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
        loadFragment(StateSemenBasicInformationFragment(viewEdit, itemId,dId))
        // Select the first tab
        mBinding?.tabLayout?.getTabAt(0)?.select()
    }

    override fun onSaveAsDraft() {
        onBackPressedDispatcher.onBackPressed()
    }

}