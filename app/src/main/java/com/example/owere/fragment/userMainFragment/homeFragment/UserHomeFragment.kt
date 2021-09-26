package com.example.owere.fragment.userMainFragment.homeFragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.owere.R
import com.example.owere.adapter.UserHomeTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserHomeFragment: Fragment(R.layout.user_home_fragment) {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager2
    private val tabTitle = listOf("디자이너", "미용실")

    /**
     * tabLayout과 viewPager를 초기화하기 위해 사용했습니다.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        rootView?.let{
            tabLayout = it.findViewById(R.id.userHomeTabLayout)
            viewPager = it.findViewById(R.id.userHomeViewPager)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()
    }

    private fun initTabLayout(){
        repeat(2) {
            tabLayout.addTab(tabLayout.newTab())    // 탭 2개 생성
        }

        initViewPager()

        // TabLayout과 ViewPager 연결 + 탭 타이틀 지정
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun initViewPager(){
        viewPager.isSaveEnabled = false
        viewPager.adapter = UserHomeTabAdapter(this, tabLayout.tabCount)
    }
}