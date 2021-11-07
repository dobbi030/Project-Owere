package com.blooburn.owere.user.activity.main.userReservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY

//지도에서 미용실 클릭하여 선택 후 미용실 결정 액티비티
class ReserveSalonActivity : AppCompatActivity() {

    //프로필에서 전달받을 디자이너 객체
    private lateinit var designerData: UserDesignerItem
    //전달받을 선택한 메뉴
    private lateinit var menu: StyleMenuItem

    //전달받을 기장 옵션
    private lateinit var lengthOption: String
    //전달받을 선택할 미용실
    private lateinit var selectedShop: ShopListItem


    //레이아웃 텍스트뷰
    private val salonName : TextView by lazy {
        findViewById(R.id.reserve_salon_name_text)
    }
    private val salonReview : TextView by lazy {
        findViewById(R.id.reserve_salon_designer_review)
    }
    private val salonRating : TextView by lazy {
        findViewById(R.id.reserve_salon_rating_text)
    }
    private val salonAdress : TextView by lazy {
        findViewById(R.id.reserve_salon_address_text)
    }
    private val choiceCompleteButton : TextView by lazy {
        findViewById(R.id.salon_choice_complete_button)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_salon)


        //수신 인텐트로 전달받은 디자이너 정보 저장
        getDataFromIntent()

        initView()
    }


    /**
     * 레이아웃에 전달받은 객체 정보 표시
     */
    private fun initView(){
        salonName.text = selectedShop!!.name
        salonAdress.text = selectedShop!!.area
        salonRating.text = selectedShop!!.rating.toString()
        salonReview.text = selectedShop!!.reviewCount.toString()

        //선택완료 버튼 리스너
        choiceCompleteButton.setOnClickListener {
            var intent = Intent(this,ReserveTimeActivity::class.java)
            //선택한 미용실 객체 전송
            intent.putExtra("selectedShop", selectedShop)

            //디자이너 객체 전송
            intent.putExtra(DESIGNER_DATA_KEY, designerData)
            //선택한 메뉴 객체 전송
            intent.putExtra("SESLECTED_MENU_DATA_KEY", menu)
            //메뉴선택메뉴의 옵션 전송(기장 추가)
            intent.putExtra("lengthOption", lengthOption)

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

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)!!   // DesignerData 객체 읽기
        menu = extras!!.getParcelable("SESLECTED_MENU_DATA_KEY")!! //선택한 메뉴 객체 읽기
        lengthOption = extras.getString("lengthOption").toString()//선택한 옵션
        selectedShop = extras.getParcelable("selectedShop")!!// 선택한 미용실 객체읽기

        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }


}