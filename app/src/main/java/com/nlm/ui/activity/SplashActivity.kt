package com.nlm.ui.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.nlm.R
import com.nlm.databinding.ActivitySplashBinding
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    private var mBinding: ActivitySplashBinding? = null
    private var mDelay = 3000
    override fun initView() {
        mBinding=viewDataBinding
        Handler(Looper.getMainLooper()).postDelayed({
            if (Utility.getPreferenceString(this@SplashActivity, PrefEntities.TOKEN).isNotEmpty())
            {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, mDelay.toLong())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
}