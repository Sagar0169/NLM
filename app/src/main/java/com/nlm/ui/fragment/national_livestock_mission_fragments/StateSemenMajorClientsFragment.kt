package com.nlm.ui.fragment.national_livestock_mission_fragments

import com.nlm.R
import com.nlm.databinding.FragmentStateSemenMajorClientsBinding
import com.nlm.utilities.BaseFragment


class StateSemenMajorClientsFragment :BaseFragment<FragmentStateSemenMajorClientsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_state_semen_major_clients
    private var mBinding: FragmentStateSemenMajorClientsBinding? = null
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