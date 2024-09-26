package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmAssistanceforEaBinding
import com.nlm.model.NlmEdp
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class NlmAssistanceForEa : BaseActivity<ActivityNlmAssistanceforEaBinding>() {
    private var mBinding: ActivityNlmAssistanceforEaBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private lateinit var onlyCreated: List<NlmEdp>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_nlm_assistancefor_ea


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent = Intent(
                this@NlmAssistanceForEa,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 16)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = intent?.getIntExtra("isFrom", 0)!!
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        onlyCreated = listOf(
            NlmEdp(
                "No",
                "2024-08-21"
            ),
            NlmEdp(
                "No",
                "2024-08-13"
            ),
            NlmEdp(
                "No",
                "2024-08-13"
            ),
        )




        mBinding!!.fabAddAgency.setOnClickListener {
            val intent = Intent(this, AddNewAssistanceForEaActivity::class.java).putExtra("isFrom", isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = NlmEdpAdapter(onlyCreated, isFrom,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvNlmEdp.layoutManager = layoutManager
        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}