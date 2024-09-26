package com.nlm.ui.activity.national_livestock_mission

import android.view.View
import com.nlm.R
import com.nlm.databinding.ActivityImportOfExoticGoatBinding
import com.nlm.utilities.BaseActivity

class ImportOfExoticGoatForms : BaseActivity<ActivityImportOfExoticGoatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat
    private var mBinding: ActivityImportOfExoticGoatBinding? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
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