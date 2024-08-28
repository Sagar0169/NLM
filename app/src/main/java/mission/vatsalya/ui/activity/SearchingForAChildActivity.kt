package mission.vatsalya.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.ActivitySearchingForAchildBinding
import mission.vatsalya.download_manager.AndroidDownloader
import mission.vatsalya.ui.fragment.BasicDetailsFragment
import mission.vatsalya.ui.fragment.FacialAttributesFragment
import mission.vatsalya.ui.fragment.QuickSearchFragment
import mission.vatsalya.ui.fragment.TextSeachFragment
import mission.vatsalya.utilities.BaseActivity

class SearchingForAChildActivity() : BaseActivity<ActivitySearchingForAchildBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_searching_for_achild

    private var mBinding: ActivitySearchingForAchildBinding?=null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupTabLayout()
        // Set default fragment

        loadFragment(QuickSearchFragment())
        showCustomDialog()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

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
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
            val downloader = AndroidDownloader(this)
            downloader.downloadFile("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
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
            addTab(newTab().setText("Quick Search"))
            addTab(newTab().setText("Text Search"))



            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(QuickSearchFragment())
                        1 -> loadFragment(TextSeachFragment())
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