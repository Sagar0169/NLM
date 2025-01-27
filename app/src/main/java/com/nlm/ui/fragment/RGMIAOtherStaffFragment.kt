package com.nlm.ui.fragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentRGMIAOtherStaffBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseFragment


class RGMIAOtherStaffFragment : BaseFragment<FragmentRGMIAOtherStaffBinding>() {
    private var mBinding: FragmentRGMIAOtherStaffBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override val layoutId: Int
        get() = R.layout.fragment_r_g_m__i_a__other__staff
    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

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
        fun SaveAndNext(view: View) {
            listener?.onNextButtonClick()

        }
        fun SaveAsDraft(view: View) {
            savedAsDraftClick?.onSaveAsDraft()
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