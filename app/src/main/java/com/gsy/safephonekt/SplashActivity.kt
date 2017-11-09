package com.gsy.safephonekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * 手机卫士splash界面
 */
val TAG = "SplashActivity"
fun log(msg: String) {
    if (DEBUG) LogUtils.d(TAG, msg)
}

class SplashActivity : AppCompatActivity() {
    var mRlRoot: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        initAnimation()
        checkVersion()
    }

    /**
     * 访问服务器，获取数据
     */
    private fun checkVersion() {
        Thread {
            val url = URL("http://172.18.15.212:8080/guardversion.json")
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 5000
            conn.connectTimeout = 5000
            conn.requestMethod = "GET"
            val responseCode = conn.responseCode
            log("responseCode = $responseCode")
            if (responseCode == 200) {
                val inputStream = conn.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line = reader.readLine()
                val jsonString = StringBuilder()
                while (line != null) {
                    jsonString.append(line)
                    line = reader.readLine()
                }
                log("jsonString = $jsonString")
                reader.close()                      // 关闭输入流
                conn.disconnect()
            }
        }.start()
    }

    private fun initAnimation() {
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = 3000
        alpha.fillAfter = true   // 界面停留在初始状态

        val rote = RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rote.duration = 3000
        rote.fillAfter = true

        val scale = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scale.duration = 3000
        scale.fillAfter = true

        val attSet = AnimationSet(true)
        attSet.addAnimation(alpha)
        attSet.addAnimation(rote)
        attSet.addAnimation(scale)

        mRlRoot!!.startAnimation(attSet)
    }

    private fun initView() {
        mRlRoot = findViewById(R.id.rl_splash_root)
    }
}