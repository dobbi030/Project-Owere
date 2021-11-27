package com.blooburn.owere.user.activity.signUpActivity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.user.fragment.signUpFragment.ChooseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


//회원가입 버튼 누른 후 액티비티
// 고객, 디자이너를 선택해주세요

class SignUpActivity1_choice : AppCompatActivity() {

    private val chooseFragment = ChooseFragment()   //고객 디자이너 선택 프래그먼트

    private val backButton: ImageView by lazy {
        findViewById(R.id.signUp1_back_button)
    }

    private lateinit var auth: FirebaseAuth

    lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_activity1_choice)

        supportFragmentManager.beginTransaction()//트랜잭션을 연다. (작업을 시작한다고 알려주는 기능->commit까지 작업진행)
            .apply {
                replace(R.id.signUpFragmentContainer, chooseFragment) //초기 프래그먼트 부착
                commit()
            }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        backButton.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            finish()
        }
    }
}