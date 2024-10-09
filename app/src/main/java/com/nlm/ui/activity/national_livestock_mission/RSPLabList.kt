package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityRsplabBinding
import com.nlm.model.Rsp_lab_data
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.ui.adapter.RSPLABListAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class RSPLabList : BaseActivity<ActivityRsplabBinding>(){
    override val layoutId: Int
        get() = R.layout.activity_rsplab
    private var mBinding: ActivityRsplabBinding? = null
    private lateinit var implementingAdapter: RSPLABListAdapter

    private lateinit var nodalOfficerList: List<Rsp_lab_data>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            Rsp_lab_data(
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
            val intent = Intent(this@RSPLabList, RspLabSemenForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }
    private fun implementingAgency() {
        implementingAdapter = RSPLABListAdapter(nodalOfficerList,0,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvRspLabView.layoutManager = layoutManager
        mBinding!!.rvRspLabView.adapter = implementingAdapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


}