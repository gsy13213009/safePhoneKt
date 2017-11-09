package com.gsy.safephonekt

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.*
import android.widget.TextView
import android.widget.Toast
import com.gsy.safephonekt.data.UrlBean
import org.json.JSONException
import org.json.JSONObject
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * 手机卫士splash界面
 */
const val TAG = "SplashActivity"
val REQUEST_STORAGE_PERMISSION = 3
val ERROR_CODE_TYPE = 4
fun log(msg: String) {
    if (DEBUG) LogUtils.d(TAG, msg)
}

fun toast(msg: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show()
    }
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
                LOAD_MAIN -> loadMain()
                SHOW_UPDATE_DIALOG -> showUpdateDialog()
                ERROR_CODE_TYPE -> {
                    when (msg.arg1) {
                        404 -> toast("404资源找不到")
                        4001 -> toast("没有网络")
                        4002 -> toast("url格式错误")
                        4003 -> toast("json数据有问题")

                    }
                    loadMain()
                }
            }
        }
    }

    /**
     * 显示是否更新对话框
     */
    private fun showUpdateDialog() {
        val build = AlertDialog.Builder(this)
        build.setTitle("提醒")
                .setMessage("是否更新新版本，新版本具有以下特性：${urlBean.desc}")
                .setNegativeButton("取消") { _, _ ->
                    // 进入主界面
                    loadMain()
                }
                .setPositiveButton("更新") { _, _ ->
                    log("更新主界面")
                    val params = RequestParams(urlBean.url)
                    params.saveFilePath = "/sdcard/xxx.apk"
                    File("/sdcard/xxx.apk").delete()
                    x.http().get(params, object : Callback.CommonCallback<File> {
                        override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                            toast("下载失败 $ex")
                            log("下载失败 $ex")
                        }

                        override fun onCancelled(cex: Callback.CancelledException?) {
                        }

                        override fun onSuccess(result: File?) {
                            toast("下载成功")
                            installApk()
                        }

                        override fun onFinished() {
                        }
                    })
                }
                .setOnCancelListener {
                    loadMain()
                }
                .show()

    }

    /**
     * 进入主界面
     */
    private fun loadMain() {
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        finish()
    }

    /**
     * 安装apk
     */
    private fun installApk() {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        val type = "application/vnd.android.package-archive"
        var data = Uri.fromFile(File("/sdcard/xxx.apk"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.gsy.safephonekt.fileprovider", File("/sdcard/xxx.apk"));
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(data, type)
        startActivityForResult(intent, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        }

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
            var conn: HttpURLConnection? = null
            var reader: BufferedReader? = null
            var errorCode = -1
            try {
                val url = URL("http://172.18.15.212:8080/guardversion.json")
                conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 5000
                conn.connectTimeout = 5000
                conn.requestMethod = "GET"
                val responseCode = conn.responseCode
                log("responseCode = $responseCode")
                if (responseCode == 200) {
                    val inputStream = conn.inputStream
                    reader = BufferedReader(InputStreamReader(inputStream))
                    var line = reader.readLine()
                    val jsonString = StringBuilder()
                    while (line != null) {
                        jsonString.append(line)
                        line = reader.readLine()
                    }
                    urlBean = parseJson(jsonString.toString())
                    log("urlBean = $urlBean")
                } else {
                    errorCode = 404
                }
            } catch (e: MalformedURLException) {
                errorCode = 4002
                log(e.toString())
            } catch (e: JSONException) {
                errorCode = 4003
                log(e.toString())
            } catch (e: IOException) {
                errorCode = 4001
                log(e.toString())
            } finally {
                val msg = Message.obtain()
                if (errorCode == -1) {
                    msg.what = isNewVersion()
                } else {
                    msg.what = ERROR_CODE_TYPE
                    msg.arg1 = errorCode
                }
                val endTime = System.currentTimeMillis()
                val delayTime = if (endTime - startTime >= 3000) 0 else 3000 - endTime + startTime
                mHandle.postDelayed({
                    mHandle.sendMessage(msg)
                }, delayTime)
                reader?.close()                      // 关闭输入流
                conn?.disconnect()
            }
        }.start()
    }

    /**
     * 判断是否有新版本
     */
    private val isNewVersion = {
        val serverCode = urlBean.versionCode
        log("serverCode $serverCode versionCode $versionCode")
        // 对比自己的版本
        if (serverCode > versionCode) {
            SHOW_UPDATE_DIALOG
        } else {
            LOAD_MAIN
        }
    }

    /**
     * 解析版本信息
     */
    private fun parseJson(jsonString: String): UrlBean {
        val jsonObj = JSONObject(jsonString)
        return UrlBean(jsonObj.optString("url"), jsonObj.optInt("versionCode"), jsonObj.optString("desc"))
    }


    /**
     * 设置动画
     */
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

    /**
     * 初始化view
     */
    private fun initView() {
        mRlRoot = findViewById(R.id.rl_splash_root)
        tvVersionName = findViewById(R.id.tv_splash_version_name) as TextView
    }

    /**
     * 权限申请
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_STORAGE_PERMISSION -> Toast.makeText(this, "申请外部存储权限成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 如果用户取消，则直接进入主界面
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadMain()
        super.onActivityResult(requestCode, resultCode, data)
    }

}