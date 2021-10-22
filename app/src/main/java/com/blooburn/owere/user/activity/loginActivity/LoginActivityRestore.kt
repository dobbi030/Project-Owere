package com.blooburn.owere.user.activity.loginActivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.blooburn.owere.R
import java.util.regex.Pattern


class LoginActivityRestore : AppCompatActivity() {
    private val submitButton: AppCompatButton by lazy { findViewById(R.id.continue_Button) }

    private val emailTextView: TextView by lazy { findViewById(R.id.input_email) }
    private val emailPattern: Pattern = android.util.Patterns.EMAIL_ADDRESS


    fun checkEmail(): Boolean {
        val email = emailTextView.text.toString().trim()
        val p = Pattern.matches(emailPattern.toString(), email)
        return if (p) {
            emailTextView.setTextColor(Color.BLACK)
            true
        } else {
            Log.d("restore", "되는 중")
            emailTextView.setTextColor(Color.RED)
            false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_restore)

        emailTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }
        })

        submitButton.setOnClickListener {
            if (!checkEmail()) { //틀린 경우
                Toast.makeText(applicationContext, "이메일 형식에 맞게 입력하세요!", Toast.LENGTH_LONG)
                    .show()
            } else { //맞는 경우
                val intent = Intent(this, LoginActivityCheckEmail::class.java)
                startActivity(intent)
            }
        }
    }
}
