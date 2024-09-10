package com.nlm.ui.fragment

import com.nlm.R
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.utilities.BaseFragment



class NLSIAGoverningBodyBoardOfDirectorsFragment : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__governing_body__board__of__directors

    private var mBinding: FragmentNLSIAGoverningBodyBoardOfDirectorsBinding?=null

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