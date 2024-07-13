package top.libreeze.path.forbid.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.database.FileOperateRecordDao
import top.libreeze.path.forbid.utils.RootUtils

class AppSettingsFragment: PreferenceFragmentCompat() ,Preference.OnPreferenceClickListener{

    private val DAO:FileOperateRecordDao = FileOperateRecordDao()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_settings)
        findPreference<Preference>("settings_all_enabled")?.onPreferenceClickListener = this
        findPreference<Preference>("settings_project_address")?.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        preference.let {
            when {
                preference?.key.equals("settings_all_enabled") -> {
                    DAO.findAll().let {
                        for (record in  it) {
                            RootUtils.setFilePermission(record.originMode, record.filepath)
                        }
                        DAO.clearAll()
                        Toast.makeText(activity, "全部解除成功",Toast.LENGTH_SHORT).show()
                    }
                }
                preference?.key == "settings_project_address" -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.url_github)))
                    )
                }
                preference?.key == "settings_join_group" -> {
                    joinGroup()
                }
            }
        }
        return true
    }

    private fun joinQQGroup(key: String): Boolean {
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


    private fun joinGroup() {
        joinQQGroup("QN-Svg1br3W6TNCGDlpRxhS_yL-5zgEL")
    }

}