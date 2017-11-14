package com.gsy.safephonekt.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by gsy on 2017/11/10.
 */
class RollTextView : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defState: Int) : super(context, attrs, defState)

    override fun isFocused(): Boolean {
        return true
    }
}