package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.NodalOfficer
import com.nlm.model.OnlyCreated
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.OnlyCreatedAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class StateMobileVeterinaryActivity : BaseActivity<ActivityStateMobileVeterinaryBinding>() {
    private var mBinding: ActivityStateMobileVeterinaryBinding? = null
    private lateinit var onlyCreatedAdapter: OnlyCreatedAdapter
    private lateinit var onlyCreated: List<OnlyCreated>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom : Int = 0

    override val layoutId: Int
        get() = R.layout.activity_state_mobile_veterinary


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        isFrom = intent?.getIntExtra("isFrom", 0)!!

        when (isFrom) {
            1 -> {
                mBinding!!.tvHeading.text = "State Mobile Veterinary Unit"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", null, null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", null, null, null,
                    ),
                )
            }

            2 -> {
                mBinding!!.tvHeading.text = "District Mobile Veterinary Unit"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", null, null,
                    ),
                )
            }

            3 -> {
                mBinding!!.tvHeading.text = "Block Mobile Veterinary Unit"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", "West Tripura", null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", "JAMMU", null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", "CHANGLANG", null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", "PANNA", null,
                    ),
                )
            }

            4 -> {
                mBinding!!.tvHeading.text = "Beneficiary/farmer Mobile Veterinary Unit"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", "West Tripura", "West Tripura",
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", "JAMMU", "West Tripura",
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", "CHANGLANG", "West Tripura",
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", "PANNA", "West Tripura",
                    ),
                )
            }
            5 -> {
                mBinding!!.tvHeading.text = "List of State Vaccination Programme"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", null, null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", null, null, null,
                    ),
                )
            }
            6 -> {
                mBinding!!.tvHeading.text = "List of District Vaccination Programme"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", null, null,
                    ),
                )
            }

            7 -> {
                mBinding!!.tvHeading.text = "List of Beneficiary/farmer Vaccination Programme"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", "West Tripura", "West Tripura",
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", "JAMMU", "West Tripura",
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", "CHANGLANG", "West Tripura",
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", "PANNA", "West Tripura",
                    ),
                )
            }

            8 -> {
                mBinding!!.tvHeading.text = "List of ASCAD State"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", null, null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", null, null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", null, null, null,
                    ),
                )
            }

            9 -> {
                mBinding!!.tvHeading.text = "List of ASCAD District"
                onlyCreated = listOf(
                    OnlyCreated(
                        "CHANDIGARH",
                        "2024-08-28", "West Tripura", null, null,
                    ),
                    OnlyCreated(
                        "HIMACHAL PRADESH",
                        "2024-08-28", "JAMMU", null, null,
                    ),
                    OnlyCreated(
                        "BIHAR",
                        "2024-08-27", "CHANGLANG", null, null,
                    ),
                    OnlyCreated(
                        "GOA",
                        "2024-08-21", "PANNA", null, null,
                    ),
                )
            }

        }



        mBinding!!.fabAddAgency.setOnClickListener {
            val intent = Intent(this, AddNewMobileVeterinaryUnit::class.java).putExtra("isFrom",isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = OnlyCreatedAdapter(onlyCreated, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvMobileVeterinaryUnit.layoutManager = layoutManager
        mBinding!!.rvMobileVeterinaryUnit.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}