package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.PhoneInputFragmentBinding

class PhoneInputFragment : Fragment(R.layout.phone_input_fragment) {


    private var binding : PhoneInputFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneInputFragmentBinding = PhoneInputFragmentBinding.bind(view)

        binding = phoneInputFragmentBinding

    }
}