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

    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}