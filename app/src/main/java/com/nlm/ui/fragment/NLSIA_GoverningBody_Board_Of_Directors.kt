package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.utilities.BaseFragment



class NLSIA_GoverningBody_Board_Of_Directors : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(){
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