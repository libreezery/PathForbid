package top.libreeze.path.forbid.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "file_operate_record")
public class FileOperateRecord {

    public FileOperateRecord() {
    }

    @DatabaseField(generatedId = true)
    private int id = 0;

    // 文件路径
    @DatabaseField
    private String filepath;

    // 文件名称
    @DatabaseField
    private String filename;

    // 应用包名
    @DatabaseField
    private String packageName;

    // 原始权限
    @DatabaseField
    private int originMode;

    // 当前权限
    @DatabaseField
    private int currentMode;

    public FileOperateRecord(String filepath, String filename, String packageName, int originMode, int currentMode) {
        this.filepath = filepath;
        this.filename = filename;
        this.packageName = packageName;
        this.originMode = originMode;
        this.currentMode = currentMode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getOriginMode() {
        return originMode;
    }

    public void setOriginMode(int originMode) {
        this.originMode = originMode;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }
}
