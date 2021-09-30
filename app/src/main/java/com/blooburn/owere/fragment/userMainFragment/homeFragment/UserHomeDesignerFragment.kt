package com.blooburn.owere.fragment.userMainFragment.homeFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.UserHomeDesignerFragmentBinding

class UserHomeDesignerFragment: Fragment(R.layout.user_home_designer_fragment) {

    private var binding: UserHomeDesignerFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserHomeDesignerFragmentBinding.bind(view)

    }
}