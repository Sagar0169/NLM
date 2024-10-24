package com.nlm.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentVitroPartTwoBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.BaseFragment


class VitroPartTwoFragment : BaseFragment<FragmentVitroPartTwoBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_vitro_part_two
    private var mBinding: FragmentVitroPartTwoBinding? = null
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var programmeList: MutableList<Array<String>>

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf(""))
        adapter = SupportingDocumentAdapter(programmeList)
        recyclerView.adapter = adapter
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