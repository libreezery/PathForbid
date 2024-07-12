package top.libreeze.path.forbid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.topjohnwu.superuser.Shell;

import top.libreeze.path.forbid.database.AppDatabaseOpenHelper;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static AppDatabaseOpenHelper databaseOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Shell.enableVerboseLogging = true;
        Shell.setDefaultBuilder(Shell.Builder.create()
                .setFlags(Shell.FLAG_MOUNT_MASTER)
                .setTimeout(10));
        databaseOpenHelper = new AppDatabaseOpenHelper(this, "file_operate.db",null,1);
    }

    public static AppDatabaseOpenHelper getDatabaseOpenHelper() {
        return databaseOpenHelper;
    }

    /**
     * 获取全局上下文
     * @return 上下文
     */
    public static Context getContext() {
        return context;
    }
}
