package com.gsy.safephonekt

import android.app.Application
import android.content.Context
import org.xutils.x

/**
 * Created by gsy on 2017/11/9.
 */
class BaseApplication: Application() {
    companion object {
        private lateinit var mContext :Context
        fun getContext():Context  = mContext
    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
        x.Ext.init(this)
    }
}