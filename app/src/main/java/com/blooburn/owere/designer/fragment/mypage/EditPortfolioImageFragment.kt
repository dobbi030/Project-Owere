package com.blooburn.owere.designer.fragment.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blooburn.owere.designer.activity.myPage.EditDesignerPortfolioActivity
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.DesignerPortfolioImageFragment
import com.google.firebase.storage.StorageReference

class EditPortfolioImageFragment(imagePath: StorageReference) : DesignerPortfolioImageFragment(imagePath) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.setOnClickListener{
            val intent = Intent(requireContext(), EditDesignerPortfolioActivity::class.java)
            startActivity(intent)
        }
    }
}