package com.nlm.ui.activity.rashtriya_gokul_mission

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.ActivityAddRgmvitroFertilizatonBinding
import com.nlm.model.NlmEdp
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.ui.fragment.RGMIAOtherStaffFragment
import com.nlm.ui.fragment.VitroPartOneFragment
import com.nlm.ui.fragment.VitroPartTwoFragment
import com.nlm.utilities.BaseActivity

class AddRGMVitroFertilizatonActivity : BaseActivity<ActivityAddRgmvitroFertilizatonBinding>(),
    OnNextButtonClickListener, OnBackSaveAsDraft {
    private var mBinding: ActivityAddRgmvitroFertilizatonBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private lateinit var onlyCreated: List<NlmEdp>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0
    private var viewEdit: String? = null
    var itemId: Int? = null

    override val layoutId: Int
        get() = R.layout.activity_add_rgmvitro_fertilizaton


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.extras?.getInt("itemId")
        setupTabLayout()
        loadFragment(VitroPartOneFragment(viewEdit,itemId))

    }

    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("To be filled by IA"))
            addTab(newTab().setText("To be filled by NLM"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
//                            onTabClicks()
                            loadFragment(VitroPartOneFragment(viewEdit,itemId))
                        }

                        1 -> {
//                            onTabClicks()
                            loadFragment(VitroPartTwoFragment(viewEdit,itemId))

                        }


//

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

    override fun setVariables() {
    }

    override fun setObservers() {
    }
    override fun onSaveAsDraft() {
        onBackPressedDispatcher.onBackPressed()
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
        loadFragment(VitroPartOneFragment(viewEdit,itemId))

        // Select the first tab
        mBinding?.tabLayout?.getTabAt(0)?.select()
    }
}