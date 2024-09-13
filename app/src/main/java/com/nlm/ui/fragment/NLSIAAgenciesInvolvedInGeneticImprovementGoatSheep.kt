package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding
import com.nlm.utilities.BaseFragment


class NLSIAAgenciesInvolvedInGeneticImprovementGoatSheep:
    BaseFragment<FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__agencies_involved_in_genetic_improvement_goat_sheep
    private var mBinding: FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding?=null
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