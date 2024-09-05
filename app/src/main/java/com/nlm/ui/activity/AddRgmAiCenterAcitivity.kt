package com.nlm.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivityAddNewFspPlantStorageBinding
import com.nlm.databinding.ActivityAddNlmFpForestLandBinding
import com.nlm.databinding.ActivityAddRgmAiCenterAcitivityBinding
import com.nlm.model.NlmEdp
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.utilities.BaseActivity

class AddRgmAiCenterAcitivity : BaseActivity<ActivityAddRgmAiCenterAcitivityBinding>() {
    private var mBinding: ActivityAddRgmAiCenterAcitivityBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgrammeAdapter
    private lateinit var programmeList: MutableList<Array<String>>

    override val layoutId: Int
        get() = R.layout.activity_add_rgm_ai_center_acitivity


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = ProgrammeAdapter(programmeList)
        recyclerView.adapter = adapter

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