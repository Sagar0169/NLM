package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.databinding.ActivityNlmComponentANddBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NLMComponentA
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.ui.adapter.ndd.NLMComponentAAdapter
import com.nlm.utilities.BaseActivity

class NLMComponentANDDActivity : BaseActivity<ActivityNlmComponentANddBinding>() {
    private var mBinding: ActivityNlmComponentANddBinding? = null
    private lateinit var adapter: NLMComponentAAdapter
    private lateinit var list: List<NLMComponentA>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_nlm_component_a_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@NLMComponentANDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 25)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        list = listOf(

            NLMComponentA(
                "TAMIL NADU",
                "Sheila BenjaminBenjaminBenjamin",
                "NPDD_MP_01C",
                "2024",
                "submitted",
                "2024-08-20"

                ),
  NLMComponentA(
                "TAMIL NADU",
                "Sheila Benjamin",
                "NPDD_MP_01C",
                "2024",
                "submitted",
                "2024-08-20"

                ),
  NLMComponentA(
                "TAMIL NADU",
                "Sheila Benjamin",
                "NPDD_MP_01C",
                "2024",
                "submitted",
                "2024-08-20"

                ),
  NLMComponentA(
                "TAMIL NADU",
                "Sheila Benjamin",
                "NPDD_MP_01C",
                "2024",
                "submitted",
                "2024-08-20"

                ),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@NLMComponentANDDActivity,AddNLMComponentA::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = NLMComponentAAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}