package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityNlmFspPlantStorageBinding
import com.nlm.model.NlmFpForest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NlmFpForestAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class NlmFspPlantStorageActivity : BaseActivity<ActivityNlmFspPlantStorageBinding>() {
    private var mBinding: ActivityNlmFspPlantStorageBinding? = null
    private lateinit var onlyCreatedAdapter: NlmFpForestAdapter
    private lateinit var onlyCreated: List<NlmFpForest>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_nlm_fsp_plant_storage


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@NlmFspPlantStorageActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 14)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
//        {
//            mBinding!!.fabAddAgency.hideView()
//        }
        onlyCreated = listOf(
                NlmFpForest(
                    state = "GUJARAT",
                    district = "DAHOD",
                    location = "test",
                    agencyName = "test",
                    areaCovered = "N/A",
                    created = "2024-08-21",
            ),
            NlmFpForest(
                state = "DELHI",
                district = "NORTH WEST",
                location = "test",
                agencyName = "test",
                areaCovered = "N/A",
                created = "2024-08-21"
            ),
            NlmFpForest(
                state = "GUJARAT",
                district = "DAHOD",
                location = "test",
                agencyName = "test",
                areaCovered = "N/A",
                created = "2024-08-21"
            ),

            )




        mBinding!!.fabAddAgency.setOnClickListener {
            val intent =
                Intent(this, AddNewFspPlantStorageActivity::class.java).putExtra("isFrom", 3)
            startActivity(intent)
        }
        onlyCreatedAdapter()
    }

    private fun onlyCreatedAdapter() {
        onlyCreatedAdapter = NlmFpForestAdapter(onlyCreated, 3,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvNlmEdp.layoutManager = layoutManager
        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}