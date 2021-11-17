package com.blooburn.owere.user.activity.loginActivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.blooburn.owere.R


class LoginActivityCheckEmail : AppCompatActivity() {

    private val button_go_back: AppCompatButton by lazy { findViewById(R.id.button_go_back) }
    private val button_use_another_email: TextView by lazy { findViewById(R.id.another_email) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_check_email)

        button_go_back.setOnClickListener {
            val intent = Intent(this, LoginActivityChoice::class.java)
            startActivity(intent)
        }

        button_use_another_email.setOnClickListener {
            val intent = Intent(this, LoginActivityRestore::class.java)
            startActivity(intent)
        }
    }
}