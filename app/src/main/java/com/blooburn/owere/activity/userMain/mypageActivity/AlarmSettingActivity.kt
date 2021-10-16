package com.blooburn.owere.activity.userMain.mypageActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.blooburn.owere.R

class AlarmSettingActivity : AppCompatActivity() {

    private val gobackButton by lazy {
        findViewById<ConstraintLayout>(R.id.alarmsetting_goback_button)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting)

    }
}