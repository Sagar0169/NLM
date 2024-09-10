package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentRSPAverageSemenDoseBinding
import com.nlm.utilities.BaseFragment


class RSPAverageSemenDoseFragment : BaseFragment<FragmentRSPAverageSemenDoseBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__average__semen__dose
    private var mBinding: FragmentRSPAverageSemenDoseBinding?=null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction=ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

    }
}