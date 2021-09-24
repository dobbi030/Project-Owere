package com.example.owere.fragment.signUpFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.owere.R
import com.example.owere.databinding.PasswordInputFragmentBinding

class PasswordInputFragment : Fragment(R.layout.password_input_fragment) {

    private var binding : PasswordInputFragmentBinding? = null

    private var password : String? = null


    private val email by lazy {
        arguments?.getString("gmail")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        Log.d("gmail", email.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.test?.text = email.toString()

        val passwordInputFragment = PasswordInputFragmentBinding.bind(view)
        binding = passwordInputFragment


        binding?.let {
            password = it.passwordEditText!!.text.toString()


        }


    }

    override fun onPause() {
        super.onPause()
        //회원가입 마지막 프래그먼트로 정보 전달
        var lastFragment = MyPositionSetFragment()
        var bundle = Bundle()
        bundle.putString("password",password)

        lastFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
    }


}