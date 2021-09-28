package com.blooburn.owere.activity.signUpActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.blooburn.owere.R
import com.blooburn.owere.fragment.signUpFragment.*

//회원가입 버튼 누른 후 액티비티
// 고객, 디자이너를 선택해주세요

class SignUpActivity1_choice : AppCompatActivity() {


    private val chooseFragment = ChooseFragment()   //고객 디자이너 선택 프래그먼트



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