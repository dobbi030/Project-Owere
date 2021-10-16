package com.blooburn.owere.adapter.myPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.activity.userMain.mypageActivity.InterestActivity
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingDesignerFragment
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingSalonFragment
import com.blooburn.owere.fragment.userMainFragment.myPage.InterestDesignerFragment
import com.blooburn.owere.fragment.userMainFragment.myPage.InterestShopFragment
import com.blooburn.owere.fragment.userMainFragment.myPage.InterestStyleFragment

class InterestTabAdapter(fragment: InterestActivity, private var tabCount: Int)
    : FragmentStateAdapter(fragment) {



    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InterestDesignerFragment()
            1->InterestShopFragment()
            else -> InterestStyleFragment()

        }
    }


}