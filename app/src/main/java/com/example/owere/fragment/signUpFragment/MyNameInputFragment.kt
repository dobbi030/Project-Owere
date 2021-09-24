package com.example.owere.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.owere.R
import com.example.owere.databinding.MynameInputFramentBinding

class MyNameInputFragment : Fragment(R.layout.myname_input_frament) {


    private var binding : MynameInputFramentBinding? = null

    private var myName : String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mynameInputFramentBinding = MynameInputFramentBinding.bind(view)

        binding = mynameInputFramentBinding
        myName = binding?.myNameEditText?.text.toString()

    }

    override fun onPause() {
        super.onPause()
        var lastFragment = MyPositionSetFragment()
        var bundle = Bundle()
        bundle.putString("myName",myName)

        lastFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
    }
}