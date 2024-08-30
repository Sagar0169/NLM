package com.nlm.ui.activity


import android.view.View
import android.webkit.WebViewClient

import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivityAboutUsBinding

class AboutUsActivity() : BaseActivity<ActivityAboutUsBinding>() {
    private var mBinding: ActivityAboutUsBinding? = null



    override val layoutId: Int
        get() = R.layout.activity_about_us

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        mBinding?.tvAboutUsAndTermsAndCondition?.webViewClient= WebViewClient()
        mBinding?.tvAboutUsAndTermsAndCondition?.settings?.javaScriptEnabled = true
        mBinding?.tvAboutUsAndTermsAndCondition?.loadUrl("http://hrcares.in/privacy_policy")
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