package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.utilities.BaseFragment


class StateSemenInfrastructureFragment : BaseFragment<FragmentStateSemenInfrastructureBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_state__semen__infrastructure
    private var mBinding: FragmentStateSemenInfrastructureBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StateSemenInfrastructureAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {

        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", "",""))

        adapter = StateSemenInfrastructureAdapter(programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }


}