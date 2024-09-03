package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.databinding.FragmentStateSemenManpowerBinding
import com.nlm.utilities.BaseFragment


class StateSemenManpower : BaseFragment<FragmentStateSemenManpowerBinding> (){
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