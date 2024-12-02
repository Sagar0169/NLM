package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitBinding
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.model.GetDropDownRequest
import com.nlm.model.Result
import com.nlm.model.ResultGetDropDown
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.convertToRequestBody
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddVaccinationProgrammeStateLevel : BaseActivity<ActivityAddVaccinationProgrammeStateLevelBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_state_level
    private var mBinding: ActivityAddVaccinationProgrammeStateLevelBinding? = null
    private var viewModel = ViewModel()
    private var districtList = ArrayList<ResultGetDropDown>()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var districtId: Int? = null // Store selected state
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
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        if (getPreferenceOfScheme(
                this,
                AppConstants.SCHEME,
                Result::class.java
            )?.state_name?.isNotEmpty() == true
        )
        {
        mBinding?.tvState?.text = getPreferenceOfScheme(
            this,
            AppConstants.SCHEME,
            Result::class.java
        )?.state_name
        mBinding?.tvState?.isEnabled = false
        }
        mBinding?.tvChooseFile1a?.setOnClickListener {
            isFromApplication = 1
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.tvChooseFile1b?.setOnClickListener {
            isFromApplication = 2
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.tvChooseFile1c?.setOnClickListener {
            isFromApplication = 3
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.tvChooseFile1d?.setOnClickListener {
            isFromApplication = 4
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.tvChooseFile2?.setOnClickListener {
            isFromApplication = 5
            openOnlyPdfAccordingToPosition()
        }
        mBinding?.tvChooseFile3?.setOnClickListener {
            isFromApplication = 6
            openOnlyPdfAccordingToPosition()
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
        viewModel.getProfileUploadFileResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel != null) {
                if (userResponseModel.statuscode == 401) {
                    Utility.logout(this)
                } else if (userResponseModel._resultflag == 0) {
                    mBinding?.main?.let { it1 ->
                        showSnackbar(
                            it1,
                            userResponseModel.message
                        )
                    }

                } else {
                    DocumentId=userResponseModel._result.id
                    UploadedDocumentName=userResponseModel._result.document_name
                    DialogDocName?.text=userResponseModel._result.document_name
                    when (isFromApplication) {
                        1 -> {
                            mBinding?.etChooseFile1a?.text = UploadedDocumentName

                        }

                        2 -> {
                            mBinding?.etChooseFile1b?.text = UploadedDocumentName

                        }

                        3 -> {
                            mBinding?.etChooseFile1c?.text = UploadedDocumentName

                        }

                        4 -> {
                            mBinding?.etChooseFile1d?.text = UploadedDocumentName

                        }

                        5 -> {
                            mBinding?.etChooseFile2?.text = UploadedDocumentName

                        }

                        6 -> {
                            mBinding?.etChooseFile3?.text = UploadedDocumentName

                        }

                        else -> {
                            DialogDocName?.text = UploadedDocumentName

                        }
                    }
                    mBinding?.main?.let { it1 ->
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
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun state(view: View) {
            showBottomSheetDialog("State")
        }
        fun saveAndNext(view: View) {
              if(vaild())
              {
                  showSnackbar(mBinding!!.main, "Data Saved")
              }
              else{
                  showSnackbar(mBinding!!.main, "Please fill all the fields")
              }
        }
        fun saveAsDraft(view: View) {
            if(vaild())
            {
                showSnackbar(mBinding!!.main, "Data Saved")
            }
            else{
                showSnackbar(mBinding!!.main, "Please fill all the fields")
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

            "State" -> {
                dropDownApiCall(paginate = false, loader = true)
                selectedList = districtList
                selectedTextView = mBinding!!.tvState
            }


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
    private fun vaild(): Boolean {
        return !(mBinding?.etInput3?.text.toString().isEmpty()&&mBinding?.etInput2?.text.toString().isEmpty()&&mBinding?.etInput1a?.text.toString().isEmpty()&&mBinding?.etInput1b?.text.toString().isEmpty()&&mBinding?.etInput1c?.text.toString().isEmpty()&&mBinding?.etInput1d?.text.toString().isEmpty()&&mBinding?.etInput1e?.text.toString().isEmpty()&&mBinding?.etRemark3?.text.toString().isEmpty()&&mBinding?.etRemark2?.text.toString().isEmpty()&&mBinding?.etRemark1a?.text.toString().isEmpty()&& mBinding?.etRemark1b?.text.toString().isEmpty() && mBinding?.etRemark1c?.text.toString().isEmpty()&&mBinding?.etRemark1d?.text.toString().isEmpty()&&mBinding?.etRemark1e?.text.toString().isEmpty()&&mBinding?.etChooseFile1a?.text.toString().isEmpty()||mBinding?.etChooseFile1b?.text.toString().isEmpty()&&mBinding?.etChooseFile1c?.text.toString().isEmpty()&&mBinding?.etChooseFile1d?.text.toString().isEmpty()&&mBinding?.etChooseFile2?.text.toString().isEmpty()&&mBinding?.etChooseFile3?.text.toString().isEmpty())

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
                                        uploadDocument(DocumentName,uri)
                                    }

                                    2 -> {
                                        uploadDocument(DocumentName,uri)
                                    }

                                    3 -> {
                                        uploadDocument(DocumentName,uri)
                                    }

                                    4 -> {
                                        uploadDocument(DocumentName,uri)
                                    }

                                    5 -> {
                                        uploadDocument(DocumentName,uri)
                                    }

                                    6 -> {
                                        uploadDocument(DocumentName,uri)
                                    }

                                    else -> {
                                        uploadDocument(DocumentName,uri)
                                    }
                                }
                        }

                    }
                }
            }
        }
    }

}
    private fun uploadDocument(DocumentName:String?,uri:Uri){
        val requestBody = convertToRequestBody(this, uri)
        body = MultipartBody.Part.createFormData(
            "document_name",
            DocumentName,
            requestBody
        )
        viewModel.getProfileUploadFile(
            context = this,
            document_name = body,
            user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
            table_name = getString(R.string.state_vaccination_programme).toRequestBody(MultipartBody.FORM),
        )
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