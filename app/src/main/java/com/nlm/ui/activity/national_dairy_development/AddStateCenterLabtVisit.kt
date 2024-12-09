package com.nlm.ui.activity.national_dairy_development

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemUploadDocEdit
import com.nlm.databinding.ActivityAddDcsCenterVisitBinding
import com.nlm.databinding.ActivityAddStateCenterLabVisitBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentAdapter
import com.nlm.ui.adapter.RSPSupportingDocumentIAAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.SupportingDocumentAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.toast
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody

class AddStateCenterLabtVisit : BaseActivity<ActivityAddStateCenterLabVisitBinding>(), CallBackDeleteAtId,
    CallBackItemUploadDocEdit {
    private var mBinding: ActivityAddStateCenterLabVisitBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: SupportingDocumentAdapter
    private lateinit var stateAdapter: BottomSheetAdapter
    var body: MultipartBody.Part? = null
    private var viewModel = ViewModel()
    private var viewEdit: String? = null
    var itemId: Int? = null
    private var dId: Int? = null
    private var districtId: Int? = null // Store selected state
    private var loading = true
    private var DocumentId: Int? = null
    private lateinit var DocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var viewDocumentList: ArrayList<ImplementingAgencyDocument>
    private lateinit var totalListDocument: ArrayList<ImplementingAgencyDocument>
    private var districtList = ArrayList<ResultGetDropDown>()
    private var addDocumentAdapter: RSPSupportingDocumentAdapter? = null
    private var addDocumentIAAdapter: RSPSupportingDocumentIAAdapter? = null
    private var UploadedDocumentName: String? = null
    private var DialogDocName: TextView? = null
    private var DocumentName: String? = null
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var isSubmitted: Boolean = false
    private var savedAsEdit: Boolean = false
    private var savedAsDraft: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_add_state_center_lab_visit

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        viewEdit = intent.getStringExtra("View/Edit")
        itemId = intent.getIntExtra("itemId", 0)
        dId = intent.getIntExtra("dId", 0)
        DocumentList = arrayListOf()
        totalListDocument = arrayListOf()
        viewDocumentList = arrayListOf()
        mBinding?.etState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.etState?.isEnabled = false
        mBinding?.etState?.setTextColor(Color.parseColor("#000000"))
        nlmAdapter()

    }
    private fun nlmAdapter() {
        addDocumentAdapter = RSPSupportingDocumentAdapter(
            this,
            DocumentList,
            viewEdit,
            this,
            this
        )
        mBinding?.recyclerView2?.adapter = addDocumentAdapter
        mBinding?.recyclerView2?.layoutManager = LinearLayoutManager(this)
    }


    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.getDropDownResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this@AddStateCenterLabtVisit)
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
                    mBinding?.clParent?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
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

        fun addDocDialog(view: View) {
            addDocumentDialog(this@AddStateCenterLabtVisit, null, null)
        }

        fun backPress(view: View) {
            onBackPressed()
        }
        fun save(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            isSubmitted = true
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 0)
            } else {
                saveDataApi(null, 0)
            }


        }


        fun saveAsDraft(view: View) {
            totalListDocument.clear()

            totalListDocument.addAll(DocumentList)
            totalListDocument.addAll(viewDocumentList)
            if (viewEdit == "view") {
//                listener?.onNextButtonClick()
            }

            if (viewEdit == "edit") {
                savedAsEdit = true
            }
            if (itemId != 0) {
                saveDataApi(itemId, 1)
            } else {
                saveDataApi(null, 1)
            }
            savedAsDraft = true
        }
    }
    private fun saveDataApi(itemId: Int?, draft: Int?) {}
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
                selectedTextView = mBinding!!.etDistrict
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
            if (id != -1) {
                districtId = id
            }
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
    private fun addDocumentDialog(
        context: Context,
        selectedItem: ImplementingAgencyDocument?,
        position: Int?
    ) {
        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_add_document_dialog,
            null,
            false
        )
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
        lp.dimAmount = 0.5f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        DialogDocName = bindingDialog.etDoc

        if (selectedItem != null) {
            if (getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id == 8
            ) {
                UploadedDocumentName = selectedItem.nlm_document
                bindingDialog.etDoc.text = selectedItem.nlm_document
                bindingDialog.etDescription.setText(selectedItem.description)
            } else {
                UploadedDocumentName = selectedItem.ia_document
                bindingDialog.etDoc.text = selectedItem.ia_document
                bindingDialog.etDescription.setText(selectedItem.description)
            }

        }
        bindingDialog.tvChooseFile.setOnClickListener {
            openOnlyPdfAccordingToPosition()
        }
        bindingDialog.btnDelete.setOnClickListener {
            dialog.dismiss()
        }


        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etDescription.text.toString()
                    .isNotEmpty() && bindingDialog.etDoc.text.toString().isNotEmpty()
            ) {
                if (selectedItem != null) {
                    if (position != null) {
                        if (getPreferenceOfScheme(
                                this,
                                AppConstants.SCHEME,
                                Result::class.java
                            )?.role_id == 8
                        ) {
                            DocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = null,
                                    nlm_document = UploadedDocumentName,
                                    ahidf_id = selectedItem.ahidf_id,
                                    id = selectedItem.id,
                                )
                            addDocumentAdapter?.notifyItemChanged(position)

                        } else {
                            viewDocumentList[position] =
                                ImplementingAgencyDocument(
                                    description = bindingDialog.etDescription.text.toString(),
                                    ia_document = UploadedDocumentName,
                                    nlm_document = null,
                                    ahidf_id = selectedItem.ahidf_id,
                                    id = selectedItem.id,
                                )
                            addDocumentIAAdapter?.notifyItemChanged(position)
                        }

                        dialog.dismiss()
                    }

                } else {
                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                nlm_document = UploadedDocumentName,
                                id = null,
                                ahidf_id = null,
                                ia_document = null
                            )
                        )
                    } else {
                        viewDocumentList.add(
                            ImplementingAgencyDocument(
                                bindingDialog.etDescription.text.toString(),
                                ia_document = UploadedDocumentName,
                                id = null,
                                ahidf_id = null,
                                nlm_document = null
                            )
                        )
                        Log.d("Debug", "viewDocumentList: ${viewDocumentList.size}")

                    }

                    if (getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id == 8
                    ) {
                        DocumentList.size.minus(1).let {
                            addDocumentAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
//
                        }
                    } else {

                        viewDocumentList.size.minus(1).let {
                            addDocumentIAAdapter?.notifyItemInserted(it)
                            dialog.dismiss()
                        }
                        toast("else")
                    }
                }
            } else {
                showSnackbar(
                    mBinding!!.clParent,
                    getString(R.string.please_enter_atleast_one_field)
                )
            }
        }
        dialog.show()
    }

    private fun openOnlyPdfAccordingToPosition() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, REQUEST_iMAGE_PDF)
    }

    @SuppressLint("Range")
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
                                DialogDocName?.text = DocumentName


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
//                                table_name = getString(R.string.ahidf_document).toRequestBody(
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

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position: Int) {
        TODO("Not yet implemented")
    }
}