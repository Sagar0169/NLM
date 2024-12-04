package com.nlm.ui.activity.livestock_health_disease.ascad

import android.content.Intent
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityAscadBinding
import com.nlm.utilities.AppConstants
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
            startActivity(Intent(
                this@AscadActivity,
                AscadListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.state)))
        }

        fun district(view: View) {
            startActivity(
                Intent(
                    this@AscadActivity,
                    AscadListActivity::class.java
                ).putExtra(AppConstants.IS_FROM, getString(R.string.district))
            )
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