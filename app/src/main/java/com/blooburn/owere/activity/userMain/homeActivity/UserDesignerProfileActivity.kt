package com.blooburn.owere.activity.userMain.homeActivity

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.DesignerPortfolioSliderAdapter
import com.blooburn.owere.item.UserDesignerItem
import com.blooburn.owere.item.UserReview
import com.blooburn.owere.util.DesignerProfileHandler
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDesignerProfileActivity : AppCompatActivity(), DesignerProfileHandler {

    private var designerData: UserDesignerItem? = null
    private val reviewImagePathList = mutableListOf<String>()

    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private lateinit var designerReference: DatabaseReference
    private lateinit var portfolioReference: StorageReference
    private lateinit var priceChartReference: DatabaseReference

    private lateinit var sliderAdapter: DesignerPortfolioSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_designer_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_user_designer_profile)
        initToolbar(toolbar)

        val sliderViewPager = findViewById<ViewPager2>(R.id.view_pager_user_designer_profile)
        sliderAdapter = DesignerPortfolioSliderAdapter(this)
        sliderViewPager.adapter = sliderAdapter

        initDataAndView() // 모든 뷰와 관련된 데이터 초기화 작업
    }


    private fun initDataAndView(){
        // 수신 인텐트로 전달받은 디자이너 정보 저장
        getDesignerDataFromIntent()
        setDataReferences() // 디자이너 아이디로 DB에서 데이터들 Reference 설정

        // 포트폴리오 이미지 레퍼런스들을 스토리지에서 가져온다
        // path = "portfolio/[디자이너 id]"
        getPortfolioImagesPath()
        setDesignerInformation()
        getAndSetReviewImages()
        getAndSetReviews()
    }

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun getDesignerDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        designerData = extras!!.getParcelable("designerData")   // DesignerData 객체 읽기
        if (designerData == null) {
            finish()
        }
    }

    /**
     * 디자이너 아이디로 DB Reference 설정
     */
    private fun setDataReferences() {
        // 디자이너의 데이터 참조
        designerReference = databaseReference.child("Designers/${designerData?.designerId}")
        // 디자이너 포트폴리오 이미지 참조
        portfolioReference = storageReference.child("portfolio/${designerData?.designerId}")
        // 가격표 참조
        priceChartReference =
            databaseReference.child("designerPriceChart/${designerData?.designerId}")
    }

    /**
     * 포트폴리오 이미지 레퍼런스들을 스토리지에서 불러와서 어댑터에 전달한다.
     */
    private fun getPortfolioImagesPath() {
        portfolioReference.listAll()
            .addOnSuccessListener {
                sliderAdapter.setList(it.items)
                sliderAdapter.notifyDataSetChanged()
            }
    }

    /**
     * 디자이너 프로필 set
     */
    private fun setDesignerInformation() {
        if (designerData == null) {
            return
        }

        val reviewStar = convertRatingToStar(designerData!!.rating)

        // 디자이너 프로필 보여주는 view
        findViewById<View>(R.id.view_user_designer_profile_information).apply {
            bindProfileImage(
                this,
                this.findViewById(R.id.image_user_designer),
                designerData!!.profileImagePath,
                true
            )
            this.findViewById<TextView>(R.id.text_user_designer_name).text =
                designerData!!.name
            this.findViewById<TextView>(R.id.text_user_designer_area).text =
                designerData!!.area
            this.findViewById<TextView>(R.id.text_user_designer_matching).text =
                getString(R.string.matching_rate, designerData?.matchingRate)
            this.findViewById<TextView>(R.id.text_user_designer_star).text = reviewStar
        }

        getAndSetIntroduction() // 디자이너 소개글

        // 리뷰 별점, 평점, 개수
        findViewById<TextView>(R.id.text_user_designer_profile_review_star).text = reviewStar
        findViewById<TextView>(R.id.text_user_designer_profile_review_rating).text =
            designerData!!.rating.toString()
        findViewById<TextView>(R.id.text_user_designer_profile_review_count).text =
            designerData!!.reviewCount.toString()
    }

    /**
     * 디자이너 소개글 불러와서 적용
     */
    private fun getAndSetIntroduction() {
        designerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 디자이너 소개글
                findViewById<TextView>(R.id.text_user_designer_profile_introduction).text =
                    snapshot.child("introduction").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 리뷰 이미지 경로 저장
     */
    private fun getAndSetReviewImages() {

        // 미리보기 리뷰 이미지 버튼을 담고 있는 parent layout
        val parentLayout =
            findViewById<ViewGroup>(R.id.layout_user_designer_profile_review_images)

        designerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 리뷰 이미지 경로
                snapshot.child("reviewImages").children.forEachIndexed { i, pathSnapshot ->

                    reviewImagePathList.add(pathSnapshot.value.toString())  // 이미지 경로 저장

                    // 이미지 적용
                    bindProfileImage(
                        parentLayout,
                        parentLayout.getChildAt(i) as ImageButton,
                        reviewImagePathList[i],
                        false
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 미리보는 리뷰 데이터를 가져와서 데이터 연결
     */
    private fun getAndSetReviews() {

        // 미리보기 리뷰를 담는 parent layout
        val parentLayout = findViewById<ViewGroup>(R.id.layout_user_designer_profile_reviews)

        designerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 미리보는 리뷰를 가져온다
                snapshot.child("reviews").children.forEachIndexed{ i, reviewSnapshot ->
                    val userReview = reviewSnapshot.getValue(UserReview::class.java) ?: return
                    val reviewItemView = parentLayout.getChildAt(i)

                    // 리뷰 view에 데이터 바인딩
                    bindReviewData(parentLayout, reviewItemView, userReview)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 리뷰 view에 데이터 바인딩
     */
    private fun bindReviewData(parentView: ViewGroup, reviewItemView: View, userReview: UserReview){
        reviewItemView.apply{
            val profileImageView = this.findViewById<ImageView>(R.id.image_item_review) // 유저 프로필 사진
            bindProfileImage(parentView, profileImageView, userReview.userImagePath, true)

            this.findViewById<TextView>(R.id.text_item_review_nickname).text = userReview.userName  // 유저 네임
            this.findViewById<TextView>(R.id.text_item_review_description).text = userReview.description // 후기
            this.findViewById<TextView>(R.id.text_item_review_dates).text = userReview.dates // 날짜
            this.findViewById<TextView>(R.id.text_item_review_rating).text = convertRatingToStar(userReview.rating) // 별점
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