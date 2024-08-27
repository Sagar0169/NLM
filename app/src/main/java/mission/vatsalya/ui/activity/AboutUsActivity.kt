package mission.vatsalya.ui.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityAboutUsBinding
import mission.vatsalya.databinding.ActivityDashboardBinding
import mission.vatsalya.utilities.BaseActivity

class AboutUsActivity() : BaseActivity<ActivityAboutUsBinding>() {
    private var mBinding: ActivityAboutUsBinding? = null



    override val layoutId: Int
        get() = R.layout.activity_about_us

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        mBinding?.tvAboutUsAndTermsAndCondition?.webViewClient= WebViewClient()
        mBinding?.tvAboutUsAndTermsAndCondition?.settings?.javaScriptEnabled = true
        mBinding?.tvAboutUsAndTermsAndCondition?.loadUrl("http://hrcares.in/privacy_policy")
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
    fun backPress(view: View){
        onBackPressedDispatcher.onBackPressed()
    }
}


}