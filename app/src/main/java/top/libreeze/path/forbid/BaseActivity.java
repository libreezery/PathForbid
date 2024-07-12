package top.libreeze.path.forbid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.topjohnwu.superuser.Shell;

import top.libreeze.path.forbid.bean.AppSettings;

public class BaseActivity extends AppCompatActivity {

    protected AppSettings appSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSettings = AppSettings.getInstance(this);
    }

    /**
     * 提示消息
     * @param msg 消息内容
     */
    protected void toast(String msg) {
        runOnUiThread(()-> Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show());
    }





}
