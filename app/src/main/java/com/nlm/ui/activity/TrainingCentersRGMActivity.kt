package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityBreedMultiplicationRgmBinding
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.databinding.ActivityTrainingCentersRgmBinding
import com.nlm.model.MilkUnionVisit
import com.nlm.model.NodalOfficer
import com.nlm.model.TrainingCenters
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.ui.adapter.rgm.BreedMultiplicationAdapter
import com.nlm.ui.adapter.rgm.TrainingCentersAdapter
import com.nlm.utilities.BaseActivity

class TrainingCentersRGMActivity : BaseActivity<ActivityTrainingCentersRgmBinding>() {
    private var mBinding: ActivityTrainingCentersRgmBinding? = null
    private lateinit var adapter: TrainingCentersAdapter
    private lateinit var list: List<TrainingCenters>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_training_centers_rgm


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@TrainingCentersRGMActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 32)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

            TrainingCenters(
                "TAMIL NADU",
                "CHENGALPATTU,CHENNAI",
                "Enim optio repudiandae sit voluptas omnis iure rerum veniam sed molestiae amet in beatae ut",
                "Submitted",
                "2024-08-20"

                ),
   TrainingCenters(
                "TAMIL NADU",
                "CHENGALPATTU,CHENNAI",
                "Enim optio repudiandae sit voluptas omnis iure rerum veniam sed molestiae amet in beatae ut",
                "Submitted",
                "2024-08-20"

                ),
   TrainingCenters(
                "TAMIL NADU",
                "CHENGALPATTU,CHENNAI",
                "Enim optio repudiandae sit voluptas omnis iure rerum veniam sed molestiae amet in beatae ut",
                "Submitted",
                "2024-08-20"

                ),
   TrainingCenters(
                "TAMIL NADU",
                "CHENGALPATTU,CHENNAI",
                "Enim optio repudiandae sit voluptas omnis iure rerum veniam sed molestiae amet in beatae ut",
                "Submitted",
                "2024-08-20"

                ),
   TrainingCenters(
                "TAMIL NADU",
                "CHENGALPATTU,CHENNAI",
                "Enim optio repudiandae sit voluptas omnis iure rerum veniam sed molestiae amet in beatae ut",
                "Submitted",
                "2024-08-20"

                ),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@TrainingCentersRGMActivity,AddTrainingCenters::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = TrainingCentersAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}