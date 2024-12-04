package com.nlm.ui.activity

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
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
import com.nlm.utilities.crypt.EncryptionHelper
import com.nlm.viewModel.ViewModel
import com.nlm.biometric.BiometricPromptManager
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private var mBinding: ActivityLoginBinding? = null
    private var viewModel = ViewModel()
    private var isPasswordVisible = false

    override val layoutId: Int
        get() = R.layout.activity_login
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        val isRemembered = getPreferenceString(this, AppConstants.REMEMBER_MEE) == "isChecked"
//        mBinding?.checkBoxRememberMe?.isChecked = isRemembered

        if (isRemembered) {
            mBinding?.etUsername?.setText(
                getPreferenceString(
                    this,
                    AppConstants.USERNAME
                ).let { EncryptionHelper.decrypt(it) }
            )
            mBinding?.etPassword?.setText(
                getPreferenceString(
                    this,
                    AppConstants.PASSWORD
                ).let { EncryptionHelper.decrypt(it) }
            )
        }


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        mBinding?.checkBoxRememberMe?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utility.savePreferencesString(this@LoginActivity, AppConstants.REMEMBER_MEE, "isChecked")
            } else {
                Utility.savePreferencesString(this@LoginActivity, AppConstants.REMEMBER_MEE, "unchecked")
            }
        }



    }

    private fun showBiometricPrompt() {
        // Launch the biometric prompt in a coroutine
        lifecycleScope.launch {
            // Show biometric prompt
            promptManager.showBiometricPrompt(
                title = "Unlock App",
                description = "Authenticate to log in"
            )

            // Collect biometric result within a coroutine
            promptManager.promptResults.collect { biometricResult ->
                when (biometricResult) {
                    is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        // Authentication successful, retrieve stored credentials
                        val userName =
                            getPreferenceString(this@LoginActivity, AppConstants.USERNAME)
                        val password =
                            getPreferenceString(this@LoginActivity, AppConstants.PASSWORD)
                        Log.d("loginwithcred", userName)
                        Log.d("loginwithcred", password)
                        loginWithCredentials(userName, password)
                    }

                    is BiometricPromptManager.BiometricResult.AuthenticationError,
                    BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                        // Show toast for failure
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        // Handle other scenarios (e.g., biometric not set)
                    }
                }
            }
        }
    }

    private fun loginWithCredentials(userName: String, password: String) {
        if (userName.isNotEmpty() && password.isNotEmpty()) {
            // Proceed with login using saved credentials
            viewModel.getLoginApi(
                this,
                LoginRequest(EncryptionHelper.decrypt(userName), EncryptionHelper.decrypt(password))
            )
        } else {
            // Show error if no valid credentials
            Toast.makeText(this, "No valid saved credentials", Toast.LENGTH_SHORT).show()
        }
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
                        mBinding?.etUsername?.text.toString().trim(),
                        mBinding?.etPassword?.text.toString().trim()
                    )
                )
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun authentication(view: View) {
            showBiometricPrompt()
        }

        fun visiblePassword(view: View) {
            if (isPasswordVisible) {
                // Hide password
                mBinding?.etPassword?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                mBinding?.ivPassEye?.setImageResource(R.drawable.ic_login_hide_eye) // Change to eye-off icon
                isPasswordVisible = false
            } else {
                // Show password
                mBinding?.etPassword?.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                mBinding?.ivPassEye?.setImageResource(R.drawable.ic_eye_open) // Change to eye-on icon
                isPasswordVisible = true
            }

            // Move cursor to the end of the text after toggling
            mBinding?.etPassword?.text?.length?.let { it1 -> mBinding?.etPassword?.setSelection(it1) }
        }
    }

    private fun valid(): Boolean {
        if (mBinding?.etUsername?.text.toString().trim()
                .isEmpty() && mBinding?.etPassword?.text.toString().trim().isEmpty()
        ) {
            mBinding?.clParent?.let { showSnackbar(it, getString(R.string.mandatory_fields)) }
            return false
        }
        return if (mBinding?.etUsername?.text.toString().trim().isEmpty()) {
            mBinding?.clParent?.let { showSnackbar(it, getString(R.string.please_enter_email_id)) }
            false
        } else if (mBinding?.etPassword?.text.toString().trim().isEmpty()) {
            mBinding?.clParent?.let { showSnackbar(it, getString(R.string.Please_enter_password)) }
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
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    Preferences.setPreference(
                        this, AppConstants.SCHEME, Result(
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
                    // Save user ID and password in preferences for testing (unencrypted for now)
                    val userName = mBinding?.etUsername?.text.toString().trim()
                    val password = mBinding?.etPassword?.text.toString().trim()

                    if (userName.isNotEmpty() && password.isNotEmpty()) {
                        val encryptedUserName = EncryptionHelper.encrypt(userName)
                        val encryptedPassword = EncryptionHelper.encrypt(password)

                        // Save encrypted data in SharedPreferences
                        Utility.savePreferencesString(
                            this,
                            AppConstants.USERNAME,
                            encryptedUserName
                        )
                        Utility.savePreferencesString(
                            this,
                            AppConstants.PASSWORD,
                            encryptedPassword
                        )
                    }

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
            mBinding?.clParent?.let { it1 -> showSnackbar(it1, it) }
        }
    }
}