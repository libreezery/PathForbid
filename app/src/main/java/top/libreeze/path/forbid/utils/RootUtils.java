package top.libreeze.path.forbid.utils;

import com.topjohnwu.superuser.Shell;

import java.util.ArrayList;
import java.util.List;

import top.libreeze.path.forbid.App;
import top.libreeze.path.forbid.bean.AppFile;

public class RootUtils {

    /**
     * 扫描文件夹下所有文件
     *
     * @param filepath 文件夹路径
     * @return 文件列表
     */
    public static List<AppFile> listFiles(String filepath, AppFile parent) {
        ArrayList<AppFile> appFiles = new ArrayList<>();
        if (Shell.getShell().isRoot()) {
            Shell.Result result = Shell.cmd(toybox() + " ls " + filepath).exec();
            if (result.isSuccess()) {
                // 执行ls结果
                if (!result.getOut().isEmpty()) {
                    if (!filepath.endsWith("/")) {
                        filepath = filepath + "/";
                    }
                    for (String filename : result.getOut()) {
                        String actual_path = filepath + filename;
                        if (parent != null) {
                            appFiles.add(new AppFile(actual_path, parent));
                            continue;
                        }
                        appFiles.add(new AppFile(actual_path));
                    }
                }
            }
        }
        return appFiles;
    }

    public static List<AppFile> listFiles(AppFile appFile) {
        return listFiles(appFile.getFilepath(), appFile);
    }

    /**
     * 获取文件信息
     *
     * @param filepath 文件路径
     * @return String
     */
    public static String obtainFileInfo(String filepath) {
        if (Shell.getShell().isRoot()) {
            Shell.Result exec = Shell.cmd(toybox() + " stat -c '%a<>%F<>%n<>%s<>%X<>%Y' " + filepath).exec();
            if (exec.isSuccess()) {
                // 成功则返回消息
                if (!exec.getOut().isEmpty()) {
                    return exec.getOut().get(0);
                }
            }
        }
        return "";
    }

    /**
     * 获取设备cpu架构
     *
     * @return cpu架构
     */
    public static String obtainCpuAbi() {
        if (Shell.getShell().isRoot()) {
            Shell.Result result = Shell.cmd("getprop ro.product.cpu.abi").exec();
            if (result.isSuccess() || result.getOut().isEmpty()) {
                return result.getOut().get(0);
            }
        }
        return "arm-v7a";
    }

    /**
     * 设置文件夹权限为 500
     *
     * @param filepath 文件路径
     * @return 是否成功
     */
    public static boolean setFilePermission(int permi, String filepath) {
        if (Shell.getShell().isRoot()) {
            Shell.Result exec = Shell.cmd("chmod " + permi + " " + filepath).exec();
            return exec.isSuccess();
        }
        return false;
    }

    /**
     * 使用命令获取文件大小
     *
     * @param path 文件路径
     * @return 大小
     */
    public static long getFileSize(String path) {
        if (Shell.getShell().isRoot()) {
            Shell.Result exec = Shell.cmd(toybox() + " du -sb " + path).exec();
            if (exec.isSuccess() && !exec.getOut().isEmpty()) {
                String s = exec.getOut().get(0);
                return Long.parseLong(s.split("\t")[0]);
            }
        }
        return 0;
    }

    public static String toybox() {
        return App.getContext().getFilesDir().getAbsolutePath() + "/toybox";
    }

    /**
     * 删除文件夹或文件
     * @param path 文件路径
     * @return 是否成功
     */
    public static boolean deleteFile(String path) {
        if (Shell.getShell().isRoot()) {
            Shell.Result result = Shell.cmd(toybox() + " rm -rf " + path).exec();
            return result.isSuccess();
        }
        return false;
    }
}
