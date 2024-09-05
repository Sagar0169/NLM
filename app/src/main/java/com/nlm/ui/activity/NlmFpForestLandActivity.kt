package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmFpForestLandBinding
import com.nlm.model.NlmEdp
import com.nlm.model.NlmFpForest
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.ui.adapter.NlmFpForestAdapter
import com.nlm.utilities.BaseActivity

class NlmFpForestLandActivity : BaseActivity<ActivityNlmFpForestLandBinding>() {
    private var mBinding: ActivityNlmFpForestLandBinding? = null
    private lateinit var onlyCreatedAdapter: NlmFpForestAdapter
    private lateinit var onlyCreated: List<NlmFpForest>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_nlm_fp_forest_land


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@NlmFpForestLandActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 15)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = intent?.getIntExtra("isFrom", 0)!!

        when(isFrom){
            1->{
                mBinding!!.tvHeading.text="List of Fpfrom Non Forest"
            }
        }
        onlyCreated = listOf(
            NlmFpForest(
                "GUJARAT",
                "DAHOD",
                "test",
                "test",
                "N/A",
                "2024-08-21"
            ),
            NlmFpForest(
                "DELHI",
                "NORTH WEST",
                "test",
                "test",
                "N/A",
                "2024-08-21"
            ),
            NlmFpForest(
                "GUJARAT",
                "DAHOD",
                "test",
                "test",
                "N/A",
                "2024-08-21"
            ),

        )




        mBinding!!.fabAddAgency.setOnClickListener {
            val intent =
                Intent(this, AddNlmFpForestLandActivity::class.java).putExtra("isFrom", isFrom)
            startActivity(intent)
        }


        onlyCreatedAdapter()

    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = NlmFpForestAdapter(onlyCreated, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvNlmEdp.layoutManager = layoutManager
        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}