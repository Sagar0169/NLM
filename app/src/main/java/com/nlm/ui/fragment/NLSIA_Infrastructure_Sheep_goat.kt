package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.utilities.BaseFragment


class NLSIA_Infrastructure_Sheep_goat : BaseFragment<FragmentNLSIAInfrastructureSheepGoatBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__infrastructure__sheep_goat

    private var mBinding: FragmentNLSIAInfrastructureSheepGoatBinding?=null
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