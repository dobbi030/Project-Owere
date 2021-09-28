package com.blooburn.owere.activity.UserMain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.fragment.userMainFragment.browseFragment.BrowseFragment
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.chattingFragmet
import com.blooburn.owere.fragment.userMainFragment.homeFragment.UserHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        val homeFragment = UserHomeFragment()
        val browseFragment = BrowseFragment()
        val chattingFragment = chattingFragmet()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        replaceFragment(homeFragment)   // HomeFragment에서 시작한다

        // 바텀 네비게이션 클릭 리스너
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.browse -> replaceFragment(browseFragment)
                R.id.chatting -> replaceFragment(chattingFragment)
                R.id.reservation -> {}
                else -> {}
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