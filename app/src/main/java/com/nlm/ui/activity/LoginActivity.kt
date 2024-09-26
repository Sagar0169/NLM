package com.nlm.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityLoginBinding
import com.nlm.model.LoginRequest
import com.nlm.utilities.BaseActivity
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
//            if (valid())
//                viewModel.getLoginApi(
//                    this@LoginActivity, LoginRequest(
//                        mBinding!!.etUsername.text.toString().trim(),
//                        mBinding!!.etPassword.text.toString().trim()
//                    )
//                )

        val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
            startActivity(intent)

        }

        fun register(view: View) {
//            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
//            startActivity(intent)

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
        } else if (!(Utility.isValidEmail(mBinding!!.etUsername.text.toString().trim()))) {
            showSnackbar(mBinding!!.clParent, getString(R.string.enter_valid_id))
            return false
        } else if (mBinding!!.etPassword.text.toString().trim().isEmpty()) {
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

//                    userResponseModel._result.userId.let { it1 ->
//                        Utility.savePreferencesInt(
//                            this, AppConstants.USER_ID,
//                            it1
//                        )
//                    }
//                    userResponseModel._result.usertype.let { it1 ->
//                        Utility.savePreferencesString(
//                            this, AppConstants.USER_TYPE,
//                            it1
//                        )
//                    }
//                    userResponseModel._result.token.let { it1 ->
//                        Utility.savePreferencesString(
//                            this, com.nlm.utilities.PrefEntities.TOKEN,
//                            it1
//                        )
//                    }
//                    if (userResponseModel._result.usertype == "Admin") {
//                        startActivity(
//                            Intent(
//                                this@LoginActivity,
//                                DashboardActivity::class.java
//                            ).putExtra(AppConstants.INFO, 3)
//                        )
//                        finishAffinity()
//                    } else {
//                        startActivity(
//                            Intent(
//                                this@LoginActivity,
//                                DashboardActivity::class.java
//                            ).putExtra(AppConstants.INFO, 3)
//                        )
//                        finishAffinity()
//                    }
                }
            }
        }

        viewModel.errors.observe(this) {
            showSnackbar(mBinding!!.clParent, it)
        }
    }

}