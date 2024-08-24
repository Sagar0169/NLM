package mission.vatsalya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChangePasswordBinding
import mission.vatsalya.databinding.ActivityLoginBinding
import mission.vatsalya.utilities.BaseActivity

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    private var mBinding: ActivityChangePasswordBinding? = null
    private var passVisible: Boolean = true
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etOldPassword: EditText

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

        mBinding!!.ivEyeVisible1.setOnClickListener {
            togglePasswordVisibility(mBinding!!.etOldPassword, mBinding!!.ivEyeVisible1)
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
        etOldPassword = mBinding!!.etOldPassword
        etPassword = mBinding!!.etNewPassword
        etConfirmPassword = mBinding!!.etConfirmPassword


        val oldPassword = etOldPassword.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (oldPassword.isEmpty()) {
            etOldPassword.error = "Please enter a password"
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Please enter a password"
            return false
        } else if (!isValidPassword(password)) {
            etPassword.error =
                "Password should contain at least one lowercase letter, one uppercase letter, one digit, and one special character."
            return false
        }
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Please confirm your password"
            return false
        } else if (confirmPassword != password) {
            etConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{6,}\$"
        val pattern = Regex(passwordPattern)
        return pattern.matches(password)
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {
        if (editText.text.isNotEmpty()) {
            if (passVisible) {
                editText.transformationMethod = null
                imageView.setImageResource(R.drawable.ic_login_hide_eye)
            } else {
                editText.transformationMethod = PasswordTransformationMethod()
                imageView.setImageResource(R.drawable.ic_eye_open)
            }
            editText.setSelection(editText.text.length)
            passVisible = !passVisible
        }
    }

}