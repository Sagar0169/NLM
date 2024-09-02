package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.utilities.BaseActivity

class MilkUnionVisitNDDActivity : BaseActivity<ActivityMilkUnionVisitNddBinding>() {
    private var mBinding: ActivityMilkUnionVisitNddBinding? = null
    private lateinit var adapter: MilkUnionVisitAdapter
    private lateinit var list: List<MilkUnionVisit>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_milk_union_visit_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

                ),
            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

            ),
            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

            ),
            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

            ),
            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

            ),
            MilkUnionVisit(
                "TAMIL NADU",
                "Sheila Benjamin",
                "CHENGALPATTU,CHENNAI",
                "Shri Sudhanshu Shekhar",
                "2024-08-20"

            ),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@MilkUnionVisitNDDActivity,AddMilkUnionVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = MilkUnionVisitAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}