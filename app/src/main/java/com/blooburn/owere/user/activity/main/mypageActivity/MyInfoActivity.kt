package com.blooburn.owere.user.activity.main.mypageActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.blooburn.owere.R

// 마이페이지 -> 내 정보 액티비티
class MyInfoActivity : AppCompatActivity() {

    private val gobackButton by lazy {
        findViewById<ConstraintLayout>(R.id.goback_button)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        gobackButton.setOnClickListener {
            finish()
        }





    }
}