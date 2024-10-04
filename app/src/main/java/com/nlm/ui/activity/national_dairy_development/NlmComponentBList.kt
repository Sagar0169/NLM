package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmComponentBlistBinding
import com.nlm.model.NLM_CompB
import com.nlm.ui.adapter.NlmComponentBadapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class NlmComponentBList : BaseActivity<ActivityNlmComponentBlistBinding>() {
    private var mBinding: ActivityNlmComponentBlistBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_nlm_component_blist
    private lateinit var implementingAdapter: NlmComponentBadapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<NLM_CompB>
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)== AppConstants.SUPER_ADMIN ||
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME)== AppConstants.NPDD_State_Level_Monitor|| Utility.getPreferenceString(this, AppConstants.ROLE_NAME)== AppConstants.ADMIN )
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            NLM_CompB(
                "mzfmd",
                "ARUNACHAL PRADESH",
                "EAST SIANG",
                "ansbd",
                "nsd",
                "2024-08-20"
            ),
            NLM_CompB(
                "test",
                "LAKSHADWEEP",
                "LAKSHADWEEP DISTRICT",
                "test",
                "test",
                "2024-08-10"),

            )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent =
                Intent(this@NlmComponentBList, NlmComponentBDairyDevelopment::class.java).putExtra("isFrom", 1)
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
//            val intent = Intent(
//                this@Semen_Station_List,
//                FilterStateActivity::class.java
//            ).putExtra("isFrom", 34)
//            startActivity(intent)
            Toast.makeText(this@NlmComponentBList,"filter to be added ",Toast.LENGTH_SHORT).show()
        }
    }
    private fun implementingAgency() {
        implementingAdapter = NlmComponentBadapter(this,nodalOfficerList,Utility.getPreferenceString(this,AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }

}