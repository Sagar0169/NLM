package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitBinding
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitDistrictBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.AddNewMobileVeterinaryUnitAdapter
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody

class AddNewMobileVeterinaryUnitDistrict : BaseActivity<ActivityAddNewMobileVeterinaryUnitDistrictBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitDistrictBinding? = null
    private lateinit var addNewMobileUnit: AddNewMobileVeterinaryUnitAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null
    private var viewModel = ViewModel()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var districtId: Int? = null // Store selected state
    var isFromApplication = 0
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null


    override val layoutId: Int
        get() = R.layout.activity_add_new_mobile_veterinary_unit_district


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun state(view: View) {
            showBottomSheetDialog("State")
        }
        fun district(view: View) {
            showBottomSheetDialog("District")
        }

        fun save(view: View) {
            if (mBinding?.etInputOne?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(it, "Please Fill All The Input and Remark Fields")

                }
                return
            }
            if (mBinding?.etInputTwo?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputThree?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputFour?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputFive?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }


            if (mBinding?.etRemarkOne?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkTwo?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkThree?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkFour?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
        }
        fun saveAsDraft(view: View) {
            if (mBinding?.etInputOne?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(it, "Please Fill All The Input and Remark Fields")

                }
                return
            }
            if (mBinding?.etInputTwo?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputThree?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputFour?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etInputFive?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }


            if (mBinding?.etRemarkOne?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkTwo?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkThree?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkFour?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
            if (mBinding?.etRemarkFive?.text.toString().isEmpty()) {
                mBinding?.clParent?.let {
                    showSnackbar(
                        it,
                        "Please Fill All The Input and Remark Fields"
                    )
                }
                return
            }
        }


    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false

        mBinding?.etChooseOne?.setOnClickListener {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseTwo?.setOnClickListener {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseThree?.setOnClickListener {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseFour?.setOnClickListener {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.etChooseFive?.setOnClickListener {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }
    }


    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_iMAGE_PDF -> {
                    data?.data?.let { uri ->
                        val projection = arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                        )
                        val cursor = this.contentResolver.query(
                            uri,
                            projection,
                            null,
                            null,
                            null
                        )
                        cursor?.use {
                            if (it.moveToFirst()) {
                                DocumentName =
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                                when (isFromApplication) {
                                    1 -> {
                                        mBinding?.tvNoFileOne?.text = DocumentName
                                    }

                                    2 -> {
                                        mBinding?.tvNoFileTwo?.text = DocumentName
                                    }

                                    3 -> {
                                        mBinding?.tvNoFileThree?.text = DocumentName
                                    }

                                    4 -> {
                                        mBinding?.tvNoFileFour?.text = DocumentName
                                    }

                                    5 -> {
                                        mBinding?.tvNoFileFive?.text = DocumentName
                                    }

                                    else -> {
                                        DialogDocName?.text = DocumentName
                                    }

                                }


                                val requestBody = convertToRequestBody(this, uri)
                                body = MultipartBody.Part.createFormData(
                                    "document_name",
                                    DocumentName,
                                    requestBody
                                )
//                                use this code to add new view with image name and uri
                            }
//                            viewModel.getProfileUploadFile(
//                                context = this,
//                                table_name = getString(R.string.fsp_plant_storage_document).toRequestBody(
//                                    MultipartBody.FORM
//                                ),
//                                document_name = body,
//                                user_id = getPreferenceOfScheme(
//                                    this,
//                                    AppConstants.SCHEME,
//                                    Result::class.java
//                                )?.user_id,
//                            )
                        }
                    }
                }
            }
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
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
    }
    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(this)
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
//            "typeSemen" -> {
//                selectedList = typeSemen
//                selectedTextView = mBinding!!.tvSemenStation
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
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView.text = selectedItem
            districtId = id
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }



        layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.layoutManager = layoutManager
        rvBottomSheet.adapter = stateAdapter
        rvBottomSheet.addOnScrollListener(recyclerScrollListener)
        bottomSheetDialog.setContentView(view)


        // Rotate drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
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

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                20,
                "Districts",
                currentPage,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
    }
}