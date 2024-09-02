package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentRSPAverageSemenDoseBinding
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.databinding.FragmentRSPSuggestionsForImprovementBinding
import com.nlm.utilities.BaseFragment


class RSP_Suggestions_for_improvement : BaseFragment<FragmentRSPSuggestionsForImprovementBinding>() {
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