package com.nlm.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.app.Dialog
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.nlm.utilities.BaseActivity
import nlm.R
import nlm.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var mBinding: ActivityDashboardBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_dashboard

    override fun initView() {
        mBinding = viewDataBinding


        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }
        mBinding?.leftDrawerMenu?.tvform4?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, EditProfile::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvHome?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, DashboardActivity::class.java)
            startActivity(intent)
        }

        mBinding?.leftDrawerMenu?.tvChangePassword?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }


        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mBinding?.leftDrawerMenu?.ivEdit?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, EditProfile::class.java)
            startActivity(intent)
        }

        mBinding?.contentNav?.tvMyChildMissing?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ChildMissingActivity::class.java)
            startActivity(intent)
//            showCustomDialog()
        }

        mBinding?.contentNav?.tvEnterDetails?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SightedChildActivity::class.java)
            startActivity(intent)
//            showCustomDialog()
        }

        mBinding?.leftDrawerMenu?.tvform1?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ChildMissingActivity::class.java)
            startActivity(intent)

        }
        mBinding?.leftDrawerMenu?.tvform2?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SightedChildActivity::class.java)
            startActivity(intent)

        }
        mBinding?.leftDrawerMenu?.tvform3?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SearchingForAChildActivity::class.java)
            startActivity(intent)
        }
        mBinding?.contentNav?.tvSearchChild?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SearchingForAChildActivity::class.java)
            startActivity(intent)

        }
        mBinding?.leftDrawerMenu?.tvPrivacyPolicy?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AboutUsActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvTermsAndConditions?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AboutUsActivity::class.java)
            startActivity(intent)
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
            Log.d("CustomDialog", "POCSO Act 2012 link clicked")
        }

        // Handle the click on the Proceed button
        btnProceed.setOnClickListener {
            dialog.dismiss()
            // Continue with the proceed action
        }

        dialog.show()
    }


    private fun toggleLeftDrawer() {
        if (mBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.END)
        } else {
            mBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
        // This will close the app and all the activities in the task.
    }


}