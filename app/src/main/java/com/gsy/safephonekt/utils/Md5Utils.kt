package com.gsy.safephonekt.utils

import com.gsy.safephonekt.DEBUG
import com.gsy.safephonekt.LogUtils
import java.security.MessageDigest

/**
 * md5 加密器
 */
val TAG = "Md5Utils"

class Md5Utils {
    companion object {
        private fun log(str: String) {
            if (DEBUG) LogUtils.d(TAG, str)
        }

        fun md5(str: String): String {
            val md = MessageDigest.getInstance("MD5")
            val bytes = str.toByteArray()
            val digest = md.digest(bytes)
            val builder = StringBuilder()
            for (byte in digest) {
                val b = byte.toInt() and 0xff
                var hexString = Integer.toHexString(b)
                if (hexString.length == 1) {
                    hexString = "0" + hexString
                }
                builder.append(hexString)
            }
            log(builder.toString())
            return builder.toString()
        }
    }
}