package com.nlm.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.databinding.ActivityDashboardBinding
import com.nlm.model.LogoutRequest
import com.nlm.model.Result
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.MobileVeterinaryActivity
import com.nlm.ui.activity.national_dairy_development.DCSCenterVisitNDDActivity
import com.nlm.ui.activity.national_dairy_development.DairyPlantVisitNDDActivity
import com.nlm.ui.activity.national_dairy_development.MilkProcessingNDDActivity
import com.nlm.ui.activity.national_dairy_development.MilkProductMarketingNDDActivity
import com.nlm.ui.activity.national_dairy_development.MilkUnionVisitNDDActivity
import com.nlm.ui.activity.national_dairy_development.NLMComponentANDDActivity
import com.nlm.ui.activity.national_dairy_development.NlmComponentBList
import com.nlm.ui.activity.national_dairy_development.ProductivityEnhancementServicesNDDActivity
import com.nlm.ui.activity.national_dairy_development.StateCenterLabVisitNDDActivity
import com.nlm.ui.activity.national_livestock_mission.ArtificialInseminationList
import com.nlm.ui.activity.national_livestock_mission.ImportOfExoticGoatList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.ui.activity.national_livestock_mission.NlmAssistanceForEa
import com.nlm.ui.activity.national_livestock_mission.NlmAssistanceForQFSPActivity
import com.nlm.ui.activity.national_livestock_mission.NlmEdpActivity
import com.nlm.ui.activity.national_livestock_mission.NlmFpForestLandActivity
import com.nlm.ui.activity.national_livestock_mission.NlmFspPlantStorageActivity
import com.nlm.ui.activity.national_livestock_mission.RSPLabList
import com.nlm.ui.activity.national_livestock_mission.ReportsOfNlmComponentActivity
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
import com.nlm.utilities.Nlm
import com.nlm.utilities.PrefEntities

