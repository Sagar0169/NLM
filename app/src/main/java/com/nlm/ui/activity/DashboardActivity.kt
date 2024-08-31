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
import com.nlm.R
import com.nlm.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var mBinding: ActivityDashboardBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_dashboard

    override fun initView() {
        mBinding = viewDataBinding


        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }

        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mBinding?.leftDrawerMenu?.tvMasterImplementingAgency?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ImplementingAgencyMasterActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvMobileVeterinaryUnits?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MobileVeterinaryActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.ivEdit?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, EditProfile::class.java)
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
        mBinding?.leftDrawerMenu?.tvMilkUnionVisitReport?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MilkUnionVisitNDDActivity::class.java)
            startActivity(intent)
        }

    }
    override fun setVariables() {
    }

    override fun setObservers() {
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