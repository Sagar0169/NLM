package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.databinding.ActivityRspLabSemenBinding
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPAvailabilityOfEquipmentFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPAverageSemenDoseFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPBasicInformationFragment
import com.nlm.ui.fragment.national_livestock_mission_fragments.RSPSuggestionsForImprovement
import com.nlm.utilities.BaseActivity

class RspLabSemenForms() : BaseActivity<ActivityRspLabSemenBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_rsp_lab_semen
    private var mBinding: ActivityRspLabSemenBinding? = null
    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        loadFragment(RSPBasicInformationFragment())
    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }


        fun group(view: View){

        }

    }
    override fun setVariables() {

    }

    override fun setObservers() {

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }


}