package com.example.owere.activity.signUpActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.owere.R
import com.example.owere.fragment.signUpFragment.*

//회원가입 버튼 누른 후 액티비티
// 고객, 디자이너를 선택해주세요

class SignUpActivity1_choice : AppCompatActivity() {


    private val chooseFragment = ChooseFragment()   //고객 디자이너 선택 프래그먼트

    private val phoneInputFragment = PhoneInputFragment()   //전화번호 입력 프래그먼트

    private val authInputFragment = AuthInputFragment()// 인증번호 입력 프래그먼트

    private val emailInputFragment = EmailInputFragment() // 이메일 입력 프래그먼트

    //private val passwordInputFragment = PasswordInputFragment() //비밀번호 입력 프래그먼트

    private val myNameInputFragment = MyNameInputFragment() //이름 입력 프래그먼트

    private val myPositionSetFragment = MyPositionSetFragment() //위치 입력 프래그먼트

    private val backButton : ImageView by lazy {
        findViewById(R.id.signUp1_back_button)
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_activity1_choice)



        supportFragmentManager.beginTransaction()//트랜잭션을 연다. (작업을 시작한다고 알려주는 기능->commit까지 작업진행)
            .apply {
                replace(R.id.signUpFragmentContainer, chooseFragment) //초기 프래그먼트 부착
                commit()
            }
















    }

}