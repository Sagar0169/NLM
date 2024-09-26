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
        mContext = this
        printHashKey(this)
    }

    companion object{

        @JvmField
        var mContext: Context? = null

        @JvmStatic
        fun getToken(): String {
            return if(Utility.getPreferenceString(mContext!!, PrefEntities.TOKEN).isEmpty())
                " "
            else
                "Bearer ".plus(Utility.getPreferenceString(mContext!!, PrefEntities.TOKEN))
        }


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