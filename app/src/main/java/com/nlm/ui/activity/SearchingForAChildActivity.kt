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
import com.nlm.ui.fragment.QuickSearchFragment
import com.nlm.ui.fragment.TextSeachFragment
import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivitySearchingForAchildBinding

class SearchingForAChildActivity() : BaseActivity<ActivitySearchingForAchildBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_searching_for_achild

    private var mBinding: ActivitySearchingForAchildBinding?=null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        // Set default fragment

        loadFragment(QuickSearchFragment())
        showCustomDialog()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

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
            addTab(newTab().setText("Quick Search"))
            addTab(newTab().setText("Text Search"))



            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(QuickSearchFragment())
                        1 -> loadFragment(TextSeachFragment())
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


}