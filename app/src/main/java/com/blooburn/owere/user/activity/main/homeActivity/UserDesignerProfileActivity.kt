package com.blooburn.owere.user.activity.main.homeActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemPriceMenuBinding
import com.blooburn.owere.user.activity.main.chattingActivity.ChattingActivity
import com.blooburn.owere.user.activity.main.userReservation.ReserveMenuActivity
import com.blooburn.owere.user.adapter.home.DesignerPortfolioSliderAdapter
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.AllPricesFragment
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.user.item.UserReview
import com.blooburn.owere.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class UserDesignerProfileActivity : AppCompatActivity(), DesignerProfileHandler {

    private var designerData: UserDesignerItem? = null
    private val reviewImagePathList = mutableListOf<String>()
    private val pricePath = "커트"

    //유저 정보를 넘겨주기 위해 파이어베이스 인증 인스턴트 생성
    private val auth : FirebaseAuth by lazy{
        Firebase.auth
    }
    //유저 아이디를 넘겨줄것임
    private lateinit var userId : String

    private val databaseReference = databaseInstance.reference
    private val storageReference = storageInstance.reference
    private lateinit var designerReference: DatabaseReference
    private lateinit var portfolioReference: StorageReference
    private lateinit var priceChartReference: DatabaseReference

    private lateinit var sliderAdapter: DesignerPortfolioSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_designer_profile)

        userId = auth.currentUser!!.uid
        val toolbar = findViewById<Toolbar>(R.id.toolbar_user_designer_profile)
        initToolbar(toolbar)

        val sliderViewPager = findViewById<ViewPager2>(R.id.view_pager_user_designer_profile)
        sliderAdapter = DesignerPortfolioSliderAdapter(this)
        sliderViewPager.adapter = sliderAdapter

        initDataAndView() // 모든 뷰의 데이터 초기화 작업

        initButton()

    }

    /**
     * 가격 전체 보기 -> 프래그먼트 생성
     */
    private val priceClickListener = View.OnClickListener {
        val fragment = AllPricesFragment(isAdditionTreatment = false)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_user_designer_profile, fragment)
            .addToBackStack(ALL_PRICES_FRAGMENT_NAME)
            .commitAllowingStateLoss()
    }


    private fun initDataAndView() {
        // 수신 인텐트로 전달받은 디자이너 정보 저장
        getDesignerDataFromIntent()
        setDataReferences() // 디자이너 아이디로 DB에서 데이터들 Reference 설정

        // 포트폴리오 이미지 레퍼런스들을 스토리지에서 가져온다
        // path = "portfolio/[디자이너 id]"
        getPortfolioImagesPath()
        setDesignerInformation()
        getAndSetFirstPrice()
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

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)   // DesignerData 객체 읽기
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


        Log.d("로그", "$designerData")
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
     * 첫 메뉴(커트) 가격표 불러와서 저장
     */
    private fun getAndSetFirstPrice() {
        priceChartReference.child(pricePath)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    // 서브 메뉴 개수 만큼 동적으로 view를 생성해서 추가
                    snapshot.children.forEach {
                        val menuItem = it.getValue(StyleMenuItem::class.java) ?: return

                        // LayoutInflater를 사용해서 메뉴 동적 생성 and 뷰바인딩 사용
                        val menuViewBinding = ItemPriceMenuBinding.inflate(
                            layoutInflater,
                            findViewById(R.id.layout_user_designer_profile_menu),
                            true
                        )

                        menuViewBinding.textPriceMenuTitle.text = menuItem.menuName
                        menuViewBinding.textPriceMenuPrice.text = "${menuItem.menuPrice}"
                        menuViewBinding.textPriceMenuTime.text = menuItem.menuTime
                        //todo: 옵션은 지금 생략했음
                    }
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
                snapshot.child("reviews").children.forEachIndexed { i, reviewSnapshot ->
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
    private fun bindReviewData(
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

    /**
     * 버튼 초기화
     */
    private fun initButton() {
        val priceButton = findViewById<Button>(R.id.button_user_designer_profile_prices)
        priceButton.setOnClickListener(priceClickListener)

        //예약하기 버튼
        val reserveButton = findViewById<AppCompatButton>(R.id.button_user_designer_profile_reserve)
        reserveButton.setOnClickListener {
            //예약하기의 메뉴선택 액티비티로 전환, 디자이너 객체 정보 전달
            val intent = Intent(this, ReserveMenuActivity::class.java)
            intent.putExtra(DESIGNER_DATA_KEY, designerData)
            startActivity(intent)
        }
        //채팅하기 버튼
        val chattingButton = findViewById<AppCompatButton>(R.id.button_user_designer_profile_chatting)
        chattingButton.setOnClickListener{

            //채팅방으로 이동
            val intent = Intent(this, ChattingActivity::class.java)
            intent.putExtra(DESIGNER_DATA_KEY, designerData)
            //채팅방 아이디 전송 @User@${userId}@UserId@$디자이너ID
            intent.putExtra("chatRoomId","@User@${userId}@Designer@${designerData!!.designerId}")
            startActivity(intent)
        }
    }
}