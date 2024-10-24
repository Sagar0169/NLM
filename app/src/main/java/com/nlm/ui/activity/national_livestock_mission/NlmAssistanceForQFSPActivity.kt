package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmAssistanceForQfspactivityBinding
import com.nlm.model.NlmFpForest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NlmFpForestAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class NlmAssistanceForQFSPActivity : BaseActivity<ActivityNlmAssistanceForQfspactivityBinding>() {
    private var mBinding: ActivityNlmAssistanceForQfspactivityBinding? = null
    private lateinit var onlyCreatedAdapter: NlmFpForestAdapter
    private lateinit var onlyCreated: List<NlmFpForest>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_nlm_assistance_for_qfspactivity


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@NlmAssistanceForQFSPActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 13)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = 4
//
//        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
//        {
//            mBinding!!.fabAddAgency.hideView()
//        }
        onlyCreated = listOf(
            NlmFpForest(
                "GUJARAT",
                "DAHOD",
                "test",
                "Murray and Shepard LLC",
                "N/A",
                "2024-08-21",
                "Aut dolore illum officiis veniam unde",
                "Iusto blanditiis rerum enim magni et exe",
            ),
            NlmFpForest(
                "DELHI",
                "NORTH WEST",
                "test",
                "Clayton Sullivan Co",
                "N/A",
                "2024-08-21",
                "Qui praesentium aut accusamus dolorem se",
                "Veritatis perferendis totam ipsum sunt",
            ),
        )




        mBinding!!.fabAddAgency.setOnClickListener {
            val intent =
                Intent(this, AddNlmAssistanceForQFSPActivity::class.java).putExtra("isFrom", isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = NlmFpForestAdapter(onlyCreated, isFrom,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvNlmEdp.layoutManager = layoutManager
        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}