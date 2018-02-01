package com.gsy.safephonekt.fragment

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gsy.safephonekt.R

/**
 * Created by gsy on 2017/11/15.
 */
abstract class BaseFragmet(val context: Context) {
    val mFrameLayout: FrameLayout
    private val mRootView: View

    init {
        mRootView = View.inflate(context, R.layout.vp_setup_root, null)
        mFrameLayout = mRootView.findViewById(R.id.fl_root)
    }

    public fun getRootView() = mRootView

    abstract fun initData()

    abstract fun canNext():Boolean
}