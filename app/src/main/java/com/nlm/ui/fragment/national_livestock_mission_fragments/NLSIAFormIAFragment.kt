package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAFormIAFragment : BaseFragment<FragmentNLSIAFormBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a_form
    private var viewModel = ViewModel()
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomSheetAdapter: StateAdapter
    protected lateinit var mActivityMain: NLMIAForm
    private var listener: OnNextButtonClickListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var mBinding:FragmentNLSIAFormBinding?=null


    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()

        tabLayout=requireActivity().findViewById(R.id.tabLayout)
        mBinding?.etState?.text= getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_name
        mBinding?.etState?.isEnabled=false
        mActivityMain = activity as NLMIAForm

    }

    override fun setVariables() {

    }

    override fun setObservers() {
      viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
          val userResponseModel = it
          if (userResponseModel.statuscode == 401) {
              Utility.logout(requireContext())
          }
          if (userResponseModel!=null)
          {
              if(userResponseModel._resultflag==0){
                  showSnackbar(mBinding!!.clParent, userResponseModel.message)
              }
              else{
                  listener?.onNextButtonClick()
                  showSnackbar(mBinding!!.clParent, userResponseModel.message)
              }
          }
      }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    inner class ClickActions {

        fun state(view: View){showBottomSheetDialog("state")}
        fun save(view: View){
            viewModel.getImplementingAgencyAddApi(requireContext(),true,
                ImplementingAgencyAddRequest(
                    part = "part1",
                    state_code = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                    name_location_of_ai = mBinding?.etNameAndLocationOfIa?.text.toString(),
                    director_dg_ceo_name = mBinding?.etDirectorDGCeoName?.text.toString(),
                    technical_staff_regular_employee = mBinding?.etTechnicalStaffRegularDepute?.text.toString().toIntOrNull(),
                    technical_staff_manpower_deputed = mBinding?.etTechnicalStaffManpowerDepute?.text.toString().toIntOrNull(),
                    admn_staff_regular_employee = mBinding?.etAdminStaffEmployeeDepute?.text.toString().toIntOrNull(),
                    admn_staff_manpower_deputed = mBinding?.etAdminStaffManpowerDepute?.text.toString().toIntOrNull(),
                    other_staff_regular_employee = mBinding?.etOtherStaffEmployeeDepute?.text.toString().toIntOrNull(),
                    other_staff_manpower_deputed = mBinding?.etOtherStaffManpowerDepute?.text.toString().toIntOrNull(),
                    organizational_chart = mBinding?.etOrganisationalChart?.text.toString(),
                    user_id = getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    is_deleted = 0,
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