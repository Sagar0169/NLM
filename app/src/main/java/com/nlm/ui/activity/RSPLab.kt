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
import com.nlm.databinding.ActivityRspLabSemenBinding
import com.nlm.databinding.ActivityRsplabBinding
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.BaseActivity

class RSPLab : BaseActivity<ActivityRsplabBinding>(){
    override val layoutId: Int
        get() = R.layout.activity_rsplab
    private var mBinding: ActivityRsplabBinding? = null
    private lateinit var implementingAdapter: NSLP_IA_Adapter

    private lateinit var nodalOfficerList: List<OnlyCreatedNlm>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        nodalOfficerList = listOf(
            OnlyCreatedNlm(
                "HARYANA",
                "2024-08-27",
                "AMBALA",
                "",
                "234242342432","2017","tesd"

            ),
            OnlyCreatedNlm(
                "GUJARAT",
                "2024-08-23",
                "JUNAGADH",
                "",
                "9996543218","2017","test2"

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
            val intent = Intent(this@RSPLab,NLSIAForm::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }
    private fun implementingAgency() {
        implementingAdapter = NSLP_IA_Adapter(nodalOfficerList,0)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvRspLabView.layoutManager = layoutManager
        mBinding!!.rvRspLabView.adapter = implementingAdapter
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