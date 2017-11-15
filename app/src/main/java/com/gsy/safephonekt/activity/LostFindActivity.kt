package com.gsy.safephonekt.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gsy.safephonekt.DEBUG
import com.gsy.safephonekt.LogUtils
import com.gsy.safephonekt.R
import com.gsy.safephonekt.constants.IS_SETUP
import com.gsy.safephonekt.utils.SpTools

class LostFindActivity : AppCompatActivity() {
    private val TAG = "LostFindActivity"
    fun log(msg: String) {
        if (DEBUG) LogUtils.d(TAG, msg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_find)
        if (SpTools.getBoolean(IS_SETUP, false)) {
            initView()
            log("onCreate initView")
        } else {
            startActivity(Intent(this, StartupActivity::class.java))
            log("onCreate startActivity StartupActivity")
            finish()
        }
    }

    private fun initView() {


    }
}