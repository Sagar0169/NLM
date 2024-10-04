package com.nlm.ui.fragment.national_livestock_mission_fragments

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.FragmentRSPAverageSemenDoseBinding
import com.nlm.ui.adapter.rgm.AverageSemenDoseAdapter
import com.nlm.utilities.BaseFragment


class RSPAverageSemenDoseFragment : BaseFragment<FragmentRSPAverageSemenDoseBinding>() {
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