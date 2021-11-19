package com.blooburn.owere.designer.activity.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.designer.fragment.chatting.ChattingForDesignerFragment
import com.blooburn.owere.designer.fragment.home.DesignerHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DesignerMainActivity : AppCompatActivity() {

    private val homeFragment: DesignerHomeFragment by lazy{
        DesignerHomeFragment()
    }
    //채팅 프래그먼트
    private val chattingFragment: ChattingForDesignerFragment by lazy{
        ChattingForDesignerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_main)

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation_view_designer_main)
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewItemClickListener)
    }

    /**
     * 프래그먼트 교체
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragment_container_view_designer_main, fragment)
                commit()
            }
    }

    private val bottomNavigationViewItemClickListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId){
                R.id.designer_home -> replaceFragment(homeFragment)
                R.id.designer_browse -> {}
                R.id.designer_chatting -> replaceFragment(chattingFragment)
                R.id.designer_recruit -> {}
                else -> {}
            }

            true
        }
}