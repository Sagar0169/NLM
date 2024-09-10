package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentStateSemenManpowerBinding
import com.nlm.utilities.BaseFragment


class StateSemenManpowerFragment : BaseFragment<FragmentStateSemenManpowerBinding> (){
    override val layoutId: Int
        get() = R.layout.fragment_state_semen_manpower
    private var mBinding: FragmentStateSemenManpowerBinding?=null
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