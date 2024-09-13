package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentRGMCompositionOfGoverningBinding
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.ui.adapter.ProgrammeAdapter_2_view
import com.nlm.utilities.BaseFragment

class RGMCompositionOfGoverningFragment : BaseFragment<FragmentRGMCompositionOfGoverningBinding>(){
private var mBinding: FragmentRGMCompositionOfGoverningBinding? = null
override val layoutId: Int
    get() = R.layout.fragment_r_g_m__composition__of_governing
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgrammeAdapter
    private lateinit var adapter2: ProgrammeAdapter_2_view
    private lateinit var programmeList: MutableList<Array<String>>
    private lateinit var programmeList2: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        recyclerView = mBinding?.recyclerView2!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = ProgrammeAdapter(programmeList)
        recyclerView.adapter = adapter

        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())

        programmeList2 = mutableListOf()
        programmeList2.add(arrayOf("", ""))

        adapter2 = ProgrammeAdapter_2_view(programmeList2)
        mBinding?.recyclerView1?.adapter = adapter2
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

}