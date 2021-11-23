package com.blooburn.owere.user.fragment.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.PhoneInputFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class PhoneInputFragment : Fragment(R.layout.phone_input_fragment) {


    private var binding : PhoneInputFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneInputFragmentBinding = PhoneInputFragmentBinding.bind(view)

        binding = phoneInputFragmentBinding

    }
}