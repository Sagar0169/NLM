package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.content.Intent
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.ui.activity.livestock_health_disease.StateMobileVeterinaryActivity
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
            val intent = Intent(
                this@MobileVeterinaryActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 1)
            startActivity(intent)
        }

        fun district(view: View) {
            val intent = Intent(
                this@MobileVeterinaryActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 2)
            startActivity(intent)
        }

        fun block(view: View) {
            val intent = Intent(
                this@MobileVeterinaryActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 3)
            startActivity(intent)
        }

        fun farmer(view: View) {
            val intent = Intent(
                this@MobileVeterinaryActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 4)
            startActivity(intent)
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