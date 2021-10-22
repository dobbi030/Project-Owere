package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.user.adapter.home.UserHomeTabAdapter
import com.blooburn.owere.databinding.UserHomeFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserHomeFragment: Fragment(R.layout.user_home_fragment) {

    private var binding: UserHomeFragmentBinding? = null
    lateinit var tabAdapter: UserHomeTabAdapter
    private val tabTitle = listOf("디자이너", "미용실")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userHomeFragmentBinding = UserHomeFragmentBinding.bind(view)
        binding = userHomeFragmentBinding

        initTabLayout()
    }

    private fun initTabLayout(){
        val tabLayout = binding?.tabLayoutUserHome!!

        repeat(2) {
            tabLayout.addTab(tabLayout.newTab())    // 탭 2개 생성
        }

        initViewPager()

        // TabLayout과 ViewPager 연결 + 탭 타이틀 지정
        TabLayoutMediator(tabLayout, binding?.viewPagerUserHome!!){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun initViewPager(){
        binding?.viewPagerUserHome?.apply{
            isSaveEnabled = false
            tabAdapter = UserHomeTabAdapter(
                this@UserHomeFragment,
                binding?.tabLayoutUserHome!!.tabCount
            )
            adapter = tabAdapter
        }
    }
}