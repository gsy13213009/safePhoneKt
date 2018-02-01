package com.gsy.safephonekt.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.TextView
import com.gsy.safephonekt.DEBUG
import com.gsy.safephonekt.LogUtils
import com.gsy.safephonekt.R
import com.gsy.safephonekt.fragment.*


/**
 * Created by gsy on 2017/11/15.
 */
class StartupActivity : AppCompatActivity() {
    private val TAG = "StartupActivity"
    fun log(msg: String) {
        if (DEBUG) LogUtils.d(TAG, msg)
    }

    private lateinit var mTvTitle: TextView
    private lateinit var mViewPager: ViewPager
    private lateinit var mBtPre: Button
    private lateinit var mBtNext: Button
    private lateinit var leftEdge: EdgeEffect
    private lateinit var rightEdge: EdgeEffect
    lateinit var mView: Array<BaseFragmet>
    var mCurrent = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        initView()
        initData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initData() {
        mView = arrayOf(SetupFragment1(this), SetupFragment2(this), SetupFragment3(this), SetupFragment4(this))
        mViewPager.adapter = MyAdapter()
        mCurrent = mViewPager.currentItem
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                log("positionOffset $positionOffset positionOffsetPixels $positionOffsetPixels")
                if (position == mView.size - 1) {
                    mBtNext.text = "完成"
                } else {
                    mBtNext.text = "下一步"
                }
                if (position == 0) {
                    mBtPre.visibility = View.GONE
                } else {
                    mBtPre.visibility = View.VISIBLE
                }
                mCurrent = position
            }

            override fun onPageSelected(position: Int) {
            }

        })
        mViewPager.setOnTouchListener { _, _ ->
            !mView[mCurrent].canNext()
        }

    }


    private fun initView() {
        mTvTitle = findViewById(R.id.tv_title) as TextView
        mViewPager = findViewById(R.id.vp_setup) as ViewPager
        mBtPre = findViewById(R.id.bt_pre) as Button
        mBtNext = findViewById(R.id.bt_next) as Button

        val leftEdgeField = ViewPager::class.java.getDeclaredField("mLeftEdge")
        val rightEdgeField = ViewPager::class.java.getDeclaredField("mRightEdge")
        if (leftEdgeField != null && rightEdgeField != null) {
            leftEdgeField.isAccessible = true
            rightEdgeField.isAccessible = true
            leftEdge = leftEdgeField.get(mViewPager) as EdgeEffect
            rightEdge = rightEdgeField.get(mViewPager) as EdgeEffect
        }
    }


    inner class MyAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View?, any: Any?) = view === any

        override fun getCount() = mView.size

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val baseFragment = mView[position]
            baseFragment.initData()
            val rootView = baseFragment.getRootView()
            container?.addView(rootView)
            return rootView
        }

        override fun destroyItem(container: ViewGroup?, position: Int, any: Any?) {
            container?.removeView(any as View)
        }


    }

    public fun prev(view: View) {
        if (mCurrent > 0) {
            mViewPager.currentItem = mCurrent - 1
        }
    }

    public fun next(view: View) {
        if (!mView[mCurrent].canNext()) return
        if (mCurrent < 3) {
            mViewPager.currentItem = mCurrent + 1
        }
    }

}

