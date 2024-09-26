package com.nlm.ui.activity.rashtriya_gokul_mission

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddRgmAiCenterAcitivityBinding
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity

class AddRgmAiCenterAcitivity : BaseActivity<ActivityAddRgmAiCenterAcitivityBinding>() {
    private var mBinding: ActivityAddRgmAiCenterAcitivityBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgrammeAdapter
    private lateinit var stateAdapter: StateAdapter

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var programmeList: MutableList<Array<String>>
    private val stateList = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
    )
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