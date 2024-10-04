package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityStateSemenBankListBinding
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class StateSemenBankList : BaseActivity<ActivityStateSemenBankListBinding>() {
    private var mBinding: ActivityStateSemenBankListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank_list
    private lateinit var implementingAdapter: NSLP_IA_Adapter

    private lateinit var nodalOfficerList: List<OnlyCreatedNlm>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            OnlyCreatedNlm(
                "DELHI",
                "2024-08-27",
                "NORTH",
                "",
                "234242342432","1977","Voluptas inventore n"

            ),
            OnlyCreatedNlm(
                "N/A",
                "2024-08-23",
                "N/A",
                "",
                "895353543535453","2000","Ab voluptatem cum deserunt fugiat cupiditate omnis quis magni"

            ),
            OnlyCreatedNlm(
                "HARYANA",
                "2024-08-12",
                "KARNAL",
                "",
                "9996543218","2017","test"

            ),
            OnlyCreatedNlm(
                "N/A",
                "2024-08-21",
                "N/A",
                "",
                "9990157283","1947","T-29 Okhla New Delhi"

            ),
            OnlyCreatedNlm(
                "N/A",
                "2024-08-16",
                "N/A",
                "",
                "9990157283","2001","Facilis perspiciatis cillum commodo atque nulla culpa"

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
        implementingAdapter = NSLP_IA_Adapter(nodalOfficerList,2,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvStateSemenLabView.layoutManager = layoutManager
        mBinding!!.rvStateSemenLabView.adapter = implementingAdapter
    }
}