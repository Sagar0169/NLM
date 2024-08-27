package mission.vatsalya.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import mission.vatsalya.utilities.ImagePickerHelper
import mission.vatsalya.utilities.PermissionHelper
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityEditProfileBinding
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.Constants.MEDIA_PAGE_FLAG_REGISTER_PROFILE
import mission.vatsalya.utilities.Constants.REQUEST_CAMERA_IMAGE_RESULT
import mission.vatsalya.utilities.Constants.REQUEST_CAMERA_PERMISSION
import mission.vatsalya.utilities.Constants.REQUEST_CAMERA_PERMISSION_MEDIA
import mission.vatsalya.utilities.Constants.REQUEST_CAMERA_VIDEO_RESULT
import mission.vatsalya.utilities.Constants.REQUEST_CODE_DOC
import mission.vatsalya.utilities.Constants.REQUEST_WRITE_STORAGE_PERMISSION_DOC
import mission.vatsalya.utilities.FileUtils
import mission.vatsalya.utilities.FileUtils.saveDocFile
import java.io.File

class EditProfile() : BaseActivity<ActivityEditProfileBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_edit_profile
    private var mBinding: ActivityEditProfileBinding? = null
    lateinit var imagePicker: ImagePickerHelper
    var observerSnackBarInt = ObservableInt()
    var imagePhoto: File? = null

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        imagePicker = ImagePickerHelper(this,observerSnackBarInt )
        observeEvents()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun save(view: View){

        }
        fun uploadDocument(view: View){

        }
        fun edit(view: View){
            mBinding?.tvEdit?.setOnClickListener {
                mBinding?.etName?.isEnabled=true
                mBinding?.etName?.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mBinding?.etName, InputMethodManager.SHOW_IMPLICIT)
                mBinding?.etEmail?.isEnabled=true
                mBinding?.etphoneNumber?.isEnabled=true
                mBinding?.etMobile?.isEnabled=true
                mBinding?.rbMale?.isEnabled=true
                mBinding?.rbFemale?.isEnabled=true

            }
        }
        fun dob(view: View){

        }
        fun profileCamera(view: View){
            imagePicker.showImageMediaDialog(MEDIA_PAGE_FLAG_REGISTER_PROFILE)
        }

        fun uploadProfile(view: View){

        }

        fun backPress(view: View){
            onBackPressedDispatcher.onBackPressed()
        }
    }
        fun observeEvents() {
            imagePicker.onMediaDataGet.observe(this@EditProfile) { pojoMediaData ->
                if (pojoMediaData != null) {
                    loadProfileImage(pojoMediaData.mediaCompressFile)
//                    mBinding.tvSize.text = pojoMediaData.mediaCompressFile.extension
                }
            }
        }
        private fun loadProfileImage(file: File) {
            imagePhoto = file
            val requestOptions = glideCenterCropOptions(R.drawable.place_hold_banner)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            mBinding?.ivUserImage?.let {
                Glide.with(this@EditProfile).load(file).apply(requestOptions).transform(CircleCrop())
                    .into(it)
            }
        }
        private fun glideBaseOptions(drawable: Int): RequestOptions {
            return RequestOptions().placeholder(drawable)
                .error(drawable).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }

        private fun glideCenterCropOptions(drawable: Int): RequestOptions {
            return glideBaseOptions(drawable).centerCrop()
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.data != null) {
                    val fileUri = data.data
                    val fileData =
                        FileUtils.getDocPath(fileUri!!, this, observerSnackBarInt)
                    if (!TextUtils.isEmpty(fileData[0])) {
                        val docFile = saveDocFile(
                            this, fileUri,
                            fileData[0], TextUtils.equals(fileData[1], "1")
                        )
                        imagePicker.setUpSelectedMediaData(
                            docFile,
                            TextUtils.equals(fileData[1], "1")
                        )
                        //mViewModel.attachMedia(docFile, TextUtils.equals(fileData[1], "1"));
                    }
                }
            }
        } else if (requestCode == REQUEST_CAMERA_IMAGE_RESULT) {
            if (resultCode == RESULT_OK) {
                imagePicker.setUpCameraMediaResult(true)
            }
        } else if (requestCode == REQUEST_CAMERA_VIDEO_RESULT) {
            if (resultCode == RESULT_OK) {
                imagePicker.setUpCameraMediaResult(false)
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            val flag = PermissionHelper.checkPermissionResult(
                permissions,
                grantResults,
                R.string.request_read_storage_permission,
                this
            )
            if (flag == 1) {
                // navigateToBackPermission();
            } /*else if (flag == 0) {
                //check Permission again
            }*/
        } else if (requestCode == REQUEST_CAMERA_PERMISSION_MEDIA) {
            val flag = PermissionHelper.checkPermissionResult(
                permissions,
                grantResults,
                R.string.request_read_storage_permission,
                this
            )
            if (flag == 1) {
                imagePicker.captureImageOrVideo()
            } else if (flag == 0) {
                //checkPermissionForDownloadPdf();
            }
        } else if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION_DOC) {
            val flag = PermissionHelper.checkPermissionResult(
                permissions,
                grantResults,
                R.string.request_read_storage_permission,
                this
            )
            if (flag == 1) {
                imagePicker.selectImageOrVideo()
            } else if (flag == 0) {
                //checkPermissionForDownloadPdf();
            }
        }
    }

}