package com.nlm.ui.activity


import android.view.View
import android.webkit.WebViewClient

import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivityAboutUsBinding
import com.nlm.databinding.ActivityEditProfileBinding

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private var mBinding: ActivityEditProfileBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_edit_profile

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