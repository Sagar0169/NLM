package com.nlm.ui.fragment

import android.content.Intent
import android.view.View
import android.widget.Toast

import com.nlm.ui.activity.DashboardActivity
import com.nlm.utilities.BaseFragment
import nlm.R
import nlm.databinding.FragmentConfirmationBinding

class ConfirmationFragments : BaseFragment<FragmentConfirmationBinding>(){
    private var mBinding: FragmentConfirmationBinding?=null


    override val layoutId: Int
        get() = R.layout.fragment_confirmation

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
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
                Toast.makeText(requireContext(),"Missing Child Form Filled Successfully", Toast.LENGTH_SHORT).show()



        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
    }

}