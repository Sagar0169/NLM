package com.nlm.ui.activity

import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
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
        }

        fun district(view: View) {
        }

        fun block(view: View) {
        }

        fun farmer(view: View) {
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