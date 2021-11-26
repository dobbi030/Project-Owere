package com.blooburn.owere.user.adapter.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.DesignerPortfolioImageFragment
import com.google.firebase.storage.StorageReference

open class DesignerPortfolioSliderAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    var imageReferenceList = mutableListOf<StorageReference>()

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
