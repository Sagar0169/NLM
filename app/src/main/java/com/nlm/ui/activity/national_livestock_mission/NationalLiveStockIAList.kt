package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNationalLiveStockIaBinding
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import java.lang.Appendable

class NationalLiveStockIAList : BaseActivity<ActivityNationalLiveStockIaBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_national_live_stock_ia
    private lateinit var implementingAdapter: NSLP_IA_Adapter
    private var mBinding: ActivityNationalLiveStockIaBinding? = null
    private lateinit var nodalOfficerList: List<OnlyCreatedNlm>
    private var layoutManager: LinearLayoutManager? = null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        if(Utility.getPreferenceString(this,AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
        OnlyCreatedNlm(
            "ANDAMAN AND NICOBAR ISLANDS",
            "2024-08-12",
            "",
            "",
            "","Super Admin","Zenaida Dominguez"

        ),
        OnlyCreatedNlm(
            "DELHI",
            "2024-08-12",
            "",
            "",
            "","Super Admin","Name and Location of IA"

        ),
        OnlyCreatedNlm(
            "LADAKH",
            "2024-08-12",
            "",
            "",
            "","Super Admin","Chester Parsons"

        ),
        OnlyCreatedNlm(
            "CHANDIGARH",
            "2024-08-16",
            "",
            "",
            "","Super Admin","Ocean Gould"

        ),
        OnlyCreatedNlm(
            "HIMACHAL PRADESH",
            "2024-08-16",
            "",
            "",
            "","Super Admin","Orson Erickson"

        ),
        OnlyCreatedNlm(
            "BIHAR",
            "2024-08-16",
            "",
            "",
            "","Super Admin","Name and Locat"

        ),
        OnlyCreatedNlm(
            "HIMACHAL PRADESH",
            "2024-08-21",
            "",
            "",
            "","Nodal NLM","nodal nlm update"

        ),
        OnlyCreatedNlm(
            "BIHAR",
            "2024-08-21",
            "",
            "",
            "","","Name and Locat"

        ),
        OnlyCreatedNlm(
            "BIHAR",
            "2024-08-21",
            "",
            "",
            "","Angan Lal Nirala","j"

        ))
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