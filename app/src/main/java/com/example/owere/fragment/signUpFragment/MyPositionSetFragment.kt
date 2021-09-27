package com.example.owere.fragment.signUpFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.owere.R
import com.example.owere.activity.signUpActivity.SetPositionActivity
import com.example.owere.databinding.MypositionSetFragmentBinding

class MyPositionSetFragment : Fragment(R.layout.myposition_set_fragment){


    private lateinit var bind : MypositionSetFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPostionSetFragmentBinding = MypositionSetFragmentBinding.bind(view)

        bind = myPostionSetFragmentBinding


        initMypositionSetButton()









    }

    private fun initMypositionSetButton(){
//      추가 버튼리스너
        bind.myPositionSetButton1.setOnClickListener {
            //추가 화면으로 인텐트를 통해 이동
            var intent =Intent(requireContext(), SetPositionActivity::class.java)
            startActivity(intent)
        }



        bind.myPositionSetButton2.setOnClickListener {

        }
        bind.mypositionKeepButton.setOnClickListener {

        }

    }



}