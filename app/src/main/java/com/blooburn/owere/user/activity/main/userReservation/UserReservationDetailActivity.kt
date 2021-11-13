package com.blooburn.owere.user.activity.main.userReservation



import android.os.Bundle
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.blooburn.owere.R
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.ReservationBottomDialogFrament
import com.blooburn.owere.user.item.ReservationListItem



class UserReservationDetailActivity : AppCompatActivity() {

    private var reservation: ReservationListItem? = null

    private val tempDesignerId = "designer0"

    //메뉴 누를 때 나오는 바텀다이얼로그 프래그먼트
    val reservationBottomDialogFragment : ReservationBottomDialogFrament? = ReservationBottomDialogFrament()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reservation_detail)

        getReservationFromIntent()
        initBottomButtons()

    }

    /**
     * 수신 인텐트로 전달받은 예약 정보(DesignerReservation) 저장
     */

    private fun getReservationFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        reservation =
            extras!!.getParcelable("userReservationDetail")   // ReservationListItem 객체 읽기
        if (reservation == null) {
            finish()
        }
    }

    /**
     * 아래 정산관련 버튼들 초기화
     * 1. visibility
     * 2. click listener
     */
    private fun initBottomButtons() {
//        setBottomButtonsVisibility()
        //setBottomButtonsClickListener()

        var buttonMenu = findViewById<ImageView>(R.id.button_designer_reservation_detail_menu)
        buttonMenu.setOnClickListener {

            //프래그먼트로 객체 전달
            val bundle = bundleOf() //어떤 키에 어떤 값으로 번들을 담겠다
            //예약 정보 전달
            bundle.putParcelable("reservation",reservation)
            reservationBottomDialogFragment?.arguments = bundle
            reservationBottomDialogFragment?.show(supportFragmentManager, reservationBottomDialogFragment.tag)
        }
    }

//    /**
//     * 정산할 예약일 때만 아래에 정산 관련 버튼들이 보이도록 한다
//     */
//    private fun setBottomButtonsVisibility() {
//        if (reservation!!.type == TypeOfDesignerReservation.COMPLETED) {
//            findViewById<LinearLayout>(R.id.layout_designer_reservation_detail_buttons).visibility =
//                View.VISIBLE
//        }
//    }

//    private fun setBottomButtonsClickListener() {
//        setAdditionalTreatmentClickListener()
//    }
//
//    private fun setAdditionalTreatmentClickListener() {
//        val buttonView =
//            findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.button_designer_reservation_detail_additional_treatment)
//
//        buttonView.setOnClickListener {
//            val fragment = AllPricesFragment(hasCheckBox = true)
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_designer_reservation_detail, fragment)
//                .addToBackStack(ALL_PRICES_FRAGMENT_NAME)
//                .commitAllowingStateLoss()
//        }
//    }
}