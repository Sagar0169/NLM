package com.nlm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNationalLiveStockIaBinding
import com.nlm.databinding.ActivityNlsiaFormBinding
import com.nlm.model.NodalOfficer
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.BaseActivity

class National_Live_Stock_IA : BaseActivity<ActivityNationalLiveStockIaBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_national_live_stock_ia
    private lateinit var implementingAdapter: NSLP_IA_Adapter
    private var mBinding: ActivityNationalLiveStockIaBinding? = null
    private lateinit var nodalOfficerList: List<OnlyCreatedNlm>
    private var layoutManager: LinearLayoutManager? = null

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        nodalOfficerList = listOf(
        OnlyCreatedNlm(
            "ANDAMAN AND NICOBAR ISLANDS",
            "2024-08-12",
            "",
            "",
            "","","Zenaida Dominguez"

        ),
        OnlyCreatedNlm(
            "DELHI",
            "2024-08-12",
            "",
            "",
            "","","Name and Location of IA"

        ),
        OnlyCreatedNlm(
            "LADAKH",
            "2024-08-12",
            "",
            "",
            "","","Chester Parsons"

        ),
        OnlyCreatedNlm(
            "CHANDIGARH",
            "2024-08-16",
            "",
            "",
            "","","Ocean Gould"

        ),
        OnlyCreatedNlm(
            "HIMACHAL PRADESH",
            "2024-08-16",
            "",
            "",
            "","","Orson Erickson"

        ),
        OnlyCreatedNlm(
            "BIHAR",
            "2024-08-16",
            "",
            "",
            "","","Name and Locat"

        ),
        OnlyCreatedNlm(
            "HIMACHAL PRADESH",
            "2024-08-21",
            "",
            "",
            "","","nodal nlm update"

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
            "","","Name and Locat"

        ))
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@National_Live_Stock_IA,NLSIAForm::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }

    }
    private fun implementingAgency() {
        implementingAdapter = NSLP_IA_Adapter(nodalOfficerList,1)
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