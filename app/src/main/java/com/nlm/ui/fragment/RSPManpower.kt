package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.databinding.FragmentRSPManpowerBinding
import com.nlm.utilities.BaseFragment


class RSPManpower : BaseFragment<FragmentRSPManpowerBinding> (){
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p_manpower
    private var mBinding: FragmentRSPManpowerBinding?=null
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