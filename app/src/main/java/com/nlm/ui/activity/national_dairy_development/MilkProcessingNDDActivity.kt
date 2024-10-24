package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMilkProcessingNddBinding
import com.nlm.model.MilkProcessing
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.ndd.MilkProcessingAdapter
import com.nlm.utilities.BaseActivity

class MilkProcessingNDDActivity : BaseActivity<ActivityMilkProcessingNddBinding>() {
    private var mBinding: ActivityMilkProcessingNddBinding? = null
    private lateinit var adapter: MilkProcessingAdapter
    private lateinit var list: List<MilkProcessing>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_milk_processing_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@MilkProcessingNDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 29)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

         MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),
           MilkProcessing(
             "Name of the Processing Plant333",
             "Name of the Milk Unio222",
             "JHARKHAND",
             "JAMTARA",
             "2024-09-02"
         ),

        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@MilkProcessingNDDActivity, AddMilkProcessing::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = MilkProcessingAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}