package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ChooseFragmentBinding

//디자이너 or 고객인지 선택
class ChooseFragment : Fragment(R.layout.choose_fragment) {


    private var binding : ChooseFragmentBinding? = null

    private var userOrDesigner : String = "User" //스위치 고객 눌림

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
            userOrDesigner = "User"
        }
        //디자이너 선택
        binding?.designerButton?.setOnClickListener {
            binding?.userButton?.setTextColor(resources.getColor(R.color.black))
            binding?.userButton?.setBackgroundResource(R.drawable.item_bg_off)
            binding?.designerButton?.setTextColor(resources.getColor(R.color.white))
            binding?.designerButton?.setBackgroundResource(R.drawable.item_bg_on)
            userOrDesigner = "Designer"
        }

        binding?.chooseKeepButton?.setOnClickListener {


           val bundle = bundleOf("userOrDesigner" to "${userOrDesigner}") //어떤 키에 어떤 값으로 번들을 담겠다
           setFragmentResult("userOrDesigner", bundle) //request값을 가진 리스너에게 전송


            var nextFragment = EmailInputFragment()

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