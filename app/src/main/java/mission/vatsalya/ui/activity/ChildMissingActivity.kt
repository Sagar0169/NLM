package mission.vatsalya.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.ActivityLoginBinding
import mission.vatsalya.ui.fragment.BasicDetailsFragment
import mission.vatsalya.ui.fragment.FacialAttributesFragment
import mission.vatsalya.utilities.BaseActivity

class ChildMissingActivity : BaseActivity<ActivityChildMissingBinding>() {

    private var mBinding: ActivityChildMissingBinding?=null

    override val layoutId: Int
        get() = R.layout.activity_child_missing

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        // Set default fragment

            loadFragment(BasicDetailsFragment())
        showCustomDialog()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
    inner class ClickActions {

        fun login(view: View) {
            val intent = Intent(this@ChildMissingActivity, OtpActivity::class.java)
            startActivity(intent)

        }
        fun register(view: View) {
            val intent = Intent(this@ChildMissingActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_caution) // Replace with your dialog layout file name

        val tvPocsoLink: TextView = dialog.findViewById(R.id.tvPocsoLink)
        val btnProceed: Button = dialog.findViewById(R.id.btnProceed)

        // Handle the click on the POCSO Act link
        tvPocsoLink.setOnClickListener {
            Log.d("CustomDialog", "POCSO Act 2012 link clicked")
        }

        // Handle the click on the Proceed button
        btnProceed.setOnClickListener {
            dialog.dismiss()
            // Continue with the proceed action
        }

        dialog.show()
    }

    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Basic Details"))
            addTab(newTab().setText("Facial Attributes"))
            addTab(newTab().setText("Physical Attributes"))
            addTab(newTab().setText("Family Details"))
            addTab(newTab().setText("Location details"))
            addTab(newTab().setText("Upload Media"))

            addTab(newTab().setText("Fir detail"))

            addTab(newTab().setText("Confirmation"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(BasicDetailsFragment())
                        1 -> loadFragment(FacialAttributesFragment())
//                        2 -> loadFragment(FragmentThree())
//                        3 -> loadFragment(FragmentFour())
//                        4 -> loadFragment(FragmentFive())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Optional: handle tab unselected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Optional: handle tab reselected
                }
            })
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}