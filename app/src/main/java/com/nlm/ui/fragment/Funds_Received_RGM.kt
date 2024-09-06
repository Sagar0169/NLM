package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.databinding.FragmentFundsReceivedRGMBinding
import com.nlm.ui.adapter.AgencyWiseAdapter
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.utilities.BaseFragment

class Funds_Received_RGM : BaseFragment<FragmentFundsReceivedRGMBinding>() {
    private var mBinding: FragmentFundsReceivedRGMBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_funds__received__r_g_m
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AgencyWiseAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", "",""))

        adapter = AgencyWiseAdapter(programmeList)
        recyclerView.adapter = adapter
        recyclerView = mBinding?.recyclerView2!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", "",""))

        adapter = AgencyWiseAdapter(programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }


}