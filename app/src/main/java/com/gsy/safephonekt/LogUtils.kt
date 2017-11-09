package com.gsy.safephonekt

import android.util.Log

/**
 * Created by gsy on 2017/11/9.
 */

var DEBUG = true
val GLOBAL_TAG = "GsyStudy"

class LogUtils {
    companion object {
        private fun formatMsg(tag: String, msg: String) = "[$tag] [${Thread.currentThread().name}] $msg"

        fun setEnableLog(enable: Boolean) {
            DEBUG = enable
        }

        fun d(tag: String, msg: String) {
            Log.d(GLOBAL_TAG, formatMsg(tag, msg))
        }

    }
}
