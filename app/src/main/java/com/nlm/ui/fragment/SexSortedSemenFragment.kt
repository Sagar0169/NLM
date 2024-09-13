package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentSexSortedSemenBinding
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.utilities.BaseFragment


class SexSortedSemenFragment : BaseFragment<FragmentSexSortedSemenBinding>() {
    private var mBinding: FragmentSexSortedSemenBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_sex__sorted__semen
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgrammeAdapter
    private lateinit var programmeList: MutableList<Array<String>>

    override fun init() {
        mBinding=viewDataBinding
        recyclerView = mBinding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = ProgrammeAdapter(programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

}