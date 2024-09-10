package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentRSPAvailabilityOfEquipmentBinding
import com.nlm.utilities.BaseFragment


class RSPAvailabilityOfEquipmentFragment : BaseFragment<FragmentRSPAvailabilityOfEquipmentBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__availability_of_equipment
    private var mBinding: FragmentRSPAvailabilityOfEquipmentBinding?=null
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