package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentRSPSuggestionsForImprovementBinding
import com.nlm.ui.adapter.rgm.AverageSemenDoseAdapter
import com.nlm.utilities.BaseFragment


class RSPSuggestionsForImprovement : BaseFragment<FragmentRSPSuggestionsForImprovementBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__suggestions_for_improvement

    private var mBinding: FragmentRSPSuggestionsForImprovementBinding?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AverageSemenDoseAdapter
    private lateinit var programmeList: MutableList<Array<String>>

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", "",""))

        adapter = AverageSemenDoseAdapter(programmeList)
        recyclerView.adapter = adapter

    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }

}