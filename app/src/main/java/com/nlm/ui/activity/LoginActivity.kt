package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private var mBinding: ActivityLoginBinding? = null


    override val layoutId: Int
        get() = R.layout.activity_login


    inner class ClickActions {

        fun login(view: View) {
            val intent = Intent(this@LoginActivity, OtpActivity::class.java)
            startActivity(intent)
        }

        fun register(view: View) {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)

        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
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