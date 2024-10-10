package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityStateSemenBankListBinding
import com.nlm.model.State_Semen_Bank

import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.ui.adapter.StateSemenAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class StateSemenBankList : BaseActivity<ActivityStateSemenBankListBinding>() {
    private var mBinding: ActivityStateSemenBankListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank_list
    private lateinit var implementingAdapter: StateSemenAdapter

    private lateinit var nodalOfficerList: List<State_Semen_Bank>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            State_Semen_Bank(
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
            ),
            )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@StateSemenBankList, StateSemenBankForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@StateSemenBankList,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 34)
            startActivity(intent)
        }




    }
    private fun implementingAgency() {
        implementingAdapter = StateSemenAdapter(nodalOfficerList,2,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvStateSemenLabView.layoutManager = layoutManager
        mBinding!!.rvStateSemenLabView.adapter = implementingAdapter
    }
}