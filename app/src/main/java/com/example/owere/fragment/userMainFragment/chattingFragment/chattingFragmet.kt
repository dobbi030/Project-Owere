package com.example.owere.fragment.userMainFragment.chattingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.owere.R
import com.example.owere.adapter.ChattingTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class chattingFragmet : Fragment(R.layout.layout_chatting_fragment){

    private lateinit var chattingTabLayout: TabLayout
    private lateinit var chattingViewPager : ViewPager2
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

        rootView?.let {
            chattingTabLayout = it.findViewById(R.id.chattingTabLayout)
            chattingViewPager = it.findViewById(R.id.chattingViewPager)
        }

        return rootView



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChattingTabLayout()

    }

    private fun initChattingTabLayout(){
        repeat(2){
            chattingTabLayout.addTab(chattingTabLayout.newTab()) //탭 2개 생성

        }

        initChattingViewPager()

        // TabLayout과 ViewPager 연결 + 탭 타이틀 지정
        TabLayoutMediator(chattingTabLayout, chattingViewPager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }

    private fun initChattingViewPager(){
        chattingViewPager.isSaveEnabled = false
        chattingViewPager.adapter = ChattingTabAdapter(this, chattingTabLayout.tabCount)
    }



}