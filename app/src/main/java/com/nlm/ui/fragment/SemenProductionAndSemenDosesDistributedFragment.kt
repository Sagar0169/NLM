package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentSemenProductionAndSemenDosesDistributedBinding
import com.nlm.utilities.BaseFragment


class SemenProductionAndSemenDosesDistributedFragment : BaseFragment<FragmentSemenProductionAndSemenDosesDistributedBinding>() {
    private var mBinding: FragmentSemenProductionAndSemenDosesDistributedBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_semen_production_and_semen_doses_distributed

    override fun init() {
       mBinding=viewDataBinding

    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
}