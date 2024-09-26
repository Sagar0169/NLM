package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAPmcBinding
import com.nlm.utilities.BaseFragment



class NLSIAPMC : BaseFragment<FragmentNLSIAPmcBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__pmc

    private var mBinding: FragmentNLSIAPmcBinding?=null

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