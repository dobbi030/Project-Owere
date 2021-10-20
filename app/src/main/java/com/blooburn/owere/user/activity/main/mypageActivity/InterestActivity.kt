package com.blooburn.owere.user.activity.main.mypageActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.user.adapter.myPage.InterestTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class InterestActivity : AppCompatActivity() {

    private lateinit var interestTabLayout: TabLayout
    private lateinit var interestViewPager : ViewPager2
    private val tabTitle = listOf("디자이너","미용실", "스타일")

    /**
     * tabLayout과 viewPager를 초기화하기 위해 사용했습니다.
     */



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)


        interestTabLayout = findViewById(R.id.myInterestTabLayout)
        interestViewPager = findViewById(R.id.myInterestViewPager)

        initInterestTabLayout()

    }


    private fun initInterestTabLayout(){
        repeat(3){
            interestTabLayout.addTab(interestTabLayout.newTab()) //탭 3개 생성

        }

        initInterestViewPager()

        // TabLayout과 ViewPager 연결 + 탭 타이틀 지정
        TabLayoutMediator(interestTabLayout, interestViewPager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }

    private fun initInterestViewPager() {
        interestViewPager.isSaveEnabled = false
        interestViewPager.adapter = InterestTabAdapter(this, interestTabLayout.tabCount)
    }
}