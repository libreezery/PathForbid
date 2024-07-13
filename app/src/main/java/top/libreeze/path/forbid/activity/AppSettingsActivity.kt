package top.libreeze.path.forbid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.fragment.AppSettingsFragment

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    private lateinit var settingsFragment: AppSettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        settingsFragment = AppSettingsFragment()

        // 显示设置
        supportFragmentManager.beginTransaction().apply {
            add(R.id.appSettingsFrameLayout,settingsFragment,"settings_fragment")
        }.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}