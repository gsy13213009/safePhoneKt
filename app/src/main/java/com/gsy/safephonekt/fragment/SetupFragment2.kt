package com.gsy.safephonekt.fragment

import android.content.Context
import android.view.View
import com.gsy.safephonekt.R

/**
 * Created by gsy on 2017/11/15.
 */
class SetupFragment2(context: Context) : BaseFragmet(context) {
    init {
        val view = View.inflate(context, R.layout.vp_setup2, null)
        mFrameLayout.addView(view)
    }

    override fun initData() {

    }

}