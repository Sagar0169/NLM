package com.nlm.ui.fragment

import android.view.View
import com.nlm.utilities.BaseFragment
import nlm.R
import nlm.databinding.FragmentTextSeachBinding


class TextSeachFragment() : BaseFragment<FragmentTextSeachBinding>() {



    override val layoutId: Int
        get() = R.layout.fragment_text_seach
    private var mBinding:FragmentTextSeachBinding?=null
            override fun init() {
                mBinding=viewDataBinding
                mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
    }

}