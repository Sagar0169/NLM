package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentSemenStationManpowerBinding
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.BaseFragment


class SemenStationManpowerFragment : BaseFragment<FragmentSemenStationManpowerBinding>() {
    private var mBinding: FragmentSemenStationManpowerBinding? = null
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var programmeList: MutableList<Array<String>>
    override val layoutId: Int
        get() = R.layout.fragment_semen_station_manpower

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf(""))
        adapter = SupportingDocumentAdapter(programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
       }
}