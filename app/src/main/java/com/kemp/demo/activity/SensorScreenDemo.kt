package com.kemp.demo.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import com.kemp.annotations.Description
import com.kemp.annotations.Label
import com.kemp.demo.R
import com.kemp.demo.launchmode.LaunchModeActivity
import com.kemp.demo.utils.DebugLog
import kotlinx.android.synthetic.main.activity_sensor_screen.*
import org.jetbrains.anko.sdk27.coroutines.onClick

@Label("SensorScreenDemo")
@Description("横竖屏切换")
class SensorScreenDemo : LaunchModeActivity() {

    private var isPortrait = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_screen)



        btn.onClick {
            requestedOrientation = if (isPortrait) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isPortrait = !isPortrait
        }
    }

    private fun getSensor() {
        val s = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        DebugLog.d("onConfigurationChanged")
    }
}