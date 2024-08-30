package com.nlm.utilities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import com.nlm.ui.adapter.ArrayAdapterWithIcon
import com.nlm.utilities.Constants.REQUEST_CAMERA_IMAGE_RESULT
import com.nlm.utilities.Constants.REQUEST_CAMERA_PERMISSION_MEDIA
import com.nlm.utilities.Constants.REQUEST_CAMERA_VIDEO_RESULT
import com.nlm.utilities.Constants.REQUEST_CODE_DOC
import com.nlm.utilities.Constants.REQUEST_WRITE_STORAGE_PERMISSION_DOC
import com.nlm.utilities.Constants.VALID_IMAGE_FILE_SIZE_KB
import com.nlm.utilities.Constants.VIDEO_MIN_SIZE_DURATION
import com.nlm.utilities.FileUtils.checkFileExists
import com.nlm.utilities.FileUtils.checkForValidImage
import com.nlm.utilities.FileUtils.checkForValidVideo
import com.nlm.utilities.FileUtils.compressFullImage
import com.nlm.utilities.FileUtils.deleteFile
import com.nlm.utilities.FileUtils.getCameraFile
import com.nlm.utilities.FileUtils.getTempCameraFileUri
import com.nlm.utilities.FileUtils.getVideoDuration
import com.nlm.utilities.FileUtils.validFileSize
import com.nlm.utilities.FileUtils.validFileType
import com.nlm.utilities.PermissionHelper.isPermissionWithGranted
import nlm.R
import java.io.File

/**
 * Created by Kishan Kadiuya on 25-01-2023.
 */
open class ImagePicker(
    private val mActivity: AppCompatActivity,
    private val observerSnackBarInt: ObservableInt
) : ListenerMediaAdd {
    private val mContext: Context
    var tempFileLocation: String? = null
    private var mMediaItem = 0
    private var mMediaIsImage = false
    private var listenerMediaData: ListenerMediaData? = null
    private fun getString(res: Int): String {
        return mActivity.getString(res)
    }

    fun selectMediaDocDialog() {
        val builder = AlertDialog.Builder(mActivity, R.style.WhiteAlertDialogTheme)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.text_select))
        val items = mActivity.resources.getStringArray(R.array.select_media_doc_text)
        val icons = arrayOf(R.drawable.ic_vector_photo_gallery, R.drawable.ic_vector_document)
        val adapter: ListAdapter = ArrayAdapterWithIcon(mActivity, items, icons)
        builder.setSingleChoiceItems(adapter, 0) { dialog: DialogInterface, which: Int ->
            selectMediaDocType(which)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.text_cancel)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.show()
    }

    /**
     * Display a dialog with options to attach photo.
     */
    protected fun showImageMediaDialog() {
        mMediaItem = 0
        val builder = AlertDialog.Builder(mActivity,R.style.WhiteAlertDialogTheme)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.text_select))
        val items = mActivity.resources.getStringArray(R.array.select_photo_media_text)
        val icons = arrayOf(R.drawable.ic_vector_photo, R.drawable.ic_vector_photo_gallery)
        val adapter: ListAdapter = ArrayAdapterWithIcon(mActivity, items, icons)
        builder.setSingleChoiceItems(adapter, 0) { dialog: DialogInterface, which: Int ->
            mMediaItem = which
            setUpMediaSelection(true)
            //validateMediaSelection(true);
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.text_cancel)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.show()
    }

    /**
     * Display a dialog with options to attach video.
     */
    protected fun showVideoMediaDialog() {
        val builder = AlertDialog.Builder(mActivity)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.text_select))
        val items = mActivity.resources.getStringArray(R.array.select_video_media_text)
        val icons = arrayOf(R.drawable.ic_vector_video, R.drawable.ic_vector_video_gallery)
        val adapter: ListAdapter = ArrayAdapterWithIcon(mActivity, items, icons)
        builder.setSingleChoiceItems(adapter, 0) { dialog: DialogInterface, which: Int ->
            mMediaItem = if (which == 0) {
                2
            } else {
                3
            }
            setUpMediaSelection(true)
            //validateMediaSelection(false);
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.text_cancel)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.show()
    }

    protected fun selectMediaDialog() {
        mMediaItem = 0
        val builder = AlertDialog.Builder(mActivity)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.text_select))
        val items = mActivity.resources.getStringArray(R.array.select_media_text)
        val icons = arrayOf(
            R.drawable.ic_vector_photo,
            R.drawable.ic_vector_photo_gallery,
            R.drawable.ic_vector_video,
            R.drawable.ic_vector_video_gallery
        )
        val adapter: ListAdapter = ArrayAdapterWithIcon(mActivity, items, icons)
        builder.setSingleChoiceItems(adapter, 0) { dialog: DialogInterface, which: Int ->
            mMediaItem = which
            setUpMediaSelection(true)
            //validateMediaSelection((mMediaItem == 0 || mMediaItem == 1));
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.text_cancel)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.show()
    }

    private fun validateMediaSelection(isImage: Boolean) {
        /*if (needToAdd(isImage)) {
            setUpMediaSelection(true);
        }*/
    }

    fun selectMediaDocType(which: Int) {
        val isImage = which == 0
        if (isImage) {
            showImageMediaDialog()
        } else {

            setUpDocSelection()
        }
    }

    protected fun selectMediaType(which: Int) {
        mMediaItem = which
        setUpMediaSelection(true)
    }

    private fun setUpDocSelection() {
        selectDoc()
//        if (isPermissionWithGranted(
//                arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE),
//                REQUEST_WRITE_STORAGE_PERMISSION_DOC,
//                mActivity
//            )
//        ) {
//
//            selectDoc()
//        }
    }

    private fun setUpMediaSelection(isWithPermission: Boolean) {
        if (isWithPermission) {
            if (mMediaItem == 1) {
                mMediaIsImage = true
                checkPermissionForAttachDoc()
            } else if (mMediaItem == 2) {
                mMediaIsImage = false
                captureMedia()
            } else if (mMediaItem == 3) {
                mMediaIsImage = false
                checkPermissionForAttachDoc()
            } else {
                mMediaIsImage = true
                captureMedia()
            }
        } else {
            if (mMediaItem == 1) {
                selectImageOrVideo()
            } else if (mMediaItem == 2) {
                captureImageOrVideo()
            } else if (mMediaItem == 3) {
                selectImageOrVideo()
            } else {
                captureImageOrVideo()
            }
        }
    }

    private fun checkPermissionForAttachDoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            /*if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestWriteExternalStoragePermission(Manifest.permission.READ_MEDIA_IMAGES, REQUEST_WRITE_STORAGE_PERMISSION_DOC, getResources().getString(R.string.request_read_storage_permission));
                return ;
            }*/
            selectImageOrVideo()
        } else {
            if (isPermissionWithGranted(
                    arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE_PERMISSION_DOC,
                    mActivity
                )
            ) {
                selectImageOrVideo()
            }
        }
    }

    fun selectDoc() {

        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        mActivity.startActivityForResult(
            Intent.createChooser(intent, "Select PDF"),
            REQUEST_WRITE_STORAGE_PERMISSION_DOC
        )
    }

    fun selectImageOrVideo() {
        val mimeTypes: Array<String>
        mimeTypes = arrayOf("image/*")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = mimeTypes[0]
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        mActivity.startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_CODE_DOC
        )


