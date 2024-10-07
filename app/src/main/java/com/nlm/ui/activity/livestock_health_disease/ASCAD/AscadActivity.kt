package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityAscadBinding
import com.nlm.ui.activity.livestock_health_disease.StateMobileVeterinaryActivity
import com.nlm.utilities.BaseActivity

class AscadActivity : BaseActivity<ActivityAscadBinding>() {
    private var mBinding: ActivityAscadBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_ascad


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            val intent = Intent(
                this@AscadActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 8)
            startActivity(intent)
        }

        fun district(view: View) {
            val intent = Intent(
                this@AscadActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 9)
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