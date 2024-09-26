package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentNLSIACompositionOfAdvisoryBinding
import com.nlm.utilities.BaseFragment



class NLSIACompositionOfAdvisoryCommittee : BaseFragment<FragmentNLSIACompositionOfAdvisoryBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__composition_of_advisory

    private var mBinding: FragmentNLSIACompositionOfAdvisoryBinding?=null

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