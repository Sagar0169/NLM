package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.NlmIADistrictWiseNoAdapter
import com.nlm.utilities.BaseFragment


class NLSIAAgenciesInvolvedInGeneticImprovementGoatSheep:
    BaseFragment<FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__agencies_involved_in_genetic_improvement_goat_sheep

    private var mBinding: FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NlmIADistrictWiseNoAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "",""))
        adapter = NlmIADistrictWiseNoAdapter  (programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }
}