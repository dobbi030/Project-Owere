package com.blooburn.owere.adapter.userHome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.fragment.userMainFragment.homeFragment.DesignerPortfolioImageFragment
import com.google.firebase.storage.StorageReference

class DesignerPortfolioSliderAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var imageReferenceList = mutableListOf<StorageReference>()

    override fun getItemCount(): Int {
        return imageReferenceList.size
    }

    override fun createFragment(position: Int): Fragment {
        return DesignerPortfolioImageFragment(imageReferenceList[position])
    }

    fun setList(inputList: MutableList<StorageReference>){
        imageReferenceList = inputList
    }
}
