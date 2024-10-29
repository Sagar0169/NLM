package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentStateSemenInfrastructureBinding
import com.nlm.databinding.ItemStateSemenInfragoatBinding
import com.nlm.databinding.ItemStateSemenManpowerBinding
import com.nlm.model.StateSemenInfraGoat
import com.nlm.model.StateSemenManPower
import com.nlm.ui.adapter.RspManPowerAdapter
import com.nlm.ui.adapter.StateSemenInfrastructureAdapter
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView


class StateSemenInfrastructureFragment : BaseFragment<FragmentStateSemenInfrastructureBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_state__semen__infrastructure
    private var mBinding: FragmentStateSemenInfrastructureBinding? = null
    private lateinit var stateSemenInfraGoatList: MutableList<StateSemenInfraGoat>
    private var stateSemenInfraGoatAdapter: StateSemenInfrastructureAdapter? = null
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraft: Boolean = false
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override fun init() {

        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        stateSemenInfraGoatAdapter()
    }

    private fun stateSemenInfraGoatAdapter() {
        stateSemenInfraGoatList = mutableListOf()
        stateSemenInfraGoatAdapter =
            StateSemenInfrastructureAdapter(stateSemenInfraGoatList)
        mBinding?.recyclerView1?.adapter = stateSemenInfraGoatAdapter
        mBinding?.recyclerView1?.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    private fun compositionOfGoverningNlmIaDialog(context: Context, isFrom: Int) {
        val bindingDialog: ItemStateSemenInfragoatBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_state_semen_infragoat,
            null,
            false
        )
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.showView()

        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etListOfEquipment.text.toString().isNotEmpty() || bindingDialog.etYearOfProcurement.text.toString().isNotEmpty()) {
                stateSemenInfraGoatList.add(
                    StateSemenInfraGoat(
                        bindingDialog.etListOfEquipment.text.toString(),
                        bindingDialog.etYearOfProcurement.text.toString(),
                        id = null,
                    )
                )
                stateSemenInfraGoatList.size.minus(1).let {
                    stateSemenInfraGoatAdapter?.notifyItemInserted(it)
                }
                dialog.dismiss()
            } else {
                showSnackbar(
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }
        dialog.show()
    }


    inner class ClickActions {
        fun saveAsDraft(view: View) {}
        fun save(view: View) {
            listener?.onNextButtonClick()
        }

        fun otherManpowerPositionDialog(view: View) {
            compositionOfGoverningNlmIaDialog(requireContext(), 1)
        }
    }


}