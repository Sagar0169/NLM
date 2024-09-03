package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentRSPAverageSemenDoseBinding
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.utilities.BaseFragment


class RSP_Average_Semen_Dose : BaseFragment<FragmentRSPAverageSemenDoseBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__average__semen__dose
    private var mBinding: FragmentRSPAverageSemenDoseBinding?=null
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