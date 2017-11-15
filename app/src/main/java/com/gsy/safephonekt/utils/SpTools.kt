package com.gsy.safephonekt.utils

import android.content.Context
import com.gsy.safephonekt.BaseApplication
import com.gsy.safephonekt.constants.SP_FILE

/**
 * Created by gsy on 2017/11/14.
 */
class SpTools {
    companion object {
        fun putString(key: String, value: String) {
            val sp = BaseApplication.getContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            sp.edit().putString(key, value).apply()
        }

        fun getString(key: String, defValue: String): String {
            val sp = BaseApplication.getContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            return sp.getString(key, defValue)
        }

        fun putBoolean(key: String, value: Boolean) {
            val sp = BaseApplication.getContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            sp.edit().putBoolean(key, value).apply()
        }

        fun getBoolean(key: String, defValue: Boolean): Boolean {
            val sp = BaseApplication.getContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            return sp.getBoolean(key, defValue)
        }

    }
}