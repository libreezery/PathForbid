package top.libreeze.path.forbid.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
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

    /**
     * 获取全部的记录
     * @return List
     */
    public List<FileOperateRecord> findAll() {
        try {
            return DAO.queryForAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            AppLogger.error("获取全部记录失败:", throwables);
        }
        return null;
    }

    /**
     * 全部删除
     */
    public void clearAll() {
        DeleteBuilder<FileOperateRecord, Integer> fileOperateRecordIntegerDeleteBuilder = DAO.deleteBuilder();
        try {
            fileOperateRecordIntegerDeleteBuilder.where().isNotNull("filepath");
            fileOperateRecordIntegerDeleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            AppLogger.error("删除全部记录失败:", throwables);
        }
    }
}
