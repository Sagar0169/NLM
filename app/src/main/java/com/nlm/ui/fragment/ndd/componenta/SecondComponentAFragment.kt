package com.nlm.ui.fragment.ndd.componenta

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentSecondComponentABinding
import com.nlm.databinding.FragmentSecondDpvBinding
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapterNDD
import com.nlm.utilities.BaseFragment
import java.util.Calendar

class SecondComponentAFragment : BaseFragment<FragmentSecondComponentABinding>() {
    private var mBinding: FragmentSecondComponentABinding? = null
    private var isSelected: Boolean? = false
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private lateinit var adapter: SupportingDocumentAdapterNDD
    private lateinit var recyclerView: RecyclerView
    private lateinit var programmeList: MutableList<Array<String>>

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val stateList = listOf(
        "Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia"
    )
    private val districtList = listOf(
        // Andhra Pradesh
        "Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna",
        "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam",
        "Vizianagaram", "West Godavari", "YSR Kadapa",
    )
    private val policeStationList = listOf(
        // Andhra Pradesh
        "Please Select",
//        "Chittoor", "East Godavari", "Guntur", "Krishna",
//        "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam",
//        "Vizianagaram", "West Godavari", "YSR Kadapa",
    )
    override val layoutId: Int
        get() = R.layout.fragment_second_component_a

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
//        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
//        mBinding!!.tvPoliceStation.setOnClickListener { showBottomSheetDialog("Police_Station") }
//        mBinding!!.tvDate.setOnClickListener {
//            showDatePicker(mBinding!!.tvDate)
//        }
        recyclerView = mBinding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        programmeList = mutableListOf()
        programmeList.add(arrayOf(""))
        adapter = SupportingDocumentAdapterNDD(programmeList)
        recyclerView.adapter = adapter

    }


    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun login(view: View) {


        }

        fun next(view: View) {
            listener?.onNextButtonClick()

        }

        fun backPress(view: View) {

        }

        fun showDatePicker(view: View) {

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

    private fun showDatePicker(textView: TextView) {
        // Get current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Date selected
                val selectedDate = "${String.format("%02d", selectedDayOfMonth)}/" +
                        "${String.format("%02d", selectedMonth + 1)}/$selectedYear"
                textView.text = selectedDate
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            },
            year, month, day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis


        // Show the dialog
        datePickerDialog.show()
    }

}