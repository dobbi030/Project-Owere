package com.example.owere.fragment.signUpFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.owere.R

class MyPositionSetFragment : Fragment(R.layout.myposition_set_fragment){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isUser = arguments?.getBoolean("isUser")
        val email = arguments?.getString("email")
        val password = arguments?.getString("password")
        val myName = arguments?.getString("myName")

//        Log.d("isUser", isUser.toString())
//        Log.d("email", email.toString())
//        Log.d("password", password.toString())
//        Log.d("myName", myName.toString())


    }



}