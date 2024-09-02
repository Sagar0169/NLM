package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAAgenciesInvolvedInGeneticImprovementGoatSheepBinding
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.utilities.BaseFragment


class NLSIA_Agencies_involved_in_genetic_improvement_goat_sheep:
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