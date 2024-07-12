package top.libreeze.path.forbid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.logging.Level;

import top.libreeze.path.forbid.bean.FileOperateRecord;
import top.libreeze.path.forbid.utils.AppLogger;

public class AppDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {
    public AppDatabaseOpenHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FileOperateRecord.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            AppLogger.logger.log(Level.SEVERE,"Create database fail :", throwables);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }
}
