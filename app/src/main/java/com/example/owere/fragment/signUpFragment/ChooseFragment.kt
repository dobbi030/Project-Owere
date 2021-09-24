package com.example.owere.fragment.signUpFragment

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.owere.R
import com.example.owere.databinding.ChooseFragmentBinding

class ChooseFragment : Fragment(R.layout.choose_fragment) {


    private var binding : ChooseFragmentBinding? = null

    private var isUser : Boolean = true //스위치 고객 눌림

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val chooseFragmentBinding = ChooseFragmentBinding.bind(view)
        binding = chooseFragmentBinding

        //고객 선택
        binding?.userButton?.setOnClickListener {
            binding?.userButton?.setTextColor(resources.getColor(R.color.white))
            binding?.userButton?.setBackgroundResource(R.drawable.item_bg_on)
            binding?.designerButton?.setTextColor(resources.getColor(R.color.black))
            binding?.designerButton?.setBackgroundResource(R.drawable.item_bg_off)
            isUser = true
        }
        //디자이너 선택
        binding?.designerButton?.setOnClickListener {
            binding?.userButton?.setTextColor(resources.getColor(R.color.black))
            binding?.userButton?.setBackgroundResource(R.drawable.item_bg_off)
            binding?.designerButton?.setTextColor(resources.getColor(R.color.white))
            binding?.designerButton?.setBackgroundResource(R.drawable.item_bg_on)
            isUser = false
        }

        binding?.chooseKeepButton?.setOnClickListener {
            var nextFragment = EmailInputFragment()
            var bundle = Bundle()
            bundle.putBoolean("isUser",isUser)

            nextFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
            Log.d("isUser", isUser.toString())

            activity?.supportFragmentManager!!.beginTransaction()//트랜잭션을 연다. (작업을 시작한다고 알려주는 기능->commit까지 작업진행)
                .apply {
                    replace(R.id.signUpFragmentContainer, nextFragment)
                    commit()
                }
        }


    }

    override fun onPause() {
        super.onPause()
        //회원가입 마지막 프래그먼트로 정보 전달

    }


}