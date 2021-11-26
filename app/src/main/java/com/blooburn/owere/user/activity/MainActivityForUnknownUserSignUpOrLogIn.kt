package com.blooburn.owere.user.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.loginActivity.LoginActivityTemp
import com.blooburn.owere.user.activity.signUpActivity.SignUpActivity1_choice

//회원가입 로그인 선택 화면
class MainActivityForUnknownUserSignUpOrLogIn : AppCompatActivity() {

    //회원가입 이동 버튼 초기화
    private val signUpButton: TextView by lazy {
        findViewById(R.id.signUp_Button)
    }

    //로그인 이동 버튼
    private val logInButton: TextView by lazy {
        findViewById(R.id.login_Button)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_for_unknown_user_sign_up_or_log_in)

        //회원가입 버튼 리스너
        signUpButton.setOnClickListener {
            //회원가입 액티비티로 이동하는 인텐트
            var intent = Intent(this, SignUpActivity1_choice::class.java)
            startActivity(intent)
        }

        //로그인 버튼 리스너
        logInButton.setOnClickListener {
            //로그인 액티비티로 이동하는 인텐트
            var intent = Intent(this, MainActivityForUnknownUser::class.java)
            // var intent = Intent(this, LoginActivity_choice::class.java)
//                getAppKeyHash()
            startActivity(intent)
        }

    }
}


//디버깅 해시키 받을 때 사용
//       fun getAppKeyHash() {
//                try {
//                    val info =
//                        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//                    for (i in info.signatures) {
//                        val md: MessageDigest = MessageDigest.getInstance("SHA")
//                        md.update(i.toByteArray())
//
//                        val something: String = String(Base64.encode(md.digest(), 0))
//                        Log.e("Debug key", something)
//                    }
//                } catch (e: Exception) {
//                    Log.e("Not found", e.toString())
//                }
//            }
//        getAppKeyHash()