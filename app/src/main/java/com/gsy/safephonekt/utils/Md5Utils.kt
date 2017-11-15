package com.gsy.safephonekt.utils

import com.gsy.safephonekt.DEBUG
import com.gsy.safephonekt.LogUtils
import java.security.MessageDigest

/**
 * Created by gsy on 2017/11/14.
 */
val TAG = "Md5Utils"

class Md5Utils {
    companion object {
        fun log(str: String) {
            if (DEBUG) LogUtils.d(TAG, str)
        }

        fun md5(str: String): String {
            val md = MessageDigest.getInstance("MD5")
            val bytes = str.toByteArray()
            val digest = md.digest(bytes)
            val builder = StringBuilder()
            for (byte in digest) {
                val b = byte.toInt() and 0xff
                var hexStirng = Integer.toHexString(b)
                if (hexStirng.length == 1) {
                    hexStirng = "0" + hexStirng
                }
                builder.append(hexStirng)
            }
            log(builder.toString())
            return builder.toString()
        }
    }
}