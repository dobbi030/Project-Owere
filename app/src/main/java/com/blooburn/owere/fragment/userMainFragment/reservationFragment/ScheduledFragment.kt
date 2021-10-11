package com.blooburn.owere.fragment.userMainFragment.reservationFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.UserHomeTabAdapter
import com.blooburn.owere.adapter.userReservation.ScheduledTabAdapter
import com.blooburn.owere.databinding.ScheduledReservationLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator

//예정된 예약
class ScheduledFragment : Fragment(R.layout.scheduled_reservation_layout) {


    private var binding : ScheduledReservationLayoutBinding? = null

    lateinit var tabAdapter: ScheduledTabAdapter
    private val tabTitle = listOf("확정된 예약", "대기중인 예약")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduledReservationLayoutBinding = ScheduledReservationLayoutBinding.bind(view)
        binding = scheduledReservationLayoutBinding

        initTabLayout()


    }

    private fun initTabLayout() {

        val tabLayout = binding?.tabLayoutScheduledReservation!!

        repeat(2){
            tabLayout.addTab(tabLayout.newTab())
        }

        initViewPager()
        // TabLayout과 ViewPager 연결 + 탭 타이틀 지정
        TabLayoutMediator(tabLayout, binding?.viewPagerScheduledReservation!!){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun initViewPager() {
        binding?.viewPagerScheduledReservation?.apply{
            isSaveEnabled = false
            tabAdapter = ScheduledTabAdapter(
                this@ScheduledFragment,
                binding?.tabLayoutScheduledReservation!!.tabCount
            )
            adapter = tabAdapter
        }
    }
}