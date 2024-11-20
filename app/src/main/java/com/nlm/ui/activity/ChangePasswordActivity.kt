package com.nlm.ui.activity

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.nlm.utilities.BaseActivity
import com.nlm.R
import com.nlm.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    private var mBinding: ActivityChangePasswordBinding? = null
    private var passVisible: Boolean = true

    override val layoutId: Int
        get() = R.layout.activity_change_password


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun submit(view: View) {
            if (validateFields()) {
                val intent = Intent(this@ChangePasswordActivity, DashboardActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Password Changes Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            else {
//                Toast.makeText(
//                    this@ChangePasswordActivity,
//                    "Please Check your Password",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        mBinding?.ivEyeVisible1?.setOnClickListener {
            togglePasswordVisibility(mBinding?.etOldPassword, mBinding?.ivEyeVisible1)
        }

        mBinding!!.ivEyeVisible2.setOnClickListener {
            togglePasswordVisibility(mBinding!!.etNewPassword, mBinding!!.ivEyeVisible2)
        }

        mBinding!!.ivEyeVisible3.setOnClickListener {
            togglePasswordVisibility(mBinding!!.etConfirmPassword, mBinding!!.ivEyeVisible3)
        }

    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    private fun validateFields(): Boolean {
        if (mBinding?.etOldPassword?.text.toString().trim().isEmpty()) {
            mBinding?.etOldPassword?.error = getString(R.string.please_enter_a_old_password)
            return false
        }
        if (mBinding?.etNewPassword?.text.toString().trim().isEmpty()) {
            mBinding?.etNewPassword?.error = getString(R.string.please_enter_a_new_password)
            return false
        } else if (!isValidPassword(mBinding?.etNewPassword?.text.toString().trim())) {
            mBinding?.etNewPassword?.error =
                getString(R.string.password_should_contain_at_least_one_lowercase_letter_one_uppercase_letter_one_digit_and_one_special_character)
            return false
        }
        if (mBinding?.etConfirmPassword?.text.toString().trim().isEmpty()) {
            mBinding?.etConfirmPassword?.error = getString(R.string.please_confirm_your_password)
            return false
        } else if (mBinding?.etConfirmPassword?.text.toString().trim() != mBinding?.etNewPassword?.text.toString().trim()) {
            mBinding?.etConfirmPassword?.error = getString(R.string.passwords_do_not_match)
            return false
        }

        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{6,}\$"
        val pattern = Regex(passwordPattern)
        return pattern.matches(password)
    }

    private fun togglePasswordVisibility(editText: EditText?, imageView: ImageView?) {
        if (editText?.text?.isNotEmpty() == true) {
            if (passVisible) {
                editText.transformationMethod = null
                imageView?.setImageResource(R.drawable.ic_login_hide_eye)
            } else {
                editText.transformationMethod = PasswordTransformationMethod()
                imageView?.setImageResource(R.drawable.ic_eye_open)
            }
            editText.setSelection(editText.text.length)
            passVisible = !passVisible
        }
    }

}