package top.libreeze.path.forbid.bean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import top.libreeze.path.forbid.utils.RootUtils;

public class AppFile {

    private final ArrayList<AppFile> childFile = new ArrayList<>();
    private AppFile parent;
    private boolean isDirectory = false;
    private long fileSize = 0;
    private String filename;
    private String filepath;
    private int permission;

    // 通过File类进行初始化
    public AppFile(String file) {
        init(file);
    }

    public AppFile(String file, AppFile parent) {
        init(file);
        this.parent = parent;
    }

    // 初始化信息
    public void init(String file) {
        String info = RootUtils.obtainFileInfo(file);
        if (info.isEmpty())
            return;
        // drwx------ directory /data/user/0/com.huluxia.gametools 4096 1720085072 1720699446
        String[] strings = info.split("<>");
        // 获取权限
        permission = Integer.parseInt(strings[0]);
        // 获取是否是文件夹
        isDirectory = "directory".equalsIgnoreCase(strings[1]);
        // 获取文件路径
        filepath = strings[2];
        String[] split = filepath.split("/");
        // 获取文件名称
        if (filepath.endsWith("/")) {
            filename = split[split.length - 2];
        } else {
            filename = split[split.length - 1];
        }
        // 获取文件大小
        fileSize = RootUtils.getFileSize(filepath);
        if (isDirectory) {
            fileSize -= 4;
        }

    }

    /**
     * 返回文件
     *
     * @return success
     */
    public AppFile getParent() {
        return parent;
    }


    // 获取子目录所有文件大小
    public long getChildFileSize() {
        long filesize = 0;
        if (isDirectory) {
            for (AppFile appFile : childFile) {
                filesize += appFile.fileSize;
            }
            filesize += 1;
        }
        return filesize;
    }

    // 获取子文件
    private ArrayList<AppFile> getChildFiles(AppFile file) {
        ArrayList<AppFile> appFiles = new ArrayList<>();
        if (file.isDirectory()) {
            appFiles.addAll(RootUtils.listFiles(file));
        }
        return appFiles;
    }

    /**
     * 主动获取子文件
     */
    public void obtainChild() {
        this.childFile.clear();
        this.childFile.addAll(getChildFiles(this));
        Collections.sort(childFile, (o1, o2) -> o1.fileSize > o2.fileSize ? -1 : 0);
    }

    public int getPermission() {
        return permission;
    }

    /**
     * 加载当前文件夹所有文件夹的大小
     */
    public void loadAllFileSize() {
        if (isDirectory) {
            // 加载子文件
            if (childFile.isEmpty()) {
                obtainChild();
            }
            for (AppFile file : childFile) {
                file.loadAllFileSize();
            }
            fileSize = getChildFileSize();
        }

    }

    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * 获取兆字节
     *
     * @return MB 单位大小
     */
    public String getMebibyte() {
        int kbytes = 1024; // KB
        int bytes = 1024 * 1024; // MB
        if (fileSize < kbytes) {
            return fileSize + "B";
        }
        if (fileSize < bytes) {
            double v = fileSize * 1.0 / kbytes;
            return getFormatedFileSize(v) + "K";
        }
        double v = fileSize * 1.0 / bytes;
        return getFormatedFileSize(v) + "M";
    }

    private String getFormatedFileSize(double v) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        return numberInstance.format(v);
    }

    public ArrayList<AppFile> getChildFile() {
        if (childFile.isEmpty()) {
            obtainChild();
        }
        return childFile;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
