package com.nlm.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityLoginBinding
import com.nlm.model.LoginRequest
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private var mBinding: ActivityLoginBinding? = null
    var viewModel = ViewModel()

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
    }


    inner class ClickActions {

        fun login(view: View) {
//            startActivity(
//                Intent(
//                    this@LoginActivity,
//                    DashboardActivity::class.java
//                ).putExtra(AppConstants.INFO, 3)
//            )
            if (valid())
                viewModel.getLoginApi(
                    this@LoginActivity, LoginRequest(
                        mBinding!!.etUsername.text.toString().trim(),
                        mBinding!!.etPassword.text.toString().trim()
                    )
                )
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun valid(): Boolean {
        if (mBinding!!.etUsername.text.toString().trim()
                .isEmpty() && mBinding!!.etPassword.text.toString().trim().isEmpty()
        ) {
            showSnackbar(mBinding!!.clParent, getString(R.string.mandatory_fields))
            return false
        }
        return if (mBinding!!.etUsername.text.toString().trim().isEmpty()) {
            showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_email_id))
            false
        }
        else if (mBinding!!.etPassword.text.toString().trim().isEmpty()) {
            showSnackbar(mBinding!!.clParent, getString(R.string.Please_enter_password))
            false
        } else true
    }


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.loginResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                } else {

                    Log.d("Login Data", userResponseModel._resultflag.toString())


                    userResponseModel._result.user_id?.let { it1 ->
                        Utility.savePreferencesInt(
                            this, AppConstants.USER_ID,
                            it1
                        )
                    }

                    userResponseModel._result.role_id?.let { it1 ->
                        Utility.savePreferencesString(
                            this, AppConstants.ROLE_ID,
                            it1
                        )
                    }
                    userResponseModel._result.token?.let { it1 ->
                        Utility.savePreferencesString(
                            this, PrefEntities.TOKEN,
                            it1
                        )

                    }

                    userResponseModel._result.role_name?.let { it1 ->
                        Utility.savePreferencesString(this,AppConstants.ROLE_NAME,
                            it1
                        )
                    }
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            )
                        )
                        finishAffinity()

                }
            }
        }

        viewModel.errors.observe(this) {
            showSnackbar(mBinding!!.clParent, it)
        }
    }

}