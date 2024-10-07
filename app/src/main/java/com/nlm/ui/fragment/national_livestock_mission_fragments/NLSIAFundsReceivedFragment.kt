package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFundsReceivedBinding
import com.nlm.ui.adapter.NlmIADistrictWiseNoAdapter
import com.nlm.ui.adapter.NlmIAFundsRecievedAdapter
import com.nlm.utilities.BaseFragment


class NLSIAFundsReceivedFragment : BaseFragment<FragmentNLSIAFundsReceivedBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__funds__received
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NlmIAFundsRecievedAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    private var mBinding: FragmentNLSIAFundsReceivedBinding?=null

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "","","",""))
        adapter = NlmIAFundsRecievedAdapter  (programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {




    }
}