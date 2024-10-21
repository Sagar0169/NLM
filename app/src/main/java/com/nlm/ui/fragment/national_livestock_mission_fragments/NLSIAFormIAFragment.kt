package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAFormIAFragment() : BaseFragment<FragmentNLSIAFormBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a_form
    private var viewModel = ViewModel()
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
        viewModel.init()
        mBinding?.tvState?.text= getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.tvState?.isEnabled=false

    }

    override fun setVariables() {

    }

    override fun setObservers() {
      viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
          val userResponseModel = it
          if (userResponseModel!=null)
          {
              if(userResponseModel._resultflag==0){
                  showSnackbar(mBinding!!.clParent, userResponseModel.message)
              }
              else{
                  showSnackbar(mBinding!!.clParent, userResponseModel.message)
              }
          }
      }
    }
    inner class ClickActions {



        fun group(view: View){
            showBottomSheetDialog("group")
        }
        fun role(view: View){showBottomSheetDialog("role")}
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun designation(view: View){showBottomSheetDialog("designation")}
        fun save(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                    mBinding?.etNameAndLocationOfIa?.text.toString(),
                    mBinding?.etDirectorDGCeoName?.text.toString(),
                    mBinding?.etTechnicalStaffRegularDepute?.text.toString().toInt(),
                    mBinding?.etTechnicalStaffManpowerDepute?.text.toString().toInt(),
                    mBinding?.etAdminStaffEmployeeDepute?.text.toString().toInt(),
                    mBinding?.etAdminStaffManpowerDepute?.text.toString().toInt(),
                    mBinding?.etOtherStaffEmployeeDepute?.text.toString().toInt(),
                    mBinding?.etOtherStaffManpowerDepute?.text.toString().toInt(),
                    mBinding?.etOrganisationalChart?.text.toString(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,

                )
            )
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