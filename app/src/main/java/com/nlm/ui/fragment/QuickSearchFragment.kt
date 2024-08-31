package com.nlm.ui.fragment

import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.R
import com.nlm.databinding.FragmentQuickSearchBinding
import java.util.Calendar


class QuickSearchFragment() : BaseFragment<FragmentQuickSearchBinding>(){
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
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
    private val policeStation = listOf(
        // Andhra Pradesh
        "Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna",
        "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam",
        "Vizianagaram", "West Godavari", "YSR Kadapa",
    )
    override val layoutId: Int
        get() = R.layout.fragment_quick_search

    private var mBinding:FragmentQuickSearchBinding?=null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        setupSpinner(mBinding!!.spinnerAge, R.array.year_array)
        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
        mBinding!!.tvPoliceStationDrop.setOnClickListener { showBottomSheetDialog("Police") }
        mBinding!!.rlDateOfFir.setOnClickListener {
            showDatePicker(mBinding!!.tvDateOfFir)
        }
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
            "State" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvState
            }

            "District" -> {
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrict
            }
            "Police" -> {
                selectedList = policeStation
                selectedTextView = mBinding!!.tvPoliceStationDrop
            }
            else -> return
        }

        // Set up the adapter
        stateAdapter = StateAdapter(selectedList, requireContext()) { selectedItem ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
        bottomSheetDialog.setContentView(view)

        // Rotate drawable
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                rotatedDrawable,
                null
            )
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
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

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            R.layout.spinner_selected_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position != 0) { // If an item other than the default is selected
                    (view as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun FirDetail(view: View) {
            if (mBinding?.llFir?.isVisible == true)
            {
                mBinding?.llFir?.hideView()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add)
                mBinding!!.TvFIR.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
            else{
                mBinding?.llFir?.showView()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_minus)
                mBinding!!.TvFIR.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
    }
}