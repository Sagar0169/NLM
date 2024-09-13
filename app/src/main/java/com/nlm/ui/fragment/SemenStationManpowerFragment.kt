package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentSemenStationManpowerBinding
import com.nlm.utilities.BaseFragment


class SemenStationManpowerFragment : BaseFragment<FragmentSemenStationManpowerBinding>() {
    private var mBinding: FragmentSemenStationManpowerBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_semen_station_manpower

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