package top.libreeze.path.forbid.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.activity.FileManageActivity
import top.libreeze.path.forbid.bean.ApplicationData

@SuppressLint("SetTextI18n", "SdCardPath")
class AppOperationDialog(private val activity: Activity, private val data:ApplicationData) {

    private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)

    private lateinit var dialog:AlertDialog


    private var baseView:View =
        LayoutInflater.from(activity).inflate(R.layout.dialog_app_operation, null)
    private var privatePath:TextView
    private var privatePath2:TextView
    private var appName:TextView
    private var appPackageName:TextView
    private var cancelButton: Button

    init {
        privatePath = baseView.findViewById(R.id.dialogAppOperationAppPrivatePath1Tv)
        privatePath2 = baseView.findViewById(R.id.dialogAppOperationAppPrivatePath2Tv)
        appName = baseView.findViewById(R.id.dialogAppOperationAppName)
        appPackageName = baseView.findViewById(R.id.dialogAppOperationAppPackageName)
        cancelButton = baseView.findViewById(R.id.dialogAppOperationCancel)

        baseView.findViewById<RelativeLayout>(R.id.dialogAppOperationAppPrivatePath1).setOnClickListener {
            FileManageActivity.startActivity(activity, data.applicationInfo.dataDir,data.applicationInfo.packageName)
        }

        baseView.findViewById<RelativeLayout>(R.id.dialogAppOperationAppPrivatePath2).setOnClickListener {
            FileManageActivity.startActivity(activity, "/sdcard/Android/data/" + data.applicationInfo.packageName,data.applicationInfo.packageName)
        }

        privatePath.text = data.applicationInfo.dataDir
        privatePath2.text = "/sdcard/Android/data/" + data.applicationInfo.packageName
        appName.text = data.appName
        appPackageName.text = data.applicationInfo.packageName
        cancelButton.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.setView(baseView)
        dialog = dialogBuilder.create()
    }


    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }


}