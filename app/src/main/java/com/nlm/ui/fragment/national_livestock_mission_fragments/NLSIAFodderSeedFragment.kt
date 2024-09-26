package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFodderSeedBinding
import com.nlm.utilities.BaseFragment


class NLSIAFodderSeedFragment : BaseFragment<FragmentNLSIAFodderSeedBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a_fodder_seed

    private var mBinding: FragmentNLSIAFodderSeedBinding?=null

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {

    }
}