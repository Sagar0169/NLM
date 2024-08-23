package mission.vatsalya.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityDashboardBinding
import mission.vatsalya.databinding.ActivityRegistrationBinding
import mission.vatsalya.utilities.BaseActivity

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var mBinding: ActivityDashboardBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_dashboard

    override fun initView() {
        mBinding = viewDataBinding


//        mBinding?.contentNav?.ivBackPress?.setOnClickListener {
//            toggleLeftDrawer()
//        }

    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    private fun toggleLeftDrawer() {
        if (mBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.END)
        } else {
            mBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }
}