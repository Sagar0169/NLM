package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.databinding.ActivityStateCenterLabVisitNddBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NodalOfficer
import com.nlm.model.StateCenterVisit
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
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
            val intent = Intent(this@StateCenterLabVisitNDDActivity,AddMilkUnionVisit::class.java).putExtra("isFrom",1)
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