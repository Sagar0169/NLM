package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.databinding.FragmentNLSIAReportingSystemBinding
import com.nlm.utilities.BaseFragment

class NLSIA_Constraints_faced_by_IA : BaseFragment<FragmentNLSIAConstraintsFacedByIABinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__constraints_faced_by__i_a

    private var mBinding: FragmentNLSIAConstraintsFacedByIABinding?=null

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