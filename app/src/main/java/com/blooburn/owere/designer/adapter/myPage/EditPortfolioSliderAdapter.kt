package com.blooburn.owere.designer.adapter.myPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blooburn.owere.designer.fragment.myPage.EditPortfolioImageFragment
import com.blooburn.owere.user.adapter.home.DesignerPortfolioSliderAdapter

class EditPortfolioSliderAdapter(fragmentActivity: FragmentActivity) :
    DesignerPortfolioSliderAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return EditPortfolioImageFragment(this.imageReferenceList[position])
    }
}