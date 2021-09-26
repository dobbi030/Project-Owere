package com.example.owere.fragment.signUpFragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.owere.R
import com.example.owere.databinding.MynameInputFramentBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyNameInputFragment : Fragment(R.layout.myname_input_frament) {


    private var binding : MynameInputFramentBinding? = null

    private var myName : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mynameInputFramentBinding = MynameInputFramentBinding.bind(view)

        binding = mynameInputFramentBinding

        initKeepButton()//계속하기버튼 메소드

        initEditText()  //빈 텍스트 예외처리



    }

    private fun initKeepButton(){
        //계속하기 버튼 클릭리스너
        binding?.mynameKeepButton?.setOnClickListener {

            myName = binding?.myNameEditText?.text.toString() //클릭 후 myname 입력 값 받아옴

//            DB 상태

//            root{
//                Users{
//                    userId:###
//                    myName: KoJeongMin
//                }
//            }

            val myName = myName.toString()
//            DB 사용
            //reference에서 Users라는 child를 선택
            val currentUserDB = Firebase.database.reference.child("Users").child(myName)
            val user = mutableMapOf<String, Any>()
            user["myName"] = myName
            currentUserDB.updateChildren(user)      //제일 상위에 DB 안에Users라는 List가 생기고 그안에
            // userId라는 항목으로 오브젝트가 생기고 그안에 user가 저장 유저는 userId를 가지고잇음
            //Users라는 키도 따로 빼둠.

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.signUpFragmentContainer, MyPositionSetFragment())
                .commit()
        }
    }



    //EditText 예외처리
    private fun initEditText(){

        binding?.let {
            it.mynameKeepButton.visibility = INVISIBLE
            it.mynameKeepButton.isEnabled = false
        }


        val editText = binding?.myNameEditText
        editText?.addTextChangedListener{
            var enabled = editText?.text.isNotEmpty()
            if(enabled){
                binding?.mynameKeepButton?.isEnabled = true
                binding?.mynameKeepButton?.visibility = VISIBLE
            }
        }

    }


}