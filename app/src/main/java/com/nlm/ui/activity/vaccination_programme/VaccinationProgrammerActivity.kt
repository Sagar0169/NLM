package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.databinding.ActivityVaccinationProgrammerBinding
import com.nlm.model.OnlyCreated
import com.nlm.ui.adapter.OnlyCreatedAdapter
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
            val intent = Intent(
                this@VaccinationProgrammerActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 5)
            startActivity(intent)
        }

        fun district(view: View) {
            val intent = Intent(
                this@VaccinationProgrammerActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 6)
            startActivity(intent)
        }


        fun farmer(view: View) {
            val intent = Intent(
                this@VaccinationProgrammerActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 7)
            startActivity(intent)
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