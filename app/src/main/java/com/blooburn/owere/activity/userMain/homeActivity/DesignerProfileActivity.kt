package com.blooburn.owere.activity.userMain.homeActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.DesignerImageSliderAdapter

class DesignerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sliderViewPager = findViewById<ViewPager2>(R.id.view_pager_designer_profile)
        val sliderAdapter = DesignerImageSliderAdapter(this)
        sliderViewPager.adapter = sliderAdapter
    }
}