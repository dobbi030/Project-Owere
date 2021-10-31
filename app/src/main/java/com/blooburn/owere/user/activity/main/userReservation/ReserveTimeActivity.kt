package com.blooburn.owere.user.activity.main.userReservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.DesignerProfileHandler
import com.blooburn.owere.util.databaseInstance
import com.blooburn.owere.util.storageInstance
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference

//예약하기 액티비티 (날짜, 시간 예약)
class ReserveTimeActivity : AppCompatActivity(), DesignerProfileHandler {

    //프로필에서 전달받을 디자이너 객체
    private var designerData: UserDesignerItem? = null
    //전달받을 선택한 메뉴
    private var menu: StyleMenuItem? = null

    //전달받을 기장 옵션
    private var lengthOption: String = ""
    //전달받을 선택할 미용실
    private var selectedShop: ShopListItem? = null



    private val databaseReference = databaseInstance.reference
    private val storageReference = storageInstance.reference
    private lateinit var designerReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)


        initDataAndView()  // 모든 뷰의 데이터 초기화 작업

    }

    private fun initDataAndView(){
        // 수신 인텐트로 전달받을 정보 할당
        getDataFromIntent()
        setDataReferences()// 디자이너 아이디로 DB에서 데이터들 Reference 설정

        setDesignerInformation()

    }

    /**
     * 디자이너 아이디로 DB Reference 설정
     */
    private fun setDataReferences() {
        // 디자이너의 데이터 참조
        designerReference = databaseReference.child("Designers/${designerData?.designerId}")
        // 가격표 참조
//        priceChartReference =
//            databaseReference.child("designerPriceChart/${designerData?.designerId}")
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
        findViewById<View>(R.id.reserve_activity_designer_profile).apply {
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


        // 리뷰 별점, 평점, 개수
        findViewById<TextView>(R.id.text_user_designer_profile_review_star).text = reviewStar
        findViewById<TextView>(R.id.text_user_designer_profile_review_rating).text =
            designerData!!.rating.toString()
        findViewById<TextView>(R.id.text_user_designer_profile_review_count).text =
            designerData!!.reviewCount.toString()
    }

    private fun initButton(){
        var keepButton = findViewById<TextView>(R.id.reserve_time_keepbutton)
        keepButton.setOnClickListener {
            var intent = Intent(this, ReserveMenuActivity::class.java)
            intent.putExtra("designerData",designerData)
            startActivity(intent)
        }
    }

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun getDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)   // DesignerData 객체 읽기
        menu = extras!!.getParcelable("SESLECTED_MENU_DATA_KEY") //선택한 메뉴 객체 읽기
        lengthOption = extras.getString("lengthOption").toString()//선택한 옵션
        selectedShop = extras.getParcelable("selectedShop")// 선택한 미용실 객체읽기

        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }



}