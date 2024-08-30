package com.nlm.ui.fragment

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.ui.activity.DashboardActivity
import com.nlm.ui.adapter.RelationshipAdapter
import com.nlm.utilities.BaseFragment
import nlm.R
import nlm.databinding.FragmentSightedConfirmationBinding


class SightedConfirmationFragment : BaseFragment<FragmentSightedConfirmationBinding>() {
    private var mBinding: FragmentSightedConfirmationBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val relationList = listOf(
        "Parent", "Legal Guardian", "Other",
    )

    override val layoutId: Int
        get() = R.layout.fragment_sighted_confirmation

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun submit(view: View) {
            val intent = Intent(requireContext(),DashboardActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(),"Form Filled Successfully",Toast.LENGTH_SHORT).show()
        }

        fun next(view: View) {
            listener?.onNextButtonClick()

        }

        fun backPress(view: View) {

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