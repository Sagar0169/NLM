package com.nlm.ui.activity.livestock_health_disease.ascad

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddAscadStateBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.model.StateAscadAddRequest
import com.nlm.model.StateMobileVeterinaryUnitAddRequest
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddAscadStateActivity : BaseActivity<ActivityAddAscadStateBinding>() {
    private var mBinding: ActivityAddAscadStateBinding? = null
    private var viewModel = ViewModel()
    private var stateList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var stateId: Int? = null // Store selected state
    private var DocumentId: Int? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var chooseDocName: String? = null
    var body: MultipartBody.Part? = null
    var isFromApplication = 0
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: BottomSheetAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false


    override val layoutId: Int
        get() = R.layout.activity_add_ascad_state

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
    }

    override fun setVariables() {
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name?.isNotEmpty() == true
        ) {
            mBinding?.tvState?.text = getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name
            mBinding?.tvState?.isEnabled = false
        } else {
            mBinding?.tvState?.isEnabled = true
        }
        if (viewEdit == "view") {
            mBinding?.tvState?.isEnabled = false
            mBinding?.etInputOne?.isEnabled = false
            mBinding?.etRemarkOne?.isEnabled = false
            mBinding?.etInputTwo?.isEnabled = false
            mBinding?.etRemarkTwo?.isEnabled = false
            mBinding?.etInputThree?.isEnabled = false
            mBinding?.etRemarkThree?.isEnabled = false
            mBinding?.etInputFour?.isEnabled = false
            mBinding?.etRemarkFour?.isEnabled = false
            mBinding?.etInputFive?.isEnabled = false
            mBinding?.etRemarkFive?.isEnabled = false
            mBinding?.llUploadFileOne?.isEnabled = false
            mBinding?.llUploadFileTwo?.isEnabled = false
            mBinding?.llUploadFileThree?.isEnabled = false
            mBinding?.llUploadFileFour?.isEnabled = false
            mBinding?.llUploadFileFive?.isEnabled = false
            mBinding?.llSaveDraftAndSubmit?.hideView()
            viewEditApi()
        }
        if (viewEdit == "edit") {
            viewEditApi()
        }
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateList.clear()

                        val remainingCount = userResponseModel.total_count % 100
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 100
                            count
                        } else {
                            val count = userResponseModel.total_count / 100
                            count + 1
                        }
                    }
                    stateList.addAll(userResponseModel._result)
                    stateAdapter.notifyDataSetChanged()
                }
            }
        }
        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }

                } else {
                    DocumentId = userResponseModel._result.id
                    UploadedDocumentName = userResponseModel._result.document_name
                    DialogDocName?.text = userResponseModel._result.document_name
                    when (isFromApplication) {
                        1 -> {
                            mBinding?.etChooseFileOne?.text = UploadedDocumentName

                        }

                        2 -> {
                            mBinding?.etChooseFileTwo?.text = UploadedDocumentName

                        }

                        3 -> {
                            mBinding?.etChooseFileThree?.text = UploadedDocumentName
                        }

                        4 -> {
                            mBinding?.etChooseFileFour?.text = UploadedDocumentName
                        }

                        5 -> {
                            mBinding?.etChooseFileFive?.text = UploadedDocumentName
                        }

                        else -> {
                            DialogDocName?.text = UploadedDocumentName
                        }
                    }
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }
                }
            }
        }
        viewModel.stateAscadAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    if (savedAsDraft) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        if (viewEdit == "view" || viewEdit == "edit") {
                            if (savedAsEdit) {
                                onBackPressedDispatcher.onBackPressed()
                                return@observe
                            }
                            toast(viewEdit.toString())
                            mBinding?.tvState?.text = userResponseModel._result.state_name
                            mBinding?.etInputOne?.setText(userResponseModel._result.input_annual_action_plan)
                            mBinding?.etRemarkOne?.setText(userResponseModel._result.annual_action_plan_remarks)
                            mBinding?.etInputTwo?.setText(userResponseModel._result.input_state_prioritizes_critical_disease)
                            mBinding?.etRemarkTwo?.setText(userResponseModel._result.state_prioritizes_critical_disease_remarks)
                            mBinding?.etInputThree?.setText(userResponseModel._result.input_scheduling_of_vaccination)
                            mBinding?.etRemarkThree?.setText(userResponseModel._result.scheduling_of_vaccination_remarks)
                            mBinding?.etInputFour?.setText(userResponseModel._result.input_financial_planning_for_state_share)
                            mBinding?.etRemarkFour?.setText(userResponseModel._result.financial_planning_for_state_share_remarks)
                            mBinding?.etInputFive?.setText(userResponseModel._result.input_purchase_of_vaccines_accessories)
                            mBinding?.etRemarkFive?.setText(userResponseModel._result.purchase_of_vaccines_accessories_remarks)
                            mBinding?.etChooseFileOne?.text = userResponseModel._result.annual_action_plan_input
                            mBinding?.etChooseFileTwo?.text = userResponseModel._result.state_prioritizes_critical_disease_input
                            mBinding?.etChooseFileThree?.text = userResponseModel._result.scheduling_of_vaccination_input
                            mBinding?.etChooseFileFour?.text = userResponseModel._result.financial_planning_for_state_share_input
                            mBinding?.etChooseFileFive?.text = userResponseModel._result.purchase_of_vaccines_accessories_input


                        } else {
                            onBackPressedDispatcher.onBackPressed()
                            mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                        }
                    }
                }
            }
        }
    }

    inner class ClickActions {
        fun state(view: View) {
            showBottomSheetDialog("State")
        }
        fun uploadFileOne(view: View){
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }
        fun uploadFileTwo(view: View){
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }
        fun uploadFileThree(view: View){
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }
        fun uploadFileFour(view: View){
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }
        fun uploadFileFive(view: View){
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun submit(view: View) {
            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 2)
            } else {
                saveDataApi(null, 2)
            }
        }
        fun saveAsDraft(view: View) {
            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 3)
            } else {
                saveDataApi(null, 3)
            }
        }
    }

    private fun viewEditApi() {

        viewModel.getStateAscadAdd(
            this, true,
            StateAscadAddRequest(
                id = itemId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                is_type = viewEdit
            )
        )
    }

    private fun saveDataApi(itemId: Int?, draft: Int?) {
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

        viewModel.getStateAscadAdd(
            context = this,
            loader = true,
            request = StateAscadAddRequest(
                id = itemId,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                status = draft,
                input_annual_action_plan = mBinding?.etInputOne?.text.toString().trim(),
                annual_action_plan_remarks = mBinding?.etRemarkOne?.text.toString().trim(),
                input_state_prioritizes_critical_disease =mBinding?.etInputTwo?.text.toString().trim(),
                state_prioritizes_critical_disease_remarks =mBinding?.etRemarkTwo?.text.toString().trim(),
                input_scheduling_of_vaccination =mBinding?.etInputThree?.text.toString().trim(),
                scheduling_of_vaccination_remarks =mBinding?.etRemarkThree?.text.toString().trim(),
                input_financial_planning_for_state_share = mBinding?.etInputFour?.text.toString().trim(),
                financial_planning_for_state_share_remarks = mBinding?.etRemarkFour?.text.toString().trim(),
                input_purchase_of_vaccines_accessories = mBinding?.etInputFive?.text.toString().trim(),
                purchase_of_vaccines_accessories_remarks = mBinding?.etInputFive?.text.toString().trim(),
                annual_action_plan_input = mBinding?.etChooseFileOne?.text.toString().trim(),
                state_prioritizes_critical_disease_input = mBinding?.etChooseFileTwo?.text.toString().trim(),
                scheduling_of_vaccination_input = mBinding?.etChooseFileThree?.text.toString().trim(),
                financial_planning_for_state_share_input = mBinding?.etChooseFileFour?.text.toString().trim(),
                purchase_of_vaccines_accessories_input = mBinding?.etChooseFileFive?.text.toString().trim()
            )
        )
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
        val selectedTextView: TextView?

        // Initialize based on type
        when (type) {

            "State" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = stateList
                selectedTextView = mBinding?.tvState
            }
            else -> return
        }

        // Set up the adapter
        stateAdapter = BottomSheetAdapter(this, selectedList) { selectedItem, id ->
            // Handle state item click
            selectedTextView?.text = selectedItem
            stateId = id
            selectedTextView?.setTextColor(ContextCompat.getColor(this, R.color.black))
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
        selectedTextView?.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView?.setCompoundDrawablesWithIntrinsicBounds(
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

    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }

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
                                        uploadDocument(DocumentName, uri)
                                    }

                                    2 -> {
                                        uploadDocument(DocumentName, uri)
                                    }

                                    3 -> {
                                        uploadDocument(DocumentName, uri)
                                    }

                                    4 -> {
                                        uploadDocument(DocumentName, uri)
                                    }

                                    5 -> {
                                        uploadDocument(DocumentName, uri)
                                    }

                                    6 -> {
                                        uploadDocument(DocumentName, uri)
                                    }

                                    else -> {
                                        uploadDocument(DocumentName, uri)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun uploadDocument(documentName: String?, uri: Uri) {
        val requestBody = convertToRequestBody(this, uri)
        body = MultipartBody.Part.createFormData(
            "document_name",
            documentName,
            requestBody
        )
        viewModel.getProfileUploadFile(
            context = this,
            document_name = body,
            user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            table_name = getString(R.string.ascad_state).toRequestBody(MultipartBody.FORM),
        )
    }

    private fun dropDownApiCall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDropDownApi(
            this, loader, GetDropDownRequest(
                100,
                "States",
                currentPage,
                null,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
            )
        )
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
}
