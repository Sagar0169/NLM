package mission.vatsalya.ui.activity

import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivitySightedChildBinding
import mission.vatsalya.download_manager.AndroidDownloader
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


class SightedChildActivity : BaseActivity<ActivitySightedChildBinding>(),
    SightedBasicDetailsFragment.OnNextButtonClickListener,
    SightedFacialAttributesFragment.OnNextButtonClickListener,
    SightedPhysicalAttributeFragment.OnNextButtonClickListener,
    SightedBackgroundFragment.OnNextButtonClickListener,
    SightedLocationDetailsFragment.OnNextButtonClickListener,
    SightedUploadFragment.OnNextButtonClickListener,
    SightedConfirmationFragment.OnNextButtonClickListener
{
    private var mBinding: ActivitySightedChildBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_sighted_child

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        showCustomDialog()
        setupTabLayout()
        loadFragment(SightedBasicDetailsFragment())

    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
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

    override fun onBackPressed() {
        val currentTab = mBinding?.tabLayout?.selectedTabPosition ?: 0
        if (currentTab > 0) {
            // Move to the previous tab if not on the first tab
            mBinding?.tabLayout?.getTabAt(currentTab - 1)?.select()
        } else {
            // If on the first tab, you can either call super to finish the activity or handle it differently
            super.onBackPressed()
        }
    }


    private fun setupTabLayout() {
        mBinding?.tabLayout?.apply {
            addTab(newTab().setText("Basic Details"))
            addTab(newTab().setText("Facial Attributes"))
            addTab(newTab().setText("Physical Attributes"))
            addTab(newTab().setText("Background"))
            addTab(newTab().setText("Location details"))
            addTab(newTab().setText("Upload Media"))
            addTab(newTab().setText("Confirmation"))


            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> loadFragment(SightedBasicDetailsFragment())
                        1 -> loadFragment(SightedFacialAttributesFragment())
                        2 -> loadFragment(SightedPhysicalAttributeFragment())
                        3 -> loadFragment(SightedBackgroundFragment())
                        4 -> loadFragment(SightedLocationDetailsFragment())
                        5 -> loadFragment(SightedUploadFragment())
                        6 -> loadFragment(SightedConfirmationFragment())
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
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment is SightedBasicDetailsFragment) {
            transaction.replace(R.id.frameLayout, fragment)
        } else {
            transaction.replace(R.id.frameLayout, fragment)
        }
        transaction.commit()
    }


}