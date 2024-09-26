package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.FragmentRSPAvailabilityOfEquipmentBinding
import com.nlm.ui.adapter.rgm.AvailabilityOfEquipmentAdapter
import com.nlm.utilities.BaseFragment


class RSPAvailabilityOfEquipmentFragment : BaseFragment<FragmentRSPAvailabilityOfEquipmentBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__availability_of_equipment
    private var mBinding: FragmentRSPAvailabilityOfEquipmentBinding?=null
    private lateinit var adapter: AvailabilityOfEquipmentAdapter

    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = AvailabilityOfEquipmentAdapter(programmeList)
        mBinding?.recyclerView2?.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }
}