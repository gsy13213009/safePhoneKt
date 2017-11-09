package com.gsy.safephonekt

import android.util.Log

/**
 * Created by gsy on 2017/11/9.
 */

var DEBUG = true
val GLOBAL_TAG = "GsyStudy"
val INDEX = 4

class LogUtils {
    companion object {
        private fun formatMsg(tag: String, msg: String) = "[$tag] [${Thread.currentThread().name}] $msg ${getTrace()}"

        fun setEnableLog(enable: Boolean) {
            DEBUG = enable
        }

        fun d(tag: String, msg: String) {
            Log.d(GLOBAL_TAG, formatMsg(tag, msg))
        }

        private fun getTrace(): String {
            val stacks = Throwable().stackTrace
            return if (stacks.size <= INDEX) {
                ""
            } else {
                " (" + stacks[INDEX].fileName + ":" + stacks[INDEX].lineNumber + ")"
            }
        }

    }
}
