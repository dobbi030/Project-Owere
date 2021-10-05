package com.blooburn.owere.fragment.userMainFragment.homeFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerImageSlideFragmentBinding

class DesignerImageSlideFragment(): Fragment(R.layout.designer_image_slide_fragment) {

    private var binding: DesignerImageSlideFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerImageSlideFragmentBinding.bind(view)
        // binding?.imageViewDesignerProfile?.setImageResource(image)
    }
}