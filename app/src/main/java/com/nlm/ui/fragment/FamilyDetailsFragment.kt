package com.nlm.ui.fragment

import android.content.Context
import android.view.View

import com.nlm.utilities.BaseFragment
import nlm.R
import nlm.databinding.FragmentFamilyDetailsBinding

class FamilyDetailsFragment : BaseFragment<FragmentFamilyDetailsBinding>(){
    private var mBinding: FragmentFamilyDetailsBinding?=null
    private var listener: OnNextButtonClickListener? = null


    override val layoutId: Int
        get() = R.layout.fragment_family_details

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
        fun next(view: View) {
            listener?.onNextButtonClick()

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}