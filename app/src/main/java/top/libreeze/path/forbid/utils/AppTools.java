package top.libreeze.path.forbid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import top.libreeze.path.forbid.bean.ApplicationData;

public class AppTools {

    /**
     * 查询获取所有已安装app
     * @param context 上下文
     * @return List<ApplicationData>
     */
    @SuppressLint("QueryPermissionsNeeded")
    public static List<ApplicationData> getAllApps(Context context) {
        List<ApplicationData> dataList = new ArrayList<>();
        PackageManager systemService = context.getPackageManager();
        List<ApplicationInfo> installedApplications = systemService.getInstalledApplications(PackageManager.GET_META_DATA);
        if (!installedApplications.isEmpty()) {
            for (ApplicationInfo info:installedApplications) {
                dataList.add(new ApplicationData(info));
            }
        }
        return dataList;
    }

}
