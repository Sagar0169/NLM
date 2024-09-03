package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkProcessingNddBinding
import com.nlm.databinding.ActivityMilkProductMarketingNddBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.model.MilkProcessing
import com.nlm.model.MilkProductMarketing
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.MilkProcessingAdapter
import com.nlm.ui.adapter.ndd.MilkProductMarketingAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.utilities.BaseActivity

class MilkProductMarkettingNDDActivity : BaseActivity<ActivityMilkProductMarketingNddBinding>() {
    private var mBinding: ActivityMilkProductMarketingNddBinding? = null
    private lateinit var adapter: MilkProductMarketingAdapter
    private lateinit var list: List<MilkProductMarketing>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_milk_product_marketing_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

         MilkProductMarketing(
             "Samson Hendricks",
             "Connor Petty",
             "2024-08-21",
             "HARYANA",
             "AMBALA",
             "2024-08-29"
         ),
MilkProductMarketing(
             "Samson Hendricks",
             "Connor Petty",
             "2024-08-21",
             "HARYANA",
             "AMBALA",
             "2024-08-29"
         ),
MilkProductMarketing(
             "Samson Hendricks",
             "Connor Petty",
             "2024-08-21",
             "HARYANA",
             "AMBALA",
             "2024-08-29"
         ),
MilkProductMarketing(
             "Samson Hendricks",
             "Connor Petty",
             "2024-08-21",
             "HARYANA",
             "AMBALA",
             "2024-08-29"
         ),

        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@MilkProductMarkettingNDDActivity,AddMilkUnionVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = MilkProductMarketingAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}