package com.nlm.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityAddNewAssistanceForEaBinding
import com.nlm.model.NlmEdp
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.utilities.BaseActivity

class AddNewAssistanceForEaActivity : BaseActivity<ActivityAddNewAssistanceForEaBinding>() {
    private var mBinding: ActivityAddNewAssistanceForEaBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private lateinit var onlyCreated: List<NlmEdp>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

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

//    private fun onlyCreatedAdapter() {
//        onlyCreatedAdapter = NlmEdpAdapter(onlyCreated, isFrom)
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        mBinding!!.rvNlmEdp.layoutManager = layoutManager
//        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
//    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}