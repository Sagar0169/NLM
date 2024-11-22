package com.nlm.ui.activity.national_livestock_mission
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.ActivityFspplanStorageFormsBinding
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenBasicInformationFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.StateSemenInfrastructureFragment
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility

class FSPPlanStorageForms : BaseActivity<ActivityFspplanStorageFormsBinding>(),
    OnNextButtonClickListener, OnBackSaveAsDraft {
    override val layoutId: Int
        get() = R.layout.activity_fspplan_storage_forms
    private var mBinding: ActivityFspplanStorageFormsBinding? = null
    private var mDoubleBackToExitPressedOnce = false
    private var viewEdit: String? = null
    var itemId: Int? = null
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
            addTab(newTab().setText("To Be Filled By IA"))
            addTab(newTab().setText("To Be Filled By NLM"))
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
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
        else {
            Utility.clearAllFormFilledID(this@FSPPlanStorageForms)
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

}