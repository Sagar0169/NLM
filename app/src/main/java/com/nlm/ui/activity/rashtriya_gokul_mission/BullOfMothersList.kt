package com.nlm.ui.activity.rashtriya_gokul_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityBullOfMothersListBinding
import com.nlm.model.Bull_Mothers
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.rgm.Bull_Of_Mothers_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class BullOfMothersList : BaseActivity<ActivityBullOfMothersListBinding>() {
    private var mBinding: ActivityBullOfMothersListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_bull_of_mothers_list
    private lateinit var implementingAdapter: Bull_Of_Mothers_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<Bull_Mothers>
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)==AppConstants.SUPER_ADMIN || Utility.getPreferenceString(this, AppConstants.ROLE_NAME)==AppConstants.Nodal_Officer||
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME)==AppConstants.NLM|| Utility.getPreferenceString(this, AppConstants.ROLE_NAME)==AppConstants.ADMIN )
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            Bull_Mothers(
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",

            ),
        )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@BullOfMothersList, BullMotherFarms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@BullOfMothersList,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 34)
            startActivity(intent)
        }
    }
    private fun implementingAgency() {
        implementingAdapter = Bull_Of_Mothers_Adapter(nodalOfficerList,1,Utility.getPreferenceString(this,AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}