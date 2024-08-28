package mission.vatsalya.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentBasicDetailsBinding
import mission.vatsalya.databinding.FragmentSightedBasicDetailsBinding
import mission.vatsalya.ui.activity.LoginActivity
import mission.vatsalya.ui.adapter.DistrictAdapter
import mission.vatsalya.ui.adapter.RelationshipAdapter
import mission.vatsalya.ui.adapter.StateAdapter
import mission.vatsalya.utilities.BaseFragment
import mission.vatsalya.utilities.hideView
import mission.vatsalya.utilities.showView
import java.util.Calendar


class SightedBasicDetailsFragment : BaseFragment<FragmentSightedBasicDetailsBinding>() {

    private var mBinding: FragmentSightedBasicDetailsBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter


    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val relationList = listOf(
        "Parent", "Legal Guardian", "Other",
    )

    override val layoutId: Int
        get() = R.layout.fragment_sighted_basic_details

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
//        callAdapter()


        mBinding!!.rbGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbDaYes -> {
                    // Show the TextView if "Yes" is selected
                    mBinding!!.tvDisability.showView()
                    mBinding!!.etDisability.showView()
                }

                R.id.rbDaNo -> {
                    // Hide the TextView if "No" is selected
                    mBinding!!.tvDisability.hideView()
                    mBinding!!.etDisability.hideView()
                }
            }
        }


        mBinding!!.tvDateOfSighting.setOnClickListener {
            showDatePicker(mBinding!!.tvDateOfSighting)
        }
        setupSpinner(mBinding!!.spinnerYear, R.array.year_array)
        setupSpinner(mBinding!!.spinnerMonth, R.array.month_array)
        setupSpinner(mBinding!!.spinnerFeet, R.array.feet_array)
        setupSpinner(mBinding!!.spinnerInches, R.array.inches_array)

        mBinding!!.rlRelationship.setOnClickListener {
            if (isSelected == false) {
                isSelected = true
                mBinding!!.ivArrowDown.isVisible = true
                mBinding?.ivArrowUp?.isVisible = false
                showBottomSheetDialog()
//                mBinding?.llRelation?.isVisible = true

            } else {
                isSelected = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
//                mBinding?.llRelation?.isVisible = false

            }

        }
        mBinding!!.ivArrowUp.setOnClickListener {
            isSelected = true
            mBinding?.ivArrowDown?.isVisible = true
            mBinding?.ivArrowUp?.isVisible = false
            showBottomSheetDialog()
//            mBinding?.llRelation?.isVisible = true
        }
        mBinding!!.ivArrowDown.setOnClickListener {
            isSelected = false
            mBinding?.ivArrowDown?.isVisible = false
            mBinding?.ivArrowUp?.isVisible = true
//            mBinding?.llRelation?.isVisible = false
        }
    }

    fun onClickItem(selectedItem: String) {
        mBinding!!.tvSelect.text = selectedItem
        mBinding!!.tvSelect.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        mBinding?.llRelation?.isVisible = false
        mBinding?.ivArrowDown?.isVisible = false
        mBinding?.ivArrowUp?.isVisible = true
    }


    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT // Set height to 80% of the screen
        )

        // Properly find the RecyclerView from the inflated view
        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener{
            bottomSheetDialog.dismiss()
        }

        // Set up the adapter based on the type

            stateAdapter = StateAdapter(relationList, requireContext()) { selectedItem ->
                // Handle state item click
                mBinding!!.tvSelect.text = selectedItem
                mBinding!!.tvSelect.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                mBinding?.llRelation?.isVisible = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
                bottomSheetDialog.dismiss()
            }
            rvBottomSheet.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvBottomSheet.adapter = stateAdapter
            bottomSheetDialog.setContentView(view)

            // Set a dismiss listener to reset the view visibility
            bottomSheetDialog.setOnDismissListener {
                isSelected = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
            }


        // Show the bottom sheet
        bottomSheetDialog.show()
    }


    private fun callAdapter() {
        relationAdapter = RelationshipAdapter(relationList, this)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvRelation.layoutManager = layoutManager
        mBinding!!.rvRelation.adapter = relationAdapter
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