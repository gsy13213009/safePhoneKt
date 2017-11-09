package com.gsy.safephonekt

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.*
import android.widget.TextView
import android.widget.Toast
import com.gsy.safephonekt.data.UrlBean
import org.json.JSONObject
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * 手机卫士splash界面
 */
const val TAG = "SplashActivity"

fun log(msg: String) {
    if (DEBUG) LogUtils.d(TAG, msg)
}

fun toast(msg: String) {
    Toast.makeText(BaseApplication.getContext(),msg,Toast.LENGTH_SHORT).show()
}

class SplashActivity : AppCompatActivity() {
    private lateinit var mRlRoot: View
    private lateinit var urlBean: UrlBean
    private lateinit var tvVersionName: TextView
    private var versionCode: Int = 0
    private var versionName: String = ""
    private val LOAD_MAIN = 0
    private val SHOW_UPDATE_DIALOG = 1


    private val mHandle = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                LOAD_MAIN -> startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                SHOW_UPDATE_DIALOG -> showUpdataDialog()
            }
        }
    }

    /**
     * 显示是否更新对话框
     */
    private fun showUpdataDialog() {
        val build = AlertDialog.Builder(this)
        build.setTitle("提醒")
                .setMessage("是否更新新版本，新版本具有以下特性：${urlBean.desc}")
                .setNegativeButton("取消") { _, _ ->
                    // 进入主界面
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                }
                .setPositiveButton("更新") { _, _ ->
                    log("更新主界面")
                    val params = RequestParams(urlBean.url)
                    params.saveFilePath = "/sdcard/xxx.apk"
                    x.http().get(params,object :Callback.CommonCallback<File> {
                        override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                            toast("下载失败 $ex")
                        }

                        override fun onCancelled(cex: Callback.CancelledException?) {
                        }

                        override fun onSuccess(result: File?) {
                            toast("下载成功")
                        }

                        override fun onFinished() {
                        }

                    })
                }
                .setCancelable(false)
                .show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        initData()
        initAnimation()
        checkVersion()
    }

    private fun initData() {
        val pm = packageManager
        val packageInfo = pm.getPackageInfo(packageName, 0)
        versionCode = packageInfo.versionCode
        versionName = packageInfo.versionName
        tvVersionName.text = versionName
    }

    /**
     * 访问服务器，获取数据
     */
    private fun checkVersion() {
        Thread {
            val startTime = System.currentTimeMillis()
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
                urlBean = parseJson(jsonString.toString())
                isNewVersion(startTime)
                log("urlBean = $urlBean")
                reader.close()                      // 关闭输入流
                conn.disconnect()
            }
        }.start()
    }

    private fun isNewVersion(startTime: Long) {
        val serverCode = urlBean.versionCode
        log("serverCode $serverCode versionCode $versionCode")
        val endTime = System.currentTimeMillis()
        val delayTime = if (endTime - startTime >= 3000) 0 else 3000 - endTime + startTime
        // 对比自己的版本
        mHandle.postDelayed({
            if (serverCode > versionCode) {
                mHandle.sendEmptyMessage(SHOW_UPDATE_DIALOG)
            } else {
                mHandle.sendEmptyMessage(LOAD_MAIN)
            }
        }, delayTime)
    }

    /**
     * 解析版本信息
     */
    private fun parseJson(jsonString: String): UrlBean {
        val jsonObj = JSONObject(jsonString)
        return UrlBean(jsonObj.optString("url"), jsonObj.optInt("versionCode"), jsonObj.optString("desc"))
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

        mRlRoot.startAnimation(attSet)
    }

    private fun initView() {
        mRlRoot = findViewById(R.id.rl_splash_root)
        tvVersionName = findViewById(R.id.tv_splash_version_name) as TextView
    }
}