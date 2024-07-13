package top.libreeze.path.forbid.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.logging.Level;

import top.libreeze.path.forbid.App;
import top.libreeze.path.forbid.bean.FileOperateRecord;
import top.libreeze.path.forbid.utils.AppLogger;

public class FileOperateRecordDao {
    // 一切的操作都是在 Dao 这个类进行的
    private Dao<FileOperateRecord,Integer> DAO;
    public FileOperateRecordDao() {
        // 获取 Dao
        try {
            DAO = App.getDatabaseOpenHelper().getDao(FileOperateRecord.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            AppLogger.logger.log(Level.SEVERE, "Get dao fail:",throwables);
        }
    }

    /**
     * 根据路径进行寻找
     * @param path 文件路径
     * @return FileOperateRecord
     */
    public FileOperateRecord findByPath(String path) throws SQLException {
        QueryBuilder<FileOperateRecord, Integer> fileOperateRecordIntegerQueryBuilder = DAO.queryBuilder();
        fileOperateRecordIntegerQueryBuilder.where().eq("filepath",path);
        return fileOperateRecordIntegerQueryBuilder.queryForFirst();
    }

    public void add(FileOperateRecord record) {
        try {
            DAO.create(record);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            AppLogger.error("Add FileOperateRecord fail:", throwables);
        }
    }

    public void delete(FileOperateRecord record) {
        try {
            DAO.delete(record);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
