package com.blooburn.owere.user.adapter.userReservation



import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.AllPricesFragment
import com.blooburn.owere.user.item.ReservationListItem
import com.blooburn.owere.util.ALL_PRICES_FRAGMENT_NAME
import com.blooburn.owere.util.DESIGNER_RESERVATION_DETAIL_KEY
import com.blooburn.owere.util.TypeOfDesignerReservation

class UserReservationDetailActivity : AppCompatActivity() {

    private var reservation: ReservationListItem? = null
    private val tempDesignerId = "designer0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_reservation_detail)

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
            extras!!.getParcelable("userReservationDetail")   // DesignerReservation 객체 읽기
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
        setBottomButtonsClickListener()
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

    private fun setBottomButtonsClickListener() {
        setAdditionalTreatmentClickListener()
    }

    private fun setAdditionalTreatmentClickListener() {
        val buttonView =
            findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.button_designer_reservation_detail_additional_treatment)

        buttonView.setOnClickListener {
            val fragment = AllPricesFragment(hasCheckBox = true)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_designer_reservation_detail, fragment)
                .addToBackStack(ALL_PRICES_FRAGMENT_NAME)
                .commitAllowingStateLoss()
        }
    }
}