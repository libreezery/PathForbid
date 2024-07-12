package top.libreeze.path.forbid.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import top.libreeze.path.forbid.App;

public class AppLogger {

    public static Logger logger;

    private static final Level logLevel = Level.ALL;

    static {
        logger = Logger.getLogger("app_logger");

        // 控制台
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setLevel(logLevel);
//        logger.addHandler(consoleHandler);

        // 文件
        try {
            FileHandler fileHandler = new FileHandler(getLogFile().getAbsolutePath(),true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(logLevel);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static File getLogFile() {
        return new File(App.getContext().getCacheDir(),"log.txt");
    }

    /**
     * log error info
     * @param msg toast msg
     * @param throwable 错误
     */
    public static void error(String msg, Throwable throwable) {
        logger.log(Level.SEVERE,msg,throwable);
    }

}
