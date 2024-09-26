package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentRSPSuggestionsForImprovementBinding
import com.nlm.utilities.BaseFragment


class RSPSuggestionsForImprovement : BaseFragment<FragmentRSPSuggestionsForImprovementBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__suggestions_for_improvement

    private var mBinding: FragmentRSPSuggestionsForImprovementBinding?=null
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