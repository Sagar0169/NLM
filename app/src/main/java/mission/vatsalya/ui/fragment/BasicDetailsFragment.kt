package mission.vatsalya.ui.fragment

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
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.FragmentBasicDetailsBinding
import mission.vatsalya.ui.activity.OtpActivity
import mission.vatsalya.ui.activity.RegistrationActivity
import mission.vatsalya.ui.fragment.SightedBasicDetailsFragment.OnNextButtonClickListener
import mission.vatsalya.utilities.BaseFragment

class BasicDetailsFragment : BaseFragment<FragmentBasicDetailsBinding>(){
    private var mBinding: FragmentBasicDetailsBinding?=null
    private var listener: OnNextButtonClickListener? = null



    override val layoutId: Int
        get() = R.layout.fragment_basic_details

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()

        setupSpinner(mBinding!!.spinnerYear, R.array.year_array)
        setupSpinner(mBinding!!.spinnerMonth, R.array.month_array)
        setupSpinner(mBinding!!.spinnerFeet, R.array.feet_array)
        setupSpinner(mBinding!!.spinnerInches, R.array.inches_array)
        setupSpinner(mBinding!!.spinnerRelation, R.array.inches_relation)
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
    interface OnNextButtonClickListener {
        fun onNextButtonClick()
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

    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
        fun next(view: View) {
            listener?.onNextButtonClick()

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