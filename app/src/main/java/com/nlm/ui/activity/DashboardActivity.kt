package com.nlm.ui.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.nlm.R
import com.nlm.databinding.ActivityDashboardBinding
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var mBinding: ActivityDashboardBinding? = null
    var isUserOpen = false
    var isLiveStockOpen = false
    var isNationLiveStockOpen = false
    var isNationDairyOpen = false
    var isGokulOpen = false


    override val layoutId: Int
        get() = R.layout.activity_dashboard

    override fun initView() {
        mBinding = viewDataBinding
        setDefaultDrawables()
        mBinding?.drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Do something when the drawer is sliding (optional)
                Log.d("Drawer", "Slide")
            }

            override fun onDrawerOpened(drawerView: View) {
                // Lock the drawer to prevent interaction with the content behind
                mBinding!!.contentNav.ivDrawer.hideView()

                Log.d("Drawer", "Open")
            }

            override fun onDrawerClosed(drawerView: View) {
                // Unlock the drawer to allow interaction with the content
                mBinding!!.contentNav.ivDrawer.showView()
                Log.d("Drawer", "Close")
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Handle drawer state changes (optional)
                Log.d("Drawer", "Change")

            }
        })
        // Set click listeners for menu items
        mBinding?.leftDrawerMenu?.tvUsers?.setOnClickListener {
            toggleMenuItem(
                isUserOpen,
                R.drawable.img_4,
                mBinding!!.leftDrawerMenu.llUsers,
                mBinding!!.leftDrawerMenu.tvUsers
            )
            isUserOpen = !isUserOpen
        }

        mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.setOnClickListener {
            toggleMenuItem(
                isLiveStockOpen,
                R.drawable.ic_sightedchild,
                mBinding!!.leftDrawerMenu.llLivestockHealthDisease,
                mBinding!!.leftDrawerMenu.tvLivestockHealthDisease
            )
            isLiveStockOpen = !isLiveStockOpen
        }

        mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.setOnClickListener {
            toggleMenuItem(
                isNationLiveStockOpen,
                R.drawable.img_3,
                mBinding!!.leftDrawerMenu.llNationalLivestockMission,
                mBinding!!.leftDrawerMenu.tvNationalLiveStockMission
            )
            isNationLiveStockOpen = !isNationLiveStockOpen
        }

        mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.setOnClickListener {
            toggleMenuItem(
                isNationDairyOpen,
                R.drawable.baseline_person_24,
                mBinding!!.leftDrawerMenu.llNationalDairyDevelopment,
                mBinding!!.leftDrawerMenu.tvNationalDairyDevelopment
            )
            isNationDairyOpen = !isNationDairyOpen
        }

        mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.setOnClickListener {
            toggleMenuItem(
                isGokulOpen,
                R.drawable.lock,
                mBinding!!.leftDrawerMenu.llRashtriyaGokulMission,
                mBinding!!.leftDrawerMenu.tvRashtriyaGokulMission
            )
            isGokulOpen = !isGokulOpen
        }

        // Drawer toggle button
        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }
    }

    // Method to set default arrow drawables
    private fun setDefaultDrawables() {
        setDrawableWithArrow(
            mBinding?.leftDrawerMenu?.tvUsers,
            ContextCompat.getDrawable(this, R.drawable.img_4),
            false
        )
        setDrawableWithArrow(
            mBinding?.leftDrawerMenu?.tvLivestockHealthDisease,
            ContextCompat.getDrawable(this, R.drawable.ic_sightedchild),
            false
        )
        setDrawableWithArrow(
            mBinding?.leftDrawerMenu?.tvNationalLiveStockMission,
            ContextCompat.getDrawable(this, R.drawable.img_3),
            false
        )
        setDrawableWithArrow(
            mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment,
            ContextCompat.getDrawable(this, R.drawable.baseline_person_24),
            false
        )
        setDrawableWithArrow(
            mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission,
            ContextCompat.getDrawable(this, R.drawable.lock),
            false
        )

        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }

        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mBinding?.leftDrawerMenu?.tvMasterImplementingAgency?.setOnClickListener {
            val intent =
                Intent(this@DashboardActivity, ImplementingAgencyMasterActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvMobileVeterinaryUnits?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MobileVeterinaryActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvVaccinationProgramme?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, VaccinationProgrammerActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvAscad?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AscadActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvNlmEdp?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, NlmEdpActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvAssistanceForEa?.setOnClickListener {
            val intent =
                Intent(this@DashboardActivity, NlmAssistanceForEa::class.java).putExtra("isFrom", 1)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvFpsFromForest?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, NlmFpForestLandActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvFpsFromNonForest?.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                NlmFpForestLandActivity::class.java
            ).putExtra("isFrom", 1)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvFpsPlanStorage?.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                NlmFspPlantStorageActivity::class.java
            ).putExtra("isFrom", 1)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvAssistanceQfps?.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                NlmAssistanceForQFSPActivity::class.java
            ).putExtra("isFrom", 1)
            startActivity(intent)
        }

        mBinding?.leftDrawerMenu?.tvVitroFertilization?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, RGMVitroFertilizationActivity::class.java)
            startActivity(intent)
        }

        mBinding?.leftDrawerMenu?.tvAiCenter?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, RGMAiCenterActivity::class.java)
            startActivity(intent)
        }

        mBinding?.leftDrawerMenu?.tvPrivacyPolicy?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AboutUsActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvTermsAndConditions?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AboutUsActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvImplementingAgency?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, National_Live_Stock_IA::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvMilkUnionVisitReport?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MilkUnionVisitNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvDairyPlantVisitReport?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, DairyPlantVisitNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvRspLaboratorySemen?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, RSPLab::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateSemenBank?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, StateSemenBankList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvArtificialInsemination?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Artificial_Insemination_List::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvImportExoticGoat?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Import_Of_ExoticGoat_List::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvDcsBmsCenterVisitReport?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, DCSCenterVisitNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateCenterLabVisitReport?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, StateCenterLabVisitNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvMilkProcessing?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MilkProcessingNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvMilkProductMarketing?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MilkProductMarketingNDDActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvNationalLevelComponentA?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, NLMComponentANDDActivity::class.java)
            startActivity(intent)
        }

        mBinding?.leftDrawerMenu?.tvProductivityEnhancementServices?.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                ProductivityEnhancementServicesNDDActivity::class.java
            )
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvBreedMultiplication?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, BreedMultiplicationRGMActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvTrainingCenters?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TrainingCentersRGMActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvBullMotherFarms?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Bull_Of_Mothers_List::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvSemenStation?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Semen_Station_List::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateImplementingAgency?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, List_of_RGM_IA::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateImplementingAgency?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, List_of_RGM_IA::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.llUsers?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, UserActivity::class.java)
            startActivity(intent)
        }

    }


    override fun setVariables() {
    }

    override fun setObservers() {
    }

    private fun toggleMenuItem(
        isOpen: Boolean,
        drawableStartId: Int,
        layoutToShow: View,
        textViewToUpdate: TextView
    ) {
        closeAllMenus()

        if (isOpen) {
            layoutToShow.hideView()
            setDrawableWithArrow(
                textViewToUpdate,
                ContextCompat.getDrawable(this, drawableStartId),
                false
            )
        } else {
            layoutToShow.showView()
            setDrawableWithArrow(
                textViewToUpdate,
                ContextCompat.getDrawable(this, drawableStartId),
                true
            )
        }
    }

    // Close all other menu items and reset arrows
    private fun closeAllMenus() {
        mBinding!!.leftDrawerMenu.llUsers.hideView()
        mBinding!!.leftDrawerMenu.llLivestockHealthDisease.hideView()
        mBinding!!.leftDrawerMenu.llNationalLivestockMission.hideView()
        mBinding!!.leftDrawerMenu.llNationalDairyDevelopment.hideView()
        mBinding!!.leftDrawerMenu.llRashtriyaGokulMission.hideView()

        setDefaultDrawables() // Reset arrows to default position
    }

    // Method to handle arrow rotation and setting drawable
    private fun setDrawableWithArrow(
        textView: TextView?,
        drawableStart: Drawable?,
        isOpen: Boolean
    ) {
        val arrowDrawable = if (isOpen) {
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        } else {
            rotateDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_down), 90f)
        }
        textView?.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, arrowDrawable, null)
    }

    // Rotate the drawable for the arrow direction
    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances
        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation
        return rotateDrawable
    }

    private fun toggleLeftDrawer() {
        if (mBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.END)
            Log.d("DrawerOpen", "Open")
        } else {
            mBinding?.drawerLayout?.openDrawer(GravityCompat.START)
            Log.d("DrawerOpen", "Close")

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
        // This will close the app and all the activities in the task.
    }
}