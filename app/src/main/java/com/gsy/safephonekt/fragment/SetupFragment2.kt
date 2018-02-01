package com.gsy.safephonekt.fragment

import android.content.Context
import android.telephony.TelephonyManager
import android.view.View
import android.widget.ImageView
import com.gsy.safephonekt.R
import com.gsy.safephonekt.constants.SIM_INFO
import com.gsy.safephonekt.utils.SpTools

/**
 * Created by gsy on 2017/11/15.
 */
class SetupFragment2(context: Context) : BaseFragmet(context) {

    private val mBindBtn: View
    private val mIvLock: ImageView


    init {
        val view = View.inflate(context, R.layout.vp_setup2, null)
        mFrameLayout.addView(view)
        mBindBtn = view.findViewById(R.id.rl_btn_bind_sim)
        mIvLock = view.findViewById(R.id.iv_lock)
    }

    override fun initData() {
        if (SpTools.getString(SIM_INFO, "").isEmpty()) {
            mIvLock.setImageResource(R.mipmap.unlock)
        } else {
            mIvLock.setImageResource(R.drawable.lock)
        }
        mBindBtn.setOnClickListener {
            if (SpTools.getString(SIM_INFO, "").isEmpty()) {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val simInfo = tm.simSerialNumber
                SpTools.putString(SIM_INFO, simInfo)
                mIvLock.setImageResource(R.drawable.lock)
            } else {
                mIvLock.setImageResource(R.mipmap.unlock)
                SpTools.putString(SIM_INFO, "")
            }
        }
    }

    override fun canNext(): Boolean  = SpTools.getString(SIM_INFO, "").isNotEmpty()

}