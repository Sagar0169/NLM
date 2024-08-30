package com.nlm.ui.activity

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.download_manager.AndroidDownloader
import com.nlm.model.BackgroundData
import com.nlm.model.FacialAttributeData
import com.nlm.model.LocationData
import com.nlm.model.PhysicalAttributesData
import com.nlm.model.SightedChildData
import com.nlm.ui.fragment.SightedBackgroundFragment
import com.nlm.ui.fragment.SightedBasicDetailsFragment
import com.nlm.ui.fragment.SightedConfirmationFragment
import com.nlm.ui.fragment.SightedFacialAttributesFragment
import com.nlm.ui.fragment.SightedLocationDetailsFragment
import com.nlm.ui.fragment.SightedPhysicalAttributeFragment
import com.nlm.ui.fragment.SightedUploadFragment
import com.nlm.utilities.BaseActivity
import nlm.R
import nlm.databinding.ActivitySightedChildBinding


class SightedChildActivity : BaseActivity<ActivitySightedChildBinding>(),
    SightedBasicDetailsFragment.OnNextButtonClickListener,
    SightedFacialAttributesFragment.OnNextButtonClickListener,
    SightedPhysicalAttributeFragment.OnNextButtonClickListener,
    SightedBackgroundFragment.OnNextButtonClickListener,
    SightedLocationDetailsFragment.OnNextButtonClickListener,
    SightedUploadFragment.OnNextButtonClickListener,
    SightedConfirmationFragment.OnNextButtonClickListener {
    var sightedChildData = SightedChildData()
    var facialAttributeData = FacialAttributeData()
    var physicalAttributesData = PhysicalAttributesData()
    var backgroundData = BackgroundData()
    var locationData = LocationData()

    private var mBinding: ActivitySightedChildBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_sighted_child

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        showCustomDialog()
        setupTabLayout()
        loadFragment(SightedBasicDetailsFragment())

    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_caution) // Replace with your dialog layout file name

        val tvPocsoLink: TextView = dialog.findViewById(R.id.tvPocsoLink)
        val btnProceed: Button = dialog.findViewById(R.id.btnProceed)

        // Handle the click on the POCSO Act link
        tvPocsoLink.setOnClickListener {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
            val downloader = AndroidDownloader(this)
            downloader.downloadFile("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
        }

        // Handle the click on the Proceed button
        btnProceed.setOnClickListener {
            dialog.dismiss()
            // Continue with the proceed action
        }

        dialog.show()
    }

    fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

    override fun onNextButtonClick() {
        // Collect data from current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is SightedBasicDetailsFragment) {
            sightedChildData = currentFragment.getData()
        } else if (currentFragment is SightedFacialAttributesFragment) {
            facialAttributeData = currentFragment.getData()
        } else if (currentFragment is SightedPhysicalAttributeFragment) {
            physicalAttributesData = currentFragment.getData()
        } else if (currentFragment is SightedBackgroundFragment) {
            backgroundData = currentFragment.getData()
        }else if (currentFragment is SightedLocationDetailsFragment) {
            locationData = currentFragment.getData()
        }
        moveToNextTab()
    }

    fun onTabClicks() {
        // Collect data from current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is SightedBasicDetailsFragment) {
            sightedChildData = currentFragment.getData()
        } else if (currentFragment is SightedFacialAttributesFragment) {
            facialAttributeData = currentFragment.getData()
        } else if (currentFragment is SightedPhysicalAttributeFragment) {
            physicalAttributesData = currentFragment.getData()
        } else if (currentFragment is SightedBackgroundFragment) {
            backgroundData = currentFragment.getData()
        }else if (currentFragment is SightedLocationDetailsFragment) {
            locationData = currentFragment.getData()
        }
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
            addTab(newTab().setText("Basic Details"))
            addTab(newTab().setText("Facial Attributes"))
            addTab(newTab().setText("Physical Attributes"))
            addTab(newTab().setText("Background"))
            addTab(newTab().setText("Location details"))
            addTab(newTab().setText("Upload Media"))
            addTab(newTab().setText("Confirmation"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            onTabClicks()
                            loadFragment(SightedBasicDetailsFragment())
                        }

                        1 -> {
                            onTabClicks()
                            loadFragment(SightedFacialAttributesFragment())

                        }

                        2 -> {
                            onTabClicks()
                            loadFragment(SightedPhysicalAttributeFragment())
                        }

                        3 -> {
                            onTabClicks()
                            loadFragment(SightedBackgroundFragment())
                        }

                        4 -> {
                            onTabClicks()
                            loadFragment(SightedLocationDetailsFragment())
                        }

                        5 -> {
                            onTabClicks()
                            loadFragment(SightedUploadFragment())
                        }

                        6 -> {
                            onTabClicks()
                            loadFragment(SightedConfirmationFragment())
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
        if (fragment is SightedBasicDetailsFragment) {
            fragment.setData(sightedChildData)
        } else if (fragment is SightedFacialAttributesFragment) {
            fragment.setData(facialAttributeData)
        } else if (fragment is SightedPhysicalAttributeFragment) {
            fragment.setData(physicalAttributesData)
        } else if (fragment is SightedBackgroundFragment) {
            fragment.setData(backgroundData)
        }else if (fragment is SightedLocationDetailsFragment) {
            fragment.setData(locationData)
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }


}