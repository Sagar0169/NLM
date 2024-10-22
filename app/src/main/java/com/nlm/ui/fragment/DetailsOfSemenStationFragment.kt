package com.nlm.ui.fragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentDetailsOfSemenStationBinding
import com.nlm.model.details_Semen_Station
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter

import com.nlm.utilities.BaseFragment


class DetailsOfSemenStationFragment : BaseFragment<FragmentDetailsOfSemenStationBinding>() {
    private var details_Semen_Station: details_Semen_Station? = null
    private var listener: OnNextButtonClickListener? = null
    // Call this method before displaying the fragment
    fun setData(data: details_Semen_Station) {
        details_Semen_Station = data
    }
    fun getData(): details_Semen_Station {
        return details_Semen_Station?.apply {
            State = mBinding!!.etState.text.toString()
            District = mBinding!!.etState.text.toString()
            Location = mBinding!!.etUsername.text.toString()
            Address = mBinding!!.etDescription.text.toString()
            Pin_code = mBinding!!.etPassword.text.toString()
            Phone_No = mBinding!!.etPhone.text.toString()
            Grading = mBinding!!.etyear.text.toString()
            Area_under = mBinding!!.etAreaUnder.text.toString()
            Area_fodder = mBinding!!.etAreaFodder.text.toString()
            ISO_9002 = mBinding!!.rbMentally.checkedRadioButtonId == R.id.rbMentallyYes
            Cmugrading = mBinding!!.rbCmugrading.checkedRadioButtonId == R.id.rbA
        } ?: details_Semen_Station()
    }
    override val layoutId: Int
        get() = R.layout.fragment_details_of__semen__station
    private var mBinding: FragmentDetailsOfSemenStationBinding?=null
    private lateinit var bottomSheetAdapter: StateAdapter
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
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun next(view: View) {
            listener?.onNextButtonClick()

        }
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
    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? DetailsOfSemenStationFragment.OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}