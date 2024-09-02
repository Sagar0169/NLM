package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAPmcBinding
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.utilities.BaseFragment


class NLSIA_Reporting_System : BaseFragment<FragmentNLSIAReportingSystemBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__reporting__system

    private var mBinding: FragmentNLSIAReportingSystemBinding?=null

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