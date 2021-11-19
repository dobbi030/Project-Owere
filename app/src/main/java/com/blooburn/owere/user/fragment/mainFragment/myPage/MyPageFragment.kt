package com.blooburn.owere.user.fragment.mainFragment.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.mypageActivity.*
import com.blooburn.owere.databinding.MypageFragmentLayoutBinding

//마이페이지 프래그먼트
class MyPageFragment : Fragment(R.layout.mypage_fragment_layout) {
    private var binding: MypageFragmentLayoutBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPageFragmentLayoutBinding = MypageFragmentLayoutBinding.bind(view)
        binding = myPageFragmentLayoutBinding


        initButton()


    }

    private fun initButton(){

        //내 정보 액티비티로 이동
        binding?.mypageMyinfoButton!!.setOnClickListener {
            var intent = Intent(requireContext(), MyInfoActivity::class.java)
            startActivity(intent)
        }
        //관심 액티비티로 이동
        binding?.mypageInterestButton!!.setOnClickListener {
            var intent = Intent(requireContext(), InterestActivity::class.java)
            startActivity(intent)
        }
        //공지사항 액티비티로 이동
        binding?.mypageNoticeContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(intent)
        }
        //환경설정 액티비티로 이동
        binding?.mypageSettingContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
        //알람설정 액티비티로 이동
        binding?.mypageAlarmsettingContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), AlarmSettingActivity::class.java)
            startActivity(intent)
        }

        //프로모션,이벤트 액티비티로 이동
        binding?.mypageEventContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), EventPromotionActivity::class.java)
            startActivity(intent)
        }

        //오월 소개 액티비티로 이동
        binding?.mypageIntroduceContainer!!.setOnClickListener {
            var intent = Intent(requireContext(), IntroduceActivity::class.java)
            startActivity(intent)

        }

    }
}


