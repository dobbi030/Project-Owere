package com.example.owere.fragment.signUpFragment

import android.os.Bundle
import android.text.BoringLayout
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

import com.example.owere.R
import com.example.owere.databinding.EmailInputFragmentBinding

class EmailInputFragment : Fragment(R.layout.email_input_fragment) {


    private var binding: EmailInputFragmentBinding? = null

    private var email: String? = null

    private var nextFragment = PasswordInputFragment()
    private var bundle = Bundle()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInputFragmentBinding = EmailInputFragmentBinding.bind(view)


        val isUser: Boolean by lazy {
            requireArguments().getBoolean("isUser")
        }

        binding = emailInputFragmentBinding

        email = binding?.EmailEditText!!.text.toString()

        binding?.emailKeepButton?.setOnClickListener {



        }


            nextFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌

            bundle.putString("gmail",email)
            bundle.putBoolean("isUser", isUser)



            requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.signUpFragmentContainer, nextFragment)
            .commit()

    }


}





