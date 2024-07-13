package top.libreeze.path.forbid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import top.libreeze.path.forbid.activity.AboutActivity
import top.libreeze.path.forbid.activity.AppSettingsActivity
import top.libreeze.path.forbid.adapter.ApplicationDataAdapter
import top.libreeze.path.forbid.bean.ApplicationData
import top.libreeze.path.forbid.utils.AppLogger
import top.libreeze.path.forbid.utils.AppOperationDialog
import top.libreeze.path.forbid.utils.AppTools
import top.libreeze.path.forbid.utils.RootUtils
import java.io.DataOutputStream
import java.io.File

class MainActivity : BaseActivity() {

    private var recyclerList: MutableList<ApplicationData> = mutableListOf()

    private lateinit var allAPKs: MutableList<ApplicationData>

    private var noneSystemApps: MutableList<ApplicationData> = mutableListOf()

    private lateinit var frameLayout: FrameLayout

    private lateinit var recyclerView: RecyclerView

    private lateinit var linearLayout: LinearLayout

    private lateinit var recyclerAdapter: ApplicationDataAdapter

    private var isShowSystemApps = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 设置工具栏
        setSupportActionBar(findViewById(R.id.toolbar))

        // 移动二进制文件
        RootUtils.obtainCpuAbi().run {
            assets.open(
                if (this.contains("x86")) {
                    "toybox-x86_64"
                } else {
                    "toybox"
                }
            ).let {
                FileUtils.copyInputStreamToFile(it, File(RootUtils.toybox()))
                RootUtils.setFilePermission(777, RootUtils.toybox())
            }
        }


        // 判断是否是第一次打开app
        if (appSettings.isFirstOpenApp) {
            // 显示提示框
            val apply: AlertDialog = AlertDialog.Builder(this).apply {
                title = "欢迎使用"
                setMessage(R.string.claim)
                setPositiveButton(
                    "确定"
                ) { dialog, _ ->
                    dialog?.dismiss()
                    appSettings.notFirstOpenApp()
                }
                setNegativeButton("退出") { _, _ -> finish() }
                setCancelable(false)
            }.create()
            apply.show()
            return
        }

        // 申请权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }

        // 获取设备所有应用
        allAPKs = AppTools.getAllApps(this)
        for (data in allAPKs) {
            if (!data.isSystemApp) {
                noneSystemApps.add(data)
            }
        }
        recyclerList.addAll(noneSystemApps)


        // 初始化控件
        frameLayout = findViewById(R.id.mainFrameLayout)
        recyclerView = findViewById(R.id.mainRecyclerView)
        linearLayout = findViewById(R.id.mainEmptyView)


        // 初始化信息
        if (recyclerList.isNotEmpty()) {
            linearLayout.visibility = View.GONE
            // 初始化列表
            recyclerAdapter = ApplicationDataAdapter(this, recyclerList)
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
            // 初始化点击事件
            recyclerAdapter.setOnItemClickListener { _, data ->
                AppOperationDialog(
                    this@MainActivity,
                    data
                ).show()
            }
        }

    }

    /**
     * 检测设备是否有ROOT
     */
    @kotlin.Deprecated("无需再使用，请直接使用 Shell.getShell()")
    fun requestRoot(): Boolean {
        try {
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
            if (process.exitValue() == 0) {
                // su命令执行成功，设备root
                return true
            }
            toast("设备未获取ROOT，程序无法工作")
        } catch (e: Exception) {
            e.printStackTrace()
            toast("设备未获取ROOT，程序无法工作")
        }
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val findItem = menu?.findItem(R.id.menu_search_application)
        val searchView = findItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerList.clear()
                if (newText.isNullOrBlank()) {
                    if (isShowSystemApps) {
                        recyclerList.addAll(allAPKs)
                    } else {
                        recyclerList.addAll(noneSystemApps)
                    }
                } else {
                    val mutableListOf = mutableListOf<ApplicationData>()
                    if (isShowSystemApps) {
                        mutableListOf.addAll(allAPKs)
                    } else {
                        mutableListOf.addAll(noneSystemApps)
                    }

                    for (data in mutableListOf) {
                        if (data.appName.contains(newText, true)) {
                            recyclerList.add(data)
                        }
                    }
                }
                recyclerAdapter.notifyDataSetChanged()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.menu_show_system_apps) {
            AppLogger.logger.info("点击显示系统应用菜单")
            recyclerList.clear()
            if (!item.isChecked) {
                recyclerList.addAll(allAPKs)
                item.isChecked = true
                isShowSystemApps = true
            } else {
                recyclerList.addAll(noneSystemApps)
                item.isChecked = false
                isShowSystemApps = false
            }

            recyclerAdapter.notifyDataSetChanged()
        } else if (itemId == R.id.menu_about_app) {
            startActivity(Intent(this, AboutActivity::class.java))
        } else if (itemId == R.id.menu_app_settings) {
            startActivity(Intent(this, AppSettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}