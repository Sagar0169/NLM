package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.utilities.BaseFragment



class NLSIAGoverningBodyBoardOfDirectorsFragment : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__governing_body__board__of__directors

    private var mBinding: FragmentNLSIAGoverningBodyBoardOfDirectorsBinding?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NlmIACompositionOFGoverningAdapter

    private lateinit var programmeList: MutableList<Array<String>>
    private lateinit var adapter2: NlmIACompositionOFGoverningAdapter
    private lateinit var programmeList2: MutableList<Array<String>>

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView2!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "",""))

        adapter = NlmIACompositionOFGoverningAdapter(programmeList)
        recyclerView.adapter = adapter

        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())

        programmeList2 = mutableListOf()
        programmeList2.add(arrayOf("", "",""))

        adapter2 = NlmIACompositionOFGoverningAdapter(programmeList2)
        mBinding?.recyclerView1?.adapter = adapter2
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {




    }
}