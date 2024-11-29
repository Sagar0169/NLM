package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.content.Intent
import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityVaccinationProgrammerBinding
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity

class VaccinationProgrammerActivity : BaseActivity<ActivityVaccinationProgrammerBinding>() {
    private var mBinding: ActivityVaccinationProgrammerBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_vaccination_programmer


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }


        fun state(view: View) {
            startActivity(Intent(
                this@VaccinationProgrammerActivity,
                VaccinationProgrammerListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.state)))
        }

        fun district(view: View) {
            startActivity(Intent(
                this@VaccinationProgrammerActivity,
                VaccinationProgrammerListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.district)))
        }


        fun farmer(view: View) {
            startActivity(Intent(
                this@VaccinationProgrammerActivity,
                VaccinationProgrammerListActivity::class.java
            ).putExtra(AppConstants.IS_FROM, getString(R.string.farmer_level)))
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}