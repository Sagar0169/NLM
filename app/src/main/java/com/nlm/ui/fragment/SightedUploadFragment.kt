package com.nlm.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.model.FileItem
import com.nlm.ui.adapter.ChooseFileAdapter
import com.nlm.ui.adapter.RelationshipAdapter
import com.nlm.utilities.BaseFragment
import com.nlm.R
import com.nlm.databinding.FragmentSightedUploadBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SightedUploadFragment : BaseFragment<FragmentSightedUploadBinding>(),
    ChooseFileAdapter.OnFileSelectListener {
    private var mBinding: FragmentSightedUploadBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null
    private var openCameraValue: Int? = 0
    private lateinit var adapter: ChooseFileAdapter
    private lateinit var adapter2: ChooseFileAdapter

    private val items = mutableListOf<FileItem>()
    private val items2 = mutableListOf<FileItem>()


    private var currentPhotoPosition: Int = -1
    private var currentVideoPosition: Int = -1
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val REQUEST_VIDEO_CAPTURE = 3
    private val REQUEST_VIDEO_GALLERY = 4


    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val relationList = listOf(
        "Parent", "Legal Guardian", "Other",
    )

    override val layoutId: Int
        get() = R.layout.fragment_sighted_upload

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        adapter = ChooseFileAdapter(requireContext(), items, "Photos", this)
        adapter2 = ChooseFileAdapter(requireContext(), items2, "Video", this)

        mBinding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding!!.recyclerView.adapter = adapter

        mBinding!!.recyclerViewVideo.layoutManager = LinearLayoutManager(requireContext())
        mBinding!!.recyclerViewVideo.adapter = adapter2

//        items2.add("No File Chosen")
        mBinding!!.tvChooseFile.setOnClickListener {
            if (items.size < 5) {
                upload()

            } else {
                Toast.makeText(requireContext(), "Maximum 5 photos allowed", Toast.LENGTH_SHORT)
                    .show()

            }
        }

//        mBinding!!.tvChooseFile1.setOnClickListener {
//            upload()
//        }

//        mBinding!!.tvAddPhoto.setOnClickListener {
//            adapter.addItem()
//        }
        mBinding!!.tvAddPhoto.setOnClickListener {

            if (items.size < 5) {
//                items.add("No File Chosen")
                adapter.notifyItemInserted(items.size - 1)
                mBinding!!.tvRemove.visibility = if (items.size > 1) View.VISIBLE else View.GONE
                mBinding!!.tvAddPhoto.visibility = if (items.size > 4) View.GONE else View.VISIBLE


            } else {
                Toast.makeText(requireContext(), "Maximum 5 photos allowed", Toast.LENGTH_SHORT)
                    .show()
//                mBinding!!.tvAddPhoto.visibility=View.GONE

            }
        }

        mBinding!!.tvRemove.setOnClickListener {
            if (items.size > 1) {
                items.removeAt(items.size - 1)
                adapter.notifyItemRemoved(items.size)
                mBinding!!.tvRemove.visibility = if (items.size > 1) View.VISIBLE else View.GONE
                mBinding!!.tvAddPhoto.visibility = if (items.size < 5) View.VISIBLE else View.GONE
            }
        }
        mBinding!!.tvAddPhoto2.setOnClickListener {

            if (items2.size < 5) {
//                items2.add("No File Chosen")
                adapter2.notifyItemInserted(items2.size - 1)
                mBinding!!.tvRemove2.visibility = if (items2.size > 1) View.VISIBLE else View.GONE
                mBinding!!.tvAddPhoto.visibility = if (items.size > 4) View.GONE else View.VISIBLE


            } else {
                Toast.makeText(requireContext(), "Maximum 5 Videos allowed", Toast.LENGTH_SHORT)
                    .show()
//                mBinding!!.tvAddPhoto.visibility=View.GONE

            }
        }

        mBinding!!.tvRemove2.setOnClickListener {
            if (items2.size > 1) {
                items2.removeAt(items2.size - 1)
                adapter2.notifyItemRemoved(items2.size)
                mBinding!!.tvRemove2.visibility = if (items2.size > 1) View.VISIBLE else View.GONE
                mBinding!!.tvAddPhoto.visibility = if (items.size < 5) View.VISIBLE else View.GONE
            }
        }

    }

    fun upload() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(requireContext(), R.style.WhiteAlertDialogTheme)
            .setTitle("Select Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }.setNegativeButton("Cancel"){dialog:DialogInterface,which:Int -> dialog.dismiss()}
            .show()


    }

    override fun onChooseFileClicked(position: Int, category: String) {
        currentPhotoPosition = position
        currentVideoPosition = position
        if (category == "Photos") {
            // Show a dialog to choose between camera and gallery
            // Example: Using a simple AlertDialog
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Source")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> openCamera()
                        1 -> openGallery()
                    }
                }
                .show()
        } else if (category == "Video") {
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Source")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> recordVideo()
                        1 -> selectVideoFromGallery()
                    }
                }
                .show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY)
    }

    private fun recordVideo() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
        }
    }

    private fun selectVideoFromGallery() {
        val pickVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickVideo, REQUEST_VIDEO_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    Log.d("FIle uri",imageBitmap.toString())
                    imageBitmap?.let { bitmap ->
                        // Save the bitmap to a file and get the URI
                        val fileUri = getImageUri(requireContext(),bitmap)
                        val fileName = "captured_image.jpg" // Example name, change if needed
                        Log.d("uri",imageBitmap.toString())

                        updateItem(fileName, fileUri)
                    }
                }
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImage: Uri? = data?.data
                    val filePath = selectedImage?.let { getPathFromUri(it) }
                    updateItem(filePath ?: "No File Chosen", selectedImage)
                }

                REQUEST_VIDEO_CAPTURE -> {
                    val videoUri: Uri? = data?.data
                    val filePath = videoUri?.let { getPathFromUri(it) }
                    updateItem2(filePath ?: "No File Chosen", videoUri)
                }

                REQUEST_VIDEO_GALLERY -> {
                    val selectedVideo: Uri? = data?.data
                    val filePath = selectedVideo?.let { getPathFromUri(it) }
                    updateItem2(filePath ?: "No File Chosen", selectedVideo)
                }
            }
        }
    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap): Uri {

        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    private fun getPathFromUri(uri: Uri): String? {
        // Your logic to get the file path from the URI
        return uri.path
    }

    private fun updateItem(fileName: String, fileUri: Uri?) {
        items.add(FileItem(fileName, fileUri))
        adapter.notifyItemInserted(items.size.minus(1))

//        if (currentPhotoPosition != -1) {
//            items[currentPhotoPosition] = fileName
//            adapter.notifyItemChanged(currentPhotoPosition)
//        }
    }

    private fun updateItem2(fileName: String, fileUri: Uri?) {
        if (currentVideoPosition != -1) {
            items2[currentPhotoPosition] = FileItem(fileName, fileUri)
            adapter2.notifyItemChanged(currentVideoPosition)
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun login(view: View) {


        }

        fun next(view: View) {
            listener?.onNextButtonClick()

        }

        fun backPress(view: View) {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}