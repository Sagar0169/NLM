package mission.vatsalya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivitySplashBinding
import mission.vatsalya.utilities.BaseActivity

class SplashActivity() : BaseActivity<ActivitySplashBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    private var mBinding: ActivitySplashBinding? = null
    private var mDelay = 3000
    override fun initView() {
        mBinding=viewDataBinding
        Handler(Looper.getMainLooper()).postDelayed({
//            Log.d("token---", Utility.getPreferenceString(this, AppConstants.WALK_THROUGH))

//            if (Utility.getPreferenceString(this@SplashActivity, AppConstants.WALK_THROUGH) == "true"
//            ) {
//                if (Utility.getPreferenceString(this, PrefEntities.TOKEN).isNotEmpty()) {
//                    startActivity(Intent(this, DashboardActivity::class.java))
//                } else {
//                    startActivity(Intent(this, LoginActivity::class.java))
//                }
//            } else {
//                startActivity(Intent(this, OnboardingScreenActivity::class.java))
//            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, mDelay.toLong())
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
}