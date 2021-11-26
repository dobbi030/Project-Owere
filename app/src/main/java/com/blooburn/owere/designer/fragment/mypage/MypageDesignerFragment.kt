package com.blooburn.owere.designer.fragment.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.MypageDesignerFragmentLayoutBinding
import com.blooburn.owere.designer.activity.myPage.DesignerProfileActivity
import com.blooburn.owere.user.activity.main.mypageActivity.*

//디자이너 마이페이지 프래그먼트
class MypageDesignerFragment  : Fragment(R.layout.mypage_designer_fragment_layout) {
    private var binding: MypageDesignerFragmentLayoutBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPageDesignerFragmentBinding = MypageDesignerFragmentLayoutBinding.bind(view)
        binding = myPageDesignerFragmentBinding


        initButton()


    }

    private fun initButton(){


        //공지사항 액티비티로 이동
        binding?.mypageDesignerNoticeButton!!.setOnClickListener {
            var intent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(intent)
        }

        // 소개 페이지로 이동
        binding?.mypageDesignerProfileSettingButton?.setOnClickListener {
            val intent = Intent(requireContext(), DesignerProfileActivity::class.java)
            startActivity(intent)
        }
        binding?.mypageDesignerSettingContainer?.setOnClickListener {
            var intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }


    }
}


