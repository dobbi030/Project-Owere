package com.blooburn.owere.fragment.userMainFragment.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.activity.userMain.mypageActivity.*
import com.blooburn.owere.databinding.MypageFragmentLayoutBinding

class MyPageFragment : Fragment(R.layout.mypage_fragment_layout) {
    private var binding: MypageFragmentLayoutBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPageFragmentLayoutBinding = MypageFragmentLayoutBinding.bind(view)
        binding = myPageFragmentLayoutBinding


        binding?.mypageInterestButton!!.setOnClickListener {
            var intent = Intent(requireContext(), InterestActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageNoticeContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageSettingContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageAlarmsettingContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), AlarmSettingActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageEventContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), EventPromotionActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageIntroduceContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), IntroduceActivity::class.java)
            startActivity(intent)

        }
    }
}


