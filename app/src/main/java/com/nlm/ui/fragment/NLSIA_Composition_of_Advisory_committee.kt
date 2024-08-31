package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIACompositionOfAdvisoryBinding
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.databinding.FragmentNLSIAInfrastructureSheepGoatBinding
import com.nlm.utilities.BaseFragment



class NLSIA_Composition_of_Advisory_committee : BaseFragment<FragmentNLSIACompositionOfAdvisoryBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__composition_of_advisory

    private var mBinding: FragmentNLSIACompositionOfAdvisoryBinding?=null

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