package mission.vatsalya.utilities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import mission.vatsalya.R


object PermissionHelper {
    private var mAlertDialogPermission: AlertDialog? = null
    @JvmStatic
    fun isPermissionWithGranted(
        permission: Array<String?>?,
        requestCode: Int,
        activity: Activity?
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val needPermission = checkPermissionList(permission, activity)
            val isGranted = !(needPermission != null && !needPermission.isEmpty())
            if (isGranted) {
                true
            } else {
                try {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        needPermission!!.toTypedArray(),
                        requestCode
                    )
                } catch (ignore: Exception) {
                    ignore.printStackTrace()
                }
                false
            }
        } else {
            true
        }
    }

    fun checkPermissionList(permission: Array<String?>?, context: Context?): List<String?>? {
        var needPermission: MutableList<String?>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission != null && permission.size > 0) {
            needPermission = ArrayList()
            for (aPermission in permission) {
                if (ActivityCompat.checkSelfPermission(context!!, aPermission!!) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    needPermission.add(aPermission)
                }
            }
        }
        return needPermission
    }

    /*public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            int flag = checkPermissionResult(permissions, grantResults,
                    R.string.request_read_storage_permission);
            if (flag == 1) {
                navigateToBackPermission();
            } */
    /*else if (flag == 0) {
                //check Permission again
            }*/
    /*
        }
    }*/
    fun checkPermissionResult(
        permissions: Array<out String?>, grantResults: IntArray,
        @StringRes rationalMessage: Int, activity: Activity
    ): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var anyPermissionDenied = false
            var neverAskAgainSelected = false
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    anyPermissionDenied = true
                    neverAskAgainSelected = neverAskAgain(i, permissions, activity)
                }
            }
            if (anyPermissionDenied) {
                if (neverAskAgainSelected) {
                    showAlertPermissions(rationalMessage, activity)
                    -1
                } else {
                    0
                }
            } else {
                1
            }
        } else {
            1
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgain(
        i: Int,
        permissions: Array<out String?>,
        activity: Activity
    ): Boolean {
        return !activity.shouldShowRequestPermissionRationale(
            permissions[i]!!
        )
    }

    fun showAlertPermissions(rationalMessage: Int, activity: Activity) {
        var isShow = true
        if (mAlertDialogPermission != null && mAlertDialogPermission!!.isShowing) {
            val tv_message = mAlertDialogPermission!!.findViewById<TextView>(android.R.id.message)
            if (tv_message != null && !TextUtils.equals(
                    tv_message.text,
                    activity.getString(rationalMessage)
                )
            ) {
                tv_message.text = activity.getString(rationalMessage)
            }
            isShow = false
        }
        if (isShow) {
            val alertDialog =
                AlertDialog.Builder(ContextThemeWrapper(activity, R.style.DialogTheme))
            alertDialog.setTitle(R.string.message_permission_not_granted)
            alertDialog.setMessage(rationalMessage)
            alertDialog.setCancelable(false)
            alertDialog.setNeutralButton(android.R.string.ok) { dialog: DialogInterface?, which: Int ->
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data = Uri.parse("package:" + activity.packageName)
                activity.startActivity(i)
            }
            mAlertDialogPermission = alertDialog.create()
            mAlertDialogPermission!!.show()
        }
    }
}