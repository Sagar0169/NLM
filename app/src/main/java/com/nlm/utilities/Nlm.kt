package com.nlm.utilities

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Base64
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference
import java.net.Socket
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
@HiltAndroidApp
class Nlm : Application() {

    override fun onCreate() {
        super.onCreate()
        instance=this
        mContext = this
        printHashKey(this)

//        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
//            override fun onActivityPaused(activity: Activity) {
//            }
//
//            override fun onActivityStarted(activity: Activity) {
//                if (++activityReferences == 1 && !isActivityChangingConfigurations) {
//                    Log.e("Activity State: ","In Foreground now")
////                    mSocket?.connect()
//                }
//            }
//
//            override fun onActivityDestroyed(activity: Activity) {
//            }
//
//            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
//            }
//
//            override fun onActivityStopped(activity: Activity) {
//                isActivityChangingConfigurations = activity.isChangingConfigurations
//                if (--activityReferences == 0 && !isActivityChangingConfigurations) {
//                    Log.e("Activity State: ","In Background now")
////                    mSocket?.disconnect()
//                }
//            }
//
//            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//                mActivity = WeakReference(activity)
//            }
//
//            override fun onActivityResumed(activity: Activity) {
//                mActivity = WeakReference(activity)
//            }
//        })
    }

    fun  isInternetConnected(): Boolean {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }//checks if the internet is connected
    companion object{

        lateinit var instance: Nlm

        @JvmField
        var mContext: Context? = null

        @JvmStatic
        fun getToken(): String {
            return "Bearer ".plus(Utility.getPreferenceString(mContext!!, PrefEntities.TOKEN))
        }

        private var mSocket: Socket?=null

        @JvmField
        var mActivity: WeakReference<Activity>? = null
        private var activityReferences = 0
        private var isActivityChangingConfigurations = false

        private fun getContext(): Context {
            return mContext!!
        }
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i(ContentValues.TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        }
    }
}