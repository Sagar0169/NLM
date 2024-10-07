package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMilkProductMarketingNddBinding
import com.nlm.model.MilkProductMarketing
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.ndd.MilkProductMarketingAdapter
import com.nlm.utilities.BaseActivity

class MilkProductMarketingNDDActivity : BaseActivity<ActivityMilkProductMarketingNddBinding>() {
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
        fun filter(view: View) {
            val intent = Intent(
                this@MilkProductMarketingNDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 30)
            startActivity(intent)
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
            val intent = Intent(this@MilkProductMarketingNDDActivity, AddMilkProductMarketing::class.java).putExtra("isFrom",1)
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