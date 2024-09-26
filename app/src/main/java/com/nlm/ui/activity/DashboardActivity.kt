package com.nlm.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.nlm.R
import com.nlm.databinding.ActivityDashboardBinding
import com.nlm.ui.activity.national_livestock_mission.ArtificialInseminationList
import com.nlm.ui.activity.national_livestock_mission.ImportOfExoticGoatList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockIAList
import com.nlm.ui.activity.national_livestock_mission.NlmAssistanceForEa
import com.nlm.ui.activity.national_livestock_mission.NlmAssistanceForQFSPActivity
import com.nlm.ui.activity.national_livestock_mission.NlmEdpActivity
import com.nlm.ui.activity.national_livestock_mission.NlmFpForestLandActivity
import com.nlm.ui.activity.national_livestock_mission.NlmFspPlantStorageActivity
import com.nlm.ui.activity.national_livestock_mission.RSPLabList
import com.nlm.ui.activity.national_livestock_mission.StateSemenBankList
import com.nlm.ui.activity.rashtriya_gokul_mission.BreedMultiplicationRGMActivity
import com.nlm.ui.activity.rashtriya_gokul_mission.BullOfMothersList
import com.nlm.ui.activity.rashtriya_gokul_mission.RGMAiCenterActivity
import com.nlm.ui.activity.rashtriya_gokul_mission.RGMIAList
import com.nlm.ui.activity.rashtriya_gokul_mission.RGMVitroFertilizationActivity
import com.nlm.ui.activity.rashtriya_gokul_mission.SemenStationList
import com.nlm.ui.activity.rashtriya_gokul_mission.TrainingCentersRGMActivity
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.setDrawableWithArrow
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
        RoleBased()
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
            this,
            mBinding?.leftDrawerMenu?.tvUsers,
            ContextCompat.getDrawable(this, R.drawable.img_4),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvLivestockHealthDisease,
            ContextCompat.getDrawable(this, R.drawable.ic_sightedchild),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvNationalLiveStockMission,
            ContextCompat.getDrawable(this, R.drawable.img_3),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment,
            ContextCompat.getDrawable(this, R.drawable.baseline_person_24),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission,
            ContextCompat.getDrawable(this, R.drawable.lock),
            false
        )

        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }

        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
            Utility.clearAllPreferencesExceptDeviceToken(this)
            intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
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
        mBinding?.leftDrawerMenu?.tvNationalLevelComponentB?.setOnClickListener {
            val intent =
                Intent(this@DashboardActivity, NlmComponentBList::class.java).putExtra("isFrom", 1)
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
        mBinding?.leftDrawerMenu?.tvReportsOfNlm?.setOnClickListener {
            val intent = Intent(
                this@DashboardActivity,
                ReportsOfNlmComponentActivity::class.java
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
            val intent = Intent(this@DashboardActivity, NationalLiveStockIAList::class.java)
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
            val intent = Intent(this@DashboardActivity, RSPLabList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateSemenBank?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, StateSemenBankList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvArtificialInsemination?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ArtificialInseminationList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvImportExoticGoat?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ImportOfExoticGoatList::class.java)
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
            val intent = Intent(this@DashboardActivity, BullOfMothersList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvSemenStation?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SemenStationList::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvStateImplementingAgency?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, RGMIAList::class.java)
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
                this,
                textViewToUpdate,
                ContextCompat.getDrawable(this, drawableStartId),
                false
            )
        } else {
            layoutToShow.showView()
            setDrawableWithArrow(
                this,
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

    private fun toggleLeftDrawer() {
        if (mBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.END)
            Log.d("DrawerOpen", "Open")
        } else {
            mBinding?.drawerLayout?.openDrawer(GravityCompat.START)
            Log.d("DrawerOpen", "Close")

        }
    }

    override fun onBackPressed() {
        finishAffinity()
        // This will close the app and all the activities in the task.
    }

 private fun RoleBased(){
     if (Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.Nodal_Officer||
         Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.RGM_State_Level_Monitor
     ){
         mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.showView()
         mBinding?.leftDrawerMenu?.view4?.showView()
     }
     if (Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.NPDD_State_Level_Monitor) {
         mBinding?.leftDrawerMenu?.view6?.showView()
         mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.showView()
     }
     if (Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.LHDCP_and_NLM_State_Level_Monitor) {
         mBinding?.leftDrawerMenu?.view3?.showView()
         mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.showView()
         mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.showView()
         mBinding?.leftDrawerMenu?.view5?.showView()
     }
     if (Utility.getPreferenceString(this,AppConstants.ROLE_NAME)=="Super Admin") {
         mBinding?.leftDrawerMenu?.tvUsers?.showView()
         mBinding?.leftDrawerMenu?.view2?.showView()
         mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.showView()
         mBinding?.leftDrawerMenu?.view3?.showView()
         mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.showView()
         mBinding?.leftDrawerMenu?.view4?.showView()
         mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.showView()
         mBinding?.leftDrawerMenu?.view5?.showView()
         mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.showView()
         mBinding?.leftDrawerMenu?.view6?.showView()
     }
     if (Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.NLM||Utility.getPreferenceString(this,AppConstants.ROLE_NAME)==AppConstants.ADMIN) {
         mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.showView()
         mBinding?.leftDrawerMenu?.view3?.showView()
         mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.showView()
         mBinding?.leftDrawerMenu?.view4?.showView()
         mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.showView()
         mBinding?.leftDrawerMenu?.view5?.showView()
         mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.showView()
         mBinding?.leftDrawerMenu?.view6?.showView()
     }
 }}