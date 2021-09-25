package com.example.owere.fragment.signUpFragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.owere.R
import com.example.owere.databinding.PasswordInputFragmentBinding

class PasswordInputFragment : Fragment(R.layout.password_input_fragment) {

    private var binding: PasswordInputFragmentBinding? = null

    private var password: String? = null


    private var email : String? = null

    private var customerOrDesigner: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var nextFragment = MyNameInputFragment()

        val passwordInputFragmentBinding = PasswordInputFragmentBinding.bind(view)
        binding = passwordInputFragmentBinding


        setFragmentResultListener("email") { key, bundle ->
            bundle.getString("email")?.let {
                binding?.trasmitTest?.text = it
            }
            bundle.getString("customerOrDesigner")?.let {
                binding?.trasmitTest2?.text = it
            }

        }
        binding?.trasmitTest?.text = customerOrDesigner
        binding?.trasmitTest2?.text = email


        binding?.let {
            password = it.passwordEditText!!.text.toString()    //패스워드 텍스트 값
        }

        binding?.passwordSignupButton!!.setOnClickListener {


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.signUpFragmentContainer, nextFragment)
                .commit()
        }


    }


}