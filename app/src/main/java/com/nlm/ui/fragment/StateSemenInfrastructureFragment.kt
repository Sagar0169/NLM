package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.utilities.BaseFragment


class StateSemenInfrastructureFragment : BaseFragment<FragmentStateSemenInfrastructureBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_state__semen__infrastructure
    private var mBinding: FragmentStateSemenInfrastructureBinding? = null
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