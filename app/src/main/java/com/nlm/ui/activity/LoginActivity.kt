package com.nlm.ui.activity

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import com.nlm.R
import com.nlm.databinding.ActivityLoginBinding
import com.nlm.model.LoginRequest
import com.nlm.model.Result
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Preferences
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.getPreferenceString
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private var mBinding: ActivityLoginBinding? = null
    private var viewModel = ViewModel()
    private var isPasswordVisible = false

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        toast(getPreferenceString(this,PrefEntities.TOKEN ))
        Log.d("Scheme",getPreferenceString(this,AppConstants.SCHEME))
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    inner class ClickActions {

        fun login(view: View) {
//            startActivity(
//                Intent(this@LoginActivity,
//                    DashboardActivity::class.java)
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

        fun visiblePassword(view: View) {
            if (isPasswordVisible) {
                // Hide password
                mBinding?.etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
                mBinding?.ivPassEye?.setImageResource(R.drawable.ic_login_hide_eye) // Change to eye-off icon
                isPasswordVisible = false
            } else {
                // Show password
                mBinding?.etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                mBinding?.ivPassEye?.setImageResource(R.drawable.ic_eye_open) // Change to eye-on icon
                isPasswordVisible = true
            }

            // Move cursor to the end of the text after toggling
            mBinding?.etPassword?.text?.length?.let { it1 -> mBinding?.etPassword?.setSelection(it1) }
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
                    Preferences.setPreference(
                        this,AppConstants.SCHEME,Result(
                            userResponseModel._result.name,
                            userResponseModel._result.role_id,
                            userResponseModel._result.role_name,
                            userResponseModel._result.schemes,
                            userResponseModel._result.state_code,
                            userResponseModel._result.state_name,
                           null,
                            userResponseModel._result.user_id,
                            userResponseModel._result.username
                        )
                    )

                    userResponseModel._result.token?.let { it1 ->
                        Utility.savePreferencesString(
                            this, PrefEntities.TOKEN,
                            it1
                        )

                    }
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                }
            }
        }

        viewModel.errors.observe(this) {
            showSnackbar(mBinding!!.clParent, it)
        }
    }
}