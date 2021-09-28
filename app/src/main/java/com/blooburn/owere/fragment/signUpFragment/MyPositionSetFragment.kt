package com.blooburn.owere.fragment.signUpFragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.activity.signUpActivity.SetPositionActivity
import com.blooburn.owere.databinding.MypositionSetFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPositionSetFragment : Fragment(R.layout.myposition_set_fragment) {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()


    private lateinit var bind: MypositionSetFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPostionSetFragmentBinding = MypositionSetFragmentBinding.bind(view)


        bind = myPostionSetFragmentBinding

        initMypositionSetButton()


    }

    override fun onResume() {
        super.onResume()

        val userId = auth.currentUser?.uid.toString()

        val currentUserDB1 = Firebase.database.reference.child("Users").child(userId).child("위도")
        val currentUserDB2 = Firebase.database.reference.child("Users").child(userId).child("경도")



    }


    private fun initMypositionSetButton() {
//      추가 버튼리스너
        bind.myPositionSetButton1.setOnClickListener {
            //추가 화면으로 인텐트를 통해 이동
            var intent =
                Intent(requireContext(), SetPositionActivity::class.java)

            startActivityForResult(intent, 100)
        }



        bind.myPositionSetButton2.setOnClickListener {

        }
        bind.mypositionKeepButton.setOnClickListener {

            requireActivity().finish()
        }

    }

    //주소 설정을 하였는지  위치 설정 액티비티로부터 콜백받음
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100 && resultCode == 100){
            bind.myPositionSetButton1.text = "우리집"
        }else{

        }
    }



}

