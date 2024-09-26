package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentAssetsNotBeingUsedBinding
import com.nlm.ui.adapter.rgm.AgencyWiseAdapter
import com.nlm.utilities.BaseFragment


class FragmentAssetsNotBeingUsed : BaseFragment<FragmentAssetsNotBeingUsedBinding>() {
    private var mBinding: FragmentAssetsNotBeingUsedBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment__assets_not_being_used
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AgencyWiseAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", ""))

        adapter = AgencyWiseAdapter(programmeList,1,false)
        recyclerView.adapter = adapter



        mBinding?.recyclerView2!!.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", ""))

        adapter = AgencyWiseAdapter(programmeList,1,true)
        mBinding?.recyclerView2!!.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
}