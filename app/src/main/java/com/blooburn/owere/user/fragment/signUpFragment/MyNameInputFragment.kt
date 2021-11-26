package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.blooburn.owere.R
import com.blooburn.owere.databinding.MynameInputFramentBinding
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyNameInputFragment : Fragment(R.layout.myname_input_frament) {


    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var binding : MynameInputFramentBinding? = null
    private val databaseReference = databaseInstance.reference

    private var myName : String? = null
    private var userOrDesigner: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mynameInputFramentBinding = MynameInputFramentBinding.bind(view)

        binding = mynameInputFramentBinding

        //디자이너 유저 선택 여부 가져오기
        getInfoUserOrDesigner()

        initKeepButton()//계속하기버튼 메소드

        initEditText()  //빈 텍스트 예외처리



    }
    private fun sendInfoUserOrDesigner(){
        val bundle = bundleOf("userOrDesigner" to "${userOrDesigner}") //어떤 키에 어떤 값으로 번들을 담겠다
        setFragmentResult("userOrDesigner", bundle) //request값을 가진 리스너에게 전송
    }


    /**
     * 디자이너or유저 선택 여부
     */
    private fun getInfoUserOrDesigner(){
        //
        setFragmentResultListener("userOrDesigner") { key, bundle ->
            bundle.getString("userOrDesigner")?.let {
                userOrDesigner = it
                Log.d("userDesigner", "$userOrDesigner")
            }
        }
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
            val userId = auth.currentUser?.uid.orEmpty()
//            DB 사용
            //reference에서 Users라는 child를 선택
            //유저인 경우
            if (userOrDesigner == "User"){
                val currentUserDB1 = Firebase.database.reference.child("Users").child(userId).child("userId")
                val currentUserDB2 = Firebase.database.reference.child("Users").child(userId).child("myName")
                val user = mutableMapOf<String, Any>()

                currentUserDB1.setValue(userId)
                currentUserDB2.setValue(myName)
//            val user = mutableMapOf<String, Any>()
//            user["myName"] = myName
//            user["userId"] = userId
//            currentUserDB1.updateChildren(user)
//            currentUserDB2.updateChildren(user)//제일 상위에 DB 안에Users라는 List가 생기고 그안에
                // userId라는 항목으로 오브젝트가 생기고 그안에 user가 저장 유저는 userId를 가지고잇음
                //Users라는 키도 따로 빼둠.

            }else if(userOrDesigner == "Designer"){
                //디자이너인 경우 디자이너 이름과ID DB에 추가
                    var designerUpdate = mutableMapOf<String,Any>()
                designerUpdate["designerId"] = userId
                designerUpdate["name"] = myName

                var DBReference = databaseReference.child("Designers").child(userId)

                DBReference.updateChildren(designerUpdate)


            }
            //다음 프래그먼트로 디자이너 유저 여부 전송
            sendInfoUserOrDesigner()


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