package mission.vatsalya.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
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


        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }
        mBinding?.leftDrawerMenu?.tvform4?.setOnClickListener{
            val intent = Intent(this@DashboardActivity, EditProfile::class.java)
            startActivity(intent)
        }

        mBinding?.contentNav?.tvMyChildMissing?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ChildMissingActivity::class.java)
            startActivity(intent)
//            showCustomDialog()
        }

    }

    override fun setVariables() {
    }

    override fun setObservers() {
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


    private fun toggleLeftDrawer() {
        if (mBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.END)
        } else {
            mBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun  onBackPressed() {
       finishAffinity()
         // This will close the app and all the activities in the task.
    }


}