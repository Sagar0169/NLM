package com.nlm.ui.activity.national_livestock_mission

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
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
import com.nlm.utilities.Utility

class NLMIAForm : BaseActivity<ActivityNlsiaFormBinding>(),OnNextButtonClickListener,
    OnBackSaveAsDraft {
    override val layoutId: Int
        get() = R.layout.activity_nlsia_form
    private var mBinding: ActivityNlsiaFormBinding? = null
    private var mDoubleBackToExitPressedOnce = false
    private var viewEdit: String? = null
    private var itemId: Int? = null

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
         viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId",0)
        loadFragment(NLSIAFormIAFragment(viewEdit,itemId))
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            Utility.clearAllFormFilledID(this@NLMIAForm)
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
        else {
            Utility.clearAllFormFilledID(this@NLMIAForm)
            onBackPressedDispatcher.onBackPressed()
            if (mDoubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            mDoubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(R.string.press_back_again),
                Toast.LENGTH_SHORT
            )
                .show()
            Handler(Looper.getMainLooper()).postDelayed(
                { mDoubleBackToExitPressedOnce = false },
                2000
            )

        }
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
                            loadFragment(NLSIAFormIAFragment(viewEdit,itemId))
                        }
                        1 -> {
                            loadFragment(NLSIAInfrastructureSheepGoat(viewEdit,itemId))
                        }
                        2 -> {
                            loadFragment(NLSIAGoverningBodyBoardOfDirectorsFragment(viewEdit,itemId))
                        }
                        3 -> {
                            loadFragment(NLSIAReportingSystem(viewEdit,itemId))
                        }
                        4 -> {
                            loadFragment(NLMDistrictWiseNoOfAiCenter(viewEdit,itemId))
                        }
                        5-> {
                            loadFragment(NLSIAConstraintsFacedByIAFragment(viewEdit,itemId))
                        }
                        6 -> {
                            loadFragment(NLSIAFeedFodderFragment(viewEdit,itemId))
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

     fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
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
        loadFragment(NLSIAFormIAFragment(viewEdit,itemId))

        // Select the first tab
        mBinding?.tabLayout?.getTabAt(0)?.select()
    }

    override fun onSaveAsDraft() {


        onBackPressedDispatcher.onBackPressed()
    }


}