package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityStateCenterLabVisitNddBinding
import com.nlm.model.StateCenterVisit
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.ndd.StateCenterLabVisitAdapter
import com.nlm.utilities.BaseActivity

class StateCenterLabVisitNDDActivity : BaseActivity<ActivityStateCenterLabVisitNddBinding>() {
    private var mBinding: ActivityStateCenterLabVisitNddBinding? = null
    private lateinit var adapter: StateCenterLabVisitAdapter
    private lateinit var list: List<StateCenterVisit>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_state_center_lab_visit_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@StateCenterLabVisitNDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 28)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),
StateCenterVisit(
    "MAHARASHTRA"
    ,"BEED"
    ,"Quidem itaque iste u"
    ,"2024-08-20"

),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@StateCenterLabVisitNDDActivity, AddStateCenterLabtVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = StateCenterLabVisitAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}