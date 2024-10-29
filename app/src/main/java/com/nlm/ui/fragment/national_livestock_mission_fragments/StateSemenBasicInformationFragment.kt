package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.nlm.R
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentNLSIAFormBinding
import com.nlm.databinding.FragmentStateSemenBasicInformationBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.ui.activity.national_livestock_mission.NLMIAForm
import com.nlm.ui.activity.national_livestock_mission.StateSemenBankForms
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class StateSemenBasicInformationFragment :
    BaseFragment<FragmentStateSemenBasicInformationBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_state_semen__basic_information
    private var viewModel = ViewModel()
    private var mBinding: FragmentStateSemenBasicInformationBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var savedAsDraft: Boolean = false
    private lateinit var tabLayout: TabLayout
    protected lateinit var mActivityMain: StateSemenBankForms
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    private lateinit var stateAdapter: BottomSheetAdapter
    private var districtId: Int? = null // Store selected state
    private var currentPage = 1
    private var totalPage = 1
    private var districtList = ArrayList<ResultGetDropDown>()
    private var loading = true
    private var layoutManager: LinearLayoutManager? = null


    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        mBinding?.etState?.text = getPreferenceOfScheme(
            requireContext(),
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.etState?.isEnabled = false
        mActivityMain = activity as StateSemenBankForms

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }

    override fun setVariables() {

    }

    override fun setObservers() {

        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(requireContext())
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtList.clear()

                        val remainingCount = userResponseModel.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 10
                            count
                        } else {
                            val count = userResponseModel.total_count / 10
                            count + 1
                        }
                    }
                    districtList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()

//                    mBinding?.tvNoDataFound?.hideView()
//                    mBinding?.rvArtificialInsemination?.showView()
                } else {
//                    mBinding?.tvNoDataFound?.showView()
//                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }

        viewModel.stateSemenBankAddResult.observe(viewLifecycleOwner){
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
                    if (savedAsDraft)
                    {
                        savedAsDraftClick?.onSaveAsDraft()
                    }
                    else{
                        Preferences.setPreference_int(requireContext(),AppConstants.FORM_FILLED_ID,userResponseModel._result.id)
                        listener?.onNextButtonClick()
                        showSnackbar(mBinding!!.clParent, userResponseModel.message)
                    }

                }
            }
        }

    }

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("state")
        }

        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun saveAsDraft(view: View) {
            viewModel.getStateSemenAddBankApi(
                requireContext(), true,
                StateSemenBankNLMRequest(
                    Address = mBinding?.etAddress?.text.toString(),
                    area_fodder_cultivation = mBinding?.etAreaForFodder?.text.toString(),
                    area_under_buildings = mBinding?.etAreaUnderBuild?.text.toString(),
                    district_code = districtId,
                    phone_no = mBinding?.etPhone?.text.toString().toLong(),
                    pin_code = mBinding?.etPincode?.text.toString().toInt(),
                    quality_status = mBinding?.etQuality?.text.toString(),
                    role_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    year_of_establishment = mBinding?.etyear?.text.toString(),
                    is_draft = 1,
                )
            )
            savedAsDraft=true
        }
        fun save(view: View) {
            viewModel.getStateSemenAddBankApi(
                requireContext(), true,
                StateSemenBankNLMRequest(
                    Address = mBinding?.etAddress?.text.toString(),
                    area_fodder_cultivation = mBinding?.etAreaForFodder?.text.toString(),
                    area_under_buildings = mBinding?.etAreaUnderBuild?.text.toString(),
                    district_code = districtId,
                    phone_no = mBinding?.etPhone?.text.toString().toLong(),
                    pin_code = mBinding?.etPincode?.text.toString().toInt(),
                    quality_status = mBinding?.etQuality?.text.toString(),
                    role_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.role_id,
                    state_code = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,
                    user_id = getPreferenceOfScheme(
                        requireContext(),
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.user_id.toString(),
                    year_of_establishment = mBinding?.etyear?.text.toString()
                )
            )
        }
    }

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            requireContext(), loader, GetDropDownRequest(
                20,
                "Districts",
                currentPage,
                getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.user_id,
            )
        )
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
        val selectedList: List<ResultGetDropDown>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {
//            "State" -> {
//                dropDownApiCall(paginate = false, loader = true)
//                selectedList = stateList
//                selectedTextView = binding!!.tvState
//            }
//
//            "StateNDD" -> {
//                selectedList = stateList
//                selectedTextView = binding!!.tvStateNDD
//            }

            "District" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvDistrict
            }

//            "Status" -> {
//                selectedList = status
//                selectedTextView = binding!!.tvStatus
//            }
//
//            "Reading" -> {
//                selectedList = reading
//                selectedTextView = binding!!.tvReadingMaterial
//            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(requireContext(), selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }

        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
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

//    private fun swipeForRefreshImplementingAgency() {
//        mBinding?.srlImplementingAgency?.setOnRefreshListener {
//            implementingAgencyAPICall(paginate = false, loader = true)
//            mBinding?.srlImplementingAgency?.isRefreshing = false
//        }
//    }

    private var recyclerScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount: Int? = layoutManager?.childCount
                    val totalItemCount: Int? = layoutManager?.itemCount
                    val pastVisiblesItems: Int? = layoutManager?.findFirstVisibleItemPosition()
                    if (loading) {
                        if ((visibleItemCount!! + pastVisiblesItems!!) >= totalItemCount!!) {
                            loading = false
                            if (currentPage < totalPage) {
                                //Call API here
                                dropDownApiCall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }


}