package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.utilities.BaseFragment


class NLSIA_Feed_fodder : BaseFragment<FragmentNLSIAFeedFodderBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__feed_fodder

    private var mBinding: FragmentNLSIAFeedFodderBinding?=null

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