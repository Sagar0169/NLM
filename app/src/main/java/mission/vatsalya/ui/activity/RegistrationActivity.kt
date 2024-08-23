package mission.vatsalya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityOtpBinding
import mission.vatsalya.databinding.ActivityRegistrationBinding
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.toast

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {
    private var mBinding: ActivityRegistrationBinding?=null

    override val layoutId: Int
        get() = R.layout.activity_registration


    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
      
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun login(view:View){
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        fun otp(view:View){
            val intent = Intent(this@RegistrationActivity, OtpActivity::class.java)
            startActivity(intent)
        }
    }

}