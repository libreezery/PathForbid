package top.libreeze.path.forbid.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import top.libreeze.path.forbid.BaseActivity
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.adapter.AppFileAdapter
import top.libreeze.path.forbid.bean.AppFile
import top.libreeze.path.forbid.bean.FileOperateRecord
import top.libreeze.path.forbid.database.FileOperateRecordDao
import top.libreeze.path.forbid.utils.RootUtils
import top.libreeze.path.forbid.utils.ThreadUtils

class FileManageActivity : BaseActivity() {

    private lateinit var loading_view: View
    private lateinit var recycler_view: View
    private lateinit var nothing_view: View
    private lateinit var frameLayout: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppFileAdapter
    private var data: MutableList<AppFile> = mutableListOf()
    private lateinit var currentFile: AppFile
    private lateinit var rootPath: String
    private lateinit var toolbar: MaterialToolbar
    private lateinit var appPkgName: String
    private lateinit var recordDao: FileOperateRecordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manage)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 初始化数据库
        recordDao = FileOperateRecordDao()

        // 初始化控件
        nothing_view = layoutInflater.inflate(R.layout.view_nothing, null)
        loading_view = layoutInflater.inflate(R.layout.view_loading, null)
        recycler_view = layoutInflater.inflate(R.layout.view_recyclerview, null)
        recyclerView = recycler_view.findViewById(R.id.recyclerView)
        // 初始化列表
        adapter = AppFileAdapter(this, data)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, data ->
            if (data.isDirectory) {
                navigateTo(data)
            }
        }
        adapter.setOnItemLongClickListener { view, position, fileData ->
            PopupMenu(this@FileManageActivity, view).apply {
                inflate(R.menu.file_operate_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_forbid_permission -> {
                            // 禁止应用权限
                            val findByPath: FileOperateRecord? = recordDao.findByPath(fileData.filepath)
                            if (findByPath != null) {
                                toast("该文件夹已被禁用，请解除禁用")
                            } else {
                                val fileOperateRecord = FileOperateRecord(
                                    fileData.filepath,
                                    fileData.filename,
                                    appPkgName,
                                    fileData.permission,
                                    500
                                )
                                if (RootUtils.setFilePermission(500, fileData.filepath)) {
                                    recordDao.add(fileOperateRecord)
                                    fileData.permission = 500
                                    toast("禁用成功")
                                } else {
                                    toast("禁用失败，请检查ROOT权限")
                                }
                            }
                        }
                        R.id.menu_enable_permission -> {
                            val findByPath: FileOperateRecord? = recordDao.findByPath(fileData.filepath)
                            if (findByPath == null) {
                                toast("该文件没有被禁用，无需解除")
                            } else {
                                if (RootUtils.setFilePermission(findByPath.originMode, findByPath.filepath)) {
                                    fileData.permission = findByPath.originMode
                                    recordDao.delete(findByPath)
                                    toast("解除禁用成功")
                                } else {
                                    toast("解除失败，请检查ROOT")
                                }
                            }
                        }
                        R.id.menu_delete_file -> {
                            // 删除文件
                            AlertDialog.Builder(this@FileManageActivity).apply {
                                title = "警告"
                                setMessage("删除文件/文件夹:\n\n ${fileData.filename} \n\n此操作无法撤销!")
                                setPositiveButton("确定") { _, _ ->
                                    fileData.deleteFile().run {
                                        if (this) {
                                            data.remove(fileData)
                                            adapter.notifyItemRemoved(position)
                                            adapter.notifyDataSetChanged()
                                            toast("删除成功")
                                        } else {
                                            toast("删除失败")
                                        }
                                    }
                                }
                                setNegativeButton("取消",null)
                                create()
                            }.show()
                        }
                    }
                    adapter.notifyItemChanged(position)
                    true
                }
            }.show()
        }

        frameLayout = findViewById(R.id.fileManageFrameLayout)

        // 获取传递的根路径
        val stringExtra = this.intent.getStringExtra("path")
        this.appPkgName = this.intent.getStringExtra("pkg")!!
        if (stringExtra?.isNotBlank() == true) {
            this.rootPath = stringExtra
            val call = AppFile(stringExtra)
            navigateTo(call)
        } else {
            turnNothing()
        }
    }

    private fun navigateTo(file: AppFile) {
        turnLoading()
        this.currentFile = file
        this.toolbar.subtitle = file.filepath
        ThreadUtils.getExecutors().execute {
            data.clear()
            data.addAll(file.childFile)
            turnRecycler()
            if (data.isEmpty()) {
                turnNothing()
            }
        }
    }

    private fun reloadCurrentFile() {
        data.clear()
        data.addAll(currentFile.childFile)
        turnRecycler()
        if (data.isEmpty()) {
            turnNothing()
        }
    }


    private fun turnLoading() {
        ThreadUtils.runUiThread {
            frameLayout.removeAllViews()
            frameLayout.addView(loading_view)
        }
    }

    private fun turnNothing() {
        ThreadUtils.runUiThread {
            frameLayout.removeAllViews()
            frameLayout.addView(nothing_view)
        }
    }

    private fun turnRecycler() {
        ThreadUtils.runUiThread {
            adapter.notifyDataSetChanged()
            frameLayout.removeAllViews()
            frameLayout.addView(recycler_view)
        }
    }

    companion object {
        fun startActivity(context: Context, path: String, pkgName: String) {
            val intent = Intent(context, FileManageActivity::class.java)
            intent.putExtra("path", path)
            intent.putExtra("pkg", pkgName)
            context.startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.file_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val backward = currentFile.parent
            if (backward == null) {
                finish()
                return true
            }
            if (rootPath == currentFile.filepath) {
                finish()
                return true
            }
            navigateTo(backward)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }


}