package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.SwitchFragmentCallBack
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAFormIAFragment(private val switchFragment:SwitchFragmentCallBack) : BaseFragment<FragmentNLSIAFormBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a_form
    private var viewModel = ViewModel()
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomSheetAdapter: StateAdapter

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

                  switchFragment.onClickItem(NLSIAInfrastructureSheepGoat(userResponseModel._result.id),1)
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
                    "part1",
                    getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                    mBinding?.etNameAndLocationOfIa?.text.toString(),
                    mBinding?.etDirectorDGCeoName?.text.toString(),
                    mBinding?.etTechnicalStaffRegularDepute?.text.toString().toIntOrNull(),
                    mBinding?.etTechnicalStaffManpowerDepute?.text.toString().toIntOrNull(),
                    mBinding?.etAdminStaffEmployeeDepute?.text.toString().toIntOrNull(),
                    mBinding?.etAdminStaffManpowerDepute?.text.toString().toIntOrNull(),
                    mBinding?.etOtherStaffEmployeeDepute?.text.toString().toIntOrNull(),
                    mBinding?.etOtherStaffManpowerDepute?.text.toString().toIntOrNull(),
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
                    1,
                    null,
                    null,
                    null,
                    null,
                    null,
                    getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                    null,
                    0,

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