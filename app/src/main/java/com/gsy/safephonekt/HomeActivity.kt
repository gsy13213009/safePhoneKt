package com.gsy.safephonekt

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gsy.safephonekt.constants.PASSWORD
import com.gsy.safephonekt.utils.SpTools


/**
 * Created by gsy on 2017/11/9.
 */
class HomeActivity : Activity() {
    lateinit var gvHome: GridView
    val icons = arrayOf(R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app, R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan, R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings)
    val names = arrayOf("手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀", "缓存清理", "高级工具", "设置中心")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initData()
        initEvent()
    }

    private fun initEvent() {

        gvHome.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->

            when (position) {
                0 -> showSettingPassDialog()
            }
        }
    }

    private fun showSettingPassDialog() {
        val builder = AlertDialog.Builder(this)
        val view = View.inflate(this, R.layout.dialog_setting_password, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val etPassOne = view.findViewById<EditText>(R.id.et_dialog_setting_password_one)
        val etPassConfirm = view.findViewById<EditText>(R.id.et_dialog_setting_password_two)
        val btSet = view.findViewById<Button>(R.id.bt_dialog_setting_password_set)
        val btCancel = view.findViewById<Button>(R.id.bt_dialog_setting_password_cancel)
        btSet.setOnClickListener {
            val password = etPassOne.text.trim().toString()
            if (password.isEmpty()) {
                toast("密码为空，请设置密码")
                return@setOnClickListener
            }
            val passwordConfirm = etPassConfirm.text.trim().toString()
            if (passwordConfirm.isEmpty()) {
                toast("请确认密码")
                return@setOnClickListener
            }
            if (password == passwordConfirm) {
                toast("保存密码")
                SpTools.putString(PASSWORD, passwordConfirm)
                dialog.dismiss()
            } else {
                toast("密码不一致，请确认密码")
                return@setOnClickListener
            }
        }
        btCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initView() {
        gvHome = findViewById<GridView>(R.id.gv_home) as GridView
    }

    private fun initData() {
        val adapter = MyAdapter()
        gvHome.adapter = adapter

    }

    inner class MyAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = View.inflate(this@HomeActivity, R.layout.item_home_gridview, null)
            val ivIcon = view.findViewById<ImageView>(R.id.iv_item_home_gv_icon) as ImageView
            val tvIcon = view.findViewById<TextView>(R.id.tv_item_home_gv_name) as TextView
            ivIcon.setImageResource(icons[position])
            tvIcon.text = names[position]

            return view
        }

        override fun getItem(position: Int) = icons[position]

        override fun getItemId(position: Int) = position.toLong()

        override fun getCount() = icons.size
    }
}

fun String.isEmpty() = this == null || this == ""
