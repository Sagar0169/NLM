package com.nlm.ui.fragment

import android.content.Context
import android.view.View
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentRGMDetailsOfCommitteeMeetingsBinding
import com.nlm.utilities.BaseFragment


class RGMDetailsOfCommitteeMeetings : BaseFragment<FragmentRGMDetailsOfCommitteeMeetingsBinding>() {
    private var mBinding: FragmentRGMDetailsOfCommitteeMeetingsBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_r_g_m__details_of_committee_meetings
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }
    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
//        fun state(view: View){showBottomSheetDialog("state")}
        fun SaveAndNext(view: View) {
            listener?.onNextButtonClick()

        }
        fun SaveAsDraft(view: View) {
            savedAsDraftClick?.onSaveAsDraft()
        }
    }
}