package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityAddNewAssistanceForEaBinding
import com.nlm.ui.adapter.Format6YearWiseFinancialProgressAdapter
import com.nlm.ui.adapter.ImportExoticAdapterDetailOfImport
import com.nlm.utilities.BaseActivity

class AddNewAssistanceForEaActivity : BaseActivity<ActivityAddNewAssistanceForEaBinding>() {

    private var mBinding: ActivityAddNewAssistanceForEaBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_add_new_assistance_for_ea


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }


    override fun setObservers() {
    }
}