package com.nlm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFeedFodderBinding
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.databinding.FragmentVitroPartOneBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.BaseFragment


class VitroPartOneFragment : BaseFragment<FragmentVitroPartOneBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_vitro_part_one
    private var mBinding: FragmentVitroPartOneBinding?=null
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
//        fun state(view: View){showBottomSheetDialog("state")}
//        fun district(view: View){showBottomSheetDialog("district")}
    }
//    private fun showBottomSheetDialog(type: String) {
//        bottomSheetDialog = BottomSheetDialog(requireContext())
//        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
//        view.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
//        val close = view.findViewById<TextView>(R.id.tvClose)
//
//        close.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        // Define a variable for the selected list and TextView
//        val selectedList: List<String>
//        val selectedTextView: TextView
//
//        // Initialize based on type
//        when (type) {
//
//
//            "state" -> {
//                selectedList = state
//                selectedTextView = mBinding!!.etState
//            }
//
//            "district" -> {
//                selectedList = district
//                selectedTextView = mBinding!!.etDistrict
//            }
//
//
//
//            else -> return
//        }
//        bottomSheetAdapter = BottomSheetAdapter(requireContext(),selectedList) { selectedItem ->
//            selectedTextView.text = selectedItem
//            bottomSheetDialog.dismiss()
//        }
//
//        rvBottomSheet.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        rvBottomSheet.adapter = bottomSheetAdapter
//        bottomSheetDialog.setContentView(view)
//
//        bottomSheetDialog.show()
//    }
}