import com.nlm.utilities.Preferences
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var mBinding: ActivityDashboardBinding? = null
    var isUserOpen = false
    var isLiveStockOpen = false
    var isNationLiveStockOpen = false
    var isNationDairyOpen = false
    var isGokulOpen = false
    private var viewModel = ViewModel()
    val matchingSchemeIds = mutableListOf<Int>()
    val matchingFormIds = mutableListOf<Int>()


    override val layoutId: Int
        get() = R.layout.activity_dashboard

    override fun initView() {
        mBinding = viewDataBinding
        compareSchemeIds()
        viewModel.init()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

//        viewModel.getDashboardApi(
//            this@DashboardActivity,
//            LogoutRequest(
//                Preferences.getPreferenceOfScheme(
//                    this@DashboardActivity,
//                    AppConstants.SCHEME,
//                    Result::class.java
//                ).user_id
//            )
//        )
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
//        Set click listeners for menu items
//        mBinding?.leftDrawerMenu?.tvUsers?.setOnClickListener {
//            toggleMenuItem(
//                isUserOpen,
//                R.drawable.ic_user,
//                mBinding!!.leftDrawerMenu.llUsers,
//                mBinding!!.leftDrawerMenu.tvUsers
//            )
//            isUserOpen = !isUserOpen
//        }

        mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.setOnClickListener {
            toggleMenuItem(
                isLiveStockOpen,
                R.drawable.ic_lhd,
                mBinding!!.leftDrawerMenu.llLivestockHealthDisease,
                mBinding!!.leftDrawerMenu.tvLivestockHealthDisease
            )
            isLiveStockOpen = !isLiveStockOpen
        }

        mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.setOnClickListener {
            toggleMenuItem(
                isNationLiveStockOpen,
                R.drawable.ic_nlm,
                mBinding!!.leftDrawerMenu.llNationalLivestockMission,
                mBinding!!.leftDrawerMenu.tvNationalLiveStockMission
            )
            isNationLiveStockOpen = !isNationLiveStockOpen
        }

        mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.setOnClickListener {
            toggleMenuItem(
                isNationDairyOpen,
                R.drawable.ic_ndd,
                mBinding!!.leftDrawerMenu.llNationalDairyDevelopment,
                mBinding!!.leftDrawerMenu.tvNationalDairyDevelopment
            )
            isNationDairyOpen = !isNationDairyOpen
        }

        mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.setOnClickListener {
            toggleMenuItem(
                isGokulOpen,
                R.drawable.ic_rgm,
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
//        setDrawableWithArrow(
//            mBinding?.leftDrawerMenu?.tvUsers,
//            ContextCompat.getDrawable(this, R.drawable.ic_user),
//            false
//        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvLivestockHealthDisease,
            ContextCompat.getDrawable(this, R.drawable.ic_lhd),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvNationalLiveStockMission,
            ContextCompat.getDrawable(this, R.drawable.ic_nlm),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment,
            ContextCompat.getDrawable(this, R.drawable.ic_ndd),
            false
        )
        setDrawableWithArrow(
            this,
            mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission,
            ContextCompat.getDrawable(this, R.drawable.ic_rgm),
            false
        )

        mBinding?.contentNav?.ivDrawer?.setOnClickListener {
            toggleLeftDrawer()
        }

//        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
//            Utility.clearAllPreferencesExceptDeviceToken(this)
//            intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//        }
//        mBinding?.leftDrawerMenu?.tvMasterImplementingAgency?.setOnClickListener {
//            val intent =
//                Intent(this@DashboardActivity, ImplementingAgencyMasterActivity::class.java)
//            startActivity(intent)
//        }
        mBinding?.leftDrawerMenu?.tvMobileVeterinaryUnits?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MobileVeterinaryActivity::class.java)
            startActivity(intent)
        }
        mBinding?.leftDrawerMenu?.tvEdit?.setOnClickListener {
            val intent = Intent(this@DashboardActivity, EditProfileActivity::class.java)
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
        mBinding?.leftDrawerMenu?.tvDashboard?.setOnClickListener {
            mBinding?.drawerLayout?.closeDrawers()
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
            val intent = Intent(this@DashboardActivity, NationalLiveStockMissionIAList::class.java)
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
        mBinding?.leftDrawerMenu?.tvLogout?.setOnClickListener {
            viewModel.getLogoutApi(
                this@DashboardActivity,
                LogoutRequest(
                    Preferences.getPreferenceOfScheme(
                        this@DashboardActivity,
                        AppConstants.SCHEME,
                        Result::class.java
                    ).user_id
                )
            )
//            startActivity(Intent(this, LoginActivity::class.java))
        }

//        mBinding?.leftDrawerMenu?.llUsers?.setOnClickListener {
//            val intent = Intent(this@DashboardActivity, UserActivity::class.java)
//            startActivity(intent)
//        }
    }


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.dashboardResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding!!.contentNav.tvNoOfSchemeCovered.text =
                        userResponseModel._result.no_of_covered_scheme.toString()
                    mBinding!!.contentNav.tvTotalVisits.text =
                        userResponseModel._result.total_visit.toString()
                    mBinding!!.contentNav.tvNoOfStateCovered.text =
                        userResponseModel._result.no_of_state_covered.toString()
                    mBinding!!.contentNav.tvReportSubmittedNLM.text =
                        userResponseModel._result.report_submitted_by_nlm.toString()
                }
            }
            viewModel.errors.observe(this) {
                mBinding?.contentNav?.rlParent?.let { it1 -> Utility.showSnackbar(it1, it) }
            }

        }

        viewModel.logoutResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel._resultflag == 1) {
                Nlm.closeAndRestartApplication()
            } else {
                if (userResponseModel._resultflag == 0 && userResponseModel.message == getString(R.string.you_are_not_authorized_to_access_that_location)) {
                    Nlm.closeAndRestartApplication()
                }
                toast(userResponseModel.message)
            }
        }
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
//        mBinding!!.leftDrawerMenu.llUsers.hideView()
        mBinding!!.leftDrawerMenu.llLivestockHealthDisease.hideView()
        mBinding!!.leftDrawerMenu.llNationalLivestockMission.hideView()
        mBinding!!.leftDrawerMenu.llNationalDairyDevelopment.hideView()
        mBinding!!.leftDrawerMenu.llRashtriyaGokulMission.hideView()
        setDefaultDrawables() // Reset arrows to default position
    }

    // Method to handle arrow rotation and setting drawable
    private fun setDrawableWithArrow(
        context: Context,
        textView: TextView?,
        drawableStart: Drawable?,
        isOpen: Boolean
    ) {
        // Create arrow drawable
        var arrowDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)?.let {
            // Apply the initial color (white when arrow is down)
            DrawableCompat.wrap(it).also { drawable ->
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.white))
                textView?.setTextColor(ContextCompat.getColor(context, R.color.white))

            }

        }

        // Create background drawable
        var ll = R.color.drawerOn

        // Rotate arrow if not open
        if (!isOpen) {
            arrowDrawable = Utility.rotateDrawable(arrowDrawable, 90f)?.also { drawable ->
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.black))
            }
            textView?.setTextColor(ContextCompat.getColor(context, R.color.black))

            ll = R.color.white
        }

        // Set drawables to TextView
        textView?.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, arrowDrawable, null)

        textView?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                ll
            )
        ) // Change background to black when closed

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

    override fun onBackPressed() {
        finishAffinity()
        // This will close the app and all the activities in the task.
    }

    private fun compareSchemeIds() {
        // Retrieve the schemes from preferences
        val storedSchemes = Preferences.getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.schemes
        Log.d("Scheme from response", storedSchemes.toString())
        if (storedSchemes != null) {
            for (scheme in storedSchemes) {
                val matchingLocalScheme = LocalSchemeData.localSchemes.find { it.id == scheme.id }

                if (matchingLocalScheme != null) {
                    // Add the matching scheme ID to the list
                    matchingSchemeIds.add(scheme.id)

                    // Now check for matching form IDs within the matched scheme
                    for (form in scheme.forms) {
                        val matchingLocalForm = matchingLocalScheme.forms.find { it.id == form.id }

                        if (matchingLocalForm != null) {
                            // Add the matching form ID to the list
                            matchingFormIds.add(form.id)
                        }
                    }
                }
            }

            // Log or use the matching IDs as needed
            Log.d("Matching Scheme IDs", matchingSchemeIds.toString())
            Log.d("Matching Form IDs", matchingFormIds.toString())
            for (matchingSchemeId in matchingSchemeIds) {
                if (matchingSchemeId == 198) {
                    mBinding?.leftDrawerMenu?.tvLivestockHealthDisease?.showView()
                    for (matchingFormId in matchingFormIds) {
                        if (matchingFormId == 206) {
                            mBinding?.leftDrawerMenu?.tvVaccinationProgramme?.showView()
                        } else if (matchingFormId == 207) {
                            mBinding?.leftDrawerMenu?.tvMobileVeterinaryUnits?.showView()
                        } else if (matchingFormId == 208) {
                            mBinding?.leftDrawerMenu?.tvAscad?.showView()
                        }
                    }
                } else if (matchingSchemeId == 199) {
                    mBinding?.leftDrawerMenu?.tvNationalLiveStockMission?.showView()
                    for (matchingFormId in matchingFormIds) {
                        if (matchingFormId == 203) {
                            mBinding?.leftDrawerMenu?.tvImplementingAgency?.showView()
                        } else if (matchingFormId == 221) {
                            mBinding?.leftDrawerMenu?.tvRspLaboratorySemen?.showView()
                        } else if (matchingFormId == 222) {
                            mBinding?.leftDrawerMenu?.tvStateSemenBank?.showView()
                        } else if (matchingFormId == 223) {
                            mBinding?.leftDrawerMenu?.tvArtificialInsemination?.showView()
                        } else if (matchingFormId == 224) {
                            mBinding?.leftDrawerMenu?.tvImportExoticGoat?.showView()
                        } else if (matchingFormId == 225) {
                            mBinding?.leftDrawerMenu?.tvAssistanceQfps?.showView()
                        } else if (matchingFormId == 226) {
                            mBinding?.leftDrawerMenu?.tvFpsPlanStorage?.showView()
                        } else if (matchingFormId == 227) {
                            mBinding?.leftDrawerMenu?.tvFpsFromNonForest?.showView()
                        } else if (matchingFormId == 228) {
                            mBinding?.leftDrawerMenu?.tvFpsFromForest?.showView()
                        } else if (matchingFormId == 229) {
                            mBinding?.leftDrawerMenu?.tvAssistanceForEa?.showView()
                        } else if (matchingFormId == 230) {
                            mBinding?.leftDrawerMenu?.tvNlmEdp?.showView()
                        }
                    }
                } else if (matchingSchemeId == 201) {
                    mBinding?.leftDrawerMenu?.tvNationalDairyDevelopment?.showView()
                    for (matchingFormId in matchingFormIds) {
                        if (matchingFormId == 219) {
                            mBinding?.leftDrawerMenu?.tvNationalLevelComponentA?.showView()
                        } else if (matchingFormId == 234) {
                            mBinding?.leftDrawerMenu?.tvReportsOfNlm?.showView()
                        } else if (matchingFormId == 220) {
                            mBinding?.leftDrawerMenu?.tvNationalLevelComponentB?.showView()
                        } else if (matchingFormId == 209) {
                            mBinding?.leftDrawerMenu?.tvMilkUnionVisitReport?.showView()
                        } else if (matchingFormId == 205) {
                            mBinding?.leftDrawerMenu?.tvDairyPlantVisitReport?.showView()
                        } else if (matchingFormId == 210) {
                            mBinding?.leftDrawerMenu?.tvDcsBmsCenterVisitReport?.showView()
                        } else if (matchingFormId == 211) {
                            mBinding?.leftDrawerMenu?.tvStateCenterLabVisitReport?.showView()
                        } else if (matchingFormId == 212) {
                            mBinding?.leftDrawerMenu?.tvMilkProcessing?.showView()
                        } else if (matchingFormId == 213) {
                            mBinding?.leftDrawerMenu?.tvMilkProductMarketing?.showView()
                        } else if (matchingFormId == 214) {
                            mBinding?.leftDrawerMenu?.tvProductivityEnhancementServices?.showView()
                        }
                    }
                } else if (matchingSchemeId == 204) {
                    mBinding?.leftDrawerMenu?.tvRashtriyaGokulMission?.showView()
                    for (matchingFormId in matchingFormIds) {
                        if (matchingFormId == 202) {
                            mBinding?.leftDrawerMenu?.tvStateImplementingAgency?.showView()
                        } else if (matchingFormId == 236) {
                            mBinding?.leftDrawerMenu?.tvArtificialInsemination?.showView()
                        } else if (matchingFormId == 237) {
                            mBinding?.leftDrawerMenu?.tvSemenStation?.showView()
                        } else if (matchingFormId == 238) {
                            mBinding?.leftDrawerMenu?.tvTrainingCenters?.showView()
                        } else if (matchingFormId == 239) {
                            mBinding?.leftDrawerMenu?.tvBullMotherFarms?.showView()
                        } else if (matchingFormId == 240) {
                            mBinding?.leftDrawerMenu?.tvBreedMultiplication?.showView()
                        }
                    }
                } else if (matchingSchemeId == 1) {
                    mBinding?.leftDrawerMenu?.tvUsers?.showView()
                }
            }
        } else {
            Log.d("Scheme", "Nothing found")
        }
    }
}