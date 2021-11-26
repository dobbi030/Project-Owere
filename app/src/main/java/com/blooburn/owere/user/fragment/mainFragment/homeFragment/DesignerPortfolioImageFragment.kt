package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerPortfolioImageFragmentBinding
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference

open class DesignerPortfolioImageFragment(private val imagePath: StorageReference): Fragment(R.layout.designer_portfolio_image_fragment) {

    private var binding: DesignerPortfolioImageFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerPortfolioImageFragmentBinding.bind(view)
        Glide.with(this)
            .load(imagePath)
            .centerCrop()
            .error(R.drawable.icon_person_24)
            .into(binding?.imgViewDesignerProfile!!)
    }
}