package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentRSPBasicInformationBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.rgm.AvailabilityOfEquipmentAdapter
import com.nlm.utilities.BaseFragment


class RSPBasicInformationFragment : BaseFragment<FragmentRSPBasicInformationBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_r_s_p__basic_information
    private var mBinding: FragmentRSPBasicInformationBinding?=null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: AvailabilityOfEquipmentAdapter
    private lateinit var programmeList: MutableList<Array<String>>

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = AvailabilityOfEquipmentAdapter(programmeList)
        mBinding?.recyclerView2?.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
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


            "state" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }

            "district" -> {
                selectedList = district
                selectedTextView = mBinding!!.etDistrict
            }



            else -> return
        }
        bottomSheetAdapter = StateAdapter(selectedList,requireContext()) { selectedItem ->
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