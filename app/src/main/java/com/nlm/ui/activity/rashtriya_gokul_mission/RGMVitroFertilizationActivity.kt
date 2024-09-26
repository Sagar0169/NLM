package com.nlm.ui.activity.rashtriya_gokul_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityRgmvitroFertilizationBinding
import com.nlm.model.RGMVitro
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.RGMVItroAdapter
import com.nlm.utilities.BaseActivity

class RGMVitroFertilizationActivity : BaseActivity<ActivityRgmvitroFertilizationBinding>() {
    private var mBinding: ActivityRgmvitroFertilizationBinding? = null
    private lateinit var onlyCreatedAdapter: RGMVItroAdapter
    private lateinit var onlyCreated: List<RGMVitro>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_rgmvitro_fertilization


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent = Intent(
                this@RGMVitroFertilizationActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 11)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        onlyCreated = listOf(
            RGMVitro(
                "Bihar",
                "2024-08-28", "Active"
            ),
            RGMVitro(
                "LADAKH",
                "2024-08-28", "Active"
            ),

            RGMVitro(
                "DELHI",
                "2024-08-28", "Active"
            ),

            RGMVitro(
                "JAMMU AND KASHMIR",
                "2024-08-28", "Active"
            ),

            RGMVitro(
                "KERALA",
                "2024-08-28", "Active"
            ),

            RGMVitro(
                "ARUNACHAL PRADESH",
                "2024-08-28", "Active"
            ),


            RGMVitro(
                "GOA",
                "2024-08-28", "Active"
            ),


            )



        mBinding!!.fabAddAgency.setOnClickListener {
            val intent =
                Intent(this, AddRGMVitroFertilizatonActivity::class.java).putExtra("isFrom", isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = RGMVItroAdapter(onlyCreated, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvMobileVeterinaryUnit.layoutManager = layoutManager
        mBinding!!.rvMobileVeterinaryUnit.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}