package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityDairyPlantVisitNddBinding
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.model.DairyPlantVisit
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.DairyPlantVisitAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.utilities.BaseActivity

class DairyPlantVisitNDDActivity : BaseActivity<ActivityDairyPlantVisitNddBinding>() {
    private var mBinding: ActivityDairyPlantVisitNddBinding? = null
    private lateinit var adapter: DairyPlantVisitAdapter
    private lateinit var list: List<DairyPlantVisit>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_dairy_plant_visit_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@DairyPlantVisitNDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 26)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

            DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

            ),DairyPlantVisit(
                    "NAGALAND",
            "Quos dolor veniam n",
            "KIPHIRE",
            "2024-08-20",
            "Voluptates magnam co"

        ),DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

            ),DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

                ),
DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

                ),
DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

                ),
DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

                ),
DairyPlantVisit(
                "NAGALAND",
                "Quos dolor veniam n",
                "KIPHIRE",
                "2024-08-20",
                "Voluptates magnam co"

                ),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@DairyPlantVisitNDDActivity,AddDairyPlantVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = DairyPlantVisitAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}