package com.nlm.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.nlm.R
import com.nlm.services.LocationService
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var viewDataBinding: T

    @get:LayoutRes
    abstract val layoutId: Int
    val REQUEST_iMAGE_PDF = 20
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    private val PERMISSION_REQUEST_LOCATION = 1
    private val CHANNEL_ID = "location_updates_channel"
    private val NOTIFICATION_ID = 1001
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding= DataBindingUtil.inflate(inflater,layoutId,container,false)
        init()
        setObservers()
        return viewDataBinding.root
    }

    abstract fun init()

    abstract fun setVariables()

    abstract fun setObservers()


    fun View.showStringSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.view.setBackgroundColor(Color.parseColor("#F16622"))
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setAction(R.string.Ok) {
                snackbar.dismiss()
            }
        }.show()
    }
     fun hasLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

     fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
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
                val intent = Intent(requireContext(), LocationService::class.java)
                requireContext().startService(intent)
            } else {
                Toast.makeText(requireContext(), "Location permissions are required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }
    open fun hideKeyboard(activity: Activity) {
        try {
            val inputManager: InputMethodManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    open fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,15}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }


    fun showToast(message: String?) {
        Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}