package com.nlm.ui.activity

import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityUserBinding
import com.nlm.utilities.BaseActivity

class UserActivity : BaseActivity<ActivityUserBinding>() {

    private var mBinding: ActivityUserBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_user

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressedDispatcher.onBackPressed()
        }
    }
}