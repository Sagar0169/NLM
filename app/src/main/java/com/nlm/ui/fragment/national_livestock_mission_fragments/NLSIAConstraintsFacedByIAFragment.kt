package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.utilities.BaseFragment

class NLSIAConstraintsFacedByIAFragment : BaseFragment<FragmentNLSIAConstraintsFacedByIABinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__constraints_faced_by__i_a

    private var mBinding: FragmentNLSIAConstraintsFacedByIABinding?=null

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