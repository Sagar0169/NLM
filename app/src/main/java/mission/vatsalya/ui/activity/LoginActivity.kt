package mission.vatsalya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityLoginBinding
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.toast

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private var mBinding: ActivityLoginBinding?=null


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
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        mBinding!!.etMobile.setText("+91")
        mBinding!!.etMobile.text?.let { mBinding!!.etMobile.setSelection(it.length) }


        mBinding!!.etMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Ensure that the prefix +91 is always present
                if (!s.toString().startsWith("+91")) {
                    mBinding!!.etMobile.setText("+91")
                    mBinding!!.etMobile.text?.let { mBinding!!.etMobile.setSelection(it.length) }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}