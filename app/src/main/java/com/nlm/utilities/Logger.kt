package com.nlm.utilities

import android.util.Log


class Logger {


    companion object {
        val DEBUG: Boolean = "true".toBoolean()
        fun d(tag: String?, message: String?) {
           if (DEBUG) {
                try {
                    Log.d(tag, message!!)
                } catch (e: Exception) {
                    //e.printStackTrace();
                }
           }
        }

        fun w(tag: String?, message: String?) {
            if (DEBUG) {
                try {
                    Log.w(tag, message!!)
                } catch (e: Exception) {
                    //e.printStackTrace();
                }
            }
        }

        fun i(tag: String?, message: String?) {
            if (DEBUG) {
                try {
                    Log.i(tag, message!!)
                } catch (e: Exception) {
                    //e.printStackTrace();
                }
            }
        }


        fun e(tag: String?, message: String?) {
            if (DEBUG) {
                try {
                    Log.e(tag, message!!)
                } catch (e: Exception) {
                    //e.printStackTrace();
                }
            }
        }

        fun log(message: String?) {
            e("log:", message)
        }

    }
}