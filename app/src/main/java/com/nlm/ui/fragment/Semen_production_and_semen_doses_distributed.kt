package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.databinding.FragmentSemenProductionAndSemenDosesDistributedBinding
import com.nlm.utilities.BaseFragment


class Semen_production_and_semen_doses_distributed : BaseFragment<FragmentSemenProductionAndSemenDosesDistributedBinding>() {
    private var mBinding: FragmentSemenProductionAndSemenDosesDistributedBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_semen_production_and_semen_doses_distributed

    override fun init() {
       mBinding=viewDataBinding

    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
}