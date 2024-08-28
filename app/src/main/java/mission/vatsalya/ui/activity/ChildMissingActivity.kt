package mission.vatsalya.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.ActivityLoginBinding
import mission.vatsalya.ui.fragment.BasicDetailsFragment
import mission.vatsalya.ui.fragment.ConfirmationFragments
import mission.vatsalya.ui.fragment.FacialAttributesFragment
import mission.vatsalya.ui.fragment.FamilyDetailsFragment
import mission.vatsalya.ui.fragment.FirDetailsFragment
import mission.vatsalya.ui.fragment.LocationDetailsFragment
import mission.vatsalya.ui.fragment.PhysicalAttributesFragment
import mission.vatsalya.ui.fragment.SightedBackgroundFragment
import mission.vatsalya.ui.fragment.SightedBasicDetailsFragment
import mission.vatsalya.ui.fragment.SightedConfirmationFragment
import mission.vatsalya.ui.fragment.SightedFacialAttributesFragment
import mission.vatsalya.ui.fragment.SightedLocationDetailsFragment
import mission.vatsalya.ui.fragment.SightedPhysicalAttributeFragment
import mission.vatsalya.ui.fragment.SightedUploadFragment
import mission.vatsalya.ui.fragment.UploadMediaFragment
import mission.vatsalya.utilities.BaseActivity

class ChildMissingActivity : BaseActivity<ActivityChildMissingBinding>() ,
    BasicDetailsFragment.OnNextButtonClickListener,
    FacialAttributesFragment.OnNextButtonClickListener,
    PhysicalAttributesFragment.OnNextButtonClickListener,
    FamilyDetailsFragment.OnNextButtonClickListener,
    LocationDetailsFragment.OnNextButtonClickListener,
    UploadMediaFragment.OnNextButtonClickListener,
    FirDetailsFragment.OnNextButtonClickListener{

    private var mBinding: ActivityChildMissingBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_child_missing

    override fun initView() {
        mBinding = viewDataBinding
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
                        2 -> loadFragment(PhysicalAttributesFragment())
                        3 -> loadFragment(FamilyDetailsFragment())
                        4 -> loadFragment(LocationDetailsFragment())
                        5 -> loadFragment(UploadMediaFragment())
                        6 -> loadFragment(FirDetailsFragment())
                        7 -> loadFragment(ConfirmationFragments())
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

    fun moveToNextTab() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        val nextTab = currentTab + 1
        if (nextTab < (mBinding?.tabLayout?.tabCount ?: 0)) {
            mBinding?.tabLayout?.getTabAt(nextTab)?.select()
        }
    }

    override fun onNextButtonClick() {
        moveToNextTab()
    }
}