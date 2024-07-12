package top.libreeze.path.forbid.bean;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import top.libreeze.path.forbid.App;

public class ApplicationData {

    private final ApplicationInfo applicationInfo;

    private final PackageManager packageManager;

    /**
     * 获取原始应用信息
     * @return ApplicationInfo
     */
    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    /**
     * 获取应用包管理器
     * @return PackageManager
     */
    public PackageManager getPackageManager() {
        return packageManager;
    }

    /**
     * 嵌入应用信息
     * @param applicationInfo 应用信息
     */
    public ApplicationData(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        this.packageManager = App.getContext().getPackageManager();
    }

    /**
     * 获取应用名称
     * @return 返回应用名称
     */
    public String getAppName() {
        return packageManager.getApplicationLabel(applicationInfo).toString();
    }

    /**
     * 获取应用图标
      * @return 应用图标
     */
    public Drawable getAppIcon() {
        return packageManager.getApplicationIcon(applicationInfo);
    }

    /**
     * 判断是否是系统应用
     * @return boolean
     */
    public boolean isSystemApp() {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
