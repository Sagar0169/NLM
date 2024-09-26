package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.BaseFragment


class NLSIAFormIAFragment() : BaseFragment<FragmentNLSIAFormBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a_form

    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var mBinding:FragmentNLSIAFormBinding?=null

    private val group = listOf(
        "Short", "Medium", "Long", "No Hair"
    )

    private val role = listOf(
        "Black", "Brown", "Gray"
    )

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    private val designation = listOf(
        "Folded", "Normal", "Other"
    )

    private val organisation = listOf(
        "Large", "Normal", "Small"
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



        fun group(view: View){
            showBottomSheetDialog("group")
        }
        fun role(view: View){showBottomSheetDialog("role")}
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun designation(view: View){showBottomSheetDialog("designation")}
        fun organisation(view: View){showBottomSheetDialog("organisation")}
    }
    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Define a variable for the selected list and TextView
        val selectedList: List<String>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {
//            "hairLength" -> {
//                selectedList = group
//                selectedTextView = mBinding!!.etGroup
//            }
//
//            "hairColor" -> {
//                selectedList = role
//                selectedTextView = mBinding!!.etRole
//            }
//
            "state" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }
//
//            "eyeColor" -> {
//                selectedList = district
//                selectedTextView = mBinding!!.etDistrict
//            }
//
//            "earsType" -> {
//                selectedList = designation
//                selectedTextView = mBinding!!.etDesignation
//            }
//
//            "earsSize" -> {
//                selectedList = organisation
//                selectedTextView = mBinding!!.etOrganisation
//            }

            else -> return
        }
        bottomSheetAdapter = BottomSheetAdapter(requireContext(),selectedList) { selectedItem ->
            selectedTextView.text = selectedItem
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = bottomSheetAdapter
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }

}