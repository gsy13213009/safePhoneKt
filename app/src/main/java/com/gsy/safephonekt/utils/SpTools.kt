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

        fun getString(key: String, defValue: String) = {
            val sp = BaseApplication.getContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            sp.getString(key, defValue)
        }
    }
}