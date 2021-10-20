package com.blooburn.owere.user.adapter.myPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.user.activity.main.mypageActivity.InterestActivity
import com.blooburn.owere.user.fragment.mainFragment.myPage.InterestDesignerFragment
import com.blooburn.owere.user.fragment.mainFragment.myPage.InterestShopFragment
import com.blooburn.owere.user.fragment.mainFragment.myPage.InterestStyleFragment

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