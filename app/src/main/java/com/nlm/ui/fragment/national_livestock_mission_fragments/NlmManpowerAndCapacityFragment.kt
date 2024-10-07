package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAConstraintsFacedByIABinding
import com.nlm.databinding.FragmentNlmManpowerAndCapacityBinding
import com.nlm.utilities.BaseFragment


class NlmManpowerAndCapacityFragment : BaseFragment<FragmentNlmManpowerAndCapacityBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_nlm_manpower_and_capacity

    override fun init() {
        mBinding=viewDataBinding

    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    private var mBinding: FragmentNlmManpowerAndCapacityBinding?=null
}