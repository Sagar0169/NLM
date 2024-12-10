package com.nlm.ui.activity

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.nlm.R
import com.nlm.databinding.ActivityImageGeoLocationBinding
import com.nlm.utilities.BaseActivity
import java.io.File

class ImageGeoLocationActivity : BaseActivity<ActivityImageGeoLocationBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_image_geo_location
    private var mBinding: ActivityImageGeoLocationBinding? = null
    val cameraImageUri: Uri by lazy {
        val file = File(applicationContext.filesDir, "captured_image.jpg")
        FileProvider.getUriForFile(
            applicationContext,
            "com.nlm.provider",
            file
        )
    }

     val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            startCrop(cameraImageUri)
            fetchLocation()

        } else {
            Toast.makeText(this, "Camera capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showImage(bitmap: Bitmap) {
        // Override to display the image in this activity
        mBinding?.imgViewer?.setImageBitmap(bitmap)
    }

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.btnCamera?.setOnClickListener {
            if (!hasCameraPermission() || !hasLocationPermission()) {
                requestPermissions()
            } else {
                takePicture.launch(cameraImageUri)
            }
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}
