package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.UserHomeSalonFragmentBinding

class UserHomeSalonFragment: Fragment(R.layout.user_home_salon_fragment) {

    private var binding: UserHomeSalonFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserHomeSalonFragmentBinding.bind(view)
    }
}