package com.blooburn.owere.user.activity.loginActivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.google.android.gms.common.SignInButton

class LoginActivityChoice : AppCompatActivity() {

    private val logInButton: SignInButton by lazy {
        findViewById(R.id.email_Button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_choice)

        logInButton.setOnClickListener {
            //회원가입 액티비티로 이동하는 인텐트
            val intent = Intent(this, LoginActivityEmail::class.java)
            startActivity(intent)
        }
    }
}