package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.utilities.BaseFragment


class NLSIAInfrastructureSheepGoat : BaseFragment<FragmentNLSIAInfrastructureSheepGoatBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__infrastructure__sheep_goat

    private var mBinding: FragmentNLSIAInfrastructureSheepGoatBinding?=null
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