package top.libreeze.path.forbid.bean;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    /**
     * 判断是否为第一次打开app
     * @return 是否是第一次
     */
    public boolean isFirstOpenApp() {
        return preference.getBoolean("isFirstOpenApp",true);
    }

    /**
     * 设置第一次打开app为false
     */
    public void notFirstOpenApp() {
        preference.edit().putBoolean("isFirstOpenApp",false).apply();
    }










    private static AppSettings appSettings;
    private final SharedPreferences preference;

    private AppSettings(SharedPreferences preferences) {
        this.preference = preferences;
    }

    public static synchronized AppSettings getInstance(Context context) {
        if (appSettings == null) {
            appSettings = new AppSettings(
                    context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            );
        }
        return appSettings;
    }

}
