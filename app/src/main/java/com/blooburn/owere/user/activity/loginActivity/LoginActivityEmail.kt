package com.blooburn.owere.user.activity.loginActivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R


class LoginActivityEmail : AppCompatActivity() {

    private val cannotLogInButton: TextView by lazy { findViewById(R.id.cannot_login_button) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activiity_email)

        cannotLogInButton.setOnClickListener {
            var intent = Intent(this, LoginActivityRestore::class.java)
            startActivity(intent)
        }
    }
}