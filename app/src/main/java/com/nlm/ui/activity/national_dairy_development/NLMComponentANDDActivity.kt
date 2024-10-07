package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmComponentANddBinding
import com.nlm.model.NLMComponentA
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.ndd.NLMComponentAAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

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
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)== AppConstants.SUPER_ADMIN||Utility.getPreferenceString(this, AppConstants.ROLE_NAME)== AppConstants.ADMIN )
        {
            mBinding!!.fabAdd.hideView()
        }
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
            val intent = Intent(this@NLMComponentANDDActivity, AddNLMComponentA::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = NLMComponentAAdapter(list,Utility.getPreferenceString(this,AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}