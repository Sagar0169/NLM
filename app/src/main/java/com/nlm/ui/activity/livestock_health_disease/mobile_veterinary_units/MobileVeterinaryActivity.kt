package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.content.Intent
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity

class MobileVeterinaryActivity : BaseActivity<ActivityMobileVeterinaryBinding>() {
    private var mBinding: ActivityMobileVeterinaryBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_mobile_veterinary


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            startActivity(Intent(
                this@MobileVeterinaryActivity,
                MobileVeterinaryListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.state)))
        }

        fun district(view: View) {
            startActivity(Intent(
                this@MobileVeterinaryActivity,
                MobileVeterinaryListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.district)))
        }

        fun block(view: View) {
            startActivity(Intent(
                this@MobileVeterinaryActivity,
                MobileVeterinaryListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.block_level)))
        }

        fun farmer(view: View) {
            startActivity(Intent(
                this@MobileVeterinaryActivity,
                MobileVeterinaryListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.farmer_level)))
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}