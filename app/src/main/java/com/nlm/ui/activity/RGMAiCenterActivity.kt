package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.databinding.ActivityRgmaiCenterAcitivityBinding
import com.nlm.databinding.ActivityRgmvitroFertilizationBinding
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.NodalOfficer
import com.nlm.model.OnlyCreated
import com.nlm.model.RGMVitro
import com.nlm.model.RgmAi
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.OnlyCreatedAdapter
import com.nlm.ui.adapter.RGMVItroAdapter
import com.nlm.ui.adapter.RgmAiAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class RGMAiCenterActivity : BaseActivity<ActivityRgmaiCenterAcitivityBinding>() {
    private var mBinding: ActivityRgmaiCenterAcitivityBinding? = null
    private lateinit var onlyCreatedAdapter: RgmAiAdapter
    private lateinit var onlyCreated: List<RgmAi>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_rgmai_center_acitivity


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent = Intent(
                this@RGMAiCenterActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 12)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        onlyCreated = listOf(
            RgmAi(
                "Flynn Santiago",
                "TELANGANA",
                "Mulugu",
                "Dolorem sit omnis odit aut aliquid ullam similique esse voluptatem Itaque in Nam dignissimos nesciunt",
                "02-09-2024",
                "Submitted",
                "02-09-2024",
                "Super Admin"

            ),
            RgmAi(
                "ass",
                "UTTAR PRADESH",
                "GHAZIABAD",
                "Shalimar Garden",
                "14-08-2024",
                "Submitted",
                "16-08-2024",
                "Super Admin"


            ),
        )



        mBinding!!.fabAddAgency.setOnClickListener {
            val intent =
                Intent(this, AddRgmAiCenterAcitivity::class.java).putExtra("isFrom", isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = RgmAiAdapter(onlyCreated, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvMobileVeterinaryUnit.layoutManager = layoutManager
        mBinding!!.rvMobileVeterinaryUnit.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}