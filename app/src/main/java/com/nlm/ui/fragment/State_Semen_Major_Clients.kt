package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.databinding.FragmentStateSemenMajorClientsBinding
import com.nlm.utilities.BaseFragment


class State_Semen_Major_Clients :BaseFragment<FragmentStateSemenMajorClientsBinding>() {
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