package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.blooburn.owere.R
import com.blooburn.owere.databinding.EmailInputFragmentBinding

class EmailInputFragment : Fragment(R.layout.email_input_fragment) {


    private var binding: EmailInputFragmentBinding? = null

    private var email: String? = null


    private var userOrDesigner: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val nextFragment = PasswordInputFragment()  //다음 프래그먼트

        val emailInputFragmentBinding = EmailInputFragmentBinding.bind(view)

        binding = emailInputFragmentBinding



        setFragmentResultListener("userOrDesigner") { key, bundle ->
            bundle.getString("userOrDesigner")?.let {
                userOrDesigner = it
            }
        }

        binding?.emailKeepButton?.setOnClickListener {
            email = binding?.EmailEditText!!.text.toString()




            var bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("userOrDesigner", userOrDesigner)

//
            setFragmentResult("email",bundle)   //email 키를 가진 리스너에게 전달

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.signUpFragmentContainer, nextFragment)
                .commit()


        }

        //리시브 프래그먼트(정보 받음)
        //request라는 이름으로 송신하면 동작하는 리스너
//        setFragmentResultListener("request"){key, bundle ->
//            bundle.getString("senderKey")?.let{
//
//                binding?.trasmitTest?.text = it
//            }
//        }


        val isUser: Boolean by lazy {
            requireArguments().getBoolean("isUser")
        }














    }


}





