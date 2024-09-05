package com.nlm.ui.fragment

import android.view.View
import com.nlm.R
import com.nlm.databinding.FragmentSemenStationManpowerBinding
import com.nlm.databinding.FragmentStateSemenManpowerBinding
import com.nlm.utilities.BaseFragment


class SemenStaion_Manpower : BaseFragment<FragmentSemenStationManpowerBinding>() {
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