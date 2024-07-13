package top.libreeze.path.forbid.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import top.libreeze.path.forbid.BaseActivity
import top.libreeze.path.forbid.R


class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /****************
     *
     * 发起添加群流程。群号：花开富贵-养老群(912514928) 的 key 为： QN-Svg1br3W6TNCGDlpRxhS_yL-5zgEL
     * 调用 joinQQGroup(QN-Svg1br3W6TNCGDlpRxhS_yL-5zgEL) 即可发起手Q客户端申请加群 花开富贵-养老群(912514928)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            false
        }
    }


    fun joinGroup(view: View) {
        joinQQGroup("QN-Svg1br3W6TNCGDlpRxhS_yL-5zgEL")
    }
    fun showGithub(view: View) {
        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("")))
    }
}