//        String[] mimeTypes;
//        if (mMediaIsImage) {
//            mimeTypes = new String[]{"image/*"};
//        } else {
//            mimeTypes = new String[]{"video/*"};
//        }
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//        if (mimeTypes.length > 0) {
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        }
//        mActivity.startActivityForResult(Intent.createChooser(intent, "Select Image OR Video"), REQUEST_CODE_DOC);
    }

    fun captureMedia() {
        tempFileLocation = ""
        var permission = arrayOfNulls<String>(0)
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } else {
                arrayOf(Manifest.permission.CAMERA)
                //                permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
        }
        if (isPermissionWithGranted(permission, REQUEST_CAMERA_PERMISSION_MEDIA, mActivity)) {
            captureImageOrVideo()
        }
    }

    fun captureImageOrVideo() {
        //if (mApplication.isInternetConnected()) {
        if (mMediaIsImage) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(mContext.packageManager) != null) {
                val tempImageFile = getCameraFile(mContext, true)
                if (tempImageFile != null) {
                    tempFileLocation = tempImageFile.absolutePath
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(tempImageFile.absoluteFile)
                        )
                    } else {
                        val photoUri = getTempCameraFileUri(mContext, tempImageFile)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    mActivity.startActivityForResult(intent, REQUEST_CAMERA_IMAGE_RESULT)
                } else {
                    observerSnackBarInt.set(R.string.message_camera_unabletocapture)
                }
            } else {
                observerSnackBarInt.set(R.string.message_camera_unabletoopen)
            }
        } else {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)
            if (intent.resolveActivity(mContext.packageManager) != null) {
                val tempVideoFile = getCameraFile(mContext, false)
                if (tempVideoFile != null) {
                    tempFileLocation = tempVideoFile.absolutePath
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(tempVideoFile.absoluteFile)
                        )
                    } else {
                        val photoUri = getTempCameraFileUri(mContext, tempVideoFile)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    mActivity.startActivityForResult(intent, REQUEST_CAMERA_VIDEO_RESULT)
                } else {
                    observerSnackBarInt.set(R.string.message_camera_unabletocapture)
                }
            } else {
                observerSnackBarInt.set(R.string.message_camera_unabletoopen)
            }
        }

        /*} else {
            observerSnackBarInt.set(R.string.message_noconnection);
        }*/
    }

    fun setUpCameraMediaResult(isImage: Boolean) {
        //if (mApplication.isInternetConnected()) {
        if (!TextUtils.isEmpty(tempFileLocation)) {
            var file: File? = null
            file = if (isImage) {
                checkForValidImage(mContext, tempFileLocation!!)
            } else {
                checkForValidVideo(mContext, tempFileLocation!!)
            }
            if (file != null) {
                setUpMediaData(file, isImage)
            } else {
                observerSnackBarInt.set(if (isImage) R.string.message_camera_prepareimage else R.string.message_camera_preparevideo)
            }
        }
        /*} else {
            mViewModel.observerSnackBarInt.set(R.string.message_no_internet);
        }*/
    }

    private fun setUpMediaData(mediaFile: File, isImage: Boolean) {
        if (validFileType(mediaFile.absolutePath, arrayOf(".mp4", ".jpg", ".jpeg", ".png"))) {
            setUpSelectedMediaData(mediaFile, isImage)
        } else {
            observerSnackBarInt.set(R.string.error_valid_medai_format)
        }
    }

    fun setUpSelectedMediaData(mediaFile: File?, isImage: Boolean) {
        if (isImage) {
            val compressedFile = compressFullImage(mContext, mediaFile!!, VALID_IMAGE_FILE_SIZE_KB)
            deleteFile(mediaFile)
            if (checkFileExists(compressedFile)) {
                if (validFileSize(compressedFile.length(), VALID_IMAGE_FILE_SIZE_KB)) {
                    if (listenerMediaData != null) {
                        listenerMediaData!!.onMediaCapture(true, compressedFile, null)
                    }
                } else {
                    observerSnackBarInt.set(R.string.error_valid_image_size)
                }
            } else {
                observerSnackBarInt.set(R.string.error_valid_medai_format)
            }
        } else {
            val duration = getVideoDuration(mContext, Uri.fromFile(mediaFile))
            if (duration < VIDEO_MIN_SIZE_DURATION) {
                observerSnackBarInt.set(R.string.message_video_duration)
            } /* else if (duration > VIDEO_MAX_SIZE_DURATION) {
                mViewModel.observerSnackBarInt.set(R.string.message_video_duration);
            }*/ else {
                /* new VideoCompressor(mActivity, listenerVideoCompressor)
                        .execute(mediaFile.getAbsolutePath());*/
            }
        }
    }

    fun setUpDocData(mediaFile: File?) {
        if (checkFileExists(mediaFile)) {
            //if (validFileSize(mediaFile.length(), VALID_VIDEO_FILE_SIZE_KB)) {
            if (listenerMediaData != null) {
                listenerMediaData!!.onDocCapture(mediaFile)
            }
            /*  } else {
                observerSnackBarInt.set(R.string.error_valid_pdf_size);
            }*/
        } else {
            observerSnackBarInt.set(R.string.error_valid_pdf_format)
        }
    }

  /*  private val listenerVideoCompressor: ListenerVideoCompressor =
        object : ListenerVideoCompressor {
            override fun compressedFile(compressedFile: File?) {
                if (checkFileExists(compressedFile)) {
                    if (validFileSize(compressedFile!!.length(), VALID_VIDEO_FILE_SIZE_KB)) {
                        val thumbImage =
                            getVideoThumbnailImage(mContext, compressedFile.absolutePath)
                        if (listenerMediaData != null) {
                            listenerMediaData!!.onMediaCapture(false, compressedFile, thumbImage)
                        }
                    } else {
                        observerSnackBarInt.set(R.string.error_valid_video_size)
                    }
                } else {
                    observerSnackBarInt.set(R.string.error_valid_medai_format)
                }
            }
        }*/

    init {
        mContext = mActivity.applicationContext
    }

    protected fun setListenerMediaData(listenerMediaData: ListenerMediaData?) {
        this.listenerMediaData = listenerMediaData
    }

    override fun needToAdd(isImage: Boolean): Boolean {
        return true
    }

    interface ListenerMediaData {
        fun onMediaCapture(isImage: Boolean, mediaCompressFile: File?, videoThumbFile: File?)
        fun onDocCapture(docFile: File?)
    }
}