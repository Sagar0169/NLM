package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.databinding.ActivityNlmEdpBinding
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.NlmEdp
import com.nlm.model.NodalOfficer
import com.nlm.model.OnlyCreated
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.ui.adapter.OnlyCreatedAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NlmEdpActivity : BaseActivity<ActivityNlmEdpBinding>() {
    private var mBinding: ActivityNlmEdpBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private lateinit var onlyCreated: List<NlmEdp>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_nlm_edp


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        onlyCreated = listOf(
            NlmEdp(
                "Omnis in ipsam sunt",
                "2024-08-28"
            ),
            NlmEdp(
                "Please comment",
                "2024-08-28"
            ),
            NlmEdp(
                "Voluptatem et veniam",
                "2024-08-28"
            ),
            NlmEdp(
                "Aliqua Voluptatibus",
                "2024-08-28"
            ),


        )


        mBinding!!.fabAddAgency.setOnClickListener {
            val intent = Intent(this, AddNlmEdpActivity::class.java).putExtra("isFrom",isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = NlmEdpAdapter(onlyCreated, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvNlmEdp.layoutManager = layoutManager
        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}