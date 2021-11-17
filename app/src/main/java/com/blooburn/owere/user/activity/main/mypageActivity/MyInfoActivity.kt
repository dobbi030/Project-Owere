package com.blooburn.owere.user.activity.main.mypageActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blooburn.owere.R
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

// 마이페이지 프래그먼트 -> 내 정보 액티비티
class MyInfoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private val gobackButton by lazy {
        findViewById<ImageView>(R.id.activity_myinfo_backbutton)
    }
    private val phoneText :TextView by lazy {
        findViewById(R.id.myinfo_activity_name_text)
    }
    private val name :TextView by lazy{
        findViewById(R.id.myinfo_activity_name_text)
    }
    private val gender : TextView by lazy {
        findViewById(R.id.myinfo_gender_text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)




        auth = FirebaseAuth.getInstance()


        //현재 유저의 전화번호 가져오기
        phoneText.text = "010 구글 로그인 구현 되면 가능010"
        //구글 로그인 구현하였을 때 사용할 것
        //phoneText.text = auth.currentUser?.phoneNumber.toString()

        //사용자 이름 가져오기
        name.text = " 구글 로그인 구현 되면 가능"
        //name.text= auth.currentUser?.displayName.toString()




        gobackButton.setOnClickListener {
            finish()
        }





    }
}