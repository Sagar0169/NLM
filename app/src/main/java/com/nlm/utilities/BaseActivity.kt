package com.nlm.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.nlm.R
import com.nlm.services.LocationService
import java.io.File
import java.util.Locale

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private val CAPTURE_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_LOCATION = 1
    private var STORAGE_STORAGE_REQUEST_CODE = 61
    val REQUEST_iMAGE_PDF = 20
    private val REQUEST_iMAGE_GALLERY = 3

    var uriTemp: Uri? = null

    private var photoFile: File? = null
    var file: File? = null

    lateinit var viewDataBinding: T

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        window.statusBarColor = Color.TRANSPARENT
//        window.statusBarColor = ContextCompat.getColor(this, R.color.darkBlue)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        performDataBinding()
        initView()
        setVariables()
        setObservers()
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        viewDataBinding.executePendingBindings()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    abstract fun initView()

    abstract fun setVariables()

    abstract fun setObservers()

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLocationAlertDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Location Not Found")
            .setMessage("Unable to fetch your location. Please enable location from settings.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun View.showStringSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.view.setBackgroundColor(Color.parseColor("#F16622"))
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setAction("Ok") {
                snackbar.dismiss()
            }
        }.show()
    }
    fun hasLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, LocationService::class.java)
                this.startService(intent)
            } else {
                Toast.makeText(this, "Location permissions are required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }
    protected open fun getLanguageLocalize(lang: String?, context: Context) {
        val config = context.resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.resources.updateConfiguration(config, null)
        } else {
            config.locale = locale
            context.resources.updateConfiguration(config, null)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            context.resources
                .updateConfiguration(config, context.resources.displayMetrics)
            context.createConfigurationContext(config)
        } else {
            context.resources
                .updateConfiguration(config, context.resources.displayMetrics)
        }
    }


    fun callAlertDialog(message: Int, context: Context?) {
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setMessage(message)
            .setTitle(resources.getString(R.string.app_name))
            .setCancelable(false)
        val alert = builder.create()
        alert.show()
    }

    fun callAlertDialog(message: String?, context: Context?) {
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setMessage(message)
            .setTitle(resources.getString(R.string.app_name))
            .setCancelable(false).setPositiveButton("ok") { _, _ -> finish() }
        val alert = builder.create()
        alert.show()
    }

    fun shareOptions() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage += "https://play.google.com/store/apps/details?id=" /* + BuildConfig.APPLICATION_ID + "\n\n"*/
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }
}

