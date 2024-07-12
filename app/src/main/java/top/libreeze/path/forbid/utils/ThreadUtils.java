package top.libreeze.path.forbid.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.Call;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    private static final ExecutorService executors = Executors.newFixedThreadPool(5);

    private static final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Runnable obj = (Runnable) msg.obj;
            obj.run();
        }
    };

    /**
     * 提交一个正常线程
     * @param runnable 运行参数
     */
    public static synchronized void submit(Runnable runnable) {
        executors.submit(runnable);
    }

    /**
     * 运行 ui 线程
     * @param runnable  运行参数
     */
    public static synchronized void runUiThread(Runnable runnable) {
        Message message = new Message();
        message.obj = runnable;
        handler.sendMessage(message);
    }

    public static ExecutorService getExecutors() {
        return executors;
    }

    public static Handler getHandler() {
        return handler;
    }
}
