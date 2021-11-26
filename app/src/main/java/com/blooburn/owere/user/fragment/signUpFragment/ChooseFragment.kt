package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ChooseFragmentBinding
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

//디자이너 or 고객인지 선택
class ChooseFragment : Fragment(R.layout.choose_fragment) {

    // Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth

    lateinit var currentUid: DatabaseReference

    private var binding: ChooseFragmentBinding? = null

    private var userOrDesigner: String = "User" //스위치 고객 눌림

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

            // 사용자 가입 모드(고객 또는 디자이너) 적용
            val user = auth.currentUser
            if (user != null) {
                currentUid = databaseInstance.getReference("Uids").child(user.uid)
            }

            if (userOrDesigner == "User") currentUid.setValue("User")
            if (userOrDesigner == "Designer") currentUid.setValue("Designer")

            // 구글 로그인만 활성화 되어 있어 이메일, 비밀번호 입력 불필요
            val nextFragment = MyNameInputFragment()

            activity?.supportFragmentManager!!.beginTransaction()//트랜잭션을 연다. (작업을 시작한다고 알려주는 기능->commit까지 작업진행)
                .apply {
                    replace(R.id.signUpFragmentContainer, nextFragment)
                    commit()
                }

            /**
             * 이메일 로그인 활성화 시 이메일 입력 프레그먼트 코드
            var nextFragment = EmailInputFragment()

            activity?.supportFragmentManager!!.beginTransaction()//트랜잭션을 연다. (작업을 시작한다고 알려주는 기능->commit까지 작업진행)
            .apply {
            replace(R.id.signUpFragmentContainer, nextFragment)
            commit()
            }
             */

        }


    }

    override fun onPause() {
        super.onPause()
        //회원가입 마지막 프래그먼트로 정보 전달

    }


}