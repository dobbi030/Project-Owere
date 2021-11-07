package com.blooburn.owere.user.activity.main.userReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ActivityReserveTimeBinding
import com.blooburn.owere.databinding.ActivityUserWaitingReservationBinding
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY

class UserWaitingReservation : AppCompatActivity() {

    //프로필에서 전달받을 디자이너 객체
    private lateinit var designerData: UserDesignerItem

    //전달받을 선택한 메뉴
    private lateinit var menu: StyleMenuItem

    //전달받을 기장 옵션
    private var lengthOption: String = ""

    //전달받을 선택할 미용실
    private lateinit var selectedShop: ShopListItem  //선택할 미용실

    private lateinit var selectedTime : String //예약한 시간

    // 예약대기 레이아웃 바인딩
    private lateinit var binding: ActivityUserWaitingReservationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserWaitingReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getDataFromIntent()

        setReservationData()




    }

    /**
     * 전달받은 예약정보를 뷰에 적용
     */
    private fun setReservationData() {
        binding.apply {
            reservationDateText.text = selectedTime
            reservationDesignerText.text = designerData.name
            reservationShopText.text = selectedShop.name
            reservationMenuText.text = menu.menuName
            reservationPriceText.text = menu.menuPrice

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
        selectedTime = extras.getString("selectedTime").toString()!!//예약 시간 받기

        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }
}