package com.nlm.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityAddNlmAssistanceForQfspactivityBinding
import com.nlm.databinding.ActivityAddNlmFpForestLandBinding
import com.nlm.model.NlmEdp
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.utilities.BaseActivity

class AddNlmAssistanceForQFSPActivity :
    BaseActivity<ActivityAddNlmAssistanceForQfspactivityBinding>() {
    private var mBinding: ActivityAddNlmAssistanceForQfspactivityBinding? = null
    private lateinit var onlyCreatedAdapter: NlmEdpAdapter
    private lateinit var onlyCreated: List<NlmEdp>
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_add_nlm_assistance_for_qfspactivity


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = intent?.getIntExtra("isFrom", 0)!!

        when (isFrom) {
            1 -> {
                mBinding!!.tvHeading.text = "Add New Fpfrom Non Forest"
            }
        }


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