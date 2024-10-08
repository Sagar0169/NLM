package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentImplementationOfNAIPRGMBinding
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.BaseFragment


class ImplementationOfNAIPRGMFragment : BaseFragment<FragmentImplementationOfNAIPRGMBinding>() {
    private var mBinding: FragmentImplementationOfNAIPRGMBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_implementation_of__n_a_i_p__r_g_m
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var programmeList: MutableList<Array<String>>
    override fun init() {
        mBinding=viewDataBinding
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
}