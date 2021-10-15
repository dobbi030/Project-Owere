package com.blooburn.owere.activity.userMain.homeActivity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.DesignerPortfolioSliderAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDesignerProfileActivity : AppCompatActivity() {

    private var designerId = ""
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private lateinit var designerReference: DatabaseReference
    private lateinit var portfolioReference: StorageReference

    private lateinit var sliderAdapter: DesignerPortfolioSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_designer_profile)

        // 수신 인텐트로 전달받은 디자이너 아이디 저장
        val extras = intent.extras ?: return
        designerId = extras.getString("designerId")?.let {
            // 디자이너의 데이터 참조
            designerReference = databaseReference.child("Designers/$it")
            // 디자이너 포트폴리오 이미지 참조
            portfolioReference = storageReference.child("portfolio/$it")
            it
        } ?: return

        val toolbar = findViewById<Toolbar>(R.id.toolbar_designer_profile)
        initToolbar(toolbar)

        val sliderViewPager = findViewById<ViewPager2>(R.id.view_pager_designer_profile)
        sliderAdapter = DesignerPortfolioSliderAdapter(this)
        sliderViewPager.adapter = sliderAdapter

        // 포트폴리오 이미지 레퍼런스들을 스토리지에서 가져온다
        // path = "portfolio/[디자이너 id]"
        loadPortfolioImagesPath()
    }

    /**
     * 포트폴리오 이미지 레퍼런스들을 스토리지에서 불러와서 어댑터에 전달한다.
     */
    private fun loadPortfolioImagesPath(){
        portfolioReference.listAll()
            .addOnSuccessListener {
                Log.d("ListResult.prefixes", "${it.prefixes}")
                Log.d("ListResult().pageToken", "${it.pageToken}")
                Log.d("ListResult().items", "${it.items}")

                sliderAdapter.setList(it.items)
                sliderAdapter.notifyDataSetChanged()
            }
    }

    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
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