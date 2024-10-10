package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNationalLiveStockIaBinding
import com.nlm.model.NLMIA_data

import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity

import com.nlm.utilities.Utility


class NationalLiveStockIAList : BaseActivity<ActivityNationalLiveStockIaBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_national_live_stock_ia
    private lateinit var implementingAdapter: NSLP_IA_Adapter
    private var mBinding: ActivityNationalLiveStockIaBinding? = null
    private lateinit var nodalOfficerList: List<NLMIA_data>
    private var layoutManager: LinearLayoutManager? = null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
//        if(Utility.getPreferenceString(this,AppConstants.ROLE_NAME)=="Super Admin")
//        {
//            mBinding!!.fabAddAgency.hideView()
//        }
        nodalOfficerList = listOf(
            NLMIA_data(
            "NA",
            "NA",
            "NA",
            "NA",


        ),
       )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@NationalLiveStockIAList, NLMIAForm::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }

    }
    private fun implementingAgency() {
        implementingAdapter = NSLP_IA_Adapter(nodalOfficerList,1,Utility.getPreferenceString(this,AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvImplementingaegency.layoutManager = layoutManager
        mBinding!!.rvImplementingaegency.adapter = implementingAdapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}