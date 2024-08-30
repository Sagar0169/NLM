package com.nlm.ui.activity

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout

import com.nlm.download_manager.AndroidDownloader
import com.nlm.ui.fragment.BasicDetailsFragment
import com.nlm.ui.fragment.ConfirmationFragments
import com.nlm.ui.fragment.FacialAttributesFragment
import com.nlm.ui.fragment.FamilyDetailsFragment
import com.nlm.ui.fragment.FirDetailsFragment
import com.nlm.ui.fragment.LocationDetailsFragment
import com.nlm.ui.fragment.PhysicalAttributesFragment
import com.nlm.ui.fragment.SightedUploadFragment
import com.nlm.ui.fragment.UploadMediaFragment
import com.nlm.utilities.BaseActivity
import nlm.R
import nlm.databinding.ActivityChildMissingBinding

class ChildMissingActivity : BaseActivity<ActivityChildMissingBinding>() ,
    BasicDetailsFragment.OnNextButtonClickListener,
    FacialAttributesFragment.OnNextButtonClickListener,
    PhysicalAttributesFragment.OnNextButtonClickListener,
    FamilyDetailsFragment.OnNextButtonClickListener,
    LocationDetailsFragment.OnNextButtonClickListener,
    UploadMediaFragment.OnNextButtonClickListener,
    FirDetailsFragment.OnNextButtonClickListener{

    private var mBinding: ActivityChildMissingBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_child_missing

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        // Set default fragment

        loadFragment(BasicDetailsFragment())
        showCustomDialog()
    }


    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun login(view: View) {
            val intent = Intent(this@ChildMissingActivity, OtpActivity::class.java)
            startActivity(intent)

        }

        fun register(view: View) {
            val intent = Intent(this@ChildMissingActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
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

    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Basic Details"))
            addTab(newTab().setText("Facial Attributes"))
            addTab(newTab().setText("Physical Attributes"))
            addTab(newTab().setText("Family Details"))
            addTab(newTab().setText("Location details"))
            addTab(newTab().setText("Upload Media"))

            addTab(newTab().setText("Fir detail"))

            addTab(newTab().setText("Confirmation"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(BasicDetailsFragment())
                        1 -> loadFragment(FacialAttributesFragment())
                        2 -> loadFragment(PhysicalAttributesFragment())
                        3 -> loadFragment(FamilyDetailsFragment())
                        4 -> loadFragment(LocationDetailsFragment())
                        5 -> loadFragment(SightedUploadFragment())
                        6 -> loadFragment(FirDetailsFragment())
                        7 -> loadFragment(ConfirmationFragments())
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

    override fun onNextButtonClick() {
        moveToNextTab()
    }
}