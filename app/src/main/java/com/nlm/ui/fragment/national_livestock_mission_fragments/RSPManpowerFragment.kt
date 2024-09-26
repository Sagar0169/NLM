package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.ui.adapter.RspManPowerAdapter
import com.nlm.utilities.BaseFragment


class RSPManpowerFragment : BaseFragment<FragmentRSPManpowerBinding> (){
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p_manpower
    private var mBinding: FragmentRSPManpowerBinding?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RspManPowerAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
//'[        recyclerView = mBinding?.recyclerView1!!
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        programmeList = mutableListOf()
//        programmeList.add(arrayOf("", "", "",""))
//
//        adapter = RspManPowerAdapter(programmeList)
//        recyclerView.adapter = adapter]'
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }
}