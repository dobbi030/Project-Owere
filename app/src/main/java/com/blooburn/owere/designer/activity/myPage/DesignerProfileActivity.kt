package com.blooburn.owere.designer.activity.myPage

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
import com.blooburn.owere.databinding.ItemPriceMenuBinding
import com.blooburn.owere.user.adapter.home.DesignerPortfolioSliderAdapter
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserReview
import com.blooburn.owere.util.DesignerProfileHandler
import com.blooburn.owere.util.databaseInstance
import com.blooburn.owere.util.storageInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class DesignerProfileActivity : AppCompatActivity(), DesignerProfileHandler {

    private var favoriteCountTextView: TextView? = null
    private val tempDesignerId = "designer0"
    private val DESIGNERS_REFERENCE_PATH = "Designers"
    private val PRICECHART_REFERENCE_PATH = "designerPriceChart"
    private var designerItem: DesignerItem? = null
    private val reviewImagePathList = mutableListOf<String>()
    private val reviewList = mutableListOf<UserReview>()

    private var portfolioSliderAdapter: DesignerPortfolioSliderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_profile)
        initToolbar(findViewById(R.id.toolbar_designer_profile))
        initPortfolioViewPager()

        fetchPortfolioImages()
        getAndSetDesignerProfileFromDB()
    }

    private fun getAndSetDesignerProfileFromDB() {
        databaseInstance.reference.child("$DESIGNERS_REFERENCE_PATH/$tempDesignerId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    designerItem = snapshot.getValue(DesignerItem::class.java)

                    fetchReviewImagePathList(snapshot)    // 미리보는 리뷰 이미지 주소 저장
                    fetchReviewList(snapshot)   // 미리보는 리뷰 객체 저장
                    initDesignerProfile()   // 전체 UI뷰 초기화
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fetchReviewImagePathList(snapshot: DataSnapshot) {
        val genericTypeIndicator = object : GenericTypeIndicator<List<String>>() {}
        snapshot.child("reviewImages").getValue(genericTypeIndicator)?.let {
            reviewImagePathList.addAll(it)
        }
    }

    private fun fetchReviewList(snapshot: DataSnapshot){
        val genericTypeIndicator = object : GenericTypeIndicator<List<UserReview>>() {}
        snapshot.child("reviews").getValue(genericTypeIndicator)?.let {
            reviewList.addAll(it)
        }
    }

    /**
     * TODO UserDesignerProfileActivity에서 중복
     */
    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(false)  // 앱 타이틀 제거
            this.setDisplayHomeAsUpEnabled(true)    // 뒤로가기 버튼
        }

        // 뒤로가기 버튼 리스너
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * 커스텀 툴바 메뉴 적용
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_designer_profile, menu)
        menu?.let { favoriteCountTextView = findFavoriteCountTextView(it) }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 툴바에 있는 좋아요 횟수 TextView
     */
    private fun findFavoriteCountTextView(menu: Menu) =
        menu.findItem(R.id.action_designer_profile_favorite)?.actionView?.findViewById<TextView>(
            R.id.text_view_favorite_count
        )

    /* TODO 툴바 아이콘 클릭 리스너 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    */

    private fun initPortfolioViewPager() {
        portfolioSliderAdapter = DesignerPortfolioSliderAdapter(this)
        findViewById<ViewPager2>(R.id.view_pager2_designer_profile).apply {
            adapter = portfolioSliderAdapter
        }
    }

    /**
     * 포트폴리오 이미지 레퍼런스(path)를 모두 가져온다
     */
    private fun fetchPortfolioImages() {
        if (portfolioSliderAdapter == null) return

        storageInstance.reference.child("portfolio/$tempDesignerId")
            .listAll().addOnSuccessListener {
                portfolioSliderAdapter!!.setList(it.items)
                portfolioSliderAdapter!!.notifyDataSetChanged()
            }
    }

    /**
     * 디자이너 프로필 정보 binding
     */
    private fun initDesignerProfile() {
        if (designerItem == null) {
            return
        }

        val reviewStar = convertRatingToStar(designerItem!!.rating)

        // 디자이너 프로필 위젯들 초기화
        bindProfileImage(
            window.decorView,
            findViewById(R.id.img_view_designer_profile_self),
            designerItem!!.profileImagePath,
            true
        )
        findViewById<TextView>(R.id.txt_view_designer_profile_name).text =
            designerItem!!.name
        findViewById<TextView>(R.id.txt_view_designer_profile_area).text =
            designerItem!!.area
        findViewById<TextView>(R.id.txt_view_designer_profile_matching).text =
            getString(R.string.matching_rate, designerItem!!.matchingRate)
        findViewById<TextView>(R.id.txt_view_designer_profile_star).text = reviewStar
        findViewById<TextView>(R.id.txt_view_designer_profile_introduction).text =
            designerItem!!.introduction

        // 리뷰 별점, 평점, 개수
        findViewById<TextView>(R.id.txt_view_designer_profile_review_star).text = reviewStar
        findViewById<TextView>(R.id.txt_designer_profile_review_rating).text =
            designerItem!!.rating.toString()
        findViewById<TextView>(R.id.txt_designer_profile_review_count).text =
            designerItem!!.reviewCount.toString()

        initPriceChart()
        initReviewImages()
        initReviewList()
    }

    /**
     * 미리보는 가격표 불러와서 저장
     */
    private fun initPriceChart() {
        databaseInstance.reference.child("$PRICECHART_REFERENCE_PATH/$tempDesignerId/커트")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    findViewById<TextView>(R.id.txt_designer_profile_price_title).text =
                        snapshot.key

                    // 서브 메뉴 개수 만큼 동적으로 view를 생성해서 추가
                    snapshot.children.forEach {
                        val menuItem = it.getValue(StyleMenuItem::class.java) ?: return

                        // LayoutInflater를 사용해서 메뉴 동적 생성 and 뷰바인딩 사용
                        val menuViewBinding = ItemPriceMenuBinding.inflate(
                            layoutInflater,
                            findViewById(R.id.layout_designer_profile_menu),
                            true
                        )

                        menuViewBinding.textPriceMenuTitle.text = menuItem.menuName
                        menuViewBinding.textPriceMenuPrice.text = menuItem.menuPrice
                        menuViewBinding.textPriceMenuTime.text = menuItem.menuTime
                        //todo: 옵션은 지금 생략했음
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    /**
     * 미리보는 리뷰 이미지들 바인딩
     */
    private fun initReviewImages() {
        // 미리보기 리뷰 이미지 버튼을 담고 있는 parent layout
        val parentLayout =
            findViewById<ViewGroup>(R.id.layout_designer_profile_review_images)

        // 리뷰 이미지 바인딩
        reviewImagePathList.indices.forEach { i ->
            bindProfileImage(
                parentLayout,
                parentLayout.getChildAt(i) as ImageButton,
                reviewImagePathList[i],
                false
            )
        }
    }

    /**
     * 미리보는 고객 리뷰 초기화
     */
    private fun initReviewList(){
        val parentLayout = findViewById<ViewGroup>(R.id.layout_designer_profile_reviews)

        reviewList.indices.forEach { i ->
            initEachReviewItem(parentLayout, parentLayout.getChildAt(i), reviewList[i])
        }

    }

    /**
     * 고객 리뷰 리스트의 아이템 뷰 초기화
     */
    private fun initEachReviewItem(
        parentView: ViewGroup,
        reviewItemView: View,
        userReview: UserReview
    ) {
        reviewItemView.apply {
            val profileImageView = this.findViewById<ImageView>(R.id.image_item_review) // 유저 프로필 사진
            bindProfileImage(parentView, profileImageView, userReview.userImagePath, true)

            this.findViewById<TextView>(R.id.text_item_review_nickname).text =
                userReview.userName  // 유저 네임
            this.findViewById<TextView>(R.id.text_item_review_description).text =
                userReview.description // 후기
            this.findViewById<TextView>(R.id.text_item_review_dates).text = userReview.dates // 날짜
            this.findViewById<TextView>(R.id.text_item_review_rating).text =
                convertRatingToStar(userReview.rating) // 별점
        }
    }
}