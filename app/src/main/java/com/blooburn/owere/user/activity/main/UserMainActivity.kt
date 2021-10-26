package com.blooburn.owere.user.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.user.fragment.mainFragment.browseFragment.BrowseFragment
import com.blooburn.owere.user.fragment.mainFragment.chattingFragment.ChattingFragmet
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.UserHomeFragment
import com.blooburn.owere.user.fragment.mainFragment.myPage.MyPageFragment
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.ReservationFragment
import com.blooburn.owere.user.item.DatabaseChild.Companion.DB_USERS
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserMainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)


        //파이어베이스 인증 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        //로그인 되어 있지 않다면 종료
        if(auth.currentUser== null){
            finish()
        }

        val userDBReference = Firebase.database.reference.child(DB_USERS).child(auth.currentUser?.uid.toString())


        val homeFragment = UserHomeFragment()
        val browseFragment = BrowseFragment()
        val chattingFragment = ChattingFragmet()
        val reservationFragment = ReservationFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        replaceFragment(homeFragment)   // HomeFragment에서 시작한다

        // 바텀 네비게이션 클릭 리스너
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.browse -> replaceFragment(browseFragment)
                R.id.chatting -> replaceFragment(chattingFragment)
                R.id.reservation -> replaceFragment(reservationFragment)
                else -> replaceFragment(myPageFragment)
            }

            true
        }
    }

    /**
     * 프래그먼트 교체
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.userFragmentContainer, fragment)
                commit()
            }
    }
}