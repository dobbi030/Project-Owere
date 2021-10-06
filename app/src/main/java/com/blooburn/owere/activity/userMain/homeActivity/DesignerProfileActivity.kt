package com.blooburn.owere.activity.userMain.homeActivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.DesignerImageSliderAdapter

class DesignerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_designer_profile)
        initToolbar(toolbar)

        val sliderViewPager = findViewById<ViewPager2>(R.id.view_pager_designer_profile)
        val sliderAdapter = DesignerImageSliderAdapter(this)
        // sliderViewPager.adapter = sliderAdapter
    }

    private fun initToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.apply{
            this.setDisplayShowTitleEnabled(false)  // 앱 타이틀 제거
            this.setDisplayHomeAsUpEnabled(true)    // 뒤로가기 버튼
        }

        // 뒤로가기 버튼 리스너
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    // 커스텀 툴바 메뉴 적용
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_designer_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* 툴바 오른쪽 아이콘 클릭 리스너 -> 나중에 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    */